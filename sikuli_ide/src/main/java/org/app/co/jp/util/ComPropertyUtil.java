package org.app.co.jp.util;

import java.io.IOException;
import java.util.Properties;

public class ComPropertyUtil {

	static Properties pro;
	
	BasicLogger logger = BasicLogger.getLogger();
	
	static {
		try {
			pro = new Properties();
			pro.load(ComPropertyUtil.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	ComPropertyUtil (String strPropertyFile) {
		try {
			pro.load(ComPropertyUtil.class.getClassLoader().getResourceAsStream(strPropertyFile));
		} catch (IOException e) {
			logger.exception(e);
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String strKey) {
		return pro.getProperty(strKey);
	}
}
