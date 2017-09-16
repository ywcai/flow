package ywcai.flow.action;


import java.io.Writer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ywcai.flow.action.inf.RequestMmInf;
import ywcai.flow.cfg.AppConfig;
import ywcai.flow.model.MmKey;
import ywcai.flow.model.PrePay;
import ywcai.flow.model.PrePayBack;
import ywcai.flow.util.MyLog;

public class RequestMm implements RequestMmInf {

	@Override
	public MmKey getMmKey(String jsCode, String authCode) {
		String url=AppConfig.mm_secret_https;
		url+= "appid="+AppConfig.appid;
		url+="&secret="+AppConfig.app_secret;
		url+="&js_code="+jsCode;
		url+="&grant_type="+authCode;
		Client client=ClientBuilder.newClient();
		WebTarget webTarget=client.target(url);
		String jsonString=webTarget.request()
				.get(String.class);
		Gson gson=new Gson();
		MmKey mmKey =gson.fromJson(jsonString, MmKey.class);
		return mmKey;
	}

	//正式向微信平台发起订单支付请求
	@Override
	public PrePayBack reqMmPay(PrePay prePay) {
		//解决stream双下划线的问题和增加数据标记问题
		XStream stream = new XStream(new XppDriver(new NoNameCoder()) {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    // 对所有xml节点的转换都增加CDATA标记
                    boolean cdata = true;
                    @Override
                    @SuppressWarnings("rawtypes")
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }

                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }


                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
		stream.alias("xml", PrePay.class);
		String xString=stream.toXML(prePay);
//		MyLog.INFO("发起预支付="+prePay.toString());
		Client client=ClientBuilder.newClient();
		WebTarget webTarget=client.target(AppConfig.mm_pre_pay_https);
		String res=webTarget
				.request()
				.post(Entity.entity(xString,MediaType.APPLICATION_XML),String.class);
//		MyLog.INFO("预支付返回="+res);
		XStream stream2=new XStream();
		stream2.alias("xml",PrePayBack.class);  
		PrePayBack prePayBack=(PrePayBack)stream2.fromXML(res);
		return prePayBack;
	}

}
