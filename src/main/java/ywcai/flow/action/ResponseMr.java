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
			rechargeNotifyResult.desc="��ֵ��ɺ�ص�������Ϊ��";
			return rechargeNotifyResult;
		}
		//		MyLog.INFO("��ֵ��ɵĻص���Ϣ:"+rechargeNotify.toString());
		if(!orderProcess.validateChargeSign(rechargeNotify))
		{
			rechargeNotifyResult.code="0001";
			rechargeNotifyResult.desc="ǩ����֤ʧ��";
		}
		else
		{
			rechargeNotifyResult.code="0000";
			rechargeNotifyResult.desc="�յ��ص�";
			Orders order=orderProcess.queryOrder(rechargeNotify.ordernum);
			if(rechargeNotify.state.equals("2"))
			{
				//��ֵ�ɹ�
				if(order.getOrderstatus()!=2)
				{
					orderProcess.myOrderPersistent(rechargeNotify);
				}
				else {
					//��ֵ�ɹ�,�����Ѿ��յ���һ�λص�����һ�β�����
					MyLog.INFO("��ֵ�ɹ�:"+order.getOrdernum()+",�����Ѿ��յ���һ�λص�,�ڶ��β�������");
				}
			}
			else {
				//ʧ��
				if(order.getOrderstatus()==1)
				{
					//��δ�����
					MyLog.INFO("��ֵʧ��:"+order.getOrdernum()+",����δ���й��κδ����ȴ�����״̬Ϊ3,Ȼ����MMƽ̨�����˿�����!");
					orderProcess.myOrderPersistent(rechargeNotify);
					//�ȴ�����֤��
//					RequestBackPay requestBackPay=new RequestBackPay();
//					requestBackPay.reqMmBackPay(orderProcess,order);
				}
				else if
				(order.getOrderstatus()==3||order.getOrderstatus()==4||order.getOrderstatus()==5)
				{
					//������ Ϊ3����� �ڵȴ��˿�ص�����Ϣ
					MyLog.INFO("��ֵʧ��:"+order.getOrdernum()+",����Ϊʧ��״̬3|4|5,˵���Ѿ���������������ڴ���!");
//					orderProcess.myOrderPersistent(rechargeNotify);
//					RequestBackPay requestBackPay=new RequestBackPay();
//					requestBackPay.reqMmBackPay(orderProcess,order);
				}
				else {
					//��ֵʧ��,�����Ѿ��յ���һ�λص�����һ�β��ٴ���
					MyLog.INFO("��ֵʧ��:"+order.getOrdernum()+",����δ֪����!");
				}
			}
		}
		return rechargeNotifyResult;		
	}
}
