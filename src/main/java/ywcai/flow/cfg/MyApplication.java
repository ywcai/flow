package ywcai.flow.cfg;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import ywcai.flow.res.ServerControl;



public class MyApplication extends ResourceConfig  {

	public  MyApplication()
	{
		 register(ServerControl.class);
		 register(JacksonJsonProvider.class);
		 register(LoggingFilter.class);
	}
}
