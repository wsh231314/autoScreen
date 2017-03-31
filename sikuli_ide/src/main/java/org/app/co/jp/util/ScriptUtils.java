package org.app.co.jp.util;

import java.io.File;
import java.io.IOException;

import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.ScriptListDao;

/**
 * create script ID and so on
 * 
 * @author shawn.shaohua.wang
 *
 */
public class ScriptUtils {
	
	/**
	 * 
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static File createScriptInfo(String strScriptName) throws IOException {
		ComDao comDao = new ComDao();
		
		//Script id
		String strScriptId = "script".concat("_").concat(comDao.getScriptSeq(CommonConstant.PATTERN_CUSTOMER));
		
		//Script file name  
		String strFileName = strScriptId.concat(".sikuli");
		
		// ITEM create
		ScriptListDao listDao = new ScriptListDao();
		
		String strPath = listDao.createScriptData(strScriptId, strFileName, strScriptName);
		
		
		return new File(strPath);
	}
	
	/**
	 * 
	 * 
	 * @param strScriptId
	 * @param strScriptName
	 * @return
	 * @throws IOException
	 */
	public static void updateScriptInfo(String strScriptId, String strScriptName) throws IOException {
		
		// ITEM create
		ScriptListDao listDao = new ScriptListDao();
		
		listDao.updateScriptData(strScriptId, strScriptName);
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static void copyById(String strScriptId, String strScriptName) throws IOException {
		ComDao comDao = new ComDao();
		
		//Script id
		String strNewScriptId = "script".concat("_").concat(comDao.getScriptSeq(CommonConstant.PATTERN_CUSTOMER));
		
		//Script new file name  
		String strFileName = strNewScriptId.concat(".sikuli");
		
		// ITEM create
		ScriptListDao listDao = new ScriptListDao();
		
		String strToPath = listDao.createScriptData(strNewScriptId, strFileName, strScriptName);
		
		String strFromPath = strToPath.replaceAll(strNewScriptId, strScriptId);
		
		Utils.copyFileByFolder(strFromPath, strToPath, strScriptId, strNewScriptId);
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static void deleteScriptId(String strScriptId) throws IOException {
		ScriptListDao dao = new ScriptListDao();
		String path = dao.deleteByList(strScriptId);
		
		Utils.deleteDirectory(path);
	}

}
