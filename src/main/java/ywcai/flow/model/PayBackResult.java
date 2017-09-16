package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PayBackResult {	
		public String 
		return_code
		,return_msg	
		,result_code			
		,err_code	
		,err_code_des	
		,appid	
		,mch_id
		,nonce_str	
		,sign	
		,transaction_id
		,out_trade_no	
		,out_refund_no	
		,refund_id
		,fee_type
		,cash_fee_type		
		,coupon_type_$1					
		,coupon_refund_fee	
		,coupon_refund_fee_$1	
		,coupon_refund_count
		,coupon_refund_id_$1;
		public int 
		refund_fee
		,settlement_refund_fee
		,total_fee
		,settlement_total_fee
		,cash_fee
		,cash_refund_fee;
		@Override
		public String toString() {
			return "PayBackResult [return_code=" + return_code + ", return_msg=" + return_msg + ", result_code="
					+ result_code + ", err_code=" + err_code + ", err_code_des=" + err_code_des + ", appid=" + appid
					+ ", mch_id=" + mch_id + ", nonce_str=" + nonce_str + ", sign=" + sign + ", transaction_id="
					+ transaction_id + ", out_trade_no=" + out_trade_no + ", out_refund_no=" + out_refund_no
					+ ", refund_id=" + refund_id + ", fee_type=" + fee_type + ", cash_fee_type=" + cash_fee_type
					+ ", coupon_type_$1=" + coupon_type_$1 + ", coupon_refund_fee=" + coupon_refund_fee
					+ ", coupon_refund_fee_$1=" + coupon_refund_fee_$1 + ", coupon_refund_count=" + coupon_refund_count
					+ ", coupon_refund_id_$1=" + coupon_refund_id_$1 + ", refund_fee=" + refund_fee
					+ ", settlement_refund_fee=" + settlement_refund_fee + ", total_fee=" + total_fee
					+ ", settlement_total_fee=" + settlement_total_fee + ", cash_fee=" + cash_fee + ", cash_refund_fee="
					+ cash_refund_fee + "]";
		}
			
}
