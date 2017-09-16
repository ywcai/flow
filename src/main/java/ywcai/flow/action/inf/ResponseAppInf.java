package ywcai.flow.action.inf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ywcai.flow.hibernate.Orders;
import ywcai.flow.model.PrePayBack;
import ywcai.flow.model.TelCell;

public interface ResponseAppInf {
	public PrePayBack resPrePay(String jsCode,String mobile,int product,int fastPay,HttpServletRequest request);
	public List<Orders> resLookOrder(String jsCode);
	public TelCell resLookTel(String mobile);
}
