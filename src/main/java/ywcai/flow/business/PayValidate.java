package ywcai.flow.business;

import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ywcai.flow.cfg.AppConfig;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.model.PayNotify;
import ywcai.flow.model.PayNotifyResult;
import ywcai.flow.util.HibernateUtil;
import ywcai.flow.util.MD5;
import ywcai.flow.util.MmSige;
import ywcai.flow.util.MyLog;

public class PayValidate {
	private PayNotify payNotify;
	public PayNotifyResult validate(PayNotify _payNotify)
	{
		PayNotifyResult payNotifyResult=new PayNotifyResult();
		payNotify=_payNotify;
		if(!validateOrder())
		{
			payNotifyResult.return_code="FAIL";
			payNotifyResult.return_msg="商户订单错误!";
			return payNotifyResult;
		}
		if(!validateUser())
		{
			payNotifyResult.return_code="FAIL";
			payNotifyResult.return_msg="商户ID不正确!";
			return payNotifyResult;
		}
		if(!validateSign())
		{
			payNotifyResult.return_code="FAIL";
			payNotifyResult.return_msg="签名验证错误!";
			MyLog.WARN("收到微信签名错误="+payNotify.toString());
			return payNotifyResult;
		}
		orderStatusPersistent();
		payNotifyResult.return_code="SUCCESS";
		payNotifyResult.return_msg="OK";
		return payNotifyResult;
	}
	private void orderStatusPersistent() {
		String hql="update Orders set delflag = ?0 , orderstatus = ?1 where ordernum = ?2 "
				+ " and openid = ?3 ";
		Session session=HibernateUtil.getCurrentSession();
		Transaction transaction=null;
		try
		{
			transaction=session.beginTransaction();
			Query<?> q=session.createQuery(hql);
			q.setParameter(0, 1);
			q.setParameter(1, 1);
			q.setParameter(2,payNotify.out_trade_no);
			q.setParameter(3,payNotify.openid);
			q.executeUpdate();
			transaction.commit();
		}
		catch(Exception e)
		{
			transaction.rollback();
			MyLog.ERR("收到支付完成通知，但订单状态更新失败="+e.toString()
			+" 微信返回数据="+payNotify.toString());
		}
//		MyLog.INFO("微信支付通知持久化成功="+payNotify.toString());
	}
	
	private boolean validateSign()
	{
		HashMap<String,String> hashMap=new HashMap<>();
		hashMap.put("return_code", payNotify.return_code);
		hashMap.put("return_msg", payNotify.return_msg);
		hashMap.put("appid", AppConfig.appid);
		hashMap.put("mch_id", AppConfig.mch_id);
		hashMap.put("device_info", payNotify.device_info);
		hashMap.put("nonce_str", payNotify.nonce_str);
		hashMap.put("sign_type", payNotify.sign_type);
		hashMap.put("result_code", payNotify.result_code);
		hashMap.put("err_code", payNotify.err_code);
		hashMap.put("err_code_des", payNotify.err_code_des);
		hashMap.put("openid", payNotify.openid);
		hashMap.put("is_subscribe", payNotify.is_subscribe);
		hashMap.put("trade_type", payNotify.trade_type);
		hashMap.put("bank_type", payNotify.bank_type);
		hashMap.put("total_fee", payNotify.total_fee+"");
		hashMap.put("settlement_total_fee", payNotify.settlement_total_fee+"");
		hashMap.put("fee_type", payNotify.fee_type+"");
		hashMap.put("cash_fee", payNotify.cash_fee+"");
		hashMap.put("cash_fee_type", payNotify.cash_fee_type);
		hashMap.put("coupon_fee_1", payNotify.coupon_fee_1+"");
		hashMap.put("coupon_count", payNotify.coupon_count+"");
		hashMap.put("coupon_type_1", payNotify.coupon_type_1);
		hashMap.put("coupon_id_1", payNotify.coupon_id_1);
		hashMap.put("transaction_id", payNotify.transaction_id);
		hashMap.put("out_trade_no", payNotify.out_trade_no);
		hashMap.put("attach", payNotify.attach);
		hashMap.put("time_end", payNotify.time_end);
		String stringA=MmSige.getStringA(hashMap);
		String stringAll=stringA+"key="+AppConfig.mch_key;
		String paySign=MD5.md5(stringAll).toUpperCase();
		if(paySign.equals(payNotify.sign))
		{
			return true;
		}
		else {
			
			return false;
		}
	}
	private boolean validateUser()
	{
		if(payNotify.appid==null||payNotify.mch_id==null)
		{
			return false;
		}
		if(!payNotify.appid.equals(AppConfig.appid)||!payNotify.mch_id.equals(AppConfig.mch_id))
		{
			return false;
		}
		return true;
	}

	private boolean validateOrder()
	{
		OrderProcess orderProcess=new OrderProcess();
		Orders myOrder=orderProcess.queryOrder(payNotify.out_trade_no);
		if(myOrder==null)
		{
			MyLog.WARN("微信订单不存在="+payNotify.toString());
			return false;
		}
		if(myOrder.getOpenid()==null)
		{
			MyLog.WARN("微信订单用户ID为空="+payNotify.toString());
			return false;
		}
		if(!myOrder.getOpenid().equals(payNotify.openid))
		{
			MyLog.WARN("微信订单用户ID不匹配="+payNotify.toString());
			return false;
		}
		if(myOrder.getTotalfee()!=payNotify.total_fee)
		{
			MyLog.WARN("微信订单金额错误="+payNotify.toString());
			return false;
		}
		if(myOrder.getOrderstatus()==1)
		{
			MyLog.WARN("重复微信订单支付通知="+payNotify.toString());
			return false;
		}
		return true;
	}
}
