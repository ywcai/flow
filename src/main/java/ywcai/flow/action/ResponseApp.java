package ywcai.flow.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import ywcai.flow.action.inf.RequestMmInf;
import ywcai.flow.action.inf.RequestMrInf;
import ywcai.flow.action.inf.ResponseAppInf;
import ywcai.flow.business.OrderProcess;
import ywcai.flow.business.PrePayProcess;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.model.MmKey;
import ywcai.flow.model.PrePay;
import ywcai.flow.model.PrePayBack;
import ywcai.flow.model.TelCell;
import ywcai.flow.util.IpUtil;
import ywcai.flow.util.MyLog;

public class ResponseApp implements ResponseAppInf  {

	RequestMmInf reqMm=new RequestMm();
	RequestMrInf reqMr;

	@Override
	public PrePayBack resPrePay(String jsCode, String mobile, int product, int fastPay, HttpServletRequest request) {
		// TODO Auto-generated method stub
		MmKey mmKey=reqMm.getMmKey(jsCode, "ywcai");
		PrePayBack prePayBack=new PrePayBack();
		if(mmKey.openid==null||mmKey.session_key==null)
		{
		    MyLog.INFO("��ȡ��¼��Կʧ�ܣ�����NULL");
			return prePayBack;
		}
		OrderProcess orderProcess=new OrderProcess();
		String clientIp=IpUtil.getIpAddr(request);
		Orders myOrder=orderProcess.CreateMyOrder(mmKey.openid, mobile, product, fastPay,clientIp);
		if(myOrder==null)
		{
			prePayBack.return_code="FAIL";
			prePayBack.return_msg="δ��ѯ���ͻ����ύ�Ĳ�Ʒ";
			return prePayBack;
		}
		//����֧��������Ϣ
		PrePayProcess prePayProcess=new PrePayProcess();
		PrePay prePay=prePayProcess.createPrePayEntry(myOrder,mmKey.session_key);
		//����Ԥ��������
		prePayBack=reqMm.reqMmPay(prePay);
		if(prePayBack.return_code.equals("FAIL"))
		{
			return prePayBack;
		}
		if(!prePayProcess.validateSign(prePayBack))
		{
			prePayBack.return_code="FAIL";
			prePayBack.return_msg="Ԥ֧������ǩ������";
			return prePayBack;
		}
		if(!orderProcess.myOrderPersistent(myOrder))
		{
			prePayBack.return_code="FAIL";
			prePayBack.return_msg="�̻������־û�ʧ��";
			return prePayBack;
		}
		return prePayBack;
	}
	@Override
	public List<Orders> resLookOrder(String jsCode) {
		// TODO Auto-generated method stub
		MmKey mmKey=reqMm.getMmKey(jsCode, "ywcai");
		OrderProcess orderProcess=new OrderProcess();
		if(mmKey.openid==null||mmKey.session_key==null)
		{
			return null;
		}
		List<Orders> list=orderProcess.queryOrders(mmKey.openid);
		return list;
	}
	@Override
	public TelCell resLookTel(String mobile) {
		// TODO Auto-generated method stub
		TelCell telCell=null;
		RequestOther requestOther=new RequestOther();
		telCell=requestOther.getTelInfo(mobile);	
		return telCell;
	}
}
