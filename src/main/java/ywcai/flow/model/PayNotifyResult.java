package ywcai.flow.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PayNotifyResult {
public String return_code,return_msg;

@Override
public String toString() {
	return "PayNotifyResult [return_code=" + return_code + ", return_msg=" + return_msg + "]";
}

//������Ϣ����ǿգ�Ϊ����ԭ��ǩ��ʧ��,������ʽУ�����

}
