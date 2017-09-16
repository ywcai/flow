package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReChargeEntry {
	public String
	userid
	,orderid
	,echo
	,timestamp
	,version
	,packcode
	,mobile
	,flowtype
	,callback_url
	,chargeSign;
	@Override
	public String toString() {
		return "MrOrder [userid=" + userid + ", orderid=" + orderid + ", echo=" + echo + ", timestamp=" + timestamp
				+ ", version=" + version + ", packcode=" + packcode + ", mobile=" + mobile + ", flowtype=" + flowtype
				+ ", callback_url=" + callback_url + ", chargeSign=" + chargeSign + "]";
	}	
}
