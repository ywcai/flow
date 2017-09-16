package ywcai.flow.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyTime {

	public static String getNowTime()
	{
		String time="";
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYYMMddHHMMSS");
		time=simpleDateFormat.format(Calendar.getInstance().getTime());
		return time;
	}

	public static String getNowDate()
	{
		String time="";
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YY-MM-dd");
		time=simpleDateFormat.format(Calendar.getInstance().getTime());
		return time;
	}
}
