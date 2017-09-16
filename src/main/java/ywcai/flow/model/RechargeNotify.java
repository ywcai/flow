package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RechargeNotify {
	public String userid,timestamp,ordernum,state,mobile,desc,sign;
	@Override
	public String toString() {
		return "RechargeNotify [userid=" + userid + ", timestamp=" + timestamp + ", ordernum=" + ordernum + ", state="
				+ state + ", mobile=" + mobile + ", desc=" + desc + ", sign=" + sign + "]";
	}
	
	
}
