package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class RechargeResult {
public String code,desc;

@Override
public String toString() {
	return "RechargeResult [code=" + code + ", desc=" + desc + "]";
}

//code: 0000表示成功;

}
