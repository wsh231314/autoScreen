package org.app.co.jp.ap;

import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.PatternListDao;
import org.app.co.jp.util.AutoDataUtil;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.bean.FileSelect;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternListDialog extends JDialog {

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
	private JLabel patternIdInit = null;
	private JCheckBox patternCheckInit = null;
	private JLabel lblExcelSelect = null;
	private JButton excelSelect = null;
	private JLabel patternNameInit = null;
	GridUtils grid = null;
	private JLabel titlePatternId = null;
	private JLabel titlePatternName = null;
	private JLabel titleSelect = null;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField searchPatternName = null;
	private JButton jButton1 = null;
	private JLabel titleDeal = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JLabel titlePatternType = null;
	private JLabel patternTypeInit = null;
	private JButton jButton4 = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public PatternListDialog() {
		super();
		try {
			initialize();
		} catch(Exception e) {
			logger.exception(e);
			e.printStackTrace();
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
        title.add("Pattern ID");
        title.add("Pattern Name");
        title.add("Pattern Type");
        title.add("Used");
        title.add("Detail");
        title.add("Copy");
		title.add("Del");

        List componentList = new ArrayList();
        componentList.add(patternIdInit);
        componentList.add(patternNameInit);
        componentList.add(patternTypeInit);
        componentList.add(patternCheckInit);
        componentList.add(jButton2);
        componentList.add(jButton3);
        componentList.add(jButton4);
        String []arrColumn = {"PATTERN_ID", "PATTERN_NAME", "PATTERN_TYPE", "SELECT", "DEAL_1", "DEAL_2", "DEAL_3"};
        String []arrTitle = {"PATTERN_ID", "PATTERN_NAME", "PATTERN_TYPE", "SELECT", "DEAL_1"};
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 15, arrTitle);
        grid.setPageInfo(pageInfoLbl);
        
        searchDetailList();
        
        setTitle("Pattern List Page");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			titlePatternType = new JLabel();
			titlePatternType.setBounds(new java.awt.Rectangle(380,75,120,20));
			titlePatternType.setHorizontalAlignment(SwingConstants.CENTER);
			titlePatternType.setText("Pattern Type");
			titlePatternType.setBackground(new Color(255, 204, 204));
			titleDeal = new JLabel();
			titleDeal.setBounds(new java.awt.Rectangle(570,75,210,20));
			titleDeal.setHorizontalAlignment(SwingConstants.CENTER);
			titleDeal.setText("Operation");
			titleDeal.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,402,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("JLabel");
			titleSelect = new JLabel();
			titleSelect.setBounds(new java.awt.Rectangle(500,75,70,20));
			titleSelect.setHorizontalAlignment(SwingConstants.CENTER);
			titleSelect.setBackground(new Color(255,204,204));
			titleSelect.setText("Select");
			titlePatternName = new JLabel();
			titlePatternName.setBounds(new java.awt.Rectangle(110,75,270,20));
			titlePatternName.setHorizontalAlignment(SwingConstants.CENTER);
			titlePatternName.setBackground(new Color(255,204,204));
			titlePatternName.setText("Pattern Name");
			titlePatternId = new JLabel();
			titlePatternId.setBounds(new java.awt.Rectangle(11,75,100,22));
			titlePatternId.setHorizontalAlignment(SwingConstants.CENTER);
			titlePatternId.setText("Pattern ID");
			lblExcelSelect = new JLabel();
			lblExcelSelect.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblExcelSelect.setText("Pattern Name");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,205,24));
			jLabel.setText("Select the pattern which want to create the test data and output the Excel file.");
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
			jPanel.add(lblExcelSelect, null);
			jPanel.add(getExcelSelect(), null);
			jPanel.add(titlePatternId, null);
			jPanel.add(titlePatternName, null);
			jPanel.add(titleSelect, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getSearchPatternName(), null);
			jPanel.add(getJButton1(), null);
			jPanel.add(titleDeal, null);
			jPanel.add(titlePatternType, null);
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
			patternTypeInit = new JLabel();
			patternTypeInit.setBounds(new java.awt.Rectangle(367,0,120,20));
			patternTypeInit.setText("pattern type");
			patternTypeInit.setBackground(new Color(255, 204, 204));
			patternNameInit = new JLabel();
			patternNameInit.setBounds(new java.awt.Rectangle(100,0,267,20));
			patternNameInit.setText("JLabel");
			patternIdInit = new JLabel();
			patternIdInit.setBounds(new java.awt.Rectangle(3,0,100,20));
			patternIdInit.setText("JLabel");
			excelSheet = new JPanel();
			excelSheet.setBounds(new java.awt.Rectangle(10,95,770,300));
			excelSheet.setLayout(null);
			excelSheet.add(patternIdInit, null);
			excelSheet.add(getExcelCheckInit(), null);
			excelSheet.add(patternNameInit, null);
			excelSheet.add(getJButton2(), null);
			excelSheet.add(getJButton3(), null);
			excelSheet.add(patternTypeInit, null);
			excelSheet.add(getJButton4(), null);
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
			btnExcelCreate.setText("Create Data");
			btnExcelCreate.setSize(new Dimension(150,30));
			btnExcelCreate.setFont(new Font("Dialog", Font.BOLD, 10));
			btnExcelCreate.setLocation(new java.awt.Point(10,507));
			btnExcelCreate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						grid.freshData();
						List valueList = grid.getValueList();
						List patternList = getSelectedList(valueList);
						if (patternList.size() == 0) {
							JOptionPane.showMessageDialog(PatternListDialog.this, "Please select the pattern which you want to create!");
							return;
						}
						if (txtSaveFileName.getText().trim().equals("")) {
							JOptionPane.showMessageDialog(PatternListDialog.this, "Please select the output file where to saved the data!");
							return;
						}
						AutoDataUtil util = new AutoDataUtil();
						util.creatDataByPatternList(patternList, txtSaveFileName.getText(), 1);
						JOptionPane.showMessageDialog(PatternListDialog.this, "Create Completed!");
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(PatternListDialog.this, "Create failed!");
						e1.printStackTrace();
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
			jButton.setLocation(new java.awt.Point(690,500));
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
	 */
	private List getSelectedList(List valueList) {
		List result = new ArrayList();
		for (int i = 0; i < valueList.size(); i++) {
			Map map = (Map)valueList.get(i); 
			//
			if (map.get("SELECT").equals("true")) {
				Map newMap = new HashMap();
				newMap.put("PATTERN_ID", map.get("PATTERN_ID"));
				newMap.put("PATTERN_NAME", map.get("PATTERN_NAME"));
				newMap.put("PATTERN_TYPE", map.get("PATTERN_TYPE"));
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
			sqlSaveButton.setLocation(new java.awt.Point(331,472));
			sqlSaveButton.setSize(new Dimension(110,20));
			sqlSaveButton.setActionCommand("");
			sqlSaveButton.setPreferredSize(new Dimension(70, 30));
			sqlSaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					FileSelect jfc = new FileSelect(); 
					int returnVal = jfc.showSaveDialog(PatternListDialog.this);
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
			txtSaveFileName.setBounds(new java.awt.Rectangle(10,472,310,20));
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
			afterButton.setLocation(new java.awt.Point(690,403));
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
		if (patternCheckInit == null) {
			patternCheckInit = new JCheckBox();
			patternCheckInit.setBounds(new java.awt.Rectangle(487,0,70,20));
			patternCheckInit.setHorizontalAlignment(SwingConstants.CENTER);
			patternCheckInit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return patternCheckInit;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getExcelSelect() {
		if (excelSelect == null) {
			excelSelect = new JButton();
			excelSelect.setText("Search");
			excelSelect.setLocation(new java.awt.Point(236,40));
			excelSelect.setSize(new Dimension(110,20));
			excelSelect.setPreferredSize(new Dimension(70, 30));
			excelSelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						searchDetailList();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(PatternListDialog.this, "Search Failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return excelSelect;
	}

	/**
	 * This method initializes searchPatternName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchPatternName() {
		if (searchPatternName == null) {
			searchPatternName = new JTextField();
			searchPatternName.setBounds(new java.awt.Rectangle(100,40,130,20));
		}
		return searchPatternName;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new java.awt.Rectangle(670,40,110,20));
			jButton1.setText("Add Pattern");
			jButton1.setPreferredSize(new Dimension(70, 30));
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PatternDialog dialog = new PatternDialog("", CommonConstant.MODE_NEW, CommonConstant.PATTERN_CUSTOMER);
					dialog.setModal(true);
					dialog.show();
					searchDetailList();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new java.awt.Rectangle(557,0,70,20));
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
					String strPatternId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strPatternName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					String strType = ((JLabel)((List)compList.get(iRow)).get(2)).getText();
					PatternDialog dialog = new PatternDialog(strPatternId, CommonConstant.MODE_UPDATE, strType);
					dialog.setPatternName(strPatternName);
					dialog.setPatternId(strPatternId);
					dialog.setModal(true);
					dialog.show();
			        searchDetailList();
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
			jButton3.setBounds(new java.awt.Rectangle(627,0,70,20));
			jButton3.setText("Copy");
			jButton3.setFont(new Font("Dialog", Font.BOLD, 10));
			jButton3.setPreferredSize(new Dimension(70, 30));
			jButton3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(5))) {
							iRow = i;
						}
					}
					String strPatternId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strPatternName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					String strType = ((JLabel)((List)compList.get(iRow)).get(2)).getText();
					PatternDialog dialog = new PatternDialog(strPatternId, CommonConstant.MODE_COPY, strType);
					dialog.setPatternName(strPatternName);
					dialog.setModal(true);
					dialog.show();
			        searchDetailList();
				}
			});
		}
		return jButton3;
	}
	
	/**
	 *
	 */
	public void searchDetailList() {
		PatternListDao dao = new PatternListDao();
		String strSearchTxt = searchPatternName.getText().trim();
		List list = dao.searchList(strSearchTxt);
		//
		grid.setData(list);
	}

	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setBounds(new java.awt.Rectangle(697,0,70,20));
			jButton4.setPreferredSize(new Dimension(70, 30));
			jButton4.setText("Del");
			jButton4.setFont(new Font("Dialog", Font.BOLD, 10));
			jButton4.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iResult = JOptionPane.showConfirmDialog(PatternListDialog.this, "Do you want to delete?");
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
					String strPatternId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strType = ((JLabel)((List)compList.get(iRow)).get(2)).getText();
					PatternListDao dao = new PatternListDao();
					dao.deleteByList(strPatternId, strType);
			        searchDetailList();
				}
			});
		}
		return jButton4;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
