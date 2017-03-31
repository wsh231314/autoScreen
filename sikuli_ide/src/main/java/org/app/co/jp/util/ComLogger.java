/*
 * @(#) ComLogger.java
 * Product :
 * Version : 1.0 (2005/11/11)
 * Copyright 2005- by Renesas Technology Corp., All rights reserved.
 */

package org.app.co.jp.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 */
public class ComLogger {

	public static final int LOG_LEVEL_DEBUG	= 0;
	public static final int LOG_LEVEL_INFO	= 1;
	public static final int LOG_LEVEL_ERROR	= 2;

	private static final String LOG_LEVEL_DEBUG_STR	= "[DBG]";
	private static final String LOG_LEVEL_INFO_STR	= "[INF]";
	private static final String LOG_LEVEL_ERROR_STR	= "[ERR]";

	private static final String FILE_EXTENSION = ".log";

	private String outputDir = "";
	private String prefix = "";
	private String lineHeader = "";
	private int logLevel = LOG_LEVEL_INFO;

	private static DecimalFormat df2 = new DecimalFormat("00");
	private static DecimalFormat df3 = new DecimalFormat("000");
	
	/**
	 */
	public ComLogger(int newLogLevel) {

		outputDir = ComPropertyUtil.getProperty("log_directory");
		prefix = "log";
		lineHeader = "###";
		logLevel = newLogLevel;
	}

	/**
	 */
	public ComLogger(String newOutputDir, String newPrefix, String newLineHeader, int newLogLevel) {

		outputDir = newOutputDir;
		prefix = newPrefix;
		lineHeader = newLineHeader;
		logLevel = newLogLevel;
	}

	public void write(int level, String message) {
		
		String mode_str = null;

		if (level == LOG_LEVEL_ERROR) {
			mode_str = LOG_LEVEL_ERROR_STR;

		} else if (level >= logLevel) {
			switch (level) {
				case LOG_LEVEL_DEBUG :
					mode_str = LOG_LEVEL_DEBUG_STR;
					break;
				case LOG_LEVEL_INFO :
					mode_str = LOG_LEVEL_INFO_STR;
					break;
				default :
					throw new IllegalArgumentException("Log-Level(" + level + ") is illegal argument.");
			}

		} else {
			// ���O���x���ɖ����Ȃ��̂ŏo�͂��Ȃ�
			return;
		}

		File logfile = null;
		FileWriter fw = null;
		PrintWriter pw = null;
		
		String current = getFormatNow();
		String date	= current.substring(0,4) + current.substring(5,7) + current.substring(8,10);
		
		String logMessage
			= new StringBuffer()
				.append(current)
				.append(" ")
				.append(mode_str)
				.append(" ")
				.append(lineHeader)
				.append(" ")
				.append(message)
				.toString();
		
		try {
			logfile = new File(outputDir + prefix + "_" + date + FILE_EXTENSION);
			
			if (logfile.exists()) {
			} else {
				logfile.createNewFile();
			}
			
			fw = new FileWriter(logfile.getAbsolutePath(), true);
			pw = new PrintWriter(fw, true);
			pw.println(logMessage);
			
		} catch (Exception e) {
			// NOP
		} finally {

			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException ioe) {
				}
			}
		}
	}
		
	private String getFormatNow()
	{
		
	//	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"), Locale.JAPAN);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+09:00"), Locale.JAPAN);
		
		String year	= Integer.toString(cal.get(Calendar.YEAR));
		String month= df2.format(cal.get(Calendar.MONTH) + 1);
		String date	= df2.format(cal.get(Calendar.DATE));
		String hour	= df2.format(cal.get(Calendar.HOUR_OF_DAY));
		String min	= df2.format(cal.get(Calendar.MINUTE));
		String sec	= df2.format(cal.get(Calendar.SECOND));
		String msec = df3.format(cal.get(Calendar.MILLISECOND));
		
		StringBuffer now_str_buf = new StringBuffer(25);
		now_str_buf.append(year);
		now_str_buf.append("/");
		now_str_buf.append(month);
		now_str_buf.append("/");
		now_str_buf.append(date);
		now_str_buf.append("-");
		now_str_buf.append(hour);
		now_str_buf.append(":");
		now_str_buf.append(min);
		now_str_buf.append(":");
		now_str_buf.append(sec);
		now_str_buf.append(".");
		now_str_buf.append(msec);
		
		return now_str_buf.toString();
	}
	
}

