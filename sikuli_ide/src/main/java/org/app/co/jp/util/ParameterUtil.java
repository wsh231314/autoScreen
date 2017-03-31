package org.app.co.jp.util;

import org.app.co.jp.dao.ParameterDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterUtil {

	static Map parameterMap;
	
	BasicLogger logger = BasicLogger.getLogger();
	
	static {
		parameterMap = new HashMap();
		ParameterDao dao = new ParameterDao();
		List list = dao.searchAllParameter();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map)list.get(i);
			//
			String fieldName = (String)map.get("FIELD_NAME");
			String fieldValue = (String)map.get("FIELD_VALUE");
			String useFlg = (String)map.get("USE_FLG");
			if (Boolean.valueOf(useFlg).booleanValue()) {
				parameterMap.put(fieldName, fieldValue);
			}
		}
	}
	
	/**
	 *
	 */
	public static void freshParameterMap() {
		parameterMap.clear();
		ParameterDao dao = new ParameterDao();
		List list = dao.searchAllParameter();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map)list.get(i);
			//
			String fieldName = (String)map.get("FIELD_NAME");
			String fieldValue = (String)map.get("FIELD_VALUE");
			String useFlg = (String)map.get("USE_FLG");
			if (Boolean.valueOf(useFlg).booleanValue()) {
				parameterMap.put(fieldName, fieldValue);
			}
		}
	}
	
	/**
	 */
	public static Map getParameterMap() {
		return parameterMap;
	}
	
	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getValueByField(String key, String value) {
		String strValue = "";
		if (parameterMap.containsKey(key)) {
			strValue = (String)parameterMap.get(key);
		} else {
			strValue = value;
		}
		
		return strValue;
	}
}
