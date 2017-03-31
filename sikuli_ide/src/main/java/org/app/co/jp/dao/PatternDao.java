package org.app.co.jp.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.app.co.jp.com.BaseDao;
import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.XMLUtils;

public class PatternDao extends BaseDao{

	//
	public static final String TABLE_LIST_XML = "TABLE_LIST.xml";
	
	public static final String PATTERN_LIST_XML = "PATTERN_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 */
	public List searchList(String strPattern, String strType) {
		if (strPattern == null || strPattern.equals("")) {
			return new ArrayList();
		}
		List result = new ArrayList();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
		} else {
			strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
		}
		String strXPATH = "//tables/table";
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Node node = (Node)commonList.get(i);
				// id
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strComment = node.selectSingleNode("comment").getText();
				String strNum = node.selectSingleNode("number").getText();
				String strButton1 = "Detail";
				String strButton2 = "Copy";
				String strButton3 = "Del";
				Map map = new HashMap();
				map.put("TABLE_ID", strId);
				map.put("TABLE_NAME", strName);
				map.put("TABLE_COMMENT", strComment);
				map.put("TABLE_NUMBER", strNum);
				map.put("DEAL_1", strButton1);
				map.put("DEAL_2", strButton2);
				map.put("DEAL_3", strButton3);
				map.put("DEAL_FLG", CommonConstant.DEAL_DEFAULT);
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 */
	public List searchCopyList(String strPattern, String strType) {
		if (strPattern == null || strPattern.equals("")) {
			return new ArrayList();
		}
		List result = new ArrayList();
		XMLUtils utils = new XMLUtils();
		TableDao dao = new TableDao();
		String strFilePath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
		} else {
			strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
		}
		String strXPATH = "//tables/table";
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Node node = (Node)commonList.get(i);
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strComment = node.selectSingleNode("comment").getText();
				String strNum = node.selectSingleNode("number").getText();
				String strButton1 = "Detail";
				String strButton2 = "Copy";
				String strButton3 = "Del";
				Map map = new HashMap();
				Map ruleMap = dao.search(strPattern, strType, strId);
				map.put("TABLE_ID", "");
				map.put("TABLE_NAME", strName);
				map.put("TABLE_COMMENT", strComment);
				map.put("TABLE_NUMBER", strNum);
				map.put("DEAL_1", strButton1);
				map.put("DEAL_2", strButton2);
				map.put("DEAL_3", strButton3);
				map.put("TABLE_RULES", ruleMap);
				map.put("DEAL_FLG", CommonConstant.DEAL_INSERT);
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 *
	 */
	public void deleteByList(List deleteList, String strPattern, String strType) {
		if (deleteList == null || deleteList.size() == 0) {
			return;
		}
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		String xmlPath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
			xmlPath = patternCommonPatternDir.concat(strPattern).concat("\\\\");
		} else {
			strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
			xmlPath = patternCustomerDir.concat(strPattern).concat("\\\\");
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
			
			for (int i = 0; i < deleteList.size(); i++) {
				Map map = (Map)deleteList.get(i);
				// id
				String strId = (String)map.get("TABLE_ID");
				String strXPATH = "//tables/table[id='".concat(strId).concat("']");
				Node node = document.selectSingleNode(strXPATH);
				Element root = node.getParent();
				root.remove(node);
				File xmlFile = new File(xmlPath.concat(strId).concat(".xml"));
				if (xmlFile.exists()) {
					xmlFile.delete();
				}
			}
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
	
	/**
	 */
	public void createByList(List dealList, String strPattern, String strType, String strPatternName) throws Exception{
		if (dealList == null || dealList.size() == 0) {
			return;
		}
		//
		ComDao comDao = new ComDao();
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		String xmlPath = "";
		if (CommonConstant.PATTERN_COMMON.equals(strType)) {
			strFilePath = patternCommonPatternDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
			xmlPath = patternCommonPatternDir.concat(strPattern).concat("\\\\");
		} else {
			strFilePath = patternCustomerDir.concat(strPattern).concat("\\\\").concat(TABLE_LIST_XML);
			xmlPath = patternCustomerDir.concat(strPattern).concat("\\\\");
		}
		if (!(new File(xmlPath).exists())) {
			new File(xmlPath).mkdir();
			String strPatternFilePath = "";
			if (CommonConstant.PATTERN_COMMON.equals(strType)) {
				strPatternFilePath = patternCommonPatternDir.concat(PATTERN_LIST_XML);
			} else {
				strPatternFilePath = patternCustomerDir.concat(PATTERN_LIST_XML);
			}
			//
			FileInputStream fisList = null;
			try {
				SAXReader reader = new SAXReader();
				File file = new File(strPatternFilePath);
				if (!file.exists()) {
					util.createBlankXml(strPatternFilePath, "patterns");
				}
				fisList = new FileInputStream(new File(strPatternFilePath));
				Document document = reader.read(fisList);
				util.preCheckAddDoc(document, "", "//patterns");
				Element rootPatternList = (Element)document.selectSingleNode("patterns");
				Element rootPattern = rootPatternList.addElement("pattern");
				rootPattern.addElement("id").setText(strPattern);
				rootPattern.addElement("name").setText(strPatternName);
				FileOutputStream out = new FileOutputStream(strPatternFilePath);
				writer.setOutputStream(out);
				writer.write(document);
				out.close();
			} catch(Exception e) {
				logger.exception(e);
				e.printStackTrace();
				throw e;
			} finally {
				if (fisList != null) {
					try {
						fisList.close();
					} catch (IOException e) {
						logger.exception(e);
						e.printStackTrace();
						throw e;
					}
				}
			}
		} else {
			String strPatternFilePath = "";
			if (CommonConstant.PATTERN_COMMON.equals(strType)) {
				strPatternFilePath = patternCommonPatternDir.concat(PATTERN_LIST_XML);
			} else {
				strPatternFilePath = patternCustomerDir.concat(PATTERN_LIST_XML);
			}
			//
			FileInputStream fisList = null;
			try {
				SAXReader reader = new SAXReader();

				fisList = new FileInputStream(new File(strPatternFilePath));
				Document document = reader.read(fisList);
				Element idElement = (Element)document.selectSingleNode("//patterns/pattern[id='".concat(strPattern).concat("']/name"));
				idElement.setText(strPatternName);
				FileOutputStream out = new FileOutputStream(strPatternFilePath);
				writer.setOutputStream(out);
				writer.write(document);
				out.close();
			} catch(Exception e) {
				logger.exception(e);
				e.printStackTrace();
				throw e;
			} finally {
				if (fisList != null) {
					try {
						fisList.close();
					} catch (IOException e) {
						logger.exception(e);
						e.printStackTrace();
						throw e;
					}
				}
			}
		}
		DocumentFactory factory = DocumentFactory.getInstance();
		//
		FileInputStream fis = null;
		FileInputStream fisUpd = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "tables");
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			
			for (int i = 0; i < dealList.size(); i++) {
				Map map = (Map)dealList.get(i);
				String dealFlg = (String)map.get("DEAL_FLG");
				if (CommonConstant.DEAL_DEFAULT.equals(dealFlg)) {
					continue;
				}
				// id
				String strTableId = (String)map.get("TABLE_ID");
				String strTableName = (String)map.get("TABLE_NAME");
				if (Check.isNull(strTableId)) {
					Document newDoc = factory.createDocument();
					strTableId = "TABLE".concat("_").concat(comDao.getTableSeq(strPattern, strType, strTableName));
					Map ruleMap = (Map)map.get("TABLE_RULES");
					Element rootTable = newDoc.addElement("TABLE").addAttribute("NAME", strTableName);
					rootTable.addElement("COMMENT").setText((String)map.get("TABLE_COMMENT"));
					rootTable.addElement("NUMBER").setText((String)map.get("TABLE_NUMBER"));
					Iterator iterator = ruleMap.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String)iterator.next();
						Element rootColumn = rootTable.addElement("COLUMN").addAttribute("NAME", key);
						Map mapProperty = (Map)ruleMap.get(key);
						Iterator iteratorPro = mapProperty.keySet().iterator();
						while (iteratorPro.hasNext()) {
							String keyPro = (String)iteratorPro.next();
							rootColumn.addElement(keyPro).setText((String)mapProperty.get(keyPro));
						}
						
					}
					FileOutputStream out = new FileOutputStream(xmlPath.concat(strTableId).concat(".xml"));
					writer.setOutputStream(out);
					writer.write(newDoc);
					out.close();
					util.preCheckAddDoc(document, "", "//tables");
					Element rootNode = (Element)document.selectSingleNode("//tables");
					Element rootTableList = rootNode.addElement("table");
					rootTableList.addElement("id").setText(strTableId);
					rootTableList.addElement("comment").setText((String)map.get("TABLE_COMMENT"));
					rootTableList.addElement("number").setText((String)map.get("TABLE_NUMBER"));
					rootTableList.addElement("name").setText((String)map.get("TABLE_NAME"));
				} else {
					fisUpd = new FileInputStream(new File(xmlPath.concat(strTableId).concat(".xml")));
					Document updDoc = reader.read(fisUpd);
					Map ruleMap = (Map)map.get("TABLE_RULES");
					Element rootTable = (Element)updDoc.selectSingleNode("TABLE");
					rootTable.selectSingleNode("COMMENT").setText((String)map.get("TABLE_COMMENT"));
					rootTable.selectSingleNode("NUMBER").setText((String)map.get("TABLE_NUMBER"));
					Iterator iterator = ruleMap.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String)iterator.next();
						String columnXPATH = "//TBALE/COLUMN[@NAME='".concat(key).concat("']");
						List rootColumnList = updDoc.selectNodes(columnXPATH);
						//
						Element rootColumn = null;
						if (rootColumnList != null && !rootColumnList.isEmpty()) {
							rootColumn = (Element)rootColumnList.get(0);
						} else {
							rootColumn = rootTable.addElement("COLUMN").addAttribute("NAME", key);
						}
						Map mapProperty = (Map)ruleMap.get(key);
						Iterator iteratorPro = mapProperty.keySet().iterator();
						while (iteratorPro.hasNext()) {
							String keyPro = (String)iteratorPro.next();
							List proList = updDoc.selectNodes(columnXPATH.concat("/").concat(keyPro));
							if (proList != null && !proList.isEmpty()) {
								((Element)proList.get(0)).setText((String)mapProperty.get(keyPro));
							} else {
								rootColumn.addElement(keyPro).setText((String)mapProperty.get(keyPro));
							}
						}
						
					}
					FileOutputStream out = new FileOutputStream(xmlPath.concat(strTableId).concat(".xml"));
					writer.setOutputStream(out);
					writer.write(updDoc);
					out.close();
					Element idNode = (Element)document.selectSingleNode("//tables/table[id='".concat(strTableId).concat("']"));
					idNode.selectSingleNode("comment").setText((String)map.get("TABLE_COMMENT"));
					idNode.selectSingleNode("number").setText((String)map.get("TABLE_NUMBER"));
				}
			}
			FileOutputStream out = new FileOutputStream(strFilePath);
			writer.setOutputStream(out);
			writer.write(document);
			out.close();
		} catch (Exception e) {
			logger.exception(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.exception(e);
					e.printStackTrace();
					throw e;
				}
			}
			if (fisUpd != null) {
				try {
					fisUpd.close();
				} catch (IOException e) {
					logger.exception(e);
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
}
