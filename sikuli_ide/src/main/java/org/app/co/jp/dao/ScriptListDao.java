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

public class ScriptListDao extends BaseDao{

	//
	public static final String SCRIPT_LIST_XML = "SCRIPT_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 * @throws Exception 
	 */
	public String createScriptData(String scriptId, String strFileName, String strScriptName) throws IOException {
		
		String result = "";
		
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		strFilePath = scriptDir.concat(SCRIPT_LIST_XML);
		
		// script folder
		String folder  = scriptDir.concat(scriptId);
		
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
				util.createBlankXml(strFilePath, "scripts");
			}
			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			util.preCheckAddDoc(document, "", "//scripts");
			Element rootScriptList = (Element)document.selectSingleNode("scripts");
			Element rootScript = rootScriptList.addElement("script");
			rootScript.addElement("id").setText(scriptId);
			rootScript.addElement("name").setText(strScriptName);
			rootScript.addElement("file").setText(strFileName);
			FileOutputStream out = new FileOutputStream(strFilePath);
			writer.setOutputStream(out);
			writer.write(document);
			out.close();
		} catch(IOException e) {
			logger.exception(e);
			e.printStackTrace();
			throw e;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
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
		
		result = result.concat(scriptDir).concat(scriptId).concat("////").concat(strFileName);
		
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param scriptId
	 * @param strScriptName
	 * @return
	 * @throws IOException
	 */
	public void updateScriptData(String scriptId, String strScriptName) throws IOException {
		
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		strFilePath = scriptDir.concat(SCRIPT_LIST_XML);
		
		FileInputStream fisList = null;
		
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "scripts");
			}
			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			Element element = (Element)document.selectSingleNode("//scripts/script[id='" + scriptId  + "']");
			element.element("name").setText(strScriptName);
			FileOutputStream out = new FileOutputStream(strFilePath);
			writer.setOutputStream(out);
			writer.write(document);
			out.close();
		} catch(IOException e) {
			logger.exception(e);
			e.printStackTrace();
			throw e;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
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
	public List<Map<String, String>> searchList(String strScriptName) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		XMLUtils utils = new XMLUtils();
		String strFilePath = scriptDir.concat(SCRIPT_LIST_XML);
		String strXPATH = "";
		if (strScriptName == null || strScriptName.equals("")) {
			strXPATH = "//scripts/script";
		} else {
			strXPATH = "//scripts/script[contains(name, \"" + strScriptName + "\")]";
		}
		
		@SuppressWarnings("rawtypes")
		List customerList = utils.searchNode(strFilePath, strXPATH);
		
		if (customerList != null) {
			for (int i = 0; i < customerList.size(); i++) {
				Node node = (Node)customerList.get(i);
				String strId = node.selectSingleNode("id").getText();
				String strName = node.selectSingleNode("name").getText();
				String strFile = node.selectSingleNode("file").getText();
				String strSelect = "false";
				String strButton1 = "Detail";
				String strButton2 = "Copy";
				String strButton3 = "Del";
				HashMap<String, String> map = new HashMap<String, String>();
				// 
				map.put("SCRIPT_ID", strId);
				map.put("SCRIPT_NAME", strName);
				map.put("SCRIPT_FILE", strFile);
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
	 *
	 */
	public String getScriptPath(String strScriptId) {
		String result = "";
		String strFilePath = scriptDir.concat(SCRIPT_LIST_XML);
		String strXPATH = "//scripts/script[id='".concat(strScriptId).concat("']");
		
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				return result;
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			Node node = document.selectSingleNode(strXPATH);
			String strFile = node.selectSingleNode("file").getText();
			
			result = result.concat(scriptDir).concat(strScriptId).concat("////").concat(strFile);
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
		return result;
	}
	
	/**
	 */
	public String deleteByList(String strScriptId) {
		
		String result = "";
		
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		String xmlPath = "";
		strFilePath = scriptDir.concat(SCRIPT_LIST_XML);
		xmlPath = scriptDir.concat(strScriptId);
		
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				return result;
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			String strXPATH = "//scripts/script[id='".concat(strScriptId).concat("']");
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
		
		result = result.concat(scriptDir).concat(strScriptId);
		
		return result;
	}
}
