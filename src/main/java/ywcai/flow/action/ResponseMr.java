package ywcai.flow.action;


import ywcai.flow.action.inf.ResponseMrInf;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.business.OrderProcess;
import ywcai.flow.model.RechargeNotify;
import ywcai.flow.model.RechargeResult;
import ywcai.flow.util.MyLog;

public class ResponseMr implements ResponseMrInf {

	@Override
	public RechargeResult resRechargeBack(RechargeNotify rechargeNotify) {

		RechargeResult rechargeNotifyResult=new RechargeResult();
		OrderProcess orderProcess=new OrderProcess();
		if(rechargeNotify==null)
		{
			rechargeNotifyResult.code="0001";
			rechargeNotifyResult.desc="充值完成后回调的数据为空";
			return rechargeNotifyResult;
		}
		//		MyLog.INFO("充值完成的回调信息:"+rechargeNotify.toString());
		if(!orderProcess.validateChargeSign(rechargeNotify))
		{
			rechargeNotifyResult.code="0001";
			rechargeNotifyResult.desc="签名验证失败";
		}
		else
		{
			rechargeNotifyResult.code="0000";
			rechargeNotifyResult.desc="收到回调";
			Orders order=orderProcess.queryOrder(rechargeNotify.ordernum);
			if(rechargeNotify.state.equals("2"))
			{
				//充值成功
				if(order.getOrderstatus()!=2)
				{
					orderProcess.myOrderPersistent(rechargeNotify);
				}
				else {
					//充值成功,但是已经收到过一次回调，这一次不处理
					MyLog.INFO("充值成功:"+order.getOrdernum()+",但是已经收到过一次回调,第二次不做处理");
				}
			}
			else {
				//失败
				if(order.getOrderstatus()==1)
				{
					//从未处理过
					MyLog.INFO("充值失败:"+order.getOrdernum()+",订单未进行过任何处理，先处理订单状态为3,然后向MM平台发起退款申请!");
					orderProcess.myOrderPersistent(rechargeNotify);
					//等待申请证书
//					RequestBackPay requestBackPay=new RequestBackPay();
//					requestBackPay.reqMmBackPay(orderProcess,order);
				}
				else if
				(order.getOrderstatus()==3||order.getOrderstatus()==4||order.getOrderstatus()==5)
				{
					//按道理 为3的情况 在等待退款回调的信息
					MyLog.INFO("充值失败:"+order.getOrdernum()+",订单为失败状态3|4|5,说明已经被处理过，无需在处理!");
//					orderProcess.myOrderPersistent(rechargeNotify);
//					RequestBackPay requestBackPay=new RequestBackPay();
//					requestBackPay.reqMmBackPay(orderProcess,order);
				}
				else {
					//充值失败,但是已经收到过一次回调，这一次不再处理
					MyLog.INFO("充值失败:"+order.getOrdernum()+",发生未知错误!");
				}
			}
		}
		return rechargeNotifyResult;		
	}
}
