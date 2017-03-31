package org.app.co.jp.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.*;
import java.util.*;

public class ExcelUtils {

	public final static String INSERT_MODE = "A";
	public final static String UPDATE_MODE = "U";
	public final static String DELETE_MODE = "D";
	
	public final static int SQL_DEFAULT_MODE = 1;
	public final static int SQL_UPDATE_MODE = 2;
	
	public final int intLineNum = 2500;
	
	private boolean blnParameterChange = true;
	
	/**
	 */
	public void ouputSql(HSSFSheet sheet, String saveFilePath, int iPattern, boolean blnParameterChange) throws Exception {
		List listInfo = new ArrayList();
		boolean bLoop = true;
		//
		this.blnParameterChange = blnParameterChange;
		int iRow = 0;
		int iBlankRow = 0;
		Map headerMap = new LinkedHashMap();
		while(bLoop) {
			HSSFRow hssfRow = sheet.getRow(iRow);
			if (isBlankRow(hssfRow)) {
				iBlankRow ++;
				headerMap.clear();
			} else {
				iBlankRow = 0;
				if(!getTestCaseChange(hssfRow, listInfo)) {
					if (!getTableName(hssfRow, listInfo)) {
						if (!getHeader(hssfRow, listInfo, headerMap)) {
							getDetail(hssfRow, listInfo, headerMap);
						}
					} else {
						headerMap.clear();
					}
				} else {
					headerMap.clear();
				}
			}
			iRow ++;
			if (iBlankRow >= 5) {
				bLoop = false;
			}
		}
		
		if (listInfo.size() > 0) {
			createScriptFile(listInfo, saveFilePath, iPattern);
		}
	}
	
	/**
	 */
	public void ouputCsvFormatSql(HSSFSheet sheet, String saveFilePath, int iPattern, int iStartColumn, int iStartRow, String strFileName, String strSheetName, int iControlColumn, boolean blnParameterChange) throws Exception {
		List listInfo = new ArrayList();
		boolean bLoop = true;
		int iRow = iStartRow;
		int iBlankRow = 0;
		//
		this.blnParameterChange = blnParameterChange;
		
		Map mapSignal = new HashMap();
		mapSignal.put("TEST_SIGNAL_MARK", strFileName);
		listInfo.add(mapSignal);
		
		Map mapTableName = new HashMap();
		mapTableName.put("TABLE_EN", strSheetName);
		mapTableName.put("TABLE_PRINT", strSheetName);
		listInfo.add(mapTableName);
		Map headerMap = new LinkedHashMap();
		//
		HSSFRow hssfRow = sheet.getRow(iRow);
		getCsvFormatHeader(hssfRow, listInfo, headerMap);
		//
		String strControlColumn = "INIT_NO_DATE_@@@@@@";
		if (iControlColumn >= 0) {
			strControlColumn = getCellValue(hssfRow.getCell((short)iControlColumn));
		}
		//
		iRow++;
		while(bLoop) {
			hssfRow = sheet.getRow(iRow);
			if (isBlankRow(hssfRow)) {
				iBlankRow ++;
				headerMap.clear();
			} else {
				iBlankRow = 0;
				getDetail(hssfRow, listInfo, headerMap);
			}
			iRow ++;
			if (iBlankRow >= 5) {
				bLoop = false;
			}
		}
		
		if (listInfo.size() > 0) {
			createCsvFormatScriptFile(listInfo, saveFilePath, iStartColumn, iPattern, strControlColumn);
		}
	}
	
	/**
	 */
	public boolean isBlankRow(HSSFRow row) {
		boolean bResult = true;
		int iCount = 0;
		if (row != null) {
			int iCellNum = row.getLastCellNum();
			for (int i = 0; i < iCellNum; i ++) {
				//
				HSSFCell cell = row.getCell(Short.parseShort(String.valueOf(i)));
			    //
				if (cell != null) {
					if (!isNullOrBlankString(getCellValue(cell))) {
						iCount++;
					}
				}
			}
			if (iCount > 0) {
				return  false;
			}
		} else {
			return true;
		}
		return bResult;
	}
	
	/**
	 */
	public boolean getTestCaseChange(HSSFRow row, List listInfo) {
		boolean bResult = false;
		int iCount = 0;
		String strInfo = "";
		int iCellNum = row.getLastCellNum();
		for (int i = 0; i < iCellNum; i++) {
			//
			HSSFCell cell = row.getCell(Short.parseShort(String.valueOf(i)));
			//
			if (cell != null) {
				if (!isNullOrBlankString(getCellValue(cell))) {
					iCount++;
					strInfo = getCellValue(cell);
				}
			}
		}
		if (iCount == 1 && strInfo.indexOf("TestData") >= 0) {
			bResult = true;
			Map map = new HashMap();
			map.put("TEST_SIGNAL_MARK", strInfo);
			listInfo.add(map);
		}
		return bResult;
	}
	
	/**
	 */
	public boolean getTableName(HSSFRow row, List listInfo) {
		boolean bResult = false;
		int iCount = 0;
		String strInfo = "";
		int iCellNum = row.getLastCellNum();
		for (int i = 0; i < iCellNum; i++) {
			//
			HSSFCell cell = row.getCell(Short.parseShort(String.valueOf(i)));
			//
			if (cell != null) {
				if (!isNullOrBlankString(getCellValue(cell))) {
					iCount++;
					strInfo = getCellValue(cell);
				}
			}
		}
		if (iCount == 1) {
			if (isAlphabetOrNumber(strInfo.replaceAll("_", ""))) {
				bResult = true;
				Map map = new HashMap();
				map.put("TABLE_EN", strInfo);
				map.put("TABLE_PRINT", strInfo);
				listInfo.add(map);
			} else if(strInfo.indexOf("(") >= 0 
					|| strInfo.indexOf("（") >= 0
					|| strInfo.indexOf(")") >= 0
				    || strInfo.indexOf("）") >= 0) {
				int iStart = -1;
				int iEnd = -1;
				if (strInfo.indexOf("(") >= 0) {
					iStart = strInfo.lastIndexOf("(") + 1;
				} else if (strInfo.indexOf("（") >= 0) {
					iStart = strInfo.lastIndexOf("（") + 1;
				}
				if (strInfo.indexOf(")") >= 0) {
					iEnd = strInfo.lastIndexOf(")");
				} else if (strInfo.indexOf("）") >= 0) {
					iEnd = strInfo.lastIndexOf("）");
				}
				String strEnTable = strInfo.substring(iStart, iEnd);
				bResult = true;
				Map map = new HashMap();
				map.put("TABLE_EN", strEnTable);
				map.put("TABLE_PRINT", strInfo);
				listInfo.add(map);
			}
		}
		return bResult;
	}

	
	/**
	 *
	 * @return
	 */
	public boolean getHeader(HSSFRow row, List listInfo, Map headerMap) {
		boolean bResult = false;
		int iCount = 0;
		//
		if (!headerMap.isEmpty()) {
			return false;
		}
		int iCellNum = row.getLastCellNum();
		for (int i = 0; i < iCellNum; i++) {
			//
			HSSFCell cell = row.getCell(Short.parseShort(String.valueOf(i)));
			//
			if (cell != null) {
				if (isDoubleByte(getCellValue(cell).replaceAll("_", ""))) {
					iCount++;
				}
			}
		}
		boolean bTable = false;
		if (listInfo != null && listInfo .size() >= 1) {
			Map tableMap = (Map)listInfo.get(listInfo .size() - 1);
			if (tableMap.containsKey("TABLE_EN") && tableMap.size() == 2) {
				bTable = true;
			}
		}
		if (iCount <= 0 && bTable) {
			// 
			bResult = true;
			for (int i = 0; i < iCellNum; i++) {
				//
				HSSFCell cell = row.getCell(Short.parseShort(String.valueOf(i)));
				//
				if (cell != null) {
					String sKey = getCellValue(cell);
					if (!isNullOrBlankString(getCellValue(cell))) {
						headerMap.put(sKey, String.valueOf(i));
					}
				}
			}
		}
		return bResult;
	}
	
	/**
	 * @return
	 */
	private boolean getCsvFormatHeader(HSSFRow row, List listInfo, Map headerMap) {
		boolean bResult = false;
		int iCellNum = row.getLastCellNum();
		bResult = true;
		for (int i = 0; i < iCellNum; i++) {
			//
			HSSFCell cell = row.getCell(Short.parseShort(String.valueOf(i)));
			//
			if (cell != null) {
				String sKey = getCellValue(cell);
				if (!isNullOrBlankString(getCellValue(cell))) {
					headerMap.put(sKey.trim(), String.valueOf(i));
				}
			}
		}
		return bResult;
	}
	
	/**
	 */
	public boolean getDetail(HSSFRow row, List listInfo, Map headerMap) {
		boolean bResult = false;
		int iStart = 0;
		boolean bDetailAddFlg = true;
		Map detailMap = new LinkedHashMap();
		if (!headerMap.isEmpty()) {
			Iterator iterator = headerMap.keySet().iterator();
			while(iterator.hasNext()) {
				String sKey = (String)iterator.next();
				short iPos = Short.parseShort(headerMap.get(sKey).toString());
				String strValue = getCellValue(row, iPos);
				//
				if (iStart == 0 && strValue.indexOf("No Data") >= 0) {
					bDetailAddFlg = false;
				}
				// 
				detailMap.put(sKey, strValue);
				iStart++;
			}
			// 
			if (bDetailAddFlg) {
				listInfo.add(detailMap);
			}
		}
		return bResult;
	}
	
	/**
	 */
	private void createScriptFile(List listInfo, String saveFilePath, int iPattern) throws Exception{
		//
		String strTable = "";
		// 
		List sqlFileInfo = new ArrayList();
		for (int i = 0; i < listInfo.size(); i ++) {
			Map map = (Map)listInfo.get(i);
			//
			String sql = "";
			if (map.containsKey("TEST_SIGNAL_MARK")) {
				sql = "--" + map.get("TEST_SIGNAL_MARK");
			}
			if (map.containsKey("TABLE_EN") && map.containsKey("TABLE_PRINT")) {
				sql = "--" + map.get("TABLE_PRINT").toString();
				strTable = map.get("TABLE_EN").toString();
			}
			
			if (!map.containsKey("TEST_SIGNAL_MARK") 
					&& !map.containsKey("TABLE_EN") 
					&& !map.containsKey("TABLE_PRINT")) {
				if (iPattern == 1) {
					sql = getInsertSql(map, strTable);
				} else {
					sql = getUpdateSql(map, strTable);
				}
			}
			if (!isNullOrBlankString(sql)) {
				sqlFileInfo.add(sql);
			}
		}
		
		File file = new File(saveFilePath);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				file, true));
		BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(out));

		//
		String strLineSeparator = System.getProperty("line.separator");

		for (int i = 0; i < sqlFileInfo.size(); i++) {
			StringBuffer line = new StringBuffer(sqlFileInfo.get(i)
					.toString());
			line.append(strLineSeparator);
			bufferedWriter.write(line.toString().toCharArray());
		}
		bufferedWriter.close();
	}
	
	/**
	 */
	private void createCsvFormatScriptFile(List listInfo, String saveFilePath, int iColumn, int iPattern, String strControlColumn) throws Exception {
		//
		String strTable = "";
		// 
		List sqlFileInfo = new ArrayList();
		for (int i = 0; i < listInfo.size(); i ++) {
			Map map = (Map)listInfo.get(i);
			//
			String sql = "";
			if (map.containsKey("TEST_SIGNAL_MARK")) {
				sql = "--" + map.get("TEST_SIGNAL_MARK");
			}
			if (map.containsKey("TABLE_EN") && map.containsKey("TABLE_PRINT")) {
				sql = "--" + map.get("TABLE_PRINT").toString();
				strTable = map.get("TABLE_EN").toString();
			}
			
			if (!map.containsKey("TEST_SIGNAL_MARK") 
					&& !map.containsKey("TABLE_EN") 
					&& !map.containsKey("TABLE_PRINT")) {
				if (iColumn != 0) {
					if (iPattern == SQL_DEFAULT_MODE) {
						String strModeStr = (String)map.get(strControlColumn);
						removeValueFromMap(map, iColumn);
						if (strModeStr != null) {
							if (strModeStr.equals(INSERT_MODE)) {
								sql = getInsertSql(map, strTable);
							} else if (strModeStr.equals(UPDATE_MODE)) {
								sql = getUpdateSql(map, strTable);
							} else if (strModeStr.equals(DELETE_MODE)) {
								sql = getDeleteSql(map, strTable);
							}
						} else {
							sql = getInsertSql(map, strTable);
						}
					} else {
						removeValueFromMap(map, iColumn);
						sql = getUpdateSql(map, strTable);
					}
				} else {
					removeValueFromMap(map, iColumn);
					if (iPattern == 1) {
						sql = getInsertSql(map, strTable);
					} else {
						sql = getUpdateSql(map, strTable);
					}
				}
			}
			if (!isNullOrBlankString(sql)) {
				sqlFileInfo.add(sql);
			}
		}
		
		File file = new File(saveFilePath);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				file, true));
		BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(out));

		//
		String strLineSeparator = System.getProperty("line.separator");

		for (int i = 0; i < sqlFileInfo.size(); i++) {
			StringBuffer line = new StringBuffer(sqlFileInfo.get(i)
					.toString());
			line.append(strLineSeparator);
			bufferedWriter.write(line.toString().toCharArray());
		}
		bufferedWriter.close();
	}
	
	/**
	 */
	private String getInsertSql(Map map, String strTable) {
		StringBuffer strRetult = new StringBuffer();
		strRetult.append("INSERT INTO ").append(strTable).append(" (");

		StringBuffer strColumn = new  StringBuffer();
		StringBuffer strValue = new  StringBuffer();
		Iterator iterator = map.keySet().iterator();
		Map parameterMap = ParameterUtil.getParameterMap();
		while(iterator.hasNext()) {
			String sKey = (String)iterator.next();
			String value = (String)map.get(sKey);
			strColumn.append(sKey).append(", ");
			if (parameterMap.containsKey(sKey) && blnParameterChange) {
				strValue.append(parameterMap.get(sKey)).append(", ");
			} else {
				strValue.append("'").append(value).append("', ");
			}
		}
		strColumn.deleteCharAt(strColumn.lastIndexOf(","));
		strValue.deleteCharAt(strValue.lastIndexOf(","));
		
		strRetult.append(strColumn).append(") VALUES (").append(strValue).append(")");
		return changeLine(strRetult, intLineNum).concat(";");
	}
	
	/**
	 */
	private String getUpdateSql(Map map, String strTable) {
		XMLUtils utils = new XMLUtils();
		StringBuffer strRetult = new StringBuffer();
		List listKey = utils.getKeyMap(strTable);
		strRetult.append("UPDATE ").append(strTable).append(" SET ");
		Iterator iterator = map.keySet().iterator();
		Map parameterMap = ParameterUtil.getParameterMap();
		while(iterator.hasNext()) {
			String sKey = (String)iterator.next();
			//
			if (listKey.contains(sKey)) {
				continue;
			}
			String value = (String)map.get(sKey);
			strRetult.append(sKey).append(" = ");
			if (parameterMap.containsKey(sKey) && blnParameterChange) {
				strRetult.append(parameterMap.get(sKey)).append(", ");
			} else {
				strRetult.append("'").append(value).append("', ");
			}
		}
		strRetult.deleteCharAt(strRetult.lastIndexOf(","));
		
		for (int i = 0; i < listKey.size(); i++) {
			String strKey = listKey.get(i).toString();
			String strValue = map.get(strKey).toString();
			if (i == 0) {
				strRetult.append(" WHERE ").append(strKey).append(" = '").append(strValue).append("' ");
			} else {
				strRetult.append(" AND ").append(strKey).append(" = '").append(strValue).append("' ");
			}
		}
		
		return changeLine(strRetult, intLineNum).concat(";");
	}
	
	/**
	 */
	private String getDeleteSql(Map map, String strTable) {
		XMLUtils utils = new XMLUtils();
		StringBuffer strRetult = new StringBuffer();
		strRetult.append("DELETE FROM ").append(strTable);
		
		List listKey = utils.getKeyMap(strTable);
		for (int i = 0; i < listKey.size(); i++) {
			String strKey = listKey.get(i).toString();
			String strValue = map.get(strKey).toString();
			if (i == 0) {
				strRetult.append(" WHERE ").append(strKey).append(" = '").append(strValue).append("' ");
			} else {
				strRetult.append(" AND ").append(strKey).append(" = '").append(strValue).append("' ");
			}
		}
		
		return strRetult.toString().concat(";");
	}
	
	/**
	 */
	public boolean isNullOrBlankString (String strValue) {
		if (strValue == null || strValue.replace(' ', ' ').trim().equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 */
	public boolean isAlphabetOrNumber(String str) {
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
	private String getCellValue(HSSFRow row, short iColumn) {
		//
		String sResult = "";
		//
		HSSFCell cell = row.getCell(iColumn);
		//
		if (cell != null) {
			// �L�[
			sResult = getCellValue(cell);
		}
		return sResult;
	}
	
	/**
	 */
	public boolean isDoubleByte(String str) {
		if (str != null && !str.equals("")) {
			return str.length() * 2 == getLength(str);
		}
		return false;
	}
	
	/**
	 */
	public int getLength(String str) {
		int length = 0;
		for (int i = 0; str != null && i < str.length(); i++) {
			length += getCharLength(str.charAt(i));
		}
		return length;
	}

	/**
	 */
	public int getCharLength(char c) {
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
	private String getCellValue (HSSFCell cell) {
		//
		String strValue = "";
		//
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			strValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			strValue = String.valueOf(cell.getNumericCellValue());
		} else {
			strValue = cell.getStringCellValue().trim();
			if (isNullOrBlankString(strValue)) {
				strValue = "";
			} else {
				boolean isNum = false;
				try 
				{
					Integer.parseInt(rTrim(strValue));
					isNum = true;
				} catch (Exception e) {
					isNum = false;
				}
				if (isNum) {
					strValue = rTrim(strValue);
				}
			}
		}
		
		return strValue;
	}
	
	/**
	 */
	private void removeValueFromMap(Map map, int iPos) {
		int iSize = 0;
		//
		List removeList = new ArrayList();
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			if (iSize >= iPos) {
				break;
			}
			String strKey = iterator.next().toString();
			removeList.add(strKey);
			iSize++;
		}
		for (int i = 0; i < removeList.size(); i++) {
			map.remove(removeList.get(i).toString());
		}
	}
	
	/**
	 */
	public String rTrim(String sValue) {
		if (sValue == null || sValue.equals("")) {
			return sValue;
		}
		
		while(sValue.charAt(sValue.length() - 1) == ' ') {
			sValue = sValue.substring(0, sValue.length() - 1);
		}
		
		return sValue;
	}
	
	/**
	 */
	public String changeLine(StringBuffer sb, int iLineCnt) {
		StringBuffer result = new StringBuffer();
		String strCut = "";
		
		//
		while (sb.length() > iLineCnt) {
			strCut = sb.substring(0, sb.lastIndexOf(",", iLineCnt) + 1);
			result.append(strCut);
			result.append("\r\n");
			sb.delete(0, sb.lastIndexOf(",", iLineCnt) + 1);
		}
		
		if (sb.length() > 0) {
			result.append(sb.toString());
		} else {
			result.delete(result.lastIndexOf("\r\n"), result.length());
		}
		
		return result.toString();
	}
}
