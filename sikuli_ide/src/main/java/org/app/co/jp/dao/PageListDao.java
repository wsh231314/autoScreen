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
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.app.co.jp.com.BaseDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Utils;
import org.app.co.jp.util.XMLUtils;

public class PageListDao extends BaseDao{

	//
	public static final String PAGE_LIST_XML = "PAGE_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 * 
	 * 
	 * @param pageId
	 * @param strPageName
	 * @throws IOException
	 */
	public void createPageListId(String pageId, String strPageName) throws IOException {
		
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		strFilePath = pageDir.concat(PAGE_LIST_XML);
		
		// script folder
		String folder  = pageDir.concat(pageId);
		
		// if not exists, create folder 
		File folderFile = new File(folder); 
		if (folderFile.exists()) {
			folderFile.mkdir();
		}
		
		FileInputStream fisList = null;
		
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "pages");
			}
			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			util.preCheckAddDoc(document, "", "//pages");
			Element rootScriptList = (Element)document.selectSingleNode("pages");
			Element rootScript = rootScriptList.addElement("page");
			rootScript.addElement("id").setText(pageId);
			rootScript.addElement("name").setText(strPageName);
			FileOutputStream out = new FileOutputStream(strFilePath);
			writer.setOutputStream(out);
			writer.write(document);
			out.close();
		} catch(IOException e) {
			logger.exception(e);
			e.printStackTrace();
			throw e;
		} catch (DocumentException e) {
			logger.exception(e);
			e.printStackTrace();
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
	
	/**
	 * update page name
	 * 
	 * @param pageId
	 * @param strPageName
	 * @return
	 * @throws IOException
	 */
	public void updatePageName(String pageId, String strPageName) throws IOException {
		
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		strFilePath = pageDir.concat(PAGE_LIST_XML);
		
		FileInputStream fisList = null;
		
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "pages");
			}
			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			Element element = (Element)document.selectSingleNode("//pages/page[id='" + pageId  + "']");
			element.element("name").setText(strPageName);
			FileOutputStream out = new FileOutputStream(strFilePath);
			writer.setOutputStream(out);
			writer.write(document);
			out.close();
		} catch(IOException e) {
			logger.exception(e);
			e.printStackTrace();
			throw e;
		} catch (DocumentException e) {
			logger.exception(e);
			e.printStackTrace();
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
	
	/**
	 *
	 */
	public List<Map<String, String>> searchList(String strPageName) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		XMLUtils utils = new XMLUtils();
		String strFilePath = pageDir.concat(PAGE_LIST_XML);
		String strXPATH = "";
		if (strPageName == null || strPageName.equals("")) {
			strXPATH = "//pages/page";
		} else {
			strXPATH = "//pages/page[contains(name, \"" + strPageName + "\")]";
		}
		
		@SuppressWarnings("rawtypes")
		List customerList = utils.searchNode(strFilePath, strXPATH);
		
		if (customerList != null) {
			for (int i = 0; i < customerList.size(); i++) {
				Node node = (Node)customerList.get(i);
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strButton1 = "Detail";
				String strButton2 = "Copy";
				String strButton3 = "Del";
				HashMap<String, String> map = new HashMap<String, String>();
				// 
				map.put("PAGE_ID", strId);
				map.put("PAGE_NAME", strName);
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
	public void deleteByList(String strPageId) {
		
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		String xmlPath = "";
		strFilePath = pageDir.concat(PAGE_LIST_XML);
		xmlPath = pageDir.concat(strPageId);
		
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
			String strXPATH = "//pages/page[id='".concat(strPageId).concat("']");
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
	
	/**
	 * 
	 */
	public String getPagePath(String strPageId) {
		String result = pageDir.concat(strPageId).concat("/");
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param strPageId
	 * @param strFieldId
	 * @return
	 * @throws Exception 
	 */
	public String getPageName(String strPageId) throws Exception {
		String strResult = "";
		
		String strPageFilePath = pageDir.concat(PAGE_LIST_XML);
		//
		FileInputStream fisList = null;
		try {
			SAXReader reader = new SAXReader();

			fisList = new FileInputStream(new File(strPageFilePath));
			Document document = reader.read(fisList);
			Element idElement = (Element)document.selectSingleNode("//pages/page[id='".concat(strPageId).concat("']/name"));
			strResult = idElement.getText();
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
		
		return strResult;
	}
}
