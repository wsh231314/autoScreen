package org.app.co.jp.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.PatternDao;
import org.app.co.jp.dao.TableDao;

import java.io.*;
import java.util.*;

/**
 *
 * @author a5062903
 *
 */
public class AutoDataUtil {

	private List outputInfoList = new ArrayList();
	
	private Map allValueMap = new HashMap();
	
	private Map numTableIdMap = new HashMap();
	
	private int iPatternStart = 0;
	
	private HSSFCellStyle headerStyle = null;
	
	private HSSFCellStyle dataStyle = null;
	
	public final int intLineNum = 2500;
	
	public boolean creatDataByPattern(String strPattern) {
		
		return true;
	}
	
	/**
	 * @throws Exception
	 */
	public int creatDataByPatternList(List patternList, String strFilePath, int iPattern) throws Exception {
		if (patternList == null || patternList.isEmpty()) {
			return 0;
		}
		for (int i = 0; i < patternList.size(); i++) {
			Map patternMap = (Map)patternList.get(i);
			String patternId = (String)patternMap.get("PATTERN_ID");
			String patternName = (String)patternMap.get("PATTERN_NAME");
			String patternType = (String)patternMap.get("PATTERN_TYPE");
			createByPatternId(patternId, patternName, patternType, strFilePath);
		}
		int iOutpurCnt = createCsvFormatScriptFile(outputInfoList, strFilePath, iPattern);
		
		createExcelScriptFile(outputInfoList, strFilePath, iPattern);
		
		return iOutpurCnt;
	}
	
	/** *
	 */
	private void createByPatternId(String patternId, String patternName, String patternType, String strFilePath) {
		Map tableMap = new LinkedHashMap();
		PatternDao patternDao = new PatternDao();
		TableDao tableDao = new TableDao();
		List tableList = patternDao.searchList(patternId, patternType);
		if (tableList == null || tableList.isEmpty()) {
			return;
		} else {
			for (int i = 0; i < tableList.size(); i++) {
				Map tableTempMap = (Map)tableList.get(i);
				String tableId = (String)tableTempMap.get("TABLE_ID");
				String tableName = (String)tableTempMap.get("TABLE_NAME");
				Map tableListmap = null;
				if (tableMap.containsKey(tableName)) {
					tableListmap = (Map)tableMap.get(tableName);
				} else {
					tableListmap = new LinkedHashMap();
				}
				Map tableDetailMap = tableDao.search(patternId, patternType, tableId);
				tableListmap.put(tableId, tableDetailMap);
				tableMap.put(tableName, tableListmap);
			}
		}
		Map testSignal = new HashMap();
		testSignal.put("TEST_SIGNAL_MARK", patternName);
		outputInfoList.add(testSignal);
		
		allValueMap.clear();
		numTableIdMap.clear();
		
		for (int i = 0; i < outputInfoList.size(); i++) {
			Map tempMap = (Map)outputInfoList.get(i);
			if (tempMap.containsKey("TEST_SIGNAL_MARK")) {
				iPatternStart = i;
			}
		}
		
		Iterator tableIterator = tableMap.keySet().iterator();
		while (tableIterator.hasNext()) {
			String tableName = tableIterator.next().toString();

			Map tableListMap = (Map)tableMap.get(tableName);
			Iterator listInterator = tableListMap.keySet().iterator();
			while(listInterator.hasNext()) {
				String tableId = listInterator.next().toString();
				//
				setTableInfo(patternId, tableId, patternType, tableName, new HashMap(), tableName);
			}
		}
	}
	
	/**
	 *
	 * 
	 * @param patternId
	 * @param strTableId
	 * @param strTypeId
	 * @param tableName
	 * @param valueMap
	 * @param before
	 */
	private void setTableInfo(String patternId, String strTableId, String strTypeId, String tableName, Map valueMap, String before) {
		TableDao dao = new TableDao();
		int iKensu = dao.getTableNumber(patternId, strTypeId, strTableId);

		Map columnMap = dao.searchByDataUtil(patternId, strTypeId, strTableId, tableName);
		if (columnMap != null && !columnMap.isEmpty()) {
		    Map keyMap = new HashMap();
		    Map selfMap = new LinkedHashMap();
		    List selfList = getSelfList(tableName, before);
			Iterator iterator = columnMap.keySet().iterator();
			while (iterator.hasNext()) {
				String strCoulmn = iterator.next().toString();
				Map propertyMap = (Map)columnMap.get(strCoulmn);
				String strFKString = (String)propertyMap.get("COLUMN_FK");
				if (!Check.isNull(strFKString)) {
					String strFKResukt = "";
					//
					String strFKKey = "";
					String strFKTable = strFKString.substring(0, strFKString.indexOf("."));
					String strFKTableID = "";
					if (strFKString.indexOf("[") > 0 && strFKString.indexOf("]") > 0) {
						strFKTableID = strFKString.substring(strFKString.indexOf("[") + 1, strFKString.indexOf("]"));
						strFKKey = strFKString.substring(strFKString.indexOf(".") + 1, strFKString.indexOf("["));
					} else {
						strFKKey = strFKString.substring(strFKString.indexOf(".") + 1);
					}
					if (!Check.isNull(strFKTableID)) {
						strFKResukt = strFKTableID.concat(".").concat(strFKTable);
					} else {
						strFKResukt = strFKTable;
					}
					if (keyMap.containsKey(strFKResukt)) {
						Set set = (Set)keyMap.get(strFKResukt);
						set.add(strFKKey);
					} else {
						Set set = new HashSet();
						set.add(strFKKey);
						keyMap.put(strFKResukt, set);
					}
				}
			}
			
			for (int i = 0; i < iKensu; i++) {
				if (!Check.isNull(strTableId)) {
					String strTestId = strTableId.concat("_").concat(String.valueOf(i + 1));
					if (allValueMap.containsKey(strTestId)) {
						continue;
					}
				}
				Iterator keyIterator = columnMap.keySet().iterator();
			    Map dealMap = new HashMap();
				while(keyIterator.hasNext()) {
					String strColumn = keyIterator.next().toString();
					String value = "";
					Map propertyMap = (Map)columnMap.get(strColumn);
					String strFK = (String)propertyMap.get("COLUMN_FK");
					if (dealMap.containsKey(strColumn)) {
						value = (String)dealMap.get(strColumn);
					} else {
						if (valueMap.containsKey(strColumn)) {
							value = (String)valueMap.get(strColumn);
						} else if (Check.isNull(strFK)){
							String strKotei = (String)propertyMap.get("COLUMN_FIRM");
							if (!Check.isNull(strKotei)) {
								value = strKotei;
							} else {
								String strRadom = (String)propertyMap.get("COLUMN_RADOM");
								if (!Check.isNull(strRadom)) {
									value = getRadomValue(strRadom);
								} else {
									String strPrefix = (String)propertyMap.get("COLUMN_PREFIX");
									String strLength = (String)propertyMap.get("COLUMN_LENGTH");
									value = getAutoCreateValue(tableName, strColumn, strPrefix, strLength);
								}
							}
						}
					}
					if (!Check.isNull(strFK)) {
						if (dealMap.containsKey(strColumn)) {
							selfMap.put(strColumn, value);
						} else {
							strFK = strFK.trim();
							String strFKResukt = "";
							String strFKTable = strFK.substring(0, strFK.indexOf("."));
							String strFKTableID = "";
							if (strFK.indexOf("[") > 0 && strFK.indexOf("]") > 0) {
								strFKTableID = strFK.substring(strFK.indexOf("[") + 1, strFK.indexOf("]"));
								strFK = strFK.substring(strFK.indexOf(".") + 1, strFK.indexOf("["));
							} else {
								strFK = strFK.substring(strFK.indexOf(".") + 1);
							}
							if (!Check.isNull(strFKTableID)) {
								strFKResukt = strFKTableID.concat(".").concat(strFKTable);
							} else {
								strFKResukt = strFKTable;
							}
							Map fkSetMap = getFKMap(patternId, strTypeId, strFKTableID, strFKTable, i);
							Set keySet = (Set)keyMap.get(strFKResukt);
							Map fkRecordDataMap = new HashMap();
							//
							Iterator fkInterator = keySet.iterator();
							while (fkInterator.hasNext()) {
								String strFkTableAndColumn = (String)fkInterator.next();
								String strFKColumn = strFkTableAndColumn.substring(strFkTableAndColumn.indexOf(".") + 1);
								Map propertyFKMap = (Map) columnMap.get(strColumn);
								String strFKValue = "";
								if (fkSetMap.isEmpty()) {
									if (dealMap.containsKey(strFKColumn)) {
										strFKValue = (String) dealMap.get(strFKColumn);
									} else {
										if (valueMap.containsKey(strFKColumn)) {
											strFKValue = (String) valueMap.get(strFKColumn);
										} else {
											String strKotei = (String) propertyFKMap.get("COLUMN_FIRM");
											if (!Check.isNull(strKotei)) {
												strFKValue = strKotei;
											} else {
												String strRadom = (String) propertyFKMap.get("COLUMN_RADOM");
												if (!Check.isNull(strRadom)) {
													strFKValue = getRadomValue(strRadom);
												} else {
													String strPrefix = (String) propertyFKMap.get("COLUMN_PREFIX");
													String strLength = (String) propertyFKMap.get("COLUMN_LENGTH");
													strFKValue = getAutoCreateValue(strFKTable, strFKColumn,strPrefix, strLength);
												}
											}
										}
										fkRecordDataMap.put(strFKColumn, strFKValue);
										dealMap.put(strFKColumn, strFKValue);
										selfMap.put(strFKColumn, strFKValue);
									}
								} else {
									dealMap.put(strFKColumn, fkSetMap.get(strFKColumn));
									selfMap.put(strFKColumn, fkSetMap.get(strFKColumn));
								}

							}
							if (fkSetMap.isEmpty()) {
								if (Check.isNull(strFKTableID)) {
									setTableInfo(null, null, strTypeId, strFKTable, fkRecordDataMap, tableName);
								} else {
									setTableInfo(patternId, strFKTableID, strTypeId, strFKTable, fkRecordDataMap, tableName);
								}
							}
						}
					} else {
						selfMap.put(strColumn, value);
					}
				}
				selfList.add(((LinkedHashMap)selfMap).clone());
				if (!Check.isNull(strTableId)) {
					allValueMap.put(strTableId.concat("_").concat(String.valueOf(i + 1)), ((LinkedHashMap)selfMap).clone());
				} else {
					allValueMap.put(tableName.concat("_").concat(String.valueOf(i + 1)), ((LinkedHashMap)selfMap).clone());
				}
				if (!tableName.equals(before)) {
					break;
				}
			}
		}
	}
	
    public boolean createDataByTable(String strTableName) {
    	return true;
    }
    
    /**
     *
     * @param strRadomString
     * @return
     */
    public String getRadomValue(String strRadomString) {
    	String []radomList = strRadomString.split(",");
    	//
    	int index = (int)(Math.random() * radomList.length);
    	if (index == radomList.length) {
    		index = index - 1;
    	}
    	
    	return radomList[index];
    }
    
    /**
     *
     * @param length
     * @return
     */
    public String getRadomValue(int length) {
    	String selectString = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
    	StringBuffer result = new StringBuffer();
    	for (int i = 0; i < length; i++) {
    		result.append(getRadomValue(selectString));
    	}
    	return result.toString();
    }
    
    /**
     *
     * @param strTable
     * @param strColumn
     * @param strPrefix
     * @param strLength
     * @return
     */
    private String getAutoCreateValue(String strTable, String strColumn, String strPrefix, String strLength) {
    	ComDao dao = new ComDao();
    	TableDao tableDao = new TableDao();
    	//
    	String value = "";
    	int length = 0;
    	Map map = tableDao.getColumnProperty(strTable, strColumn);
    	String strKeyFlg = map.get("key").toString();
    	if (!Check.isNull(strLength)) {
    		length = Integer.parseInt(strLength);
    	} else {
    		String strTempLength = (String)map.get("length");
    		if (!Check.isNull(strTempLength)) {
    			length = Integer.parseInt(strTempLength);
    		}
    	}
    	String strType = map.get("type").toString();
    	if (Boolean.valueOf(strKeyFlg).booleanValue()) {
    		if (CommonConstant.DATA_TYPE_CHAR.equals(strType) 
    				|| CommonConstant.DATA_TYPE_VARCHAR2.equals(strType) 
    				|| CommonConstant.DATA_TYPE_NUMBER.equals(strType)) {
    			String seq = dao.getDataSeq(strTable, strColumn);
    			//
    			if (CommonConstant.DATA_TYPE_NUMBER.equals(strType)) {
    				value = seq;
    			} else {
    				if (seq.length() >= length) {
    					value = seq.substring(0, length);
    				} else {
    					if (!Check.isNull(strPrefix)) {
    						if (seq.length() + strPrefix.length() >= length) {
    							value = strPrefix.substring(0, length - seq.length()).concat(seq);
    						} else {
    							value = strPrefix.concat(Utils.padFront(seq, '0', length - strPrefix.length()));
    						}
    					} else {
    						value = seq;
    					}
    				}
    			}
    		} else if (strType.indexOf(CommonConstant.DATA_TYPE_TIMESTAMP) > 0) {
    			value = "sysdate";
    		}
    	} else {
    		if (CommonConstant.DATA_TYPE_CHAR.equals(strType) 
    				|| CommonConstant.DATA_TYPE_VARCHAR2.equals(strType) 
    				|| CommonConstant.DATA_TYPE_NUMBER.equals(strType)) {
    			//
    			if (CommonConstant.DATA_TYPE_NUMBER.equals(strType)) {
    				//
    				long lPrecision = (long)(Math.random() * Math.pow(10, Integer.parseInt((String)map.get("precision"))));
    				long lScale = (long)(Math.random() * Math.pow(10, Integer.parseInt((String)map.get("scale"))));
    				if (lScale != 0) {
    					value = String.valueOf(lPrecision).concat(".").concat(String.valueOf(lScale));
    				} else {
    					value = String.valueOf(lPrecision);
    				}
    			} else {
					if (!Check.isNull(strPrefix)) {
						if (strPrefix.length() >= length) {
							value = strPrefix.substring(0, length);
						} else {
							value = strPrefix.concat(getRadomValue(length - strPrefix.length()));
						}
					} else {
						value = getRadomValue(length);
					}
    			}
    		} else if (strType.indexOf(CommonConstant.DATA_TYPE_TIMESTAMP) > 0) {
    			value = "sysdate";
    		}
    	}
    	
    	return value;
    }
    
    /**
     *
     */
    private List getSelfList(String strTableName, String strBefore) {
    	//
    	List resultList = null;
    	TableDao tableDao = new TableDao();
    	int iRow = -1;
    	if (strTableName.equals(strBefore)) {
    		for (int i = iPatternStart; i < outputInfoList.size(); i++) {
    			Map propMap = (Map)outputInfoList.get(i);
    			if (propMap.containsKey("TABLE_EN")) {
    				String strTempTableName = (String)propMap.get("TABLE_EN");
    				if (strTableName.equals(strTempTableName)) {
    					iRow = i;
    				}
    			}
    		}
    		if (iRow == -1) {
    			iRow = outputInfoList.size();
    			Map map = new HashMap();
    			map.put("TABLE_EN", strTableName);
    			map.put("TABLE_PRINT", null2empty(tableDao.getTableComment(strTableName)));
    			map.put("TABLE_INFO", new ArrayList());
    			outputInfoList.add(map);
    		}
    		resultList = (List)((Map)outputInfoList.get(iRow)).get("TABLE_INFO");
    	} else {
    		for (int i = iPatternStart; i < outputInfoList.size(); i++) {
    			Map propMap = (Map)outputInfoList.get(i);
    			if (propMap.containsKey("TABLE_EN")) {
    				String strTempTableName = (String)propMap.get("TABLE_EN");
    				if (strTableName.equals(strTempTableName)) {
    		    		resultList = (List)((Map)outputInfoList.get(i)).get("TABLE_INFO");
    		    		return resultList;
    				}
    			}
    		}
    		for (int i = iPatternStart; i < outputInfoList.size(); i++) {
    			Map propMap = (Map)outputInfoList.get(i);
    			if (propMap.containsKey("TABLE_EN")) {
    				String strTempTableName = (String)propMap.get("TABLE_EN");
    				if (strBefore.equals(strTempTableName)) {
    					iRow = i;
    				}
    			}
    		}
    		if (iRow == -1) {
    			iRow = outputInfoList.size();
    			Map map = new HashMap();
    			map.put("TABLE_EN", strTableName);
    			map.put("TABLE_PRINT", null2empty(tableDao.getTableComment(strTableName)));
    			map.put("TABLE_INFO", new ArrayList());
    			outputInfoList.add(map);
    		} else {
    			Map map = new HashMap();
    			map.put("TABLE_EN", strTableName);
    			map.put("TABLE_PRINT", null2empty(tableDao.getTableComment(strTableName)));
    			map.put("TABLE_INFO", new ArrayList());
    			outputInfoList.add(iRow, map);
    		}
    		resultList = (List)((Map)outputInfoList.get(iRow)).get("TABLE_INFO");
    	}
    	
    	return resultList;
    }
    
    /**
     */
    private Map getFKMap(String patternId, String strTypeId, String strTableId, String strTableName, int i) {
    	String strKey = "";
    	if (Check.isNull(strTableId)) {
    		strKey = strTableName.concat("_").concat(String.valueOf(i + 1));
    	} else {
    		strKey = strTableId.concat("_").concat(String.valueOf(i + 1));
    	}
    	if (allValueMap.containsKey(strKey)) {
    		return (Map)allValueMap.get(strKey);
    	} else {
        	if (Check.isNull(strTableId)) {
        		return new HashMap();
        	} else {
        		int iMaxLine = 0;
        		if (numTableIdMap.containsKey(strTableId)) {
        			iMaxLine = Integer.parseInt((String)numTableIdMap.get(strTableId));
        		} else {
        			TableDao dao = new TableDao();
        			iMaxLine = dao.getTableNumber(patternId, strTypeId, strTableId);
        			numTableIdMap.put(strTableId, String.valueOf(iMaxLine));
        		}
        		if (iMaxLine > i) {
        			strKey = strTableId.concat("_").concat(String.valueOf(i + 1));
        		} else {
        			strKey = strTableId.concat("_").concat(String.valueOf(iMaxLine));
        		}
        	}
        	if (allValueMap.containsKey(strKey)) {
        		return (Map)allValueMap.get(strKey);
        	} else {
        		return new HashMap();
        	}
    	}
    }
    
	/**
	 */
	private int createCsvFormatScriptFile(List listInfo, String saveFilePath, int iPattern) throws Exception {
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
				sqlFileInfo.add(sql);
			}
			if (map.containsKey("TABLE_EN") && map.containsKey("TABLE_PRINT")) {
				sql = "--" + map.get("TABLE_PRINT").toString();
				strTable = map.get("TABLE_EN").toString();
				sqlFileInfo.add(sql);
			}
			
			if (!map.containsKey("TEST_SIGNAL_MARK") 
					&& map.containsKey("TABLE_EN") 
					&& map.containsKey("TABLE_PRINT")
					&& map.containsKey("TABLE_INFO")) {
				List tableInfoList = (List)map.get("TABLE_INFO");
				//
				for (int m = 0; m < tableInfoList.size(); m++) {
					Map dateMap = (Map)tableInfoList.get(m);
					sql = getInsertSql(dateMap, strTable);
					if (!isNullOrBlankString(sql)) {
						sqlFileInfo.add(sql);
					}
				}
			}
		}
		
		File file = new File(saveFilePath);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(
				file, false));
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
		
		return 0;
	}
	
	/**
	 */
	private int createExcelScriptFile(List listInfo, String saveFilePath, int iPattern) throws Exception {
		//
		String strTable = "";
		int iRowNow = 1;
		HSSFWorkbook book = new HSSFWorkbook();
		book.createSheet("TestData");
		book.setSheetName(0, "TestData");
		HSSFSheet sheet = book.getSheetAt(0);
		for (int i = 0; i < listInfo.size(); i ++) {
			HSSFRow sheetSignalRow = getRow(sheet, iRowNow);
			Map map = (Map)listInfo.get(i);
			if (map.containsKey("TEST_SIGNAL_MARK")) {
				iRowNow++;
				HSSFCell cell = getCell(sheetSignalRow, 1);
				cell.setCellValue((String)map.get("TEST_SIGNAL_MARK"));
				iRowNow++;
			}
			if (map.containsKey("TABLE_EN") && map.containsKey("TABLE_PRINT")) {
				HSSFRow sheetTableNameRow = getRow(sheet, iRowNow);
				String strTablePrint = (String)map.get("TABLE_PRINT");
				strTable = map.get("TABLE_EN").toString();
				String strDisplayStr = "";
				if (!isNullOrBlankString(strTablePrint)) {
					strDisplayStr = strTablePrint.concat("(").concat(strTable).concat(")");
				} else {
					strDisplayStr = strTable;
				}
				HSSFCell cell = getCell(sheetTableNameRow, 1);
				//
				HSSFFont font = book.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				cell.getCellStyle().setFont(font);
				cell.setCellValue(strDisplayStr);
				iRowNow++;
			}
			
			if (!map.containsKey("TEST_SIGNAL_MARK") 
					&& map.containsKey("TABLE_EN") 
					&& map.containsKey("TABLE_PRINT")
					&& map.containsKey("TABLE_INFO")) {
				List tableInfoList = (List)map.get("TABLE_INFO");
				//
				for (int m = 0; m < tableInfoList.size(); m++) {
					Map dateMap = (Map)tableInfoList.get(m);
					if (m == 0) {
						HSSFRow sheetTableHeaderRow = getRow(sheet, iRowNow);
						Iterator iterator = dateMap.keySet().iterator();
						int intColumnNow = 1;
						while(iterator.hasNext()) {
							HSSFCell cell = getCell(sheetTableHeaderRow, intColumnNow);
							String sKey = (String)iterator.next();
							HSSFCellStyle style = getHeadStyle(book);
							cell.setCellStyle(style);
							cell.setCellValue(sKey);
							intColumnNow ++;
						}
						iRowNow++;
					}
					HSSFRow sheetRow = getRow(sheet, iRowNow);
					Iterator iterator = dateMap.keySet().iterator();
					int intColumnNow = 1;
					while(iterator.hasNext()) {
						HSSFCell cell = getCell(sheetRow, intColumnNow);
						String sKey = (String)iterator.next();
						String sValue = (String)dateMap.get(sKey);
						HSSFCellStyle style = getDataStyle(book);
						cell.setCellStyle(style);
						cell.setCellValue(sValue);
						intColumnNow ++;
					}
					iRowNow++;
				}
				//
				iRowNow = iRowNow + 2;
			}
		}
		//sheet.setDefaultColumnWidth((short)0);
		File file = new File(saveFilePath.substring(0, saveFilePath.lastIndexOf(".")).concat(".xls"));
		OutputStream out = new FileOutputStream(file);
		book.write(out);
		out.close();
		
		return 0;
	}
	
	/**
	 */
	private HSSFCellStyle getHeadStyle(HSSFWorkbook book) {
		if (headerStyle == null) {
			headerStyle = book.createCellStyle();
			headerStyle.setFillForegroundColor(HSSFColor.ROYAL_BLUE.PALE_BLUE.index);
			headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		} 
		return headerStyle;
	}
	
	/**
	 *
	 * @param book
	 * @return
	 */
	private HSSFCellStyle getDataStyle(HSSFWorkbook book) {
		if (dataStyle == null) {
			dataStyle = book.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		} 
		return dataStyle;
	}
	
	/**
	 * @return
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
			if (parameterMap.containsKey(sKey)) {
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
	
	/**
	 */
	public boolean isNullOrBlankString (String strValue) {
		if (strValue == null || strValue.replace(' ', ' ').trim().equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param strValue
	 * @return
	 */
	private String null2empty(String strValue) {
		if (strValue == null) {
			return "";
		} else {
			return strValue;
		}
	}
	
	/**
	 *
	 */
	private HSSFRow getRow(HSSFSheet sheet, int iRow) {
		HSSFRow result = null;
		result = sheet.getRow(iRow);
		if (result == null) {
			result = sheet.createRow(iRow);
		}
		return result;
	}
	
	/**
	 *
	 * @param row
	 * @param iColumn
	 */
	private HSSFCell getCell(HSSFRow row, int iColumn) {
		HSSFCell result = null;
		result = row.getCell((short)iColumn);
		if (result == null) {
			result = row.createCell((short)iColumn);
			result.setCellType(HSSFCell.CELL_TYPE_STRING);
		}
		return result;
	}
}
