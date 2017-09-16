package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PayNotifyResult {
public String return_code,return_msg;

@Override
public String toString() {
	return "PayNotifyResult [return_code=" + return_code + ", return_msg=" + return_msg + "]";
}

//返回信息，如非空，为错误原因：签名失败,参数格式校验错误

}
