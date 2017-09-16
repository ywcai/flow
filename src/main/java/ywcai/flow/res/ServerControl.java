package ywcai.flow.res;



import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import org.glassfish.jersey.message.internal.ReaderWriter;
import com.google.gson.Gson;
import ywcai.flow.action.ResponseApp;
import ywcai.flow.action.ResponseMm;
import ywcai.flow.action.ResponseMr;
import ywcai.flow.action.inf.ResponseAppInf;
import ywcai.flow.action.inf.ResponseMmInf;
import ywcai.flow.action.inf.ResponseMrInf;
import ywcai.flow.hibernate.Orders;
import ywcai.flow.hibernate.Product;
import ywcai.flow.model.PayNotify;
import ywcai.flow.model.PayNotifyResult;
import ywcai.flow.model.PrePayBack;
import ywcai.flow.model.RechargeNotify;
import ywcai.flow.model.RechargeResult;
import ywcai.flow.model.TelCell;
import ywcai.flow.util.MyLog;


@Path("mr")
@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
public class ServerControl {
	ResponseAppInf responseAppInf=new ResponseApp();
	ResponseMrInf responseMrInf=new ResponseMr();
	ResponseMmInf responseMmInf=new ResponseMm();



	//发起预支付请求,预支付完成后返回给小程序，由小程序吊起支付请求.
	@POST
	@Path("/order/prepay")
	public Response prePay(
			@FormParam("jsCode") String jsCode,
			@FormParam("mobile") String mobile,
			@FormParam("product") int product,
			@FormParam("fastPay") int fastPay,
			@Context HttpServletRequest request
			) 
	{
//		MyLog.INFO("小程序发起订购请求:jsCode="+jsCode+"&&mobile="+mobile+"&&product="+product+"&&fastPay="+fastPay);
		PrePayBack prePayBack=responseAppInf.resPrePay(jsCode,mobile,product,fastPay,request);
		MyLog.INFO("返回小程序的订购请求结果:"+prePayBack.toString());
		return Response.ok(prePayBack).build();
	}

	//	
	//	//小程序端微信支付完成后回调通知到自建平台
	@POST
	@Path("/order/pay/end")
	@Produces(MediaType.APPLICATION_XML)
	public Response payEnd(JAXBElement<PayNotify> element) {
		// TODO Auto-generated method stub
		PayNotify payNotify=element.getValue();
//		MyLog.INFO("微信支付完成通知:"+payNotify.transaction_id);//显示商户订单号
		//返回数据接收状态，并不需要返回业务处理逻辑
		PayNotifyResult payNotifyResult=responseMmInf.mmPayCallBack(payNotify);
		MyLog.INFO("微信平台支付单号:"+payNotify.transaction_id+" , 商户单号:"+payNotify.out_trade_no+" , 商户平台校验订单结果:"+payNotifyResult.toString());
		return Response.ok(payNotifyResult).build();
	}
	//	
	//	
	//	//小程序查询用户充值记录
	@POST
	@Path("/order/lookup")
	public Response reqLookOrder(@FormParam("jsCode") String jsCode) {
		// TODO Auto-generated method stub
		List<Orders> list=responseAppInf.resLookOrder(jsCode);
		return Response.ok(list).build();
	}
	//
	//	
	//	//米瑞第三方充值结果的回调
	@POST
	@Path("/order/recharge/back")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_PLAIN)
	public Response resRechargeBack(InputStream inputStream){
		String json="";
		try {
			json=ReaderWriter.readFromAsString(inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			json=URLDecoder.decode(json,"utf-8");
		} catch (Exception e) {
			MyLog.ERR(e.getMessage());
		}
		MyLog.INFO("米瑞充值完成的回调消息:"+json);//需要保存，无法在其他渠道查询数据
		Gson gson=new Gson();
		RechargeNotify rechargeNotify=null;
		rechargeNotify=gson.fromJson(json, RechargeNotify.class);
		RechargeResult rechargeResult= responseMrInf.resRechargeBack(rechargeNotify);
		String result=gson.toJson(rechargeResult);
//		MyLog.INFO("充值回调返回:"+result);//需要保存，无法在其他渠道查询数据
		return Response.ok(result).build();
	}


	@POST
	@Path("/product")
	public Response resProducts(@FormParam("province") String province,@FormParam("local") int local)
	{
		List<Product>  products=responseMmInf.getProducts(province,local);
		return Response.ok(products).build();
	}

	@GET
	@Path("/tel/{mobile}")
	public Response resTellCell(@PathParam("mobile") String mobile)
	{
		TelCell telCell=responseAppInf.resLookTel(mobile);
		MyLog.INFO("号码归属地查询:"+telCell.toString());//需要保存，无法在其他渠道查询数据
		return Response.ok(telCell).build();
	}
}
