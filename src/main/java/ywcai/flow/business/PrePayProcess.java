package ywcai.flow.business;

import java.util.HashMap;

import ywcai.flow.cfg.AppConfig;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.model.PrePay;
import ywcai.flow.model.PrePayBack;
import ywcai.flow.util.MD5;
import ywcai.flow.util.MmSige;
import ywcai.flow.util.MyLog;
import ywcai.flow.util.MyTime;

public class PrePayProcess {

	public PrePay createPrePayEntry(Orders myOrder, String sessionKey) {
		PrePay prePay=new PrePay();
		prePay.appid=AppConfig.appid;
		prePay.mch_id=AppConfig.mch_id;
//		prePay.device_info="WEB";//�Ǳ���
		prePay.nonce_str=myOrder.getEcho();
//		prePay.sign_type="MD5";//�Ǳ���
		prePay.body="����ͨѶ";
//		prePay.detail="֧������";//�Ǳ���
//		prePay.attach="֧������";//�Ǳ���
		prePay.out_trade_no=myOrder.getOrdernum();
//		prePay.fee_type="CNY";//�Ǳ���
		prePay.total_fee=myOrder.getTotalfee();
		prePay.spbill_create_ip=myOrder.getClientip();
//		prePay.time_start="";//�Ǳ���
//		prePay.time_expire="";//�Ǳ���
//		prePay.goods_tag="WXG";//�Ǳ���
		prePay.notify_url=AppConfig.mm_pay_end_notify_https;
		prePay.trade_type="JSAPI";
//		prePay.limit_pay="no_credit";//����ʹ�����ÿ�֧�����Ǳ���
		prePay.openid=myOrder.getOpenid();
		prePay.sign=createPaySign(prePay);
		return prePay;
	}
	private String createPaySign(PrePay prePay)
	{
		HashMap<String,String> hashMap=new HashMap<>();
		hashMap.put("appid", prePay.appid);
		hashMap.put("mch_id", prePay.mch_id);
		hashMap.put("nonce_str", prePay.nonce_str);
		hashMap.put("body", prePay.body);
		hashMap.put("out_trade_no", prePay.out_trade_no);
		hashMap.put("total_fee", prePay.total_fee+"");
		hashMap.put("spbill_create_ip", prePay.spbill_create_ip);
		hashMap.put("notify_url", prePay.notify_url);
		hashMap.put("trade_type", prePay.trade_type);
		hashMap.put("openid", prePay.openid);
		String stringA=MmSige.getStringA(hashMap);
		String stringAll=stringA+"key="+AppConfig.mch_key;
//		MyLog.INFO("Ԥ֧��ǩ���ַ���"+stringAll);
		String prePapSign=MD5.md5(stringAll).toUpperCase();
		return prePapSign;
	}
	public boolean validateSign(PrePayBack prePayBack) {
		// TODO Auto-generated method stub
		HashMap<String,String> hashMap=new HashMap<>();
		hashMap.put("return_code", prePayBack.return_code);
		hashMap.put("return_msg", prePayBack.return_msg);
		hashMap.put("appid", AppConfig.appid);
		hashMap.put("mch_id", AppConfig.mch_id);
		hashMap.put("device_info", prePayBack.device_info);
		hashMap.put("nonce_str", prePayBack.nonce_str+"");
		hashMap.put("result_code", prePayBack.result_code);
		hashMap.put("prepay_id", prePayBack.prepay_id);
		hashMap.put("trade_type", prePayBack.trade_type);
		hashMap.put("err_code", prePayBack.err_code);
		hashMap.put("err_code_des", prePayBack.err_code_des);
		String stringA=MmSige.getStringA(hashMap);
		String stringAll=stringA+"key="+AppConfig.mch_key;
//		MyLog.INFO(MyTime.getNowTime()+":  Ԥ֧������ǩ���ַ���"+stringAll);
		String prePapBackSign=MD5.md5(stringAll).toUpperCase();
		return prePapBackSign.equals(prePayBack.sign);
	}
}
