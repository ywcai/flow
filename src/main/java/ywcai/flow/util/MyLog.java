package ywcai.flow.util;

import org.apache.log4j.Logger;



public class MyLog {
	private static final Logger logger;
	static {
		try {
			logger = Logger.getLogger(MyLog.class.getName());

		} catch (Exception ex) {
			System.err.println("Initial Logger creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void DEBUG(String s) {
		logger.debug(s);
	}
	public static void INFO(String s) {
		logger.info(s);
	}
	public static void WARN(String s) {
		logger.warn(s);
	}
	public static void ERR(String s) {
		logger.error(s);
	}
	
}
