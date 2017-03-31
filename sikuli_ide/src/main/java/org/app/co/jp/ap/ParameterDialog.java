package org.app.co.jp.ap;

import org.app.co.jp.dao.ParameterDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.ParameterUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterDialog extends JDialog {

	private JPanel jPanel = null;
	private JPanel excelSheet = null;
	private JButton btnSet = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JCheckBox ckbUseable = null;
	private JLabel lblSqlOuput = null;
	GridUtils grid = null;
	private JLabel titleFileNameLbl = null;
	private JLabel titleSheetName = null;
	private JLabel titleSelect = null;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JTextField txtFieldName = null;
    private JTextField txtFieldValue = null;
    private JLabel jLabel2 = null;
    private JButton btnDel = null;
    private JButton jButton1 = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public ParameterDialog() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(650,450));
        this.setContentPane(getJPanel());
        // title
        List title = new ArrayList();
        title.add("Column name");
        title.add("Covered content");
        title.add("Used");

        // detail component
        List componentList = new ArrayList();
        componentList.add(txtFieldName);
        componentList.add(txtFieldValue);
        componentList.add(ckbUseable);
        componentList.add(btnDel);

        String []arrColumn = {"FIELD_NAME", "FIELD_VALUE", "USE_FLG", "DEAL"};
        String []arrTitle = {"FIELD_NAME", "FIELD_VALUE", "USE_FLG", "DEAL"};
        // init grid
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 10, arrTitle);

        // page label
        grid.setPageInfo(pageInfoLbl);

		// show init info
        searchSaveFieldList();
        
        // set title
        this.setTitle("Common column setting");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setBounds(new java.awt.Rectangle(546,75,75,20));
			jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel2.setText("Delete");
			jLabel2.setBackground(new Color(255, 204, 204));
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,437,20));
			jLabel.setText("When create scripts for the data in excel sheet, the columns in here, ");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new java.awt.Rectangle(10,37,437,20));
			jLabel1.setText("will be automatically covered by the content in the [Covered content] column.");
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(156,340,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("");
			titleSelect = new JLabel();
			titleSelect.setBounds(new java.awt.Rectangle(415,75,130,20));
			titleSelect.setHorizontalAlignment(SwingConstants.CENTER);
			titleSelect.setBackground(new Color(255,204,204));
			titleSelect.setText("Use flag");
			titleSheetName = new JLabel();
			titleSheetName.setBounds(new java.awt.Rectangle(162,75,250,20));
			titleSheetName.setHorizontalAlignment(SwingConstants.CENTER);
			titleSheetName.setBackground(new Color(255,204,204));
			titleSheetName.setText("Covered content");
			titleFileNameLbl = new JLabel();
			titleFileNameLbl.setBounds(new java.awt.Rectangle(10,75,150,20));
			titleFileNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
			titleFileNameLbl.setBackground(new Color(255,160,204));
			titleFileNameLbl.setText("Column name");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getBtnSet(), null);
			jPanel.add(getJButton(), null);
			jPanel.add(getExcelSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(titleFileNameLbl, null);
			jPanel.add(titleSheetName, null);
			jPanel.add(titleSelect, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(jLabel, null);
			jPanel.add(jLabel1, null);
			jPanel.add(jLabel2, null);
			jPanel.add(getJButton1(), null);
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
			excelSheet = new JPanel();
			excelSheet.setBounds(new java.awt.Rectangle(10,95,620,236));
			excelSheet.setLayout(null);
			excelSheet.add(getCkbUseable(), null);
			excelSheet.add(getTxtFieldName(), null);
			excelSheet.add(getTxtFieldValue(), null);
			excelSheet.add(getBtnDel(), null);
		}
		return excelSheet;
	}

	/**
	 * This method initializes btnExcelCreate	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSet() {
		if (btnSet == null) {
			btnSet = new JButton();
			btnSet.setText("Setting");
			btnSet.setSize(new java.awt.Dimension(90,30));
			btnSet.setLocation(new java.awt.Point(11,385));
			btnSet.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						grid.freshData();
						List valueList = grid.getValueList();
						//
						if (!checkInput(valueList)) {
							return;
						}
						List sheetList = getValidList(valueList);
						int iResult = JOptionPane.showConfirmDialog(ParameterDialog.this, "Do you want to save?");
						if (iResult != JOptionPane.YES_OPTION) {
							return;
						}

						ParameterDao dao = new ParameterDao();
						dao.createByList(sheetList);
						JOptionPane.showMessageDialog(ParameterDialog.this, "Setting succeed!");

						searchSaveFieldList();
						//
						ParameterUtil.freshParameterMap();
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(ParameterDialog.this, "Setting failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return btnSet;
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
			jButton.setSize(new java.awt.Dimension(90,30));
			jButton.setLocation(new java.awt.Point(536,385));
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
	private void searchSaveFieldList() {
		ParameterDao dao = new ParameterDao();
		List list = dao.searchAllParameter();
		//
		grid.setData(list);
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
	 */
	private List getValidList(List valueList) {
		List result = new ArrayList();

		for (int i = 0; i < valueList.size(); i++) {
			Map map = (Map)valueList.get(i); 
			String strFieldName = (String)map.get("FIELD_NAME");
			String strFieldValue = (String)map.get("FIELD_VALUE");
			//
			if (!Check.isNull(strFieldName) && !Check.isNull(strFieldValue)) {
				result.add(((HashMap)map).clone());
			}
		}
		return result;
	}
	
	/** 
	 *
	 * @return
	 */
	private boolean checkInput(List valueList) {
		for (int i = 0; i < valueList.size(); i++) {
			Map map = (Map)valueList.get(i); 
			String strFieldName = (String)map.get("FIELD_NAME");
			String strFieldValue = (String)map.get("FIELD_VALUE");
			//
			if (!Check.isNull(strFieldName) || !Check.isNull(strFieldValue)) {
				if (Check.isNull(strFieldName) || Check.isNull(strFieldValue)) {
					JOptionPane.showMessageDialog(ParameterDialog.this, "You must input the Column and the converted content at one time!");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This method initializes preButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPreButton() {
		if (preButton == null) {
			preButton = new JButton();
			preButton.setText("Prev page");
			preButton.setSize(new java.awt.Dimension(90,30));
			preButton.setLocation(new java.awt.Point(12,340));
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
			afterButton.setLocation(new java.awt.Point(536,340));
			afterButton.setText("Next page");
			afterButton.setSize(new java.awt.Dimension(90,30));
		}
		return afterButton;
	}

	/**
	 * This method initializes excelCheckInit	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCkbUseable() {
		if (ckbUseable == null) {
			ckbUseable = new JCheckBox();
			ckbUseable.setBounds(new java.awt.Rectangle(410,0,130,20));
			ckbUseable.setText("Used");
		}
		return ckbUseable;
	}

    /**
	 * This method initializes txtFieldName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtFieldName() {
		if (txtFieldName == null) {
			txtFieldName = new JTextField();
			txtFieldName.setBounds(new java.awt.Rectangle(3,0,150,20));
		}
		return txtFieldName;
	}

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtFieldValue() {
		if (txtFieldValue == null) {
			txtFieldValue = new JTextField();
			txtFieldValue.setBounds(new java.awt.Rectangle(155,0,250,20));
		}
		return txtFieldValue;
	}

    /**
	 * This method initializes btnDel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDel() {
		if (btnDel == null) {
			btnDel = new JButton();
			btnDel.setBounds(new java.awt.Rectangle(543,0,72,20));
			btnDel.setText("Delete");
			btnDel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iResult = JOptionPane.showConfirmDialog(ParameterDialog.this, "Do you want to delete the selected data?");
					if (iResult != JOptionPane.YES_OPTION) {
						return;
					}
					grid.freshData();
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(3))) {
							iRow = i;
						}
					}
					// 
					int dateRow = grid.getDataRow(iRow);
					List dataList = grid.getValueList();
					dataList.remove(dateRow);
					grid.setData(dataList);
				}
			});
		}
		return btnDel;
	}

    /**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new java.awt.Rectangle(536,27,90,30));
			jButton1.setText("Add");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					grid.freshData();
					List list = grid.getValueList();
					// 
					Map map = new HashMap();
					map.put("FIELD_NAME", "");
					map.put("FIELD_VALUE", "");
					map.put("USE_FLG", "TRUE");
					map.put("DEAL", "Delete");
					list.add(map);
					grid.setData(list);
					grid.setPageNo(grid.getMaxPageNo());
				}
			});
		}
		return jButton1;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
