package ywcai.flow.model;

public class PrePay {
	public String 
	appid
	,mch_id
//	,device_info
	,nonce_str
	,sign
//	,sign_type
	,body
//	,detail
//	,attach
	,out_trade_no
//	,fee_type
	,spbill_create_ip
//	,time_start
//	,time_expire
//	,goods_tag
	,notify_url
	,trade_type
//	,limit_pay
	,openid;
	public int total_fee;
	@Override
	public String toString() {
		return "PrePay [appid=" + appid + ", mch_id=" + mch_id + ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", body=" + body + ", out_trade_no=" + out_trade_no + ", spbill_create_ip=" + spbill_create_ip
				+ ", notify_url=" + notify_url + ", trade_type=" + trade_type + ", openid=" + openid + ", total_fee="
				+ total_fee + "]";
	}
}
