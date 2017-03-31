/*
 * @(#) Check.java
 * Product :
 * Version : 1.0 (2004/07/07)
 * Copyright 2005- by Renesas Technology Corp., All rights reserved.
 */

package org.app.co.jp.util;

import java.math.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author	O.Nakazato
 * @version 1.0
 * @since	JDK 1.4
 */
public abstract class Check{
	
	static String checkStr = null;
	
	/**
	 */
	public static boolean isNull(String str) {
		return str == null || str.equals("");
	}

	/**
	 */
	public static boolean isAlphabet(String str) {
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) > '\u007A' || str.charAt(i) < '\u0041') {
				return false;
			}
			if (str.charAt(i) > '\u005A' && str.charAt(i) < '\u0061') {
				return false;
			}
		}
		return true;
	}

	/**
	 */
	public static boolean isAlphabetOrNumber(String str) {
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) > '\u007A' || str.charAt(i) < '\u0030') {
				return false;
			}
			if (str.charAt(i) > '\u005A' && str.charAt(i) < '\u0061') {
				return false;
			}
			if (str.charAt(i) > '\u0039' && str.charAt(i) < '\u0041') {
				return false;
			}
		}
		return true;
	}

	/**
	 */
	public static boolean isAlphabetOrDigit(String str) {
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) > '\u007f') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 */
	public static boolean isFullKatakana(String str) {
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) < '\u30A1' || str.charAt(i) > '\u30FF') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 */
	public static boolean isHalfKatakana(String str) {
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) < '\uff61' || str.charAt(i) > '\uff9f' ){
				return false;
			}
		}
		return true;
	}

	/**
	 */
	public static boolean isDoubleByte(String str) {
		if (str != null) {
			return str.length() * 2 == Utils.getLength(str);
		}
		return false;
	}

	/**
	 */
	public static boolean isInteger(String str) {
		if (str == null || !isAlphabetOrDigit(str)) {
			return false;
		} else if (str.equals("")) {
			return true;
		}
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 */
	public static boolean isNumericalValue(String str) {
		if (str == null || !isAlphabetOrDigit(str)) {
			return false;
		} else if (str.equals("")) {
			return true;
		}
		try {
			new BigDecimal(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 */
	public static boolean checkLength(String str, int len) {
		if (str == null) {
			return false;
		}
		return Utils.getLength(str) == len;
	}

	/**
	 */
	public static boolean checkLengthUnder(String str, int len) {
		if (str == null) {
			return false;
		}
		return Utils.getLength(str) <= len;
	}

	/**
	 */
	public static boolean checkLengthOver(String str, int len) {
		if (str == null) {
			return false;
		}
		return Utils.getLength(str) >= len;
	}

	/**
	 */
	public static boolean checkLength(String str, int lowerLen, int upperLen) {
		return checkLengthUnder(str, upperLen) && checkLengthOver(str, lowerLen);
	}

	/**
	 */
	public static boolean checkIntegralFigure(String str, int len) {
		if (!isNumericalValue(str)) {
			return false;
		}
		str = cutSign(str);
		return checkLength(Utils.getIntegralPart(str), len);
	}

	/**
	 */
	public static boolean checkIntegralFigureUnder(String str, int len) {
		if (!isNumericalValue(str)) {
			return false;
		}
		str = cutSign(str);
		return checkLengthUnder(Utils.getIntegralPart(str), len);
	}

	/**
	 */
	public static boolean checkIntegralFigureOver(String str, int len) {
		if (!isNumericalValue(str)) {
			return false;
		}
		str = cutSign(str);
		return checkLengthOver(Utils.getIntegralPart(str), len);
	}
	
	/**
	 */
	private static String cutSign(String str) {
		if(!Check.isNull(str)){
			if(str.charAt(0)=='\u002B' || str.charAt(0)=='\u002D'){
				str = str.substring(1);
			}
		}
		return str;
	}

	/**
	 */
	public static boolean checkDecimalFigure(String str, int len) {
		if (!isNumericalValue(str)) {
			return false;
		}
		return checkLength(Utils.getDecimalPart(str), len);
	}

	/**
	 */
	public static boolean checkDecimalFigureUnder(String str, int len) {
		if (!isNumericalValue(str)) {
			return false;
		}
		return checkLengthUnder(Utils.getDecimalPart(str), len);
	}

	/**
	 */
	public static boolean checkDecimalFigureOver(String str, int len) {
		if (!isNumericalValue(str)) {
			return false;
		}
		return checkLengthOver(Utils.getDecimalPart(str), len);
	}

	/**
	 */
	public static boolean isDate(String yyyymmdd) {
		if (!isInteger(yyyymmdd)) {
			return false;
		}

		if (yyyymmdd.length() != 8) {
			return false;
		}

		return isDate(Integer.valueOf(yyyymmdd.substring(0, 4)).intValue()
					, Integer.valueOf(yyyymmdd.substring(4, 6)).intValue()
					, Integer.valueOf(yyyymmdd.substring(6, 8)).intValue());
	}

	/**
	 */
	private static boolean isDate(int yyyy, int mm, int dd) {
		Calendar cal = new GregorianCalendar(yyyy, mm - 1, dd);
		return cal.get(Calendar.YEAR) == yyyy
					&& cal.get(Calendar.MONTH) == mm - 1
					&& cal.get(Calendar.DATE) == dd;
	}

	/**
	 */
	public static int checkDate(String yyyymmddFrom, String yyyymmddTo) {
		if (!isDate(yyyymmddFrom) || !isDate(yyyymmddTo)) {
			return -2;
		}

		try {
			SimpleDateFormat fmtDate = new SimpleDateFormat("yyyyMMdd");
			Date dateFrom = fmtDate.parse(yyyymmddFrom);
			Date DateTo = fmtDate.parse(yyyymmddTo);

			if (dateFrom.before(DateTo)) {
				return 1;
			} else if (dateFrom.equals(DateTo)) {
				return 0;
			} else if (dateFrom.after(DateTo)) {
				return -1;
			}
			return -2;
		} catch (ParseException pe) {
			return -2;
		}
	}

	/**
	 */
	public static boolean isWithinPeriod(String date, String yyyymmddFrom, String yyyymmddTo) {
		int before = checkDate(date, yyyymmddFrom);
		int after  = checkDate(date, yyyymmddTo);
		if ((before == -1 || before == 0)
				&& (after == 1 || after ==0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 */
	public static boolean isSingleOnlyOrDoubleOnlyByte(String str) {
		
		if( isNull(str) ) {
			return true;
		}
		
		if( isDoubleByte(str) || (Utils.getLength(str) == str.length()) ) {
			return true;
		}
		
		return false;
	}
	
	/**
	 */
	public static boolean isSingleByteOrMark(String str) {

        if (checkStr.indexOf(str) >= 0){
        	return true;
        }
		return false;
	}
	
	/**
	 */
	public static boolean isDigitOrUpCaseAlphabet(String str) {
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) < '\u0030' || str.charAt(i) > '\u005a') {
				return false;
			}
			if (str.charAt(i) > '\u0039' && str.charAt(i) < '\u0041') {
				return false;
			}
		}
		return true;
	}
}
