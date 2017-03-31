package org.app.co.jp.util;

import org.app.co.jp.com.ComDao;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLUtils {
	
	private Map keyMap = new HashMap();

	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 */
	public List searchNode(String strFilePath, String strXpath) {
		List list = null;
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				return new ArrayList();
			}
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			list = document.selectNodes(strXpath);
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
		//
		return list;
	}
	
	
	
	/**
	 */
	public List getKeyMap (String strTableName) {
		List keyList = new ArrayList();
		
		if (keyMap.containsKey(strTableName)) {
			return (List)keyMap.get(strTableName);
		}
		
		String strXPath = "//table/column[@key='true']/name";
		String filePath =  ComPropertyUtil.getProperty("table_path").concat(strTableName).toUpperCase().concat(".xml");
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			if (!file.exists()) {
				return null;
			}
			fis = new FileInputStream(new File(filePath));
			Document document = reader.read(fis);
			List list = document.selectNodes(strXPath);
			
			for (int i = 0; i < list.size(); i++) {
				// 
				Object obj = list.get(i);
				keyList.add(((Node)obj).getText());
			}
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
		
		//
		keyMap.put(strTableName, keyList);
		
		return keyList;
	}
	
	/**
	 */
	public boolean createTablesXml(boolean replaceFlg) throws Exception {
		DocumentFactory factory = DocumentFactory.getInstance();
		XMLWriter writer = new XMLWriter();
		try {
			List list = getInputList();
			// 
			Document document = null;
			Element rootTable = null;
			String strBasePath = ComPropertyUtil.getProperty("table_path");
			String strNowTableName = "";
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String sNowTable = (String) map.get("TABLE_NAME");
				String sNowColumn = (String) map.get("COLUMN_NAME");
				if (!sNowTable.equals(strNowTableName)) {
					String tableFilePath = strBasePath.concat(sNowTable).toUpperCase().concat(".xml");
					File file = new File(tableFilePath);
					if (file.exists() && !replaceFlg) {
						continue;
					}
					if (sNowTable.indexOf("$") >= 0) {
						continue;
					}
					if (!strNowTableName.equals("")) {
						String tableSavePath = strBasePath.concat(strNowTableName).toUpperCase().concat(".xml");
						FileOutputStream out = new FileOutputStream(tableSavePath);
						writer.setOutputStream(out);
						writer.write(document);
						writer.flush();
						out.close();
					}
					document = factory.createDocument();
					rootTable = document.addElement("table");
					//
					strNowTableName = sNowTable;
				}
				Element columnElement = rootTable.addElement("column").addAttribute("key", (String) map.get("KEY_FLG"));
				columnElement.addElement("name").setText(sNowColumn);
				columnElement.addElement("type").setText((String) map.get("DATA_TYPE"));
				columnElement.addElement("length").setText((String) map.get("DATA_LENGTH"));
				columnElement.addElement("precision").setText((String) map.get("DATA_PRECISION"));
				columnElement.addElement("scale").setText((String) map.get("DATA_SCALE"));
				columnElement.addElement("nullable").setText((String) map.get("NULLABLE"));
			}
			String tableFilePath = strBasePath.concat(strNowTableName).toUpperCase().concat(".xml");
			File file = new File(tableFilePath);
			if (file.exists() && replaceFlg) {
				if (!strNowTableName.equals("")) {
					FileOutputStream out = new FileOutputStream(tableFilePath);
					writer.setOutputStream(out);
					writer.write(document);
					out.close();
				}
			} else {
				if (!strNowTableName.equals("")) {
					FileOutputStream out = new FileOutputStream(tableFilePath);
					writer.setOutputStream(out);
					writer.write(document);
					out.close();
				}
			}
		} catch (Exception e) {
			logger.exception(e);
			e.printStackTrace();
			throw e;
		}

		return true;
	}
	
	/**
	 */
	public List getInputList() throws SQLException {
		List list = null;
		ComDao dao = new ComDao();
		Connection con = dao.getConnection();
		
		try {
			list = dao.getTableColumnInfo(con);
		} catch (SQLException e) {
			logger.exception(e);
			e.printStackTrace();
		} finally {
			con.close();
		}
		return list;
	}
	
	/** 
	 */
	public void preCheckAddDoc(Document document, String nowPath, String allPath) {
		String strNotDeal = allPath.substring(nowPath.length());
		if (strNotDeal.startsWith("/")) {
			nowPath = nowPath.concat("/");
			preCheckAddDoc(document, nowPath, allPath);
		} else {
			String iNodeName = "";
			if (strNotDeal.indexOf("/") > 0) {
				iNodeName = strNotDeal.substring(0, strNotDeal.indexOf("/"));
			} else {
				iNodeName = strNotDeal;
			}
			nowPath = nowPath.concat(iNodeName);
			//
			List list = document.selectNodes(nowPath);
			if (list == null || list.isEmpty()) {
				//
				String strTempPath = nowPath.substring(0, nowPath.lastIndexOf("/"));
				if (strTempPath.replaceAll("/", "").equals("")) {
					document.addElement(iNodeName);
				} else {
					Element root = (Element)document.selectSingleNode(strTempPath);
					root.addElement(iNodeName);
				}
			}
			if (!nowPath.equals(allPath)) {
				preCheckAddDoc(document, nowPath, allPath);
			}
		}
	}
	
	/**
	 */
	public void createBlankXml(String strPath, String strRoot) {
		DocumentFactory factory = DocumentFactory.getInstance();
		//
		Document document = factory.createDocument();
		document.addElement(strRoot);
		//
		XMLWriter writer = new XMLWriter();
		//
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(strPath);
			writer.setOutputStream(out);
			writer.write(document);
			writer.flush();
			out.close();
		} catch (Exception e) {
			logger.exception(e);
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.exception(e);
					e.printStackTrace();
				}
			}
		}
	}
}
