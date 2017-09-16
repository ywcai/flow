package ywcai.flow.action.inf;

import ywcai.flow.model.MmKey;
import ywcai.flow.model.PrePay;
import ywcai.flow.model.PrePayBack;

public interface RequestMmInf {
	public MmKey getMmKey(String jsCode,String authCode);
	public PrePayBack reqMmPay(PrePay pay);
}
