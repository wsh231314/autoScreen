package org.app.co.jp.ap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.ExcelUtils;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.bean.FileSelect;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimaryExcelDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel excelSheet = null;
	private JButton btnExcelCreate = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton sqlSaveButton = null;
	private JTextField txtSaveFileName = null;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel excelLabelInit = null;
	private JCheckBox excelCheckInit = null;
	private JLabel lblSqlOuput = null;
	private JLabel lblExcelSelect = null;
	private JButton excelSelect = null;
	private JLabel excelSheetNameInit = null;
	GridUtils grid = null;
	Map excelMap = new HashMap();
	List valueList = new ArrayList();
	private JCheckBox checkMode = null;
	private JLabel titleFileNameLbl = null;
	private JLabel titleSheetName = null;
	private JLabel titleSelect = null;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JCheckBox ckbParameterFlg = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public PrimaryExcelDialog(File excelFile) {
		super();
		this.initExcelFile = excelFile;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(800,600));
        this.setContentPane(getJPanel());
        List title = new ArrayList();
        title.add("Document Name");
        title.add("Sheet Name");
        title.add("Select");

        List componentList = new ArrayList();
        componentList.add(excelLabelInit);
        componentList.add(excelSheetNameInit);
        componentList.add(excelCheckInit);
        String []arrColumn = {"EXCEL_NAME", "SHEET_NAME", "SELECT"};
        String []arrTitle = {"EXCEL_NAME", "SHEET_NAME", "SELECT"};
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 15, arrTitle);
        grid.setPageInfo(pageInfoLbl);
        dealExcelSheet(initExcelFile);
        
        this.setTitle("SQL Script Output Page");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,402,315,30));
			pageInfoLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			pageInfoLbl.setText("JLabel");
			titleSelect = new JLabel();
			titleSelect.setBounds(new java.awt.Rectangle(703,75,77,20));
			titleSelect.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			titleSelect.setBackground(new java.awt.Color(255,204,204));
			titleSelect.setText("Select");
			titleSheetName = new JLabel();
			titleSheetName.setBounds(new java.awt.Rectangle(433,75,270,20));
			titleSheetName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			titleSheetName.setBackground(new java.awt.Color(255,204,204));
			titleSheetName.setText("Sheet Name");
			titleFileNameLbl = new JLabel();
			titleFileNameLbl.setBounds(new java.awt.Rectangle(10,75,423,20));
			titleFileNameLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			titleFileNameLbl.setBackground(new java.awt.Color(255,160,204));
			titleFileNameLbl.setText("File Name");
			lblExcelSelect = new JLabel();
			lblExcelSelect.setBounds(new java.awt.Rectangle(10,43,318,20));
			lblExcelSelect.setText("You can add Excel file, if you want to deal.");
			lblSqlOuput = new JLabel();
			lblSqlOuput.setBounds(new java.awt.Rectangle(10,626,316,20));
			lblSqlOuput.setText("Please select the output place where to save the script.");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,205,24));
			jLabel.setText("Please select the sheet.");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getBtnExcelCreate(), null);
			jPanel.add(getJButton(), null);
			jPanel.add(getSqlSaveButton(), null);
			jPanel.add(getTxtSaveFileName(), null);
			jPanel.add(getExcelSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblSqlOuput, null);
			jPanel.add(lblExcelSelect, null);
			jPanel.add(getExcelSelect(), null);
			jPanel.add(getCheckMode(), null);
			jPanel.add(titleFileNameLbl, null);
			jPanel.add(titleSheetName, null);
			jPanel.add(titleSelect, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getCkbParameterFlg(), null);
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
			excelSheetNameInit = new JLabel();
			excelSheetNameInit.setBounds(new java.awt.Rectangle(423,0,270,20));
			excelSheetNameInit.setText("JLabel");
			excelLabelInit = new JLabel();
			excelLabelInit.setBounds(new java.awt.Rectangle(3,0,420,20));
			excelLabelInit.setText("JLabel");
			excelSheet = new JPanel();
			excelSheet.setBounds(new java.awt.Rectangle(10,95,770,300));
			excelSheet.setLayout(null);
			excelSheet.add(excelLabelInit, null);
			excelSheet.add(getExcelCheckInit(), null);
			excelSheet.add(excelSheetNameInit, null);
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
			btnExcelCreate.setText("SQL Create");
			btnExcelCreate.setSize(new Dimension(110,30));
			btnExcelCreate.setLocation(new java.awt.Point(11,507));
			btnExcelCreate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						grid.freshData();
						List valueList = grid.getValueList();
						List sheetList = getSelectedList(valueList);
						if (sheetList.size() == 0) {
							JOptionPane.showMessageDialog(PrimaryExcelDialog.this, "Please select the sheet you want to deal!");
							return;
						}
						if (txtSaveFileName.getText().trim().equals("")) {
							JOptionPane.showMessageDialog(PrimaryExcelDialog.this, "Please select the file where to save the script!");
							return;
						}
						
						ExcelUtils utils = new ExcelUtils();
						//
						for (int i = 0; i < sheetList.size(); i++) {
							Map map = (Map)sheetList.get(i);
							String fileName = (String)map.get("EXCEL_NAME");
							String sheetName = (String)map.get("SHEET_NAME");
							HSSFWorkbook wbk = (HSSFWorkbook)excelMap.get(fileName);
							HSSFSheet sheet = wbk.getSheet(sheetName);
							
							int iMode = 1;
							if (checkMode.isSelected()) {
								iMode = 2;
							}
							utils.ouputSql(sheet, txtSaveFileName.getText(), iMode, ckbParameterFlg.isSelected());
							// 
						}
						JOptionPane.showMessageDialog(PrimaryExcelDialog.this, "Completed!");
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(PrimaryExcelDialog.this, "Failed!");
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
			jButton.setLocation(new java.awt.Point(688,500));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hide();
				}
			});
		}
		return jButton;
	}
	
	/**
	 *
	 */
	private void dealExcelSheet(File file) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			HSSFWorkbook wbk = new HSSFWorkbook(fis);
			excelMap.put(file.getName(), wbk);
			// 
			int iCount = wbk.getNumberOfSheets();
			for (int i = 0; i < iCount; i++) {
				Map map = new HashMap();
				map.put("EXCEL_NAME", file.getName());
				map.put("SHEET_NAME", wbk.getSheetName(i));
				map.put("SELECT", "false");
				valueList.add(map);
			}
			grid.setData(valueList);
		} catch (Exception e) {
			logger.exception(e);
		}
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
	 *
	 */
	private List getSelectedList(List valueList) {
		List result = new ArrayList();
		for (int i = 0; i < valueList.size(); i++) {
			Map map = (Map)valueList.get(i); 
			//
			if (map.get("SELECT").equals("true")) {
				Map newMap = new HashMap();
				newMap.put("EXCEL_NAME", map.get("EXCEL_NAME"));
				newMap.put("SHEET_NAME", map.get("SHEET_NAME"));
				result.add(newMap);
			}
		}
		return result;
	}
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSqlSaveButton() {
		if (sqlSaveButton == null) {
			sqlSaveButton = new JButton();
			sqlSaveButton.setText("File Select");
			sqlSaveButton.setLocation(new java.awt.Point(327,448));
			sqlSaveButton.setSize(new Dimension(110,20));
			sqlSaveButton.setActionCommand("");
			sqlSaveButton.setPreferredSize(new Dimension(70, 30));
			sqlSaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					FileSelect jfc = new FileSelect(); 
					int returnVal = jfc.showSaveDialog(PrimaryExcelDialog.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						txtSaveFileName.setText(jfc.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}
		return sqlSaveButton;
	}

	/**
	 * This method initializes txtSaveFileName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSaveFileName() {
		if (txtSaveFileName == null) {
			txtSaveFileName = new JTextField();
			txtSaveFileName.setBounds(new java.awt.Rectangle(10,448,310,20));
			txtSaveFileName.setEditable(false);
		}
		return txtSaveFileName;
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
			preButton.setLocation(new java.awt.Point(10,399));
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
			afterButton.setLocation(new java.awt.Point(688,403));
			afterButton.setText("Next Page");
			afterButton.setSize(new Dimension(90,30));
		}
		return afterButton;
	}

	/**
	 * This method initializes excelCheckInit	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getExcelCheckInit() {
		if (excelCheckInit == null) {
			excelCheckInit = new JCheckBox();
			excelCheckInit.setBounds(new java.awt.Rectangle(693,0,72,20));
		}
		return excelCheckInit;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getExcelSelect() {
		if (excelSelect == null) {
			excelSelect = new JButton();
			excelSelect.setText("Select excel");
			excelSelect.setLocation(new java.awt.Point(336,43));
			excelSelect.setSize(new Dimension(110,20));
			excelSelect.setPreferredSize(new Dimension(70, 30));
			excelSelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						FileSelect jfc = new FileSelect();
						jfc.setVisible(true);
						int returnVal = jfc.showOpenDialog(PrimaryExcelDialog.this);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							if (jfc.getSelectedFile().getName().indexOf(".xls") < 0) {
								JOptionPane.showMessageDialog(PrimaryExcelDialog.this, "Please select excel file!");
								return;
							}
							if (excelMap.containsKey(jfc.getSelectedFile().getName())) {
								JOptionPane.showMessageDialog(PrimaryExcelDialog.this, "We have read this file!");
								return;
							}
							dealExcelSheet(new File(jfc.getSelectedFile().getAbsolutePath()));
						}
					} catch (Exception e1) {
						logger.exception(e1);
					}
				}
			});
		}
		return excelSelect;
	}

	/**
	 * This method initializes checkMode	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCheckMode() {
		if (checkMode == null) {
			checkMode = new JCheckBox();
			checkMode.setBounds(new java.awt.Rectangle(114,507,338,14));
			checkMode.setText("create update script");
		}
		return checkMode;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCkbParameterFlg() {
		if (ckbParameterFlg == null) {
			ckbParameterFlg = new JCheckBox();
			ckbParameterFlg.setBounds(new java.awt.Rectangle(469,507,163,14));
			ckbParameterFlg.setSelected(true);
			ckbParameterFlg.setText("convert the common column");
		}
		return ckbParameterFlg;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
