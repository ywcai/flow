package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PayNotify {
	public String	
	return_code
	,return_msg
	,appid
	,	mch_id
	,	device_info
	,	nonce_str
	,	sign
	,	sign_type
	,	result_code
	,err_code
	,	err_code_des
	,	openid
	,is_subscribe
	,	trade_type
	,	bank_type
	,	fee_type
	,	cash_fee_type
	,		coupon_id_1
	,       coupon_type_1 
	,	transaction_id
	,		out_trade_no
	,		attach
	,		time_end;
	public int total_fee=-1
	,	settlement_total_fee=-1
	,	cash_fee=-1
	,	coupon_fee_1=-1
	,	coupon_count=-1;
	@Override
	public String toString() {
		return "PayNotify [return_code=" + return_code + ", return_msg=" + return_msg + ", appid=" + appid + ", mch_id="
				+ mch_id + ", device_info=" + device_info + ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", sign_type=" + sign_type + ", result_code=" + result_code + ", err_code=" + err_code
				+ ", err_code_des=" + err_code_des + ", openid=" + openid + ", is_subscribe=" + is_subscribe
				+ ", trade_type=" + trade_type + ", bank_type=" + bank_type + ", fee_type=" + fee_type
				+ ", cash_fee_type=" + cash_fee_type + ", coupon_id_1=" + coupon_id_1 + ", coupon_type_1="
				+ coupon_type_1 + ", transaction_id=" + transaction_id + ", out_trade_no=" + out_trade_no + ", attach="
				+ attach + ", time_end=" + time_end + ", total_fee=" + total_fee + ", settlement_total_fee="
				+ settlement_total_fee + ", cash_fee=" + cash_fee + ", coupon_fee_1=" + coupon_fee_1 + ", coupon_count="
				+ coupon_count + "]";
	}

	
		
	
}
