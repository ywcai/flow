package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PrePayBack {
	public String 
	return_code,
	return_msg,
	appid,
	mch_id,
	device_info,
	nonce_str,
	sign,
	result_code,
	prepay_id,
	trade_type,
	err_code,
	err_code_des;

	@Override
	public String toString() {
		return "PrePayBack [return_code=" + return_code + ", return_msg=" + return_msg + ", appid=" + appid
				+ ", mch_id=" + mch_id + ", device_info=" + device_info + ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", result_code=" + result_code + ", prepay_id=" + prepay_id + ", trade_type=" + trade_type
				+ ", err_code=" + err_code + ", err_code_des=" + err_code_des + "]";
	}
	
}
