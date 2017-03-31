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
import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.Utils;
import org.app.co.jp.util.XMLUtils;

public class PageDao extends BaseDao{

	//
	public static final String PAGE_XML = "PAGE.xml";
	
	public static final String PAGE_LIST_XML = "PAGE_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 */
	public List<Map<String, Object>> searchList() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		strFilePath = pageDir.concat("work").concat("\\\\").concat(PAGE_XML);
		
		String strXPATH = "//fields/field";
		
		@SuppressWarnings("rawtypes")
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Node node = (Node)commonList.get(i);
				// id
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strType = node.selectSingleNode("type").getText();
				String strPath = node.selectSingleNode("path").getText();
				String strValue = node.selectSingleNode("value").getText();
				String strButton1 = "Del";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("FIELD_ID", strId);
				map.put("FIELD_NAME", strName);
				map.put("FIELD_TYPE", strType);
				map.put("FIELD_TYPE_FOR_SELECT", Utils.getComponentList());
				map.put("FIELD_VALUE", strValue);
				map.put("CAPTURE", "");
				map.put("DEAL_1", strButton1);
				map.put("file_path", strPath);
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 */
	public List<Map<String, Object>> searchListById(String strPageId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		strFilePath = pageDir.concat(strPageId).concat("\\\\").concat(PAGE_XML);
		
		String strXPATH = "//fields/field";
		
		@SuppressWarnings("rawtypes")
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Node node = (Node)commonList.get(i);
				// id
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strType = node.selectSingleNode("type").getText();
				String strPath = node.selectSingleNode("path").getText();
				String strValue = node.selectSingleNode("value").getText();
				String strButton1 = "Del";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("SELECT", "false");
				map.put("FIELD_ID", strId);
				map.put("FIELD_NAME", strName);
				map.put("FIELD_TYPE", strType);
				map.put("FIELD_TYPE_FOR_SELECT", Utils.getComponentList());
				map.put("FIELD_VALUE", strValue);
				map.put("CAPTURE", "");
				map.put("DEAL_1", strButton1);
				map.put("file_path", strPath);
				result.add(map);
			}
		}
		return result;
	}
	
	
	/**
	 *
	 */
	public void deleteByList(List<Map<String, String>> deleteList) {
		if (deleteList == null || deleteList.size() == 0) {
			return;
		}
		XMLWriter writer = new XMLWriter();
		String	strFilePath = pageDir.concat("work").concat("\\\\").concat(PAGE_XML);
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
				Map<String, String> map = deleteList.get(i);
				// id
				String strId = (String)map.get("FIELD_ID");
				// path
				String imgPath = (String)map.get("file_path");
				String strXPATH = "//fields/field[id='".concat(strId).concat("']");
				Node node = document.selectSingleNode(strXPATH);
				Element root = node.getParent();
				root.remove(node);
				File xmlFile = new File(imgPath);
				if (xmlFile.exists()) {
					imgPath = pageDir.concat("work").concat("\\\\").concat(xmlFile.getName());
					File tempFile = new File (imgPath);
					if (tempFile.exists()) {
						tempFile.delete();
					}
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
	public void createByList(List<Map<String, String>> dealList, String strPageId, String strPageName) throws Exception{
		if (dealList == null || dealList.size() == 0) {
			return;
		}
		//
		ComDao comDao = new ComDao();
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String	strFilePath = pageDir.concat("work").concat("\\\\").concat(PAGE_XML);
		String	xmlPagePath = pageDir.concat(strPageId).concat("\\\\");
		
		if (!(new File(xmlPagePath).exists())) {
			new File(xmlPagePath).mkdir();
			String strPageFilePath = pageDir.concat(PAGE_LIST_XML);
			//
			FileInputStream fisList = null;
			try {
				SAXReader reader = new SAXReader();
				File file = new File(strPageFilePath);
				if (!file.exists()) {
					util.createBlankXml(strPageFilePath, "pages");
				}
				fisList = new FileInputStream(new File(strPageFilePath));
				Document document = reader.read(fisList);
				util.preCheckAddDoc(document, "", "//pages");
				Element rootPatternList = (Element)document.selectSingleNode("pages");
				Element rootPattern = rootPatternList.addElement("page");
				rootPattern.addElement("id").setText(strPageId);
				rootPattern.addElement("name").setText(strPageName);
				FileOutputStream out = new FileOutputStream(strPageFilePath);
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
			String strPageFilePath = pageDir.concat(PAGE_LIST_XML);
			//
			FileInputStream fisList = null;
			try {
				SAXReader reader = new SAXReader();

				fisList = new FileInputStream(new File(strPageFilePath));
				Document document = reader.read(fisList);
				Element idElement = (Element)document.selectSingleNode("//pages/page[id='".concat(strPageId).concat("']/name"));
				idElement.setText(strPageName);
				FileOutputStream out = new FileOutputStream(strPageFilePath);
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
		//
		FileInputStream fis = null;
		FileInputStream fisUpd = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "fields");
			} else {
				file.delete();
				util.createBlankXml(strFilePath, "fields");
			}
			
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			
			for (int i = 0; i < dealList.size(); i++) {
				Map<String, String> map = dealList.get(i);
				// id
				String strFieldId = (String)map.get("FIELD_ID");
				String strFieldName = (String)map.get("FIELD_NAME");
				String strType = (String)map.get("FIELD_TYPE");
				String strPath = (String)map.get("file_path");
				String strVaue = (String)map.get("FIELD_VALUE");
				
				if (Check.isNull(strFieldId)) {
					strFieldId = "FIELD".concat("_").concat(comDao.getFieldSeq(strPageId, CommonConstant.PATTERN_CUSTOMER));
				}
				
				strPath = strPath.substring(0, strPath.lastIndexOf("work")) + strPageId + strPath.substring(strPath.lastIndexOf("work") + 4);
				
				strVaue = strVaue.substring(0, strVaue.lastIndexOf("work")) + strPageId + strVaue.substring(strVaue.lastIndexOf("work") + 4);
				
				util.preCheckAddDoc(document, "", "//fields");
				Element rootNode = (Element)document.selectSingleNode("//fields");
				Element rootTableList = rootNode.addElement("field");
				rootTableList.addElement("id").setText(strFieldId);
				rootTableList.addElement("name").setText(strFieldName);
				rootTableList.addElement("type").setText(strType);
				rootTableList.addElement("path").setText(strPath);
				rootTableList.addElement("value").setText(strVaue);
				
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
	
	/**
	 * 
	 * 
	 * @param strPageId
	 * @param strFieldId
	 * @return
	 */
	public String[] getFiledNameAndType(String strPageId, String strFieldId) {
		String[] arrResult = {"", ""};
		
		String strFilePath = "";
		strFilePath = pageDir.concat(strPageId).concat("\\\\").concat(PAGE_XML);
		
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				return arrResult;
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			Element idElement = (Element)document.selectSingleNode("//fields/field[id='".concat(strFieldId).concat("']"));
			String strName = idElement.selectSingleNode("name").getText();
			String strType = idElement.selectSingleNode("type").getText();
			
			arrResult[0] = strName;
			arrResult[1] = strType;
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
		
		return arrResult;
	}
}
