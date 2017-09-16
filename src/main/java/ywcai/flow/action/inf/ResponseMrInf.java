package ywcai.flow.action.inf;

import ywcai.flow.model.RechargeNotify;
import ywcai.flow.model.RechargeResult;

public interface ResponseMrInf {
	public RechargeResult resRechargeBack(RechargeNotify rechargeNotify);
}
