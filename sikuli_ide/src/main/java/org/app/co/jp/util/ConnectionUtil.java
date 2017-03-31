package org.app.co.jp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionUtil {

	private Properties con;
	
	private String fileName = "";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	public ConnectionUtil () {
		try {
			fileName = ComPropertyUtil.getProperty("connection_file");
			InputStream is = new FileInputStream(fileName);
			con = new Properties();
			con.load(is);
			is.close();
		} catch (IOException e) {
			logger.exception(e);
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 */
	public void loadConnectFile() {
		try {
			InputStream is = new FileInputStream(fileName);
			
			con = new Properties();
			con.load(is);
			is.close();
		} catch (IOException e) {
			logger.exception(e);
			e.printStackTrace();
		}
	}
	
	/**
	 */
	public void setProperty(String key, String value) {
		if (con == null) {
			loadConnectFile();
		}
		con.put(key, value);
	}
	
	/**
	 */
	public String getProperty(String key) {
		if (con == null) {
			loadConnectFile();
		}
		return con.get(key).toString();
	}
	
	/**
	 *
	 */
	public void save() {
		if (con == null) {
			return;
		}
		
		File file = null;
		FileOutputStream fos = null;
		
		try {
			file = new File(fileName);
			fos = new FileOutputStream(file);
			con.store(fos, null);
		} catch (Exception e) {
			logger.exception(e);
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.exception(e);
				fos = null;
			}
		}
	}
}
