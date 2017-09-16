package ywcai.flow.model;

public class TelCell {
	public String areaVid,ispVid,carrier;
	public String return_code,return_msg;
	public String getAreaVid() {
		return areaVid;
	}
	public void setAreaVid(String areaVid) {
		this.areaVid = areaVid;
	}
	public String getIspVid() {
		return ispVid;
	}
	public void setIspVid(String ispVid) {
		this.ispVid = ispVid;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	@Override
	public String toString() {
		return "TelCell [areaVid=" + areaVid + ", ispVid=" + ispVid + ", carrier=" + carrier + ", return_code="
				+ return_code + ", return_msg=" + return_msg + "]";
	}

}
