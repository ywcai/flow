package ywcai.flow.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import ywcai.flow.action.inf.RequestMrInf;
import ywcai.flow.business.OrderProcess;
import ywcai.flow.cfg.AppConfig;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.model.RechargeNotify;
import ywcai.flow.model.RechargeResult;
import ywcai.flow.util.MyLog;

public class RequestMr implements RequestMrInf {

	@Override
	public RechargeResult requestRecharge(String orderid) {
		RechargeResult rechargeResult = new RechargeResult();
		rechargeResult.code="7772";
		rechargeResult.desc="��������ֵƽ̨����";
		OrderProcess orderProcess=new OrderProcess();
		Orders order=orderProcess.queryOrder(orderid);
		if(order==null)
		{
			MyLog.WARN("�ն�������-"+orderid);
			//����δ��ѯ�������Ķ����쳣,��¼�����Ϣ
			rechargeResult.desc="ƽ̨�������ɹ���";
		}
		else
		{
			String reqJson=orderProcess.assembleJson(order);
			Client client=ClientBuilder.newClient();
			WebTarget webTarget=client.target(AppConfig.mr_recharge_url);
			String res="";
			Gson gson=new Gson();
			try
			{
				res=webTarget
						.request()
						.post(Entity.entity(reqJson, MediaType.TEXT_PLAIN),String.class);
				rechargeResult=gson.fromJson(res,RechargeResult.class);
			}
			catch(Exception e)
			{
				//
				MyLog.INFO(e.toString());
			}
		}
		MyLog.INFO(rechargeResult.toString());
		if(!rechargeResult.code.equals("0000"))
		{
			//û���ύ�ɹ����п��ܲ����з��ػص������ȸ��¶���ʧ�ܵ�����
			RechargeNotify rechargeNotify=new RechargeNotify();
			rechargeNotify.desc=rechargeResult.desc;
			rechargeNotify.ordernum=order.getOrdernum();
			rechargeNotify.mobile=order.getMobile();
			rechargeNotify.state="3";
			orderProcess.myOrderPersistent(rechargeNotify);
			//�����˿����󣬵ȴ�����֤�飬�ݲ�����
//			RequestBackPay requestBackPay=new RequestBackPay();
//			requestBackPay.reqMmBackPay(orderProcess,order);
		}
		return rechargeResult;
	}

	//�����ѯ��ֵ״̬
	@Override
	public void requestRechargeStatus() {


	}
}
