package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrInfo {
	public String errcode,errmsg;

	@Override
	public String toString() {
		return "ErrInfo [errcode=" + errcode + ", errmsg=" + errmsg + "]";
	}
	
}
