package ywcai.flow.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.google.gson.Gson;

import ywcai.flow.cfg.AppConfig;
import ywcai.flow.model.TelCell;
import ywcai.flow.util.MyLog;

public class RequestOther {


	public TelCell  getTelInfo(String mobile)
	{
		TelCell telCell=null;
		Client client=ClientBuilder.newClient();
		WebTarget webTarget=client.target(AppConfig.taobao_tel_look_url+mobile);
		String res=webTarget
				.request()
				.get(String.class).split("=")[1];
		MyLog.INFO(res);
		Gson gson=new Gson();
		telCell=gson.fromJson(res,TelCell.class);
		if(telCell==null)
		{
			telCell=new TelCell();
			telCell.return_code="FAIL";
			telCell.return_msg="请求第三方平台返回数据失败";
			return telCell;
		}
		if(telCell.carrier==null)
		{
			telCell.return_code="FAIL";
			telCell.return_msg="请求第三方平台返回数据失败";
			return telCell;
		}
		if(telCell.carrier.equals(""))
		{
			telCell.return_code="FAIL";
			telCell.return_msg="请求第三方平台返回数据失败";
			return telCell;
		}
		telCell.return_code="SUCCESS";
		MyLog.INFO(telCell.toString());
		return telCell;
	}
}
