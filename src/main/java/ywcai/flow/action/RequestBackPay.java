package ywcai.flow.action;


import java.io.Writer;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import ywcai.flow.business.OrderProcess;
import ywcai.flow.cfg.AppConfig;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.model.PayBack;
import ywcai.flow.model.PayBackResult;
import ywcai.flow.util.MD5;
import ywcai.flow.util.MmSige;
import ywcai.flow.util.MyLog;

public class RequestBackPay {
	//向微信平台发起订单回退请求
	public void reqMmBackPay(OrderProcess orderProcess,Orders order) {
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
		stream.alias("xml", PayBack.class);
		PayBack payBack=new PayBack();
		payBack.out_trade_no=order.getOrdernum();
		payBack.out_refund_no="TK"+order.getOrdernum();
		payBack.total_fee=order.getTotalfee();
		payBack.refund_fee=order.getTotalfee();
		payBack.nonce_str=orderProcess.createOrderEcho();
		payBack.sign=createPaySign(payBack);
		String xString=stream.toXML(payBack);
		Client client=ClientBuilder.newClient();
		WebTarget webTarget=client.target(AppConfig.mm_pre_pay_https);
		String res="";
		try {
			 res=webTarget
						.request()
						.post(Entity.entity(xString,MediaType.APPLICATION_XML),String.class);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.INFO("微信退款失败:"+order.getOrdernum()+"，错误原因：调用微信退款接口无响应");
			orderProcess.myOrderPersistent(order, 4);
			return ;
		}
		XStream stream2=new XStream();
		stream2.alias("xml",PayBackResult.class);  
		PayBackResult payBackResult=(PayBackResult)stream2.fromXML(res);
		MyLog.INFO("微信退款返回:"+payBackResult.toString());
		if(payBackResult.return_code.equals("FAIL"))
		{
			//持久化订单退款失败信息;
			MyLog.INFO("微信退款失败:"+order.getOrdernum()+"，错误原因：发起请求的签名错误");	
			orderProcess.myOrderPersistent(order, 4);
			return ;
		}
		if(payBackResult.result_code.equals("FAIL"))
		{
			//持久化订单退款失败信息;
			MyLog.INFO("微信退款失败:"+order.getOrdernum()+"，错误原因："+payBackResult.err_code);	
			orderProcess.myOrderPersistent(order, 4);
			return ;
		}
		orderProcess.myOrderPersistent(order, 5);
	}
	
		private String createPaySign(PayBack payBack)
		{
			HashMap<String,String> hashMap=new HashMap<>();			
			hashMap.put("appid", payBack.appid);
			hashMap.put("mch_id", payBack.mch_id);
			hashMap.put("nonce_str", payBack.nonce_str);
			hashMap.put("sign_type", payBack.sign_type);
			hashMap.put("out_trade_no", payBack.out_trade_no);
			hashMap.put("out_refund_no", payBack.out_refund_no);
			hashMap.put("refund_fee_type", payBack.refund_fee_type);
			hashMap.put("refund_desc", payBack.refund_desc);
			hashMap.put("total_fee", payBack.total_fee+"");
			hashMap.put("refund_fee", payBack.refund_fee+"");
			hashMap.put("op_user_id", payBack.op_user_id);
			String stringA=MmSige.getStringA(hashMap);
			String stringAll=stringA+"key="+AppConfig.mch_key;
			String payBackSign=MD5.md5(stringAll).toUpperCase();
			MyLog.INFO(stringAll);
			MyLog.INFO(payBackSign);
			return payBackSign;
	}
}
