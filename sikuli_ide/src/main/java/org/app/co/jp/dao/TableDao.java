package org.app.co.jp.dao;

import org.app.co.jp.com.BaseDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.XMLUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TableDao extends BaseDao{

	//
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 */
	public Map search(String strPattern, String strType, String strTableId) {
		Map resultMap = new LinkedHashMap();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(strTableId).concat(".xml");
		} else {
			strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(strTableId).concat(".xml");
		}
		String strXPATH = "//TABLE/COLUMN";
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Element node = (Element)commonList.get(i);
				String columnName = node.attributeValue("NAME");
				String strFirm = node.selectSingleNode("COLUMN_FIRM").getText();
				String strFk = node.selectSingleNode("COLUMN_FK").getText();
				String strPrefix = node.selectSingleNode("COLUMN_PREFIX").getText();
				String strRadom = node.selectSingleNode("COLUMN_RADOM").getText();
				String strLength = node.selectSingleNode("COLUMN_LENGTH").getText();
				Map map = new HashMap();
				map.put("COLUMN_FIRM", strFirm);
				map.put("COLUMN_FK", strFk);
				map.put("COLUMN_PREFIX", strPrefix);
				map.put("COLUMN_RADOM", strRadom);
				map.put("COLUMN_LENGTH", strLength);
				resultMap.put(columnName, map);
			}
		}
		return resultMap;
	}
	
	/**
	 */
	public List getColumns(String strTableName) {
		List resultList = new ArrayList();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		strFilePath = tableDir.concat(strTableName.toUpperCase()).concat(".xml");
		String strXPATH = "//table/column/name";
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Element node = (Element)commonList.get(i);
				resultList.add(node.getTextTrim());
			}
		}
		return resultList;
	}
	
	/**
	 */
	public boolean checkColumn(String strTableName, String strColumn) {
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		strFilePath = tableDir.concat(strTableName.toUpperCase()).concat(".xml");
		String strXPATH = "//table/column[name='".concat(strColumn).concat("']");
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList == null || commonList.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 */
	public Map getColumnProperty(String strTableName, String strColumn) {
		XMLUtils utils = new XMLUtils();
		Map mapProperty = new HashMap();
		String strFilePath = "";
		strFilePath = tableDir.concat(strTableName.toUpperCase()).concat(".xml");
		String strXPATH = "//table/column[name='".concat(strColumn).concat("']");
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null && !commonList.isEmpty()) {
			Element node = (Element)commonList.get(0);
			String strKey = node.attributeValue("key");
			String strName = node.selectSingleNode("name").getText();
			String strType = node.selectSingleNode("type").getText();
			String strLength = node.selectSingleNode("length").getText();
			String strPrecision = node.selectSingleNode("precision ").getText();
			String strScale = node.selectSingleNode("scale ").getText();
			String strNullable = node.selectSingleNode("nullable ").getText();
			mapProperty.put("key", strKey);
			mapProperty.put("name", strName);
			mapProperty.put("type", strType);
			mapProperty.put("length", strLength);
			mapProperty.put("precision", strPrecision);
			mapProperty.put("scale", strScale);
			mapProperty.put("nullable", strNullable);
		} else {
			return null;
		}
		return mapProperty;
	}
	
	/**
	 */
	public String getTableComment(String strTableName) {
		String strResult = "";
		String strFilePath = "";
		strFilePath = tableDir.concat(strTableName.toUpperCase()).concat(".xml");

		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				return "";
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			strResult = document.getRootElement().attributeValue("comment");
		} catch (Exception e) {
			logger.exception(e);
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.exception(e);
					e.printStackTrace();
				}
			}
		}
		return strResult;
	}
	
	/**
	 */
	public Map searchByDataUtil(String strPattern, String strType, String strTableId, String strTableName) {
		Map resultMap = new LinkedHashMap();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		//
		if (Check.isNull(strPattern)) {
			strFilePath = tableDir.concat(strTableName.toUpperCase()).concat(".xml");
			String strXPATH = "//table/column";
			List commonList = utils.searchNode(strFilePath, strXPATH);
			if (commonList != null) {
				for (int i = 0; i < commonList.size(); i++) {
					Element node = (Element)commonList.get(i);
					String columnName = node.selectSingleNode("name").getText();
					Map map = new HashMap();
					map.put("COLUMN_FIRM", "");
					map.put("COLUMN_FK", "");
					map.put("COLUMN_PREFIX", "");
					map.put("COLUMN_RADOM", "");
					map.put("COLUMN_LENGTH", "");
					resultMap.put(columnName, map);
				}
			}
		} else {
			if (CommonConstant.PATTERN_COMMON.equals(strType)) {
				strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(strTableId).concat(".xml");
			} else {
				strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(strTableId).concat(".xml");
			}
			String strXPATH = "//TABLE/COLUMN";
			List commonList = utils.searchNode(strFilePath, strXPATH);
			if (commonList != null) {
				for (int i = 0; i < commonList.size(); i++) {
					Element node = (Element)commonList.get(i);
					String columnName = node.attributeValue("NAME");
					String strFirm = node.selectSingleNode("COLUMN_FIRM").getText();
					String strFk = node.selectSingleNode("COLUMN_FK").getText();
					String strPrefix = node.selectSingleNode("COLUMN_PREFIX").getText();
					String strRadom = node.selectSingleNode("COLUMN_RADOM").getText();
					String strLength = node.selectSingleNode("COLUMN_LENGTH").getText();
					Map map = new HashMap();
					map.put("COLUMN_FIRM", strFirm);
					map.put("COLUMN_FK", strFk);
					map.put("COLUMN_PREFIX", strPrefix);
					map.put("COLUMN_RADOM", strRadom);
					map.put("COLUMN_LENGTH", strLength);
					resultMap.put(columnName, map);
				}
			}
		}
		return resultMap;
	}
	
	/**
	 */
	public int getTableNumber(String strPattern, String strType, String strTableId) {
		int iNum = 1;
		if (Check.isNull(strPattern)) {
			return iNum;
		}
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(strTableId).concat(".xml");
		} else {
			strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(strTableId).concat(".xml");
		}
		String strXPATH = "//TABLE/NUMBER";
		List commonList = utils.searchNode(strFilePath, strXPATH);
		
		if (commonList != null && !commonList.isEmpty()) {
			Node node = (Node)commonList.get(0);
			String strNum = node.getText();
			try {
				iNum = Integer.parseInt(strNum);
			} catch(Exception e) {
				iNum = 1;
			}
		}

		return iNum;
	}
}
