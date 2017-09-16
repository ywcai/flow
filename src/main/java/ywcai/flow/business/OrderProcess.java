package ywcai.flow.business;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.google.gson.Gson;
import ywcai.flow.cfg.AppConfig;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.hibernate.Product;
import ywcai.flow.model.ReChargeEntry;
import ywcai.flow.model.RechargeNotify;
import ywcai.flow.util.HibernateUtil;
import ywcai.flow.util.MD5;
import ywcai.flow.util.MyLog;
import ywcai.flow.util.MyTime;

public class OrderProcess {

	public Orders CreateMyOrder(String openId, String mobile, int productid, int fastPay,String clientIp)
	{
		ProductProcess productProcess=new ProductProcess();
		Product product=productProcess.queryProduct(productid);
		if(product==null)
		{
			MyLog.WARN("δ��ѯ����Ʒ");
			return null;
		}
		
		Orders myOrder=new Orders();
		myOrder.setOpenid(openId);
		myOrder.setOrdernum(createOrderId());
		myOrder.setEcho(createOrderEcho());
		myOrder.setMobile(mobile);
		myOrder.setProductid(product.getProductid());
		myOrder.setPayfast(fastPay);
		myOrder.setOrderstatus(0);
		myOrder.setTimestamp(MyTime.getNowTime());
		myOrder.setClientip(clientIp);
		myOrder.setOrderdesc("��ʼ����");
	
		myOrder.setPrice(product.getPrice());
		myOrder.setRate(product.getRate());
		myOrder.setTotalfee(product.getPrice()*product.getRate()/100);//��Ҫ��product��ȡ
		myOrder.setPackcode(product.getPackcode()+"");//��Ҫ��product��ȡ
		myOrder.setLocalproduct(product.getLocal());//�ڲ�Ʒ�������֣����ﲻ��Ҫ���ֶΣ���������
		return myOrder;
	}

	public Orders queryOrder(String ordernum)
	{
		//��ѯ�������ݿⶩ����Ϣ;
		Session session=HibernateUtil.getCurrentSession();
		List<?> list=null;
		Orders myOrder=null;
		String hql="from Orders where ordernum = ?0 "; 
		Transaction transaction=null;
		try {
			transaction=session.beginTransaction();
			Query<?> query = session.createQuery(hql);
			query.setParameter(0, ordernum);
			list = (List<?>) query.list();
			transaction.commit();
		} catch (Exception e) {
			MyLog.ERR(e.toString());
			return null;
		}
		if(list!=null)
		{
			myOrder=(Orders)list.get(0);
		}
		return myOrder;
	}
	@SuppressWarnings("unchecked")
	public List<Orders> queryOrders(String mmOpenId) {
		// ��ѯ���û������ж���ժҪ
		Session session=HibernateUtil.getCurrentSession();
		List<Orders> list=null;
		String hql="from Orders where openid = ?0 and delflag = 1 order by uid desc"; 
		Transaction transaction=null;
		try {
			transaction=session.beginTransaction();
			Query<?> query = session.createQuery(hql);
			query.setParameter(0, mmOpenId);
			list = (List<Orders>) query.list();
			transaction.commit();
		} catch (Exception e) {
			MyLog.ERR(e.toString());
		}
		return list;
	}
	public String assembleJson(Orders myOrder)
	{
		ReChargeEntry mrOrder=new ReChargeEntry();	
		mrOrder.userid=AppConfig.mr_userid;
		mrOrder.orderid=myOrder.getOrdernum();
		mrOrder.echo=myOrder.getEcho();
		mrOrder.timestamp=myOrder.getTimestamp();
		mrOrder.version="1.0";
		mrOrder.packcode=myOrder.getPackcode();
		mrOrder.mobile=myOrder.getMobile();
		mrOrder.flowtype=myOrder.getLocalproduct()+"";
		mrOrder.callback_url=AppConfig.mr_callback_url;
		mrOrder.chargeSign=getChargeSign(mrOrder);
		Gson gson=new Gson();
		String req=gson.toJson(mrOrder);
		return req;	
	}

	public boolean validateChargeSign(RechargeNotify rechargeNotify) {
		String expectSign=MD5.md5(rechargeNotify.userid
				+rechargeNotify.ordernum
				+rechargeNotify.timestamp
				+AppConfig.mr_key
				);
		return expectSign.equals(rechargeNotify.sign);
	}

	//���յ�С���򶩵����󣬳־û���ʼ����
	public boolean myOrderPersistent(Orders myOrder) {
	
		Session session=HibernateUtil.getCurrentSession();
		Transaction transaction=null;
		try
		{
			transaction=session.beginTransaction();
			session.save(myOrder);
			transaction.commit();
		}
		catch(Exception e)
		{
			transaction.rollback();
			MyLog.ERR("��ʼ�����־û�ʧ��="+e.toString());
			return false;
		}
//		MyLog.INFO("�����־û��ɹ�"+myOrder.toString());
		return true;
	}
	public boolean myOrderPersistent(Orders order,int status) {
		String hql="update Orders set orderstatus= ?0  where ordernum = ?1 "
				+ "and mobile = ?2  ";
		Session session=HibernateUtil.getCurrentSession();
		Transaction transaction=null;
		try
		{
			transaction=session.beginTransaction();
			Query<?> q=session.createQuery(hql);
			q.setParameter(0, status);//4
			q.setParameter(1,order.getOrdernum());
			q.setParameter(2,order.getMobile());
			q.executeUpdate();
			transaction.commit();
		}
		catch(Exception e)
		{
			transaction.rollback();
			MyLog.ERR("���¶���״̬��������:"+e.toString());
			return false;
		}
		return true;
	}

	//�־û����𷵻ظ��µĶ���״̬ 
	public boolean myOrderPersistent(RechargeNotify rechargeNotify) {
		String hql="update Orders set orderstatus= ?0 , orderdesc = ?1  where ordernum = ?2 "
				+ "and mobile = ?3  ";
		Session session=HibernateUtil.getCurrentSession();
		Transaction transaction=null;
		try
		{
			transaction=session.beginTransaction();
			Query<?> q=session.createQuery(hql);
			q.setParameter(0, Integer.parseInt(rechargeNotify.state));
			q.setParameter(1,rechargeNotify.desc);
			q.setParameter(2,rechargeNotify.ordernum);
			q.setParameter(3,rechargeNotify.mobile);
			q.executeUpdate();
			transaction.commit();
		}
		catch(Exception e)
		{
			transaction.rollback();
			MyLog.ERR("��ֵ���ؽ���־û�ʧ��="+e.toString()+" ��������="+rechargeNotify.toString());
			return false;
		}
		return true;
	}

	//����ǩ��
	private String getChargeSign(ReChargeEntry mrOrder)
	{
		String chargeSige=MD5.md5(AppConfig.mr_userid +
				mrOrder.orderid + 
				AppConfig.mr_key +
				mrOrder.echo +
				mrOrder.timestamp);
		return chargeSige;
	}
	//��������ƽ̨�����Ź���
	private String createOrderId() {
		String orderId="NO"+MyTime.getNowTime()+(int)(Math.random()*1000000);
		int n=23-orderId.length();
		for(int i=0;i<n;i++)
		{
			orderId+="0";
		}
		return orderId;
	}
	//32λ�����
	public String createOrderEcho() {
		String echo=MD5.md5(MyTime.getNowTime()+"|"+MyTime.getNowTime());
		return echo;
	}
	
	



}
