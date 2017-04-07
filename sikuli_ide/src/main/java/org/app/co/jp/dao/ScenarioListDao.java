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

public class ScenarioListDao extends BaseDao{

	//
	public static final String SCENARIO_LIST_XML = "SCENARIO_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 * 
	 * 
	 * @param pageId
	 * @param strPageName
	 * @throws IOException
	 */
	public void createScenarioListId(String scenarioId, String strScenarioName) throws IOException {
		
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		strFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
		
		// script folder
		String folder  = scenarioDir.concat(scenarioId);
		
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
				util.createBlankXml(strFilePath, "scenarios");
			}
			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			util.preCheckAddDoc(document, "", "//scenarios");
			Element rootScriptList = (Element)document.selectSingleNode("scenarios");
			Element rootScript = rootScriptList.addElement("scenario");
			rootScript.addElement("id").setText(scenarioId);
			rootScript.addElement("name").setText(strScenarioName);
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
	public void updatePageName(String scenarioId, String strScenarioName) throws IOException {
		
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		strFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
		
		FileInputStream fisList = null;
		
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "scenarios");
			}
			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			Element element = (Element)document.selectSingleNode("//scenarios/scenario[id='" + scenarioId  + "']");
			element.element("name").setText(strScenarioName);
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
	public List<Map<String, String>> searchList(String strScenarioName) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		XMLUtils utils = new XMLUtils();
		String strFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
		String strXPATH = "";
		if (strScenarioName == null || strScenarioName.equals("")) {
			strXPATH = "//scenarios/scenario";
		} else {
			strXPATH = "//scenarios/scenario[contains(name, \"" + strScenarioName + "\")]";
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
				map.put("SCENARIO_ID", strId);
				map.put("SCENARIO_NAME", strName);
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
	public void deleteById(String strScenarioId) {
		
		XMLWriter writer = new XMLWriter();
		String strFilePath = "";
		String xmlPath = "";
		strFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
		xmlPath = scenarioDir.concat(strScenarioId);
		
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
			String strXPATH = "//scenarios/scenario[id='".concat(strScenarioId).concat("']");
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
	 * 
	 * @param strScenarioId
	 * @return
	 * @throws Exception
	 */
	public String getScenarioName(String strScenarioId) throws Exception {
		String strResult = "";
		
		String strFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
		//
		FileInputStream fisList = null;
		try {
			SAXReader reader = new SAXReader();

			fisList = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fisList);
			Element idElement = (Element)document.selectSingleNode("//scenarios/scenario[id='".concat(strScenarioId).concat("']/name"));
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
