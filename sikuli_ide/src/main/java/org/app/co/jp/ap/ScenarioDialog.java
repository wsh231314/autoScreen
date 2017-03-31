package org.app.co.jp.ap;

import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.ScenarioDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class ScenarioDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel excelSheet = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel lblScenarioSelect;
	GridUtils grid = null;
	private JLabel titleStepId;
	private JLabel titlePageName;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField scenarioName = null;
	private JButton addPageField = null;
	private JLabel titleDeal = null;
	public List<String> _console;
	private JLabel titleSort;
	private JLabel stepIdInit;
	private JLabel pageIdInit;
	private JLabel pageNameInit;
	private JLabel fieldIdInit;
	private JLabel fieldNameInit;
	private JLabel fieldTypeInit;
	
	@SuppressWarnings("rawtypes")
	private JComboBox operationInit;
	private JCheckBox notUseInit;
	private JButton btnSortUpInit;
	
	private String strScenarioId = "";
	private String strScenarioName = "";
	private String strMode = "";
	private JLabel titleDelete;
	private JButton btnSortDownInit;
	private JButton btnDelInit;
	private JButton btnConfirm;
	
	ScenarioListDialog _parent;
	
	@SuppressWarnings("rawtypes")
	private List deleteList = new ArrayList();
	
	/**
	 * This method initializes 
	 * 
	 */
	public ScenarioDialog(ScenarioListDialog parent, String strScenarioId, String strScenarioName, String strMode) {
		super();
		_parent = parent;
		this.strScenarioId = strScenarioId;
		this.strScenarioName = strScenarioName;
		this.strMode = strMode;
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				_parent.searchDetailList();
				Utils.removeWindow(ScenarioDialog.this);
				_parent.setVisible(true);
			}
		});
		
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
        this.setSize(new Dimension(950, 600));
        this.setContentPane(getJPanel());

        List<String> title = new ArrayList<String>();
        title.add("Step Id");
        title.add("Page Id");
        title.add("Page Name");
        title.add("Field Id");
        title.add("Field Name");
        title.add("Field Type");
        title.add("Operation");
        title.add("Not Use");
        title.add("Sort Up");
        title.add("Sort Down");
        title.add("Del");

        List<JComponent> componentList = new ArrayList<JComponent>();
        componentList.add(stepIdInit);
        componentList.add(pageIdInit);
        componentList.add(pageNameInit);
        componentList.add(fieldIdInit);
        componentList.add(fieldNameInit);
        componentList.add(fieldTypeInit);
        componentList.add(operationInit);
        componentList.add(notUseInit);
        componentList.add(btnSortUpInit);
        componentList.add(btnSortDownInit);
        componentList.add(btnDelInit);
        
        String []arrColumn = {"STEP_ID", "PAGE_ID", "PAGE_NAME", "FIELD_ID", "FIELD_NAME", "FIELD_TYPE", "OPERATION", "NOT_USE", "DEAL_1", "DEAL_2", "DEAL_3"};
        String []arrTitle = {"STEP_ID", "PAGE_ID", "PAGE_NAME", "FIELD_ID", "FIELD_NAME", "FIELD_TYPE", "OPERATION", "NOT_USE", "DEAL_1", "DEAL_3"};
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 12, arrTitle);
        grid.setPageInfo(pageInfoLbl);
        
        try {
			searchDetailList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.exception(e);
		}
        
        setTitle("Scenario Page");
        
		if (CommonConstant.MODE_NEW.equals(strMode) || CommonConstant.MODE_COPY.equals(strMode)) {
			// confirm button
			btnConfirm.setText("Create");
		} else {
			btnConfirm.setText("Update");
		}
		scenarioName.setText(strScenarioName);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			titleDeal = new JLabel();
			titleDeal.setBounds(new Rectangle(590, 75, 100, 20));
			titleDeal.setHorizontalAlignment(SwingConstants.CENTER);
			titleDeal.setText("Operation");
			titleDeal.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new Rectangle(274, 402, 315, 30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("JLabel");
			titlePageName = new JLabel();
			titlePageName.setBounds(new Rectangle(150, 75, 150, 20));
			titlePageName.setHorizontalAlignment(SwingConstants.CENTER);
			titlePageName.setBackground(new Color(255,204,204));
			titlePageName.setText("Page Name");
			titleStepId = new JLabel();
			titleStepId.setBounds(new Rectangle(10, 75, 70, 20));
			titleStepId.setHorizontalAlignment(SwingConstants.CENTER);
			titleStepId.setText("Step ID");
			lblScenarioSelect = new JLabel();
			lblScenarioSelect.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblScenarioSelect.setText("Scenario Name");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(10, 10, 500, 24));
			jLabel.setText("You can create or update scenario at this page ");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getJButton(), null);
			jPanel.add(getExcelSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblScenarioSelect, null);
			jPanel.add(titleStepId, null);
			jPanel.add(titlePageName, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getScenarioName(), null);
			jPanel.add(getAddPageField(), null);
			jPanel.add(titleDeal, null);
			
			JLabel titlePageId = new JLabel();
			titlePageId.setText("Page ID");
			titlePageId.setHorizontalAlignment(SwingConstants.CENTER);
			titlePageId.setBounds(new Rectangle(11, 75, 100, 22));
			titlePageId.setBounds(80, 75, 70, 20);
			jPanel.add(titlePageId);
			
			JLabel titleFieldId = new JLabel();
			titleFieldId.setText("Field ID");
			titleFieldId.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldId.setBounds(new Rectangle(11, 75, 100, 22));
			titleFieldId.setBounds(300, 75, 70, 20);
			jPanel.add(titleFieldId);
			
			JLabel titleFieldName = new JLabel();
			titleFieldName.setText("Field Name");
			titleFieldName.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldName.setBounds(new Rectangle(11, 75, 100, 22));
			titleFieldName.setBounds(370, 75, 150, 20);
			jPanel.add(titleFieldName);
			
			JLabel titleType = new JLabel();
			titleType.setText("Field Type");
			titleType.setHorizontalAlignment(SwingConstants.CENTER);
			titleType.setBounds(new Rectangle(11, 75, 100, 22));
			titleType.setBounds(520, 75, 70, 20);
			jPanel.add(titleType);
			
			JLabel titleNotUse = new JLabel();
			titleNotUse.setText("Not Use");
			titleNotUse.setHorizontalAlignment(SwingConstants.CENTER);
			titleNotUse.setBounds(new Rectangle(570, 75, 71, 20));
			titleNotUse.setBackground(new Color(255, 204, 204));
			titleNotUse.setBounds(693, 75, 60, 20);
			jPanel.add(titleNotUse);
			jPanel.add(getTitleSort());
			jPanel.add(getTitleDelete());
			jPanel.add(getBtnConfirm());
		}
		return jPanel;
	}

	/**
	 * This method initializes excelSheet	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	@SuppressWarnings("rawtypes")
	private JPanel getExcelSheet() {
		if (excelSheet == null) {
			pageIdInit = new JLabel();
			pageIdInit.setBounds(new Rectangle(73, 0, 70, 20));
			pageIdInit.setText("JLabel");
			stepIdInit = new JLabel();
			stepIdInit.setBounds(new Rectangle(3, 0, 70, 20));
			stepIdInit.setText("JLabel");
			excelSheet = new JPanel();
			excelSheet.setBounds(new Rectangle(10, 95, 920, 300));
			excelSheet.setLayout(null);
			excelSheet.add(stepIdInit, null);
			excelSheet.add(pageIdInit, null);
			excelSheet.add(getPageNameInit());
			excelSheet.add(getFieldIdInit());
			excelSheet.add(getFieldNameInit());
			excelSheet.add(getFieldTypeInit());
			
			operationInit = new JComboBox();
			operationInit.setBounds(583, 0, 100, 20);
			excelSheet.add(operationInit);
			
			notUseInit = new JCheckBox("");
			notUseInit.setHorizontalAlignment(SwingConstants.CENTER);
			notUseInit.setBounds(693, 0, 30, 20);
			excelSheet.add(notUseInit);
			
			btnSortUpInit = new JButton();
			btnSortUpInit.addActionListener(new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(8))) {
							iRow = i;
						}
					}
					// 
					int dateRow = grid.getDataRow(iRow);
					List dataList = grid.getValueList();
					if (dateRow == 0) {
						return;
					}
					
					int page = grid.getPageNo();
					
					Map deleteMap_UP = (Map)dataList.get(dateRow);
					Map deleteMap_DOWN = (Map)dataList.get(dateRow-1);
					
					dataList.set(dateRow - 1, deleteMap_UP);
					dataList.set(dateRow, deleteMap_DOWN);
					grid.setData(dataList);
					grid.setPageNo(page);
					
				}
			});
			btnSortUpInit.setText("↑");
			btnSortUpInit.setPreferredSize(new Dimension(70, 30));
			btnSortUpInit.setFont(new Font("Dialog", Font.BOLD, 10));
			btnSortUpInit.setBounds(new Rectangle(557, 0, 70, 20));
			btnSortUpInit.setBounds(740, 0, 50, 20);
			excelSheet.add(btnSortUpInit);
			excelSheet.add(getBtnSortDownInit());
			excelSheet.add(getBtnDelInit());
		}
		return excelSheet;
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
			jButton.setLocation(new Point(840, 500));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
					_parent.searchDetailList();
					Utils.removeWindow(ScenarioDialog.this);
					_parent.setVisible(true);
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
			afterButton.setLocation(new Point(840, 403));
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
	private JTextField getScenarioName() {
		if (scenarioName == null) {
			scenarioName = new JTextField();
			scenarioName.setBounds(new Rectangle(100, 40, 500, 20));
		}
		return scenarioName;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddPageField() {
		if (addPageField == null) {
			addPageField = new JButton();
			addPageField.setBounds(new Rectangle(820, 40, 110, 20));
			addPageField.setText("Add Page Field");
			addPageField.setPreferredSize(new Dimension(70, 30));
			addPageField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					PageSelectDialog dialog = new PageSelectDialog(ScenarioDialog.this);
					
					setVisible(false);
					Utils.addWindow(dialog);
					
					dialog.setVisible(true);
				}
			});
		}
		return addPageField;
	}
	
	/**
	 * @throws Exception 
	 *
	 */
	public void searchDetailList() throws Exception {
		if (!Utils.isEmpty(strScenarioId)) {
			ScenarioDao dao = new ScenarioDao();
			List<Map<String, Object>> list = dao.searchList(strScenarioId);
			//
			grid.setData(list);
		}
	}
	private JLabel getTitleSort() {
		if (titleSort == null) {
			titleSort = new JLabel();
			titleSort.setText("Sort");
			titleSort.setHorizontalAlignment(SwingConstants.CENTER);
			titleSort.setBounds(new Rectangle(570, 75, 71, 20));
			titleSort.setBackground(new Color(255, 204, 204));
			titleSort.setBounds(730, 75, 120, 20);
		}
		return titleSort;
	}
	private JLabel getPageNameInit() {
		if (pageNameInit == null) {
			pageNameInit = new JLabel();
			pageNameInit.setText("JLabel");
			pageNameInit.setBounds(new Rectangle(73, 0, 70, 20));
			pageNameInit.setBounds(143, 0, 150, 20);
		}
		return pageNameInit;
	}
	private JLabel getFieldIdInit() {
		if (fieldIdInit == null) {
			fieldIdInit = new JLabel();
			fieldIdInit.setText("JLabel");
			fieldIdInit.setBounds(new Rectangle(73, 0, 70, 20));
			fieldIdInit.setBounds(293, 0, 70, 20);
		}
		return fieldIdInit;
	}
	private JLabel getFieldNameInit() {
		if (fieldNameInit == null) {
			fieldNameInit = new JLabel();
			fieldNameInit.setText("JLabel");
			fieldNameInit.setBounds(new Rectangle(73, 0, 70, 20));
			fieldNameInit.setBounds(363, 0, 150, 20);
		}
		return fieldNameInit;
	}
	private JLabel getFieldTypeInit() {
		if (fieldTypeInit == null) {
			fieldTypeInit = new JLabel();
			fieldTypeInit.setText("JLabel");
			fieldTypeInit.setBounds(new Rectangle(73, 0, 70, 20));
			fieldTypeInit.setBounds(513, 0, 70, 20);
		}
		return fieldTypeInit;
	}
	private JLabel getTitleDelete() {
		if (titleDelete == null) {
			titleDelete = new JLabel();
			titleDelete.setText("Delete");
			titleDelete.setHorizontalAlignment(SwingConstants.CENTER);
			titleDelete.setBounds(new Rectangle(570, 75, 71, 20));
			titleDelete.setBackground(new Color(255, 204, 204));
			titleDelete.setBounds(865, 75, 60, 20);
		}
		return titleDelete;
	}
	private JButton getBtnSortDownInit() {
		if (btnSortDownInit == null) {
			btnSortDownInit = new JButton();
			btnSortDownInit.addActionListener(new ActionListener() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public void actionPerformed(ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(9))) {
							iRow = i;
						}
					}
					// 
					int dateRow = grid.getDataRow(iRow);
					List dataList = grid.getValueList();
					if (dateRow == dataList.size() - 1) {
						return;
					}
					
					int page = grid.getPageNo();
					Map deleteMap_UP = (Map)dataList.get(dateRow + 1);
					Map deleteMap_DOWN = (Map)dataList.get(dateRow);
					
					dataList.set(dateRow, deleteMap_UP);
					dataList.set(dateRow + 1, deleteMap_DOWN);
					grid.setData(dataList);
					grid.setPageNo(page);
					
				}
			});
			btnSortDownInit.setText("↓");
			btnSortDownInit.setPreferredSize(new Dimension(70, 30));
			btnSortDownInit.setFont(new Font("Dialog", Font.BOLD, 10));
			btnSortDownInit.setBounds(new Rectangle(557, 0, 70, 20));
			btnSortDownInit.setBounds(790, 0, 50, 20);
		}
		return btnSortDownInit;
	}
	private JButton getBtnDelInit() {
		if (btnDelInit == null) {
			btnDelInit = new JButton();
			btnDelInit.addActionListener(new ActionListener() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public void actionPerformed(ActionEvent e) {
					int iResult = JOptionPane.showConfirmDialog(ScenarioDialog.this, "Do you want to delete?");
					if (iResult != JOptionPane.YES_OPTION) {
						return;
					}
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(10))) {
							iRow = i;
						}
					}
					// 
					int dateRow = grid.getDataRow(iRow);
					List dataList = grid.getValueList();
					Map deleteMap = (Map)dataList.get(dateRow);
					//
					String strId = (String)deleteMap.get("STEP_ID");
					if (!Check.isNull(strId)) {
						deleteList.add(deleteMap);
					}
					dataList.remove(dateRow);
					grid.setData(dataList);
				}
			});
			btnDelInit.setText("Del");
			btnDelInit.setPreferredSize(new Dimension(70, 30));
			btnDelInit.setFont(new Font("Dialog", Font.BOLD, 11));
			btnDelInit.setBounds(new Rectangle(557, 0, 70, 20));
			btnDelInit.setBounds(855, 0, 55, 20);
		}
		return btnDelInit;
	}
	private JButton getBtnConfirm() {
		if (btnConfirm == null) {
			btnConfirm = new JButton();
			btnConfirm.addActionListener(new ActionListener() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public void actionPerformed(ActionEvent e) {
					try {
						grid.freshData();
						List valueList = grid.getValueList();
						if ((valueList == null || valueList.size() == 0) && deleteList.size() == 0) {
							JOptionPane.showMessageDialog(ScenarioDialog.this, "Please add data field!");
							return;
						}
						
						if (valueList != null && valueList.size() > 0) {
							// scenarioId
							if (Utils.isEmpty(strScenarioId) || CommonConstant.MODE_COPY.equals(strMode)) {
								ComDao comDao = new ComDao();
								strScenarioId = "SCENARIO_".concat(comDao.getScenarioSeq(CommonConstant.PATTERN_CUSTOMER));
							}
							ScenarioDao scenarioDao = new ScenarioDao();
							scenarioDao.createByList(valueList, strScenarioId, scenarioName.getText().trim());
						}
						JOptionPane.showMessageDialog(ScenarioDialog.this, "Completed!");

						setVisible(false);
						_parent.searchDetailList();
						Utils.removeWindow(ScenarioDialog.this);
						_parent.setVisible(true);
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(ScenarioDialog.this, "Failed!");
						logger.exception(e1);
					}
				}
			});
			btnConfirm.setText("Confirm");
			btnConfirm.setSize(new Dimension(90, 30));
			btnConfirm.setLocation(new Point(790, 500));
			btnConfirm.setBounds(10, 500, 90, 30);
		}
		return btnConfirm;
	}
	
	@SuppressWarnings("unchecked")
	public void addSelectFields(PageSelectDialog dialog) {
		List<Map<String, String>> returnResult = dialog.getReturnValue();
		
		// get the page no
		int iPage = grid.getPageNo();
		
		// get data
		grid.freshData();
		List <Map<String, Object>> detailList = grid.getValueList();
		
		for (Map<String, String> map : returnResult) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("STEP_ID", "");
			item.put("PAGE_ID", map.get("PAGE_ID"));
			item.put("PAGE_NAME", map.get("PAGE_NAME"));
			item.put("FIELD_ID", map.get("FIELD_ID"));
			item.put("FIELD_NAME", map.get("FIELD_NAME"));
			item.put("FIELD_TYPE", map.get("FIELD_TYPE"));
			item.put("OPERATION", "");
			item.put("NOT_USE", "false");
			item.put("DEAL_1", CommonConstant.SORT_UP);
			item.put("DEAL_2", CommonConstant.SORT_DOWN);
			item.put("DEAL_3", "Del");
			item.put("OPERATION_FOR_SELECT", Utils.getOperationList());
			
			detailList.add(item);
		}
		
		grid.setData(detailList);
		grid.setPageNo(iPage);
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
