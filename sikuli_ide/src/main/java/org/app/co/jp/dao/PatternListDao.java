package org.app.co.jp.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.app.co.jp.com.BaseDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Utils;
import org.app.co.jp.util.XMLUtils;

public class PatternListDao extends BaseDao{

	//
	public static final String PATTERN_LIST_XML = "PATTERN_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 *
	 */
	public List searchList(String strPatternName) {
		List result = new ArrayList();
		XMLUtils utils = new XMLUtils();
		String strCommonFilePath = patternCommonPatternDir.concat(PATTERN_LIST_XML);
		String strCustomerFilePath = patternCustomerDir.concat(PATTERN_LIST_XML);
		String strXPATH = "";
		if (strPatternName == null || strPatternName.equals("")) {
			strXPATH = "//patterns/pattern";
		} else {
			strXPATH = "//patterns/pattern[contains(name, \"" + strPatternName + "\")]";
		}
		List commonList = utils.searchNode(strCommonFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Node node = (Node)commonList.get(i);
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strType = CommonConstant.PATTERN_COMMON;
				String strSelect = "false";
				String strButton1 = "Detail";
				String strButton2 = "Copy";
				String strButton3 = "Del";
				Map map = new HashMap();
				// 
				map.put("PATTERN_ID", strId);
				map.put("PATTERN_NAME", strName);
				map.put("PATTERN_TYPE", strType);
				map.put("SELECT", strSelect);
				map.put("DEAL_1", strButton1);
				map.put("DEAL_2", strButton2);
				map.put("DEAL_3", strButton3);
				result.add(map);
			}
		}
		List customerList = utils.searchNode(strCustomerFilePath, strXPATH);
		if (customerList != null) {
			for (int i = 0; i < customerList.size(); i++) {
				Node node = (Node)customerList.get(i);
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strType = CommonConstant.PATTERN_CUSTOMER;
				String strSelect = "false";
				String strButton1 = "Detail";
				String strButton2 = "Copy";
				String strButton3 = "Del";
				Map map = new HashMap();
				// 
				map.put("PATTERN_ID", strId);
				map.put("PATTERN_NAME", strName);
				map.put("PATTERN_TYPE", strType);
				map.put("SELECT", strSelect);
				map.put("DEAL_1", strButton1);
				map.put("DEAL_2", strButton2);
				map.put("DEAL_3", strButton3);
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 */
	public void deleteByList(String strPatternId, String strType) {
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		String xmlPath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(PATTERN_LIST_XML);
			xmlPath = patternCommonPatternDir.concat(strPatternId);
		} else {
			strFilePath = patternCustomerDir.concat(PATTERN_LIST_XML);
			xmlPath = patternCustomerDir.concat(strPatternId);
		}
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				return;
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			String strXPATH = "//patterns/pattern[id='".concat(strPatternId).concat("']");
			Node node = document.selectSingleNode(strXPATH);
			Element root = node.getParent();
			root.remove(node);
			Utils.deleteDirectory(xmlPath);
			FileOutputStream out = new FileOutputStream(strFilePath);
			writer.setOutputStream(out);
			writer.write(document);
			out.close();
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
	}
}
