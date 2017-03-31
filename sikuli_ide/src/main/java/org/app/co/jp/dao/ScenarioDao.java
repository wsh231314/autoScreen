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

public class ScenarioDao extends BaseDao{

	//
	public static final String SCENARIO_XML = "SCENARIO.xml";
	
	public static final String SCENARIO_LIST_XML = "SCENARIO_LIST.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 * @throws Exception 
	 */
	public List<Map<String, Object>> searchList(String strScenarioId) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		XMLUtils utils = new XMLUtils();
		String strFilePath = "";
		strFilePath = scenarioDir.concat(strScenarioId).concat("\\\\").concat(SCENARIO_XML);
		
		String strXPATH = "//steps/step";
		
		@SuppressWarnings("rawtypes")
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Node node = (Node)commonList.get(i);
				// id
				String strId = node.selectSingleNode("id").getText();
				String strPageId = node.selectSingleNode("page_id").getText();
				String strFieldId = node.selectSingleNode("field_id").getText();
				String strOperation = node.selectSingleNode("operation").getText();
				String strNotUse = node.selectSingleNode("not_use").getText();
				String strButton1 = CommonConstant.SORT_UP;
				String strButton2 = CommonConstant.SORT_DOWN;
				String strButton3 = "Del";
				
				// Page Name
				PageListDao pageListDao = new PageListDao();
				String strPageName = pageListDao.getPageName(strPageId);
				
				// Page field name and type
				PageDao pageDao = new PageDao();
				String arrResult[] = pageDao.getFiledNameAndType(strPageId, strFieldId);
				String strFiledName = arrResult[0];
				String strFiledType = arrResult[1];
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("STEP_ID", strId);
				map.put("PAGE_ID", strPageId);
				map.put("PAGE_NAME", strPageName);
				map.put("FIELD_ID", strFieldId);
				map.put("FIELD_NAME", strFiledName);
				map.put("FIELD_TYPE", strFiledType);
				map.put("OPERATION", strOperation);
				map.put("NOT_USE", strNotUse);
				map.put("DEAL_1", strButton1);
				map.put("DEAL_2", strButton2);
				map.put("DEAL_3", strButton3);
				map.put("OPERATION_FOR_SELECT", Utils.getOperationList());
				
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param deleteList
	 * @param strScenarioId
	 */
	public void deleteById(List<Map<String, String>> deleteList, String strScenarioId) {
		if (deleteList == null || deleteList.size() == 0) {
			return;
		}
		XMLWriter writer = new XMLWriter();
		String	strFilePath = scenarioDir.concat(strScenarioId).concat("\\\\").concat(SCENARIO_XML);
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
				String strId = (String)map.get("STEP_ID");
				String strXPATH = "//steps/step[id='".concat(strId).concat("']");
				Node node = document.selectSingleNode(strXPATH);
				Element root = node.getParent();
				root.remove(node);
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
	public void createByList(List<Map<String, String>> dealList, String strScenarioId, String strScenarioName) throws Exception{
		if (dealList == null || dealList.size() == 0) {
			return;
		}
		//
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String	strFilePath = scenarioDir.concat(strScenarioId).concat("\\\\").concat(SCENARIO_XML);
		String	xmlPagePath = scenarioDir.concat(strScenarioId).concat("\\\\");
		
		if (!(new File(xmlPagePath).exists())) {
			new File(xmlPagePath).mkdir();
			String strPageFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
			//
			FileInputStream fisList = null;
			try {
				SAXReader reader = new SAXReader();
				File file = new File(strPageFilePath);
				if (!file.exists()) {
					util.createBlankXml(strPageFilePath, "scenarios");
				}
				fisList = new FileInputStream(new File(strPageFilePath));
				Document document = reader.read(fisList);
				util.preCheckAddDoc(document, "", "//scenarios");
				Element rootPatternList = (Element)document.selectSingleNode("scenarios");
				Element rootPattern = rootPatternList.addElement("scenario");
				rootPattern.addElement("id").setText(strScenarioId);
				rootPattern.addElement("name").setText(strScenarioName);
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
			String strPageFilePath = scenarioDir.concat(SCENARIO_LIST_XML);
			//
			FileInputStream fisList = null;
			try {
				SAXReader reader = new SAXReader();

				fisList = new FileInputStream(new File(strPageFilePath));
				Document document = reader.read(fisList);
				Element idElement = (Element)document.selectSingleNode("//scenarios/scenario[id='".concat(strScenarioId).concat("']/name"));
				idElement.setText(strScenarioName);
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
				util.createBlankXml(strFilePath, "steps");
			} else {
				file.delete();
				util.createBlankXml(strFilePath, "steps");
			}
			
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			
			for (int i = 0; i < dealList.size(); i++) {
				Map<String, String> map = dealList.get(i);
				// id
				String strStepId = "STEP_".concat(String.valueOf(i + 1));
				String strPageId = (String)map.get("PAGE_ID");
				String strFieldId = (String)map.get("FIELD_ID");
				String strOperation = (String)map.get("OPERATION");
				String strNotUse = (String)map.get("NOT_USE");
				
				util.preCheckAddDoc(document, "", "//steps");
				Element rootNode = (Element)document.selectSingleNode("//steps");
				Element rootTableList = rootNode.addElement("step");
				rootTableList.addElement("id").setText(strStepId);
				rootTableList.addElement("page_id").setText(strPageId);
				rootTableList.addElement("field_id").setText(strFieldId);
				rootTableList.addElement("operation").setText(strOperation);
				rootTableList.addElement("not_use").setText(strNotUse);
				
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
