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
		rechargeResult.desc="第三方充值平台故障";
		OrderProcess orderProcess=new OrderProcess();
		Orders order=orderProcess.queryOrder(orderid);
		if(order==null)
		{
			MyLog.WARN("空订单请求-"+orderid);
			//处理未查询到订单的订单异常,记录相关信息
			rechargeResult.desc="平台订单生成故障";
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
			//没有提交成功，有可能不会有返回回调？，先更新订单失败的描述
			RechargeNotify rechargeNotify=new RechargeNotify();
			rechargeNotify.desc=rechargeResult.desc;
			rechargeNotify.ordernum=order.getOrdernum();
			rechargeNotify.mobile=order.getMobile();
			rechargeNotify.state="3";
			orderProcess.myOrderPersistent(rechargeNotify);
			//发起退款请求，等待申请证书，暂不发起
//			RequestBackPay requestBackPay=new RequestBackPay();
//			requestBackPay.reqMmBackPay(orderProcess,order);
		}
		return rechargeResult;
	}

	//请求查询充值状态
	@Override
	public void requestRechargeStatus() {


	}
}
