package org.app.co.jp.dao;

import org.app.co.jp.com.BaseDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.XMLUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterDao extends BaseDao{

	//
	BasicLogger logger = BasicLogger.getLogger();
	
	/**
	 *
	 */
	public List searchAllParameter() {
		List result = new ArrayList();
		XMLUtils utils = new XMLUtils();
		String strFilePath = parameterDir.concat("\\\\").concat(parameterFileName);
		String strXPATH = "//PARAMETERS/PARAMETER";
		List commonList = utils.searchNode(strFilePath, strXPATH);
		if (commonList != null) {
			for (int i = 0; i < commonList.size(); i++) {
				Element node = (Element)commonList.get(i);
				String strFirm = node.selectSingleNode("FIELD_NAME").getText();
				String strFk = node.selectSingleNode("FIELD_VALUE").getText();
				String strPrefix = node.selectSingleNode("USE_FLG").getText();
				Map map = new HashMap();
				map.put("FIELD_NAME", strFirm);
				map.put("FIELD_VALUE", strFk);
				map.put("USE_FLG", strPrefix);
				map.put("DEAL", "Delete");
				//
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 */
	public void createByList(List valueList) throws Exception{
		if (valueList == null || valueList.size() == 0) {
			return;
		}
		//
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		String strFilePath = parameterDir.concat("\\\\").concat(parameterFileName);
		if (!(new File(parameterDir).exists())) {
			new File(parameterDir).mkdir();
		}
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(strFilePath);
			if (!file.exists()) {
				util.createBlankXml(strFilePath, "PARAMETERS");
			} else {
				file.delete();
				util.createBlankXml(strFilePath, "PARAMETERS");
			}
			//
			fis = new FileInputStream(new File(strFilePath));
			Document document = reader.read(fis);
			//
			Element root = document.getRootElement();
			//
			for (int i = 0; i < valueList.size(); i++) {
				Map map = (Map)valueList.get(i);
				String strName = (String)map.get("FIELD_NAME");
				String strValue = (String)map.get("FIELD_VALUE");
				String strFlg = (String)map.get("USE_FLG");
				Element parameterElment = root.addElement("PARAMETER");
				parameterElment.addElement("FIELD_NAME").setText(strName.toUpperCase());
				parameterElment.addElement("FIELD_VALUE").setText(strValue.toUpperCase());
				parameterElment.addElement("USE_FLG").setText(strFlg.toUpperCase());
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
		}
	}
}
