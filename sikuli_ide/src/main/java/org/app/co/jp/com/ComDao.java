package org.app.co.jp.com;

import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.ConnectionUtil;
import org.app.co.jp.util.XMLUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author a5062903
 *
 */
public class ComDao extends BaseDao {
	
	private String strUser = "";
	
	private String strPassword = "";
	
	private String url = "";
	
	private String schema = "";
	
	private String strDriver = "";
	
	private static final String TABLE_SEQ_FILE = "TABLE_SEQ.xml";
	
	private static final String PATTERN_SEQ_FILE = "PATTERN_SEQ.xml";
	
	private static final String SCRIPT_SEQ_FILE = "SCRIPT_SEQ.xml";
	
	private static final String PAGE_SEQ_FILE = "PAGE_SEQ.xml";
	
	private static final String PAGE_FIELD_SEQ_FILE = "PAGE_FIELD_SEQ.xml";
	
	private static final String SCENARIO_SEQ_FILE = "SCENARIO_SEQ.xml";
	
	BasicLogger logger = BasicLogger.getLogger();
	
	public ComDao() {
        ConnectionUtil util = new ConnectionUtil();
        strUser = util.getProperty("user");
        strPassword = util.getProperty("password");
        schema = util.getProperty("schema");
        url = util.getProperty("url");
        strDriver = util.getProperty("driver");
	}
	
	/**
	 *
	 * @param pUser
	 * @param pPassword
	 * @param pSchema
	 * @param pUrl
	 * @return
	 */
	public String checkUrl(String pUser, String pPassword, String pSchema, String pUrl) {
		strUser = pUser;
		strPassword = pPassword;
		schema = pSchema;
		url = pUrl;
		// 
		Connection con = null;
		try {
			con = getConnection();
			
			StringBuffer sql = new StringBuffer();
			//
			sql.append("select * from all_objects where owner = '").append(schema.toUpperCase()).append("'");
			
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = con.prepareStatement(sql.toString());
				rs = stmt.executeQuery();

				if (!rs.next()) {
					return "Schema is not right!";
				}
			} finally {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
		} catch (SQLException e) {
			logger.exception(e);
			return e.getMessage();
		}
		
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public Connection getConnection() {
		Connection connection = null;
		
		try {
			Class.forName(strDriver);
			connection = DriverManager.getConnection(url, strUser, strPassword);
		} catch (Exception e) {
			logger.exception(e);
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, String>> getTableColumnInfo(Connection con) throws SQLException {
		//
		StringBuffer sql = new StringBuffer();
		//
		sql.append("SELECT ");
		sql.append("b.TABLE_NAME, b.COLUMN_NAME, b.DATA_TYPE, b.DATA_LENGTH, ");
		sql.append("b.DATA_PRECISION, b.DATA_SCALE, b.NULLABLE, b.COLUMN_ID, nvl2(d.COLUMN_NAME, 'true', 'false')as KEY_FLG  FROM all_objects a ");
		sql.append("inner join all_tab_columns b ");
		sql.append("on a.object_type = 'TABLE' ");
		sql.append("and a.OWNER = '").append(schema.toUpperCase()).append("' ");
		sql.append("and a.OWNER = b.OWNER ");
		sql.append("and a.OBJECT_NAME = b.TABLE_NAME ");
		sql.append("left join DBA_CONSTRAINTS c ");
		sql.append("on c.CONSTRAINT_TYPE = 'P' ");
		sql.append("and c.OWNER = '").append(schema.toUpperCase()).append("' ");
		sql.append("and b.TABLE_NAME = c.TABLE_NAME ");
		sql.append("left join DBA_CONS_COLUMNS d ");
		sql.append("on d.OWNER = '").append(schema.toUpperCase()).append("' ");
		sql.append("and c.TABLE_NAME = d.TABLE_NAME ");
		sql.append("and c.CONSTRAINT_NAME = d.CONSTRAINT_NAME ");
		sql.append("and b.COLUMN_NAME = d.COLUMN_NAME ");
		sql.append("ORDER BY b.TABLE_NAME, b.COLUMN_ID ");
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();;
		try {
			stmt = con.prepareStatement(sql.toString());
			rs = stmt.executeQuery();

			while(rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("TABLE_NAME", getValue(rs, "TABLE_NAME"));
				map.put("COLUMN_NAME", getValue(rs, "COLUMN_NAME"));
				map.put("DATA_TYPE", getValue(rs, "DATA_TYPE"));
				map.put("DATA_LENGTH", getValue(rs, "DATA_LENGTH"));
				map.put("DATA_PRECISION", getValue(rs, "DATA_PRECISION"));
				map.put("DATA_SCALE", getValue(rs, "DATA_SCALE"));
				map.put("NULLABLE", getValue(rs, "NULLABLE"));
				map.put("COLUMN_ID", getValue(rs, "COLUMN_ID"));
				map.put("KEY_FLG", getValue(rs, "KEY_FLG"));
				
				result.add(map);
			}
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @param rs
	 * @param key
	 * @return
	 * @throws SQLException 
	 */
	private String getValue(ResultSet rs, String key) throws SQLException {
		if (rs.getObject(key) != null) {
			return rs.getObject(key).toString();
		} else {
			return "";
		}
	}
	
	/**
	 *
	 * @return SEQ
	 */
	public String getTableSeq(String strPattern, String strType, String strTable) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(TABLE_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strType).concat("/").concat(strPattern);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "0";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 */
	public String getPatternSeq(String strType) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(PATTERN_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strType);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "0";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 * @return SEQ
	 */
	public String getDataSeq(String strTable, String strColumn) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(strTable).concat(".xml");
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strTable);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strTable).concat("/").concat(strColumn);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "0";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 */
	public String getScriptSeq(String strType) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(SCRIPT_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strType);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "0";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 */
	public String getPageSeq(String strType) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(PAGE_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strType);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "1";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 *
	 * @return SEQ
	 */
	public String getFieldSeq(String strPageId, String strType) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(PAGE_FIELD_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strType).concat("/").concat(strPageId);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "1";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 *
	 * @return SEQ
	 */
	public String copyPageInfo(String strFromPageId, String strToPageId, String strType) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(PAGE_FIELD_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			
			// from 
			String strFromXPATH = "//".concat(strType).concat("/").concat(strFromPageId);
			Node nodeFrom = document.selectSingleNode(strFromXPATH);
			//
			String seqFrom = nodeFrom.getText();
			
			// to
			String strToXPATH = "//".concat(strType).concat("/").concat(strToPageId);
			util.preCheckAddDoc(document, "", strToXPATH);
			Node nodeTo = document.selectSingleNode(strToXPATH);
			nodeTo.setText(seqFrom);
			
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
	/**
	 */
	public String getScenarioSeq(String strType) {
		XMLUtils util = new XMLUtils();
		XMLWriter writer = new XMLWriter();
		// SEQ
		String strSeq = "";
		//
		FileInputStream fis = null;
		try {
			SAXReader reader = new SAXReader();
			String seqFilePath = seqDir.concat(SCENARIO_SEQ_FILE);
			File file = new File(seqFilePath);
			if (!file.exists()) {
				util.createBlankXml(seqFilePath, strType);
			}
			fis = new FileInputStream(file);
			Document document = reader.read(fis);
			String strXPATH = "//".concat(strType);
			util.preCheckAddDoc(document, "", strXPATH);
			Node node = document.selectSingleNode(strXPATH);
			//
			String seq = node.getText();
			if (seq == null || seq.trim().equals("")) {
				strSeq = "1";
			} else {
				int iSeq = Integer.parseInt(seq.trim()) + 1;
				strSeq = String.valueOf(iSeq);
			}
			node.setText(strSeq);
			//
			FileOutputStream out = new FileOutputStream(seqFilePath);
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

		return strSeq;
	}
	
}
