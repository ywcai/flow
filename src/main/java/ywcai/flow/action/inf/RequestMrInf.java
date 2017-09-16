package ywcai.flow.action.inf;


import ywcai.flow.model.RechargeResult;

public interface RequestMrInf {
	public RechargeResult requestRecharge(String orderid);	
	public void requestRechargeStatus();
}
