/*
 * @(#) Utils.java
 * Product :
 * Version : 1.0 (2004/07/01)
 * Copyright 2005- by Renesas Technology Corp., All rights reserved.
 */

package org.app.co.jp.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.security.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.util.bean.DefaultComboBoxModel;
import org.app.co.jp.util.bean.SelectBean;

/**
 */
public abstract class Utils {

	public static final String FLG_Y = "Y";

	public static final String FLG_N = "N";

	public static final String FLG_1 = "1";

	public static final String FLG_0 = "0";
	
	public static List<Window> allWindow = new ArrayList<Window>();
	
	public static List<Boolean> allStatus = new ArrayList<Boolean>();
	
	public static void addWindow (Window window) {
		if(allWindow.indexOf(window) < 0) {
			allWindow.add(window);
		}
	}
	
	public static void removeWindow (Window window) {
		if(allWindow.indexOf(window) >= 0) {
			allWindow.remove(window);
		}
	}
	
	public static void clearWindow () {
		allWindow.clear();
	}
	
	public static void showAgainWindow () {
		for (Window window : allWindow) {
			window.setVisible(allStatus.get(allWindow.indexOf(window)));
		}
	}
	
	public static void hidenWindow() {
		allStatus.clear();
		for (Window window : allWindow) {
			allStatus.add(window.isVisible());
			window.setVisible(false);
		}
	}

	/**
	 */
	public static String getEncryptionCode(byte[] orignal) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return toHexString(md.digest(orignal));
	}

	/**
	 */
	public static String pad(String str, char padChar, int count) {
		StringBuffer buf = new StringBuffer(str);
		for (int i = getLength(str); i < count; i++) {
			buf.append(padChar);
		}
		return buf.toString();
	}

	/**
	 */
	public static String padFront(String str, char padChar, int count) {
		StringBuffer buf = new StringBuffer(str);
		for (int i = getLength(str); i < count; i++) {
			buf.insert(0, padChar);
		}
		return buf.toString();
	}

	/**
	 */
	public static String replaceString(String str1, String str2, String str3) {
		StringBuffer buffer = new StringBuffer();
		int index = str1.indexOf(str2);

		if (index == -1) {
			return str1;
		}

		buffer.append(str1.substring(0, index));
		buffer.append(str3);
		buffer.append(str1.substring(index + str2.length()));
		return buffer.toString();
	}

	/**
	 */
	public static String getIntegralPart(String str) {
		if (!Check.isNumericalValue(str)) {
			return null;
		}
		int index = str.indexOf(".");
		if (index == -1) {
			return str;
		}
		return str.substring(0, index);
	}

	/**
	 */
	public static String getDecimalPart(String str) {
		if (!Check.isNumericalValue(str)) {
			return null;
		}
		int index = str.indexOf(".");
		if (index == -1) {
			return "0";
		}
		return str.substring(index + 1);
	}

	/**
	 */
	public static String toHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i] & 0xFF;
			int hi = b >> 4;
			int lo = b & 0x0F;
			buf.append(hexDigits[hi]);
			buf.append(hexDigits[lo]);
		}
		return buf.toString();
	}

	/**
	 */
	protected static final char[] hexDigits = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F'
	};

	/**
	 */
	public static String formatNumber(String value, String pattern, int fractionDigits){
		for (int i = 0; value != null && i < value.length(); i++) {
			if (value.charAt(i) == '\u0045'
				|| value.charAt(i) == '\u0065') {
				return value;
			}
		}
		try{
			DecimalFormat df = new DecimalFormat(pattern);
			df.setMaximumFractionDigits(fractionDigits);
			df.setMinimumFractionDigits(fractionDigits);
			return df.format(Double.parseDouble(value));
		}catch (Exception e){
			try {
				pattern = pattern.replaceAll("\\.","/");
				pattern = pattern.replaceAll(",","\\.");
				pattern = pattern.replaceAll("/",",");
				DecimalFormat df = new DecimalFormat(pattern);
				df.setMaximumFractionDigits(fractionDigits);
				df.setMinimumFractionDigits(fractionDigits);
				String val = df.format(Double.parseDouble(value));
				val = val.replaceAll("\\.","/");
				val = val.replaceAll(",","\\.");
				val = val.replaceAll("/",",");
				return val;				
			}catch (Exception pe){
				return value;
			}
		}
	}

	/**
	 */
	public static String getNumber(String value){
		if(Check.isNull(value)){
			return value;
		}
		for (int i = 0; value != null && i < value.length(); i++) {
			if (value.charAt(i) == '\u0045'
				|| value.charAt(i) == '\u0065') {
				return value;
			}
		}
		try {
			BigDecimal num =  new BigDecimal(value);
			return num.toString();
		} catch (NumberFormatException e) {
			return value;
		}
	}

	/**
	 */
	public static String getNumber(String value, String pattern){
		if(Check.isNull(value)){
			return value;
		}
		String chkStr = new String(value);
		
		if(pattern.indexOf(".")<pattern.indexOf(",")){
			chkStr = chkStr.replaceAll("\\.","#");
			chkStr = chkStr.replaceAll(",","\\.");
			chkStr = chkStr.replaceAll("#",",");
		}

		if(Pattern.matches("^[+-]?[0-9][0-9,]*\\.?[0-9]*", chkStr)
			&& Pattern.matches(".*[0-9]$", chkStr)
			&& !Pattern.matches(".*((,.?.?,))+.*", chkStr)
			&& !Pattern.matches(".*,.?.$", chkStr)){
			
			return getNumber(chkStr.replaceAll(",",""));
			
		}else if(Pattern.matches("^[+-]?\\.[0-9][0-9]*", chkStr)){
			return getNumber(chkStr);
			
		}else{
			return value;
		}
	}
	
	/**
	 */
	public static String getUpperCase(String value) {
		if (value == null) {
			return value;
		}
		char chArg[] = value.toCharArray();
		char variable[] = new char[chArg.length];
		for (int i = 0; i < chArg.length; i++) {
			variable[i] = Character.toUpperCase(chArg[i]);
		}
		return new String(variable).trim();
	}

	/**
	 */
	public static String deleteNewParagraph(String str){
		
		if (str.lastIndexOf("\r") != -1 || str.lastIndexOf("\n") != -1 || str.lastIndexOf("\r\n") != -1) {
			str = str.replaceAll("\r","");
			str = str.replaceAll("\n","");
			str = str.replaceAll("\r\n","");
		}
		
		return str;
	}

	/**
	 */
	public static int getLength(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		int ret = 0;
		for (int i = 0; i < str.length(); i++) {
			ret += getCharLength(str.charAt(i));
		}
		return ret;
	}

	/**
	 */
	public static String[] separateString(String str, int separateTerm) {
		if (str == null) return null;

		String[] separateStr = new String[1];

		StringBuffer buffer = new StringBuffer(separateTerm);
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int intByte = getCharLength(c);
			if (count + intByte > separateTerm) {
				count = 0;
				int size = separateStr.length;
				String[] newSeparateStr = new String[size + 1];
				System.arraycopy(separateStr, 0, newSeparateStr, 0, size);
				newSeparateStr[size - 1] = buffer.toString();
				separateStr = newSeparateStr;
				buffer = new StringBuffer(separateTerm);
			}
			count += intByte;
			buffer.append(c);
		}
		separateStr[separateStr.length - 1] = buffer.toString();

		return separateStr;
	}

	/**
	 */
	public static int getCharLength(char c) {
		if (c <= '\u007f') {
			return 1;
		} else if (c >= '\uFF61' && c <= '\uFF9F') {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 */
	public static String replaceAll(String str, String pattern, String replacement) {

		String tmp = str;
		StringBuffer buf = new StringBuffer();

		int iTmp = tmp.indexOf(pattern);

		while (iTmp > -1) {
			buf.append(tmp.substring(0, iTmp));
			buf.append(replacement);
			tmp = tmp.substring(iTmp + pattern.length());
			iTmp = tmp.indexOf(pattern);
		}

		buf.append(tmp);

		return buf.toString();
	}

	/**
	 */
	public static String convertFlg(String value) {

		if (value.equals(FLG_0)) {
			return FLG_N;
		} else if (value.equals(FLG_1)) {
			return FLG_Y;
		}
		return "";
	}

	/**
	 */
	public static String reconvertFlg(String value) {

		if (value.equals(FLG_N)) {
			return FLG_0;
		} else if (value.equals(FLG_Y)) {
			return FLG_1;
		} else if (value.equals(FLG_0)) {
			return FLG_0;
		} else if (value.equals(FLG_1)) {
			return FLG_1;
		}
		return "";
	}
	
	/** 
	 */
	public static void deleteDirectory(String strDirectory) {
		File file = new File(strDirectory);
		if (file.isDirectory()) {
			File []fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					deleteDirectory(fileList[i].getAbsolutePath());
				} else {
					fileList[i].delete();
				}
			}
			file.delete();
		} else {
			if (file.isFile()) {
				file.delete();
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @param strFromFolder
	 * @param strToFolder
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void copyFileByFolder(String strFromFolder, String strToFolder, String strFromId, String strToId) throws FileNotFoundException, IOException {
		
		File fromFolder = new File(strFromFolder);
		File toFolder = new File(strToFolder);
		
		if (!toFolder.exists()) {
			toFolder.mkdirs();
		}
		
		byte [] cache = new byte[1024];
		
		for (File file : fromFolder.listFiles()) {
			try (FileInputStream input = new FileInputStream(file)) {
				File outputFile = new File(strToFolder.concat("////").concat(file.getName().replaceAll(strFromId, strToId)));
				try (FileOutputStream out = new FileOutputStream(outputFile)) {
					int i = 0;
					while ((i = input.read(cache)) > 0) {
						out.write(cache, 0, i);
					}
				}
				
			}
		}
	}
	
	/**
	 * create 'try finally' function 
	 * 
	 * @param strPythonPath
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static String addOuterDealPython(String strPythonPath) throws FileNotFoundException, IOException {
		// temp path
		String tempPath = strPythonPath.substring(0, strPythonPath.lastIndexOf(".")).concat("temp").concat(".py");
		
		try (FileInputStream input = new FileInputStream(strPythonPath); BufferedReader br = new BufferedReader(new InputStreamReader(input))) {

			try (FileOutputStream out = new FileOutputStream(tempPath); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out))) {
				// header
				bw.write("try:");
				bw.newLine();
				bw.write("    initEvidence()");
				bw.newLine();
				
				// content
				String line = "";
				while ((line = br.readLine()) != null) {
					bw.write("    ".concat(line));
					bw.newLine();
				}
				
				// footer
				bw.write("    popup('the end!')");
				bw.newLine();
				bw.write("except Exception, ex:')");
				bw.newLine();
				bw.write("    popup('error!')");
				bw.newLine();
				bw.write("finally:");
				bw.newLine();
				bw.write("    endEvidence()");
				bw.newLine();
			}
		}
		
		return tempPath;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		if ("".equals(str) || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isFileExists(String str) {
		if (str == null) {
			return false;
		}
		if ("".equals(str) || "".equals(str.trim())) {
			return false;
		}
		if (!(new File(str)).exists()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static DefaultComboBoxModel getComponentList() {
		
		Vector<SelectBean> selectList = new Vector<SelectBean>();
		SelectBean blank = new SelectBean();
		blank.setCode("");
		blank.setName("");
		selectList.add(blank);
		
		String componentList[] = CommonConstant.OPTION_COMPONENT_TYPE.split(",");
		
		for (String strComp : componentList) {
			SelectBean bean = new SelectBean();
			bean.setCode(strComp);
			bean.setName(strComp);
			selectList.add(bean);
		}
		
		DefaultComboBoxModel model = new DefaultComboBoxModel(selectList);
		
		return model;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static DefaultComboBoxModel getOperationList() {
		
		
		String componentList[] = CommonConstant.OPTION_OPERATION.split(",");
		
		Vector<SelectBean> selectList = new Vector<SelectBean>();
		SelectBean blank = new SelectBean();
		blank.setCode("");
		blank.setName("");
		selectList.add(blank);
		
		for (String strComp : componentList) {
			SelectBean bean = new SelectBean();
			bean.setCode(strComp);
			bean.setName(strComp);
			selectList.add(bean);
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(selectList);
		
		return model;
	}
	

    public static void initDisplaySchema () {
        //
        try {
            //
            Font lblFont = new Font(null, Font.BOLD, 11);
            Font btnFont = new Font(null, Font.BOLD, 10);
            Font chkFont = new Font(null, Font.BOLD, 10);

            //
            Border lblBorder = BorderFactory.createLineBorder(Color.BLACK);

            // panel
            //UIManager.put("Panel.background", new Color(200,255,255));

            UIManager.put("Button.font", btnFont);
            //UIManager.put("Button.background", new Color(236,198,236));

            UIManager.put("Label.font", lblFont);
            UIManager.put("Label.border", lblBorder);

            UIManager.put("CheckBox.font", chkFont);
//            UIManager.put("CheckBox.background", new Color(200,255,255));

            UIManager.put("ComboBox.font", chkFont);
//            UIManager.put("ComboBox.background", new Color(200,255,255));

            //
            UIManager.put("TextField.disabletextcolor", Color.GRAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}