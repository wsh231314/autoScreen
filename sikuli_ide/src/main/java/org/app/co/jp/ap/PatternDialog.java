package org.app.co.jp.ap;

import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.PatternDao;
import org.app.co.jp.dao.TableDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.GridUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class PatternDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel excelSheet = null;
	private JButton btnExcelCreate = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel tableIdInit = null;
	private JLabel lblExcelSelect = null;
	private JLabel tableNameInit = null;
	public GridUtils grid = null;
	private JLabel titleTableId = null;
	private JLabel titleTableName = null;
	private JLabel titleRecordNum = null;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField patternName = null;
	private JLabel titleDeal = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JLabel titleTableComment = null;
	private JLabel tableCommentInit = null;
	private JLabel patternId = null;
	private JButton jButton1 = null;
	private JLabel recordNumInit = null;
	private JButton addRecord = null;
	
	private String strPatternId = "";
	private String strMode = "";
	private String strType = "";
	
	private Map tableMap = new HashMap();
	
	private List deleteList = new ArrayList();
	
	/**
	 * This method initializes 
	 * 
	 */
	public PatternDialog(String strPatternId, String strMode, String strType) {
		super();
		try {
			this.strPatternId = strPatternId;
			this.strMode = strMode;
			this.strType = strType;
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
			logger.exception(e);
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(800,600));
        this.setContentPane(getJPanel());

        List title = new ArrayList();
        title.add("Table Id");
        title.add("Table");
        title.add("Comment");
        title.add("Rec num");
        title.add("Detail");
        title.add("Copy");
        title.add("Del");

        List componentList = new ArrayList();
        componentList.add(tableIdInit);
        componentList.add(tableNameInit);
        componentList.add(tableCommentInit);
        componentList.add(recordNumInit);
        componentList.add(jButton2);
        componentList.add(jButton3);
        componentList.add(jButton1);

        String []arrColumn = {"TABLE_ID", "TABLE_NAME", "TABLE_COMMENT", "TABLE_NUMBER", "DEAL_1", "DEAL_2", "DEAL_3"};
        String []arrTitle = {"TABLE_ID", "TABLE_NAME", "TABLE_COMMENT", "TABLE_NUMBER", "DEAL_1"};
        // init grid
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 15, arrTitle);
        // set title
        grid.setPageInfo(pageInfoLbl);
        
        searchDetailList();
        
        setDisplayControl();
        
        setTitle("Pattern create page");
        
		if (CommonConstant.MODE_NEW.equals(strMode)) {
			// confirm button
			btnExcelCreate.setText("Create");
		} else if (CommonConstant.MODE_COPY.equals(strMode)) {
			btnExcelCreate.setText("Copy");
		} else {
			btnExcelCreate.setText("Update");
		}
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			patternId = new JLabel();
			patternId.setBounds(new java.awt.Rectangle(95,40,95,20));
			patternId.setText("");
			titleTableComment = new JLabel();
			titleTableComment.setBounds(new java.awt.Rectangle(270,75,240,20));
			titleTableComment.setHorizontalAlignment(SwingConstants.CENTER);
			titleTableComment.setText("Comment");
			titleTableComment.setBackground(new Color(255, 204, 204));
			titleDeal = new JLabel();
			titleDeal.setBounds(new java.awt.Rectangle(580,75,200,20));
			titleDeal.setHorizontalAlignment(SwingConstants.CENTER);
			titleDeal.setText("Operation");
			titleDeal.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,480,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("");
			titleRecordNum = new JLabel();
			titleRecordNum.setBounds(new java.awt.Rectangle(510,75,70,20));
			titleRecordNum.setHorizontalAlignment(SwingConstants.CENTER);
			titleRecordNum.setBackground(new Color(255,204,204));
			titleRecordNum.setText("Rec num");
			titleTableName = new JLabel();
			titleTableName.setBounds(new java.awt.Rectangle(100,75,170,20));
			titleTableName.setHorizontalAlignment(SwingConstants.CENTER);
			titleTableName.setBackground(new Color(255,204,204));
			titleTableName.setText("Table");
			titleTableId = new JLabel();
			titleTableId.setBounds(new java.awt.Rectangle(10,75,90,20));
			titleTableId.setHorizontalAlignment(SwingConstants.CENTER);
			titleTableId.setBackground(new Color(255,160,204));
			titleTableId.setText("Table ID");
			lblExcelSelect = new JLabel();
			lblExcelSelect.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblExcelSelect.setText("Pattern Comment");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,500,24));
			jLabel.setText("Create the table's definitions which used to create data in this pattern.");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getBtnExcelCreate(), null);
			jPanel.add(getJButton(), null);
			jPanel.add(getExcelSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblExcelSelect, null);
			jPanel.add(titleTableId, null);
			jPanel.add(titleTableName, null);
			jPanel.add(titleRecordNum, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getSearchPatternName(), null);
			jPanel.add(titleDeal, null);
			jPanel.add(titleTableComment, null);
			jPanel.add(patternId, null);
			jPanel.add(getAddRecord(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes excelSheet	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExcelSheet() {
		if (excelSheet == null) {
			recordNumInit = new JLabel();
			recordNumInit.setBounds(new java.awt.Rectangle(497,0,70,20));
			recordNumInit.setText("");
			recordNumInit.setBackground(new Color(255, 204, 204));
			tableCommentInit = new JLabel();
			tableCommentInit.setBounds(new java.awt.Rectangle(260,0,237,20));
			tableCommentInit.setText("Table Comment");
			tableCommentInit.setBackground(new Color(255, 204, 204));
			tableNameInit = new JLabel();
			tableNameInit.setBounds(new java.awt.Rectangle(90,0,170,20));
			tableNameInit.setText("JLabel");
			tableIdInit = new JLabel();
			tableIdInit.setBounds(new java.awt.Rectangle(3,0,87,20));
			tableIdInit.setText("JLabel");
			excelSheet = new JPanel();
			excelSheet.setBounds(new java.awt.Rectangle(10,95,770,383));
			excelSheet.setLayout(null);
			excelSheet.add(tableIdInit, null);
			excelSheet.add(tableNameInit, null);
			excelSheet.add(getJButton2(), null);
			excelSheet.add(getJButton3(), null);
			excelSheet.add(tableCommentInit, null);
			excelSheet.add(getJButton1(), null);
			excelSheet.add(recordNumInit, null);
		}
		return excelSheet;
	}

	/**
	 * This method initializes btnExcelCreate	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnExcelCreate() {
		if (btnExcelCreate == null) {
			btnExcelCreate = new JButton();
			btnExcelCreate.setText("Confirm");
			btnExcelCreate.setSize(new Dimension(90,30));
			btnExcelCreate.setLocation(new java.awt.Point(10,520));
			btnExcelCreate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						PatternDao dao = new PatternDao();
						grid.freshData();
						List valueList = grid.getValueList();
						if ((valueList == null || valueList.size() == 0) && deleteList.size() == 0) {
							JOptionPane.showMessageDialog(PatternDialog.this, "Please add data pattern!");
							return;
						}
						
						if (deleteList.size() > 0){
							int iResult = JOptionPane.showConfirmDialog(PatternDialog.this, "We will delete some data table definition. Is it OK?");
							if (iResult != JOptionPane.YES_OPTION) {
								return;
							}
							dao.deleteByList(deleteList, strPatternId, strType);
						}
						
						if (strMode.equals(CommonConstant.MODE_NEW) ||
								strMode.equals(CommonConstant.MODE_COPY)
								) {
							ComDao comDao = new ComDao();
							strPatternId = "pattern".concat("_").concat(comDao.getPatternSeq(strType));
						}
						
						if (valueList != null && valueList.size() > 0) {
							PatternDao patternDao = new PatternDao();
							patternDao.createByList(valueList, strPatternId, strType, patternName.getText().trim());
						}
						JOptionPane.showMessageDialog(PatternDialog.this, "Completed!");
						hide();
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(PatternDialog.this, "Failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return btnExcelCreate;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Close");
			jButton.setSize(new Dimension(90,30));
			jButton.setLocation(new java.awt.Point(690,520));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hide();
				}
			});
		}
		return jButton;
	}

	/**
	 */
	public File getExcelFile() {
		return initExcelFile;
	}

	/**
	 */
	public void setExcelFile(File excelFile) {
		this.initExcelFile = excelFile;
	}
	
	/**
	 * This method initializes preButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPreButton() {
		if (preButton == null) {
			preButton = new JButton();
			preButton.setText("Prev Page");
			preButton.setSize(new Dimension(90,30));
			preButton.setLocation(new java.awt.Point(10,480));
		}
		return preButton;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAfterButton() {
		if (afterButton == null) {
			afterButton = new JButton();
			afterButton.setLocation(new java.awt.Point(690,480));
			afterButton.setText("Next Page");
			afterButton.setSize(new Dimension(90,30));
		}
		return afterButton;
	}

	/**
	 * This method initializes searchPatternName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchPatternName() {
		if (patternName == null) {
			patternName = new JTextField();
			patternName.setBounds(new java.awt.Rectangle(195,40,462,20));
		}
		return patternName;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new java.awt.Rectangle(567,0,70,20));
			jButton2.setText("Detail");
			jButton2.setFont(new Font("Dialog", Font.BOLD, 10));
			jButton2.setPreferredSize(new Dimension(70, 30));
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(4))) {
							iRow = i;
						}
					}
					//
					String strTableId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					//
					String strTableName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					int dateRow = grid.getDataRow(iRow);
					Map valueMap = (Map)grid.getValueList().get(dateRow);
					// 
					TableDialog dialog = new TableDialog(strTableId, strTableName, CommonConstant.MODE_UPDATE);
					String dealFlg = (String)valueMap.get("DEAL_FLG");
					if (CommonConstant.DEAL_DEFAULT.equals(dealFlg)) {
						TableDao dao = new TableDao();
						dialog.getPropertyMap().putAll(dao.search(strPatternId, strType, strTableId));
					} else {
						dialog.getPropertyMap().putAll((Map)valueMap.get("TABLE_RULES"));
					}
					dialog.setCommentStr((String)valueMap.get("TABLE_COMMENT"));
					dialog.setNumberStr((String)valueMap.get("TABLE_NUMBER"));
					dialog.setTableMap(getTableMap());
					dialog.setModal(true);
					dialog.show();
					if (dialog.isBlnConfirm()) {
						List dataList = grid.getValueList();
						Map updateMap = (Map)dataList.get(dateRow);
						updateMap.put("TABLE_RULES", dialog.getPropertyMap());
						updateMap.put("DEAL_FLG", CommonConstant.DEAL_UPDATE);
						updateMap.put("TABLE_COMMENT", dialog.getComment());
						updateMap.put("TABLE_NUMBER", dialog.getNumber());
						grid.setData(dataList);
						setDisplayControl();
					}
				}
			});
		}
		return jButton2;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new java.awt.Rectangle(637,0,70,20));
			jButton3.setText("Copy");
			jButton3.setFont(new Font("Dialog", Font.BOLD, 10));
			jButton3.setPreferredSize(new Dimension(70, 30));
			jButton3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(4))) {
							iRow = i;
						}
					}
					//
					String strTableId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					//
					String strTableName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					int dateRow = grid.getDataRow(iRow);
					Map valueMap = (Map)grid.getValueList().get(dateRow);
					// 
					TableDialog dialog = new TableDialog(strTableId, strTableName, CommonConstant.MODE_COPY);
					String dealFlg = (String)valueMap.get("DEAL_FLG");
					if (CommonConstant.DEAL_DEFAULT.equals(dealFlg)) {
						TableDao dao = new TableDao();
						dialog.getPropertyMap().putAll(dao.search(strPatternId, strType, strTableId));
					} else {
						dialog.getPropertyMap().putAll((Map)valueMap.get("TABLE_RULES"));
					}
					dialog.setCommentStr((String)valueMap.get("TABLE_COMMENT"));
					dialog.setNumberStr((String)valueMap.get("TABLE_NUMBER"));
					dialog.setTableMap(getTableMap());
					dialog.setModal(true);
					dialog.show();
					if (dialog.isBlnConfirm()) {
						List dataList = grid.getValueList();
						Map updateMap = new HashMap();
						updateMap.put("TABLE_RULES", dialog.getPropertyMap());
						updateMap.put("DEAL_FLG", CommonConstant.DEAL_INSERT);
						updateMap.put("TABLE_COMMENT", dialog.getComment());
						updateMap.put("TABLE_NUMBER", dialog.getNumber());
						updateMap.put("TABLE_NAME", dialog.getName());
						updateMap.put("DEAL_1", "Detail");
						updateMap.put("DEAL_2", "Copy");
						updateMap.put("DEAL_3", "Del");
						dataList.add(updateMap);
						grid.setData(dataList);
						setDisplayControl();
					}
				}
			});
		}
		return jButton3;
	}
	
	/**
	 *
	 */
	private void searchDetailList() {
		if (strPatternId != null && !strPatternId.equals("")) {
			PatternDao dao = new PatternDao();
			if (CommonConstant.MODE_COPY.equals(strMode)) {
				List list = dao.searchCopyList(strPatternId, strType);
				grid.setData(list);
			} else {
				List list = dao.searchList(strPatternId, strType);
				grid.setData(list);
			}
			
		}
	}
	
	/**
	 *
	 */
	private void setDisplayControl() {
		List compList = grid.getComponentList();
		//
		if (compList != null && !compList.isEmpty()) {
			for (int i = 0; i < compList.size(); i++) {
				List rowList = (List)compList.get(i);
				// id
				String strId = ((JLabel)rowList.get(0)).getText();
				JButton deleteButton = (JButton)rowList.get(6);
				if (Check.isNull(strId)) {
					//deleteButton.setEnabled(false);
				} else {
					//deleteButton.setEnabled(true);
				}
			}
		}
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new java.awt.Rectangle(707,0,60,20));
			jButton1.setPreferredSize(new Dimension(70, 30));
			jButton1.setText("Del");
			jButton1.setFont(new Font("Dialog", Font.BOLD, 10));
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iResult = JOptionPane.showConfirmDialog(PatternDialog.this, "Do you want to delete?");
					if (iResult != JOptionPane.YES_OPTION) {
						return;
					}
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(6))) {
							iRow = i;
						}
					}
					// 
					int dateRow = grid.getDataRow(iRow);
					List dataList = grid.getValueList();
					Map deleteMap = (Map)dataList.get(dateRow);
					//
					String strId = (String)deleteMap.get("TABLE_ID");
					if (!Check.isNull(strId)) {
						deleteList.add(deleteMap);
					}
					dataList.remove(dateRow);
					grid.setData(dataList);
					setDisplayControl();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes addRecord	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddRecord() {
		if (addRecord == null) {
			addRecord = new JButton();
			addRecord.setBounds(new java.awt.Rectangle(670,40,110,20));
			addRecord.setText("Add");
			addRecord.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					TableDialog dialog = new TableDialog("", "", CommonConstant.MODE_NEW);
					dialog.setTableMap(getTableMap());
					dialog.setModal(true);
					dialog.show();
					if (dialog.isBlnConfirm()) {
						List dataList = grid.getValueList();
						Map updateMap = new HashMap();
						updateMap.put("TABLE_RULES", dialog.getPropertyMap());
						updateMap.put("DEAL_FLG", CommonConstant.DEAL_INSERT);
						updateMap.put("TABLE_NAME", dialog.getName());
						updateMap.put("TABLE_COMMENT", dialog.getComment());
						updateMap.put("TABLE_NUMBER", dialog.getNumber());
						updateMap.put("DEAL_1", "Detail");
						updateMap.put("DEAL_2", "Copy");
						updateMap.put("DEAL_3", "Del");
						dataList.add(updateMap);
						grid.setData(dataList);
						setDisplayControl();
					}
				}
			});
		}
		return addRecord;
	}
	
	/**
	 *
	 * @return
	 */
	public Map getTableMap() {
		tableMap.clear();
		//
		grid.freshData();
		// 
		List list = grid.getValueList();
		for (int i = 0; i < list.size(); i++) {
			String tableId = (String)((Map)list.get(i)).get("TABLE_ID");
			String tableName = (String)((Map)list.get(i)).get("TABLE_NAME");
			Set tempSet = null;
			if (tableMap.containsKey(tableName)) {
				tempSet = (Set)tableMap.get(tableName);
			} else {
				tempSet = new LinkedHashSet();
			}
			if (Check.isNull(tableId)) {
				tableId = "L-".concat(String.valueOf(i));
			}
			tempSet.add(tableId);
			tableMap.put(tableName, tempSet);
		}
		return tableMap;
	}
	
	public void setPatternName(String strPatternName) {
		patternName.setText(strPatternName);
	}
	public void setPatternId(String strPatternId) {
		patternId.setText(strPatternId);
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
