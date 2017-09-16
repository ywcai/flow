package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

import ywcai.flow.cfg.AppConfig;

@XmlRootElement
public class PayBack {
			
	public String appid=AppConfig.appid,
	mch_id=AppConfig.mch_id,
	nonce_str,
	sign,
	sign_type="MD5",
	out_trade_no,
	out_refund_no,
	refund_fee_type="CNY",
	op_user_id=AppConfig.mch_id,
	refund_desc="≥‰÷µ ß∞‹";
	public int total_fee,
	refund_fee;
	@Override
	public String toString() {
		return "PayBack [appid=" + appid + ", mch_id=" + mch_id + ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", sign_type=" + sign_type + ", out_trade_no=" + out_trade_no + ", out_refund_no=" + out_refund_no
				+ ", refund_fee_type=" + refund_fee_type + ", refund_desc=" + refund_desc + ", total_fee=" + total_fee
				+ ", refund_fee=" + refund_fee + "]";
	}
	
}
