package org.app.co.jp.ap;

import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.ScenarioListDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.Utils;
import org.sikuli.ide.SikuliIDE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScenarioListDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel detailPanel = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel lblScenarioSelect;
	private JButton scenarioSelect = null;
	GridUtils grid = null;
	private JLabel titleScenarioId;
	private JLabel titleScenarioName;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField searchScenarioName = null;
	private JButton btnAddScenario = null;
	private JLabel titleDeal = null;
	
	private JLabel scenarioIdInit;
	private JLabel scenarioNameInit;
	private JButton btnDetailInit = null;
	private JButton btnCopyInit = null;
	private JButton btnDelDetail = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public ScenarioListDialog() {
		super();
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void windowClosed(WindowEvent e) {
				hide();
				Utils.clearWindow();
				SikuliIDE.getMain().setVisible(true);
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
        this.setSize(new Dimension(800,600));
        this.setContentPane(getJPanel());

        List<String> title = new ArrayList<String>();
        title.add("Scenario ID");
        title.add("Scenario Name");
        title.add("Detail");
        title.add("Copy");
		title.add("Del");

        List<JComponent> componentList = new ArrayList<JComponent>();
        componentList.add(scenarioIdInit);
        componentList.add(scenarioNameInit);
        componentList.add(btnDetailInit);
        componentList.add(btnCopyInit);
        componentList.add(btnDelDetail);
        String []arrColumn = {"SCENARIO_ID", "SCENARIO_NAME", "DEAL_1", "DEAL_2", "DEAL_3"};
        String []arrTitle = {"SCENARIO_ID", "SCENARIO_NAME", "DEAL_1"};
        grid = new GridUtils(detailPanel, title, componentList, arrColumn, preButton, afterButton, 15, arrTitle);
        grid.setPageInfo(pageInfoLbl);
        
        searchDetailList();
        
        setTitle("Scenario List Page");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			titleDeal = new JLabel();
			titleDeal.setBounds(new java.awt.Rectangle(570,75,210,20));
			titleDeal.setHorizontalAlignment(SwingConstants.CENTER);
			titleDeal.setText("Operation");
			titleDeal.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,402,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("JLabel");
			titleScenarioName = new JLabel();
			titleScenarioName.setBounds(new Rectangle(110, 75, 460, 20));
			titleScenarioName.setHorizontalAlignment(SwingConstants.CENTER);
			titleScenarioName.setBackground(new Color(255,204,204));
			titleScenarioName.setText("Scenario Name");
			titleScenarioId = new JLabel();
			titleScenarioId.setBounds(new java.awt.Rectangle(11,75,100,22));
			titleScenarioId.setHorizontalAlignment(SwingConstants.CENTER);
			titleScenarioId.setText("Scenario Id");
			lblScenarioSelect = new JLabel();
			lblScenarioSelect.setBounds(new Rectangle(10, 40, 100, 20));
			lblScenarioSelect.setText("Scenario Name");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(10, 10, 500, 24));
			jLabel.setText("Search the scenario or Add scenario with this page.");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getJButton(), null);
			jPanel.add(getDetailPanel(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblScenarioSelect, null);
			jPanel.add(getScenarioSelect(), null);
			jPanel.add(titleScenarioId, null);
			jPanel.add(titleScenarioName, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getSearchScenarioName(), null);
			jPanel.add(getBtnAddScenario(), null);
			jPanel.add(titleDeal, null);
		}
		return jPanel;
	}

	/**
	 * This method initializes excelSheet	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDetailPanel() {
		if (detailPanel == null) {
			scenarioNameInit = new JLabel();
			scenarioNameInit.setBounds(new Rectangle(100, 0, 457, 20));
			scenarioNameInit.setText("JLabel");
			scenarioIdInit = new JLabel();
			scenarioIdInit.setHorizontalAlignment(SwingConstants.CENTER);
			scenarioIdInit.setBounds(new java.awt.Rectangle(3,0,100,20));
			scenarioIdInit.setText("JLabel");
			detailPanel = new JPanel();
			detailPanel.setBounds(new java.awt.Rectangle(10,95,770,300));
			detailPanel.setLayout(null);
			detailPanel.add(scenarioIdInit, null);
			detailPanel.add(scenarioNameInit, null);
			detailPanel.add(getBtnDetailInit(), null);
			detailPanel.add(getBtnCopyInit(), null);
			detailPanel.add(getBtnDelDetail(), null);
		}
		return detailPanel;
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
				@SuppressWarnings("deprecation")
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hide();
					Utils.clearWindow();
					SikuliIDE.getMain().setVisible(true);
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
			afterButton.setLocation(new java.awt.Point(690,403));
			afterButton.setText("Next Page");
			afterButton.setSize(new Dimension(90,30));
		}
		return afterButton;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getScenarioSelect() {
		if (scenarioSelect == null) {
			scenarioSelect = new JButton();
			scenarioSelect.setText("Search");
			scenarioSelect.setLocation(new Point(260, 40));
			scenarioSelect.setSize(new Dimension(110,20));
			scenarioSelect.setPreferredSize(new Dimension(70, 30));
			scenarioSelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						searchDetailList();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(ScenarioListDialog.this, "Search Failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return scenarioSelect;
	}

	/**
	 * This method initializes searchPatternName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchScenarioName() {
		if (searchScenarioName == null) {
			searchScenarioName = new JTextField();
			searchScenarioName.setBounds(new Rectangle(120, 40, 130, 20));
		}
		return searchScenarioName;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddScenario() {
		if (btnAddScenario == null) {
			btnAddScenario = new JButton();
			btnAddScenario.setBounds(new java.awt.Rectangle(670,40,110,20));
			btnAddScenario.setText("Add Scenario");
			btnAddScenario.setPreferredSize(new Dimension(70, 30));
			btnAddScenario.addActionListener(new java.awt.event.ActionListener() {
				@SuppressWarnings("deprecation")
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ScenarioDialog dialog = new ScenarioDialog(ScenarioListDialog.this, "", "", CommonConstant.MODE_NEW);
					
					Utils.addWindow(dialog);
					hide();
					
					dialog.show();
				}
			});
		}
		return btnAddScenario;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDetailInit() {
		if (btnDetailInit == null) {
			btnDetailInit = new JButton();
			btnDetailInit.setBounds(new java.awt.Rectangle(557,0,70,20));
			btnDetailInit.setText("Detail");
			btnDetailInit.setFont(new Font("Dialog", Font.BOLD, 10));
			btnDetailInit.setPreferredSize(new Dimension(70, 30));
			btnDetailInit.addActionListener(new java.awt.event.ActionListener() {
				@SuppressWarnings({ "rawtypes", "deprecation" })
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(2))) {
							iRow = i;
						}
					}
					String strScenarioId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strScenarioName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					
					ScenarioDialog dialog = new ScenarioDialog(ScenarioListDialog.this, strScenarioId, strScenarioName, CommonConstant.MODE_UPDATE);
					
					Utils.addWindow(dialog);
					hide();
					
					dialog.show();
				}
			});
		}
		return btnDetailInit;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCopyInit() {
		if (btnCopyInit == null) {
			btnCopyInit = new JButton();
			btnCopyInit.setBounds(new java.awt.Rectangle(627,0,70,20));
			btnCopyInit.setText("Copy");
			btnCopyInit.setFont(new Font("Dialog", Font.BOLD, 10));
			btnCopyInit.setPreferredSize(new Dimension(70, 30));
			btnCopyInit.addActionListener(new java.awt.event.ActionListener() {
				@SuppressWarnings({ "rawtypes", "deprecation" })
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(3))) {
							iRow = i;
						}
					}
					
					String strScenarioId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strScenarioName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					
					ScenarioDialog dialog = new ScenarioDialog(ScenarioListDialog.this, strScenarioId, strScenarioName, CommonConstant.MODE_COPY);
					
					Utils.addWindow(dialog);
					hide();
					
					dialog.show();
				}
			});
		}
		return btnCopyInit;
	}
	
	/**
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void searchDetailList() {
		ScenarioListDao dao = new ScenarioListDao();
		String strSearchTxt = searchScenarioName.getText().trim();
		List list = dao.searchList(strSearchTxt);
		//
		grid.setData(list);
	}

	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelDetail() {
		if (btnDelDetail == null) {
			btnDelDetail = new JButton();
			btnDelDetail.setBounds(new java.awt.Rectangle(697,0,70,20));
			btnDelDetail.setPreferredSize(new Dimension(70, 30));
			btnDelDetail.setText("Del");
			btnDelDetail.setFont(new Font("Dialog", Font.BOLD, 10));
			btnDelDetail.addActionListener(new java.awt.event.ActionListener() {
				@SuppressWarnings("rawtypes")
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iResult = JOptionPane.showConfirmDialog(ScenarioListDialog.this, "Do you want to delete?");
					if (iResult != JOptionPane.YES_OPTION) {
						return;
					}
					
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(4))) {
							iRow = i;
						}
					}
					
					String strScenarioId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					ScenarioListDao dao = new ScenarioListDao();
					dao.deleteById(strScenarioId);
			        searchDetailList();
				}
			});
		}
		return btnDelDetail;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
