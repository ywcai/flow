package ywcai.flow.action;

import java.util.List;

import ywcai.flow.action.inf.ResponseMmInf;
import ywcai.flow.business.PayValidate;
import ywcai.flow.business.ProductProcess;
import ywcai.flow.hibernate.Product;
import ywcai.flow.model.PayNotify;
import ywcai.flow.model.PayNotifyResult;
import ywcai.flow.model.RechargeResult;
import ywcai.flow.util.MyLog;

public class ResponseMm implements ResponseMmInf {

	@Override
	public PayNotifyResult mmPayCallBack(PayNotify payNotify) {

		// TODO Auto-generated method stub
		//根据回调信息决定是否发起充值;
		PayValidate payValidate=new PayValidate();
		PayNotifyResult backToMm=payValidate.validate(payNotify);
		if(backToMm.return_code.equals("SUCCESS"))
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new RequestMr().requestRecharge(payNotify.out_trade_no);
				}
			}).start();
		}
		backToMm.return_code="SUCCESS";
		return backToMm;
	}

	@Override
	public List<Product> getProducts(String province, int local) {
		// TODO Auto-generated method stub
		ProductProcess productProcess=new ProductProcess();
		List<Product> products=productProcess.queryProducts(province, local);
		return products;
	}
}
