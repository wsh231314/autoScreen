/*
 * @(#) BasicLogger.java
 * Product :
 * Version : 1.0 (2004/07/07)
 * Copyright 2005- by Renesas Technology Corp., All rights reserved.
 */

package org.app.co.jp.util;

import java.io.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author	O.Nakazato
 * @version	1.0
 * @since	JDK 1.4
 */
public class BasicLogger implements Serializable {
	private static Logger logger = null;

	public static final int LEVEL_SEVERE  = 1;

	public static final int LEVEL_WARNING = 2;

	public static final int LEVEL_INFO    = 3;

	public static final int LEVEL_CONFIG  = 4;

	public static final int LEVEL_DEBUG   = 5;

	/**
	 *
	 */
	protected BasicLogger() {
		if (logger == null) {
			logger = Logger.getLogger("A1");
			PropertyConfigurator.configure(".\\conf\\log4j.properties");
		}
	}

	/**
	 */
	protected BasicLogger(String name) {
		if (logger == null) {
			logger = Logger.getLogger(name);
			PropertyConfigurator.configure(".\\conf\\log4j.properties");
		}
	}

	/**
	 */
	public static BasicLogger getLogger() {
		return new BasicLogger();
	}

	/**
	 */
	public static BasicLogger getLogger(String name) {
		return new BasicLogger(name);
	}

	/**
	 */
	public void severe(String msg) {
		severe(null, msg);
	}

	/**
	 */
	public void severe(String userId, String msg) {
		logger.fatal(msg);
	}

	/**
	 */
	public void warning(String msg) {
		warning(null, msg);
	}

	/**
	 */
	public void warning(String userId, String msg) {
		logger.warn(msg);
	}

	/**
	 */
	public void info(String msg) {
		info(null, msg);
	}

	/**
	 */
	public void info(String userId, String msg) {
		logger.fatal(msg);
	}


	/**
	 */
	public void debug(String msg) {
		debug(null, msg);
	}

	/**
	 */
	public void debug(String userId, String msg) {
		logger.debug(msg);
	}

	/**
	 */
	public void exception(Throwable e) {
		exception("", e);
	}

	/**
	 */
	public void exception(String msg, Throwable e) {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(bos));
			severe(msg + "\n" + bos.toString());
		} catch (Exception e0) {
		} finally {
			try {
				if (bos != null) bos.close();
			} catch (Exception e1) {}
		}
	}

	/**
	 */
	public void startLog(String methodName) {
		startLog(null, methodName);
	}

	/**
	 */
	public void startLog(String userId, String methodName) {
		logger.info("start " + methodName);
	}

	/**
	 */
	public void endLog(String methodName) {
		endLog(null, methodName);
	}

	/**
	 */
	public void endLog(String userId, String methodName) {
		logger.info("end " + methodName);
	}

	/**
	 */
	protected String[] inferCaller() {
		String[] caller = new String[2];
		String thisClassName = BasicLogger.class.getName();
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		int index = 0;
		while (index < stack.length) {
			StackTraceElement frame = stack[index];
			String cname = frame.getClassName();
			if (cname.equals(thisClassName)) {
				break;
			}
			index++;
		}

		while (index < stack.length) {
			StackTraceElement element = stack[index];
			String cname = element.getClassName();
			if (!cname.equals(thisClassName)) {
				caller[0] = cname;
				caller[1] = element.getMethodName();
				return caller;
			}
			index++;
		}
		return caller;
	}

}
