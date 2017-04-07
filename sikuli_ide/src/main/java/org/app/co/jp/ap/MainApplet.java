package org.app.co.jp.ap;

import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Utils;
import org.app.co.jp.util.bean.FileSelect;
import org.sikuli.ide.SikuliIDE;
import org.sikuli.script.Sikulix;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainApplet extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel = null;
	private JButton btnClose = null;
	private JLabel lblMessageLabel1 = null;
	private JButton btnExcelSelect = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JButton btnTableInfoSet = null;
	private JLabel jLabel3 = null;
	
    BasicLogger logger = BasicLogger.getLogger();
	private JButton btnPatternAdd = null;
	private JLabel jLabel2 = null;
    private JButton btnParamSet = null;
    private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;

	private JButton btnPageList = null;
	private JButton btnScenarioList;
	/**
	 * This method initializes 
	 * 
	 */
	public MainApplet() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	public void initialize() {
		this.setBounds(0, 0, 800, 600);
        this.setContentPane(getMainPanel());

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Sikulix.cleanUp(0);
				System.exit(0);
			}
		});
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(new java.awt.Rectangle(168,531,300,16));
			jLabel5.setVerticalAlignment(SwingConstants.TOP);
			jLabel5.setVerticalTextPosition(SwingConstants.TOP);
			jLabel5.setText("shawn.shaohua.wang@accenture.com");
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(165, 190, 587, 20));
			jLabel4.setText("Common column that will be covered in scripts.");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(165, 140, 587, 20));
			jLabel2.setText("Patten which can create data automatically");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(165, 440, 396, 16));
			jLabel3.setText("Import table's definitions");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new java.awt.Rectangle(165,90,587,20));
			jLabel1.setVerticalAlignment(SwingConstants.TOP);
			jLabel1.setVerticalTextPosition(SwingConstants.TOP);
			jLabel1.setText("Create scripts(create or update) with excel sheets in the test document.");
			jLabel = new JLabel();
			jLabel.setVerticalAlignment(SwingConstants.TOP);
			jLabel.setVerticalTextPosition(SwingConstants.TOP);
			jLabel.setLocation(new java.awt.Point(37,500));
			jLabel.setSize(new java.awt.Dimension(430,20));
			jLabel.setText("If you have any advice, please contract me with:");
			lblMessageLabel1 = new JLabel();
			lblMessageLabel1.setVerticalAlignment(SwingConstants.TOP);
			lblMessageLabel1.setVerticalTextPosition(SwingConstants.TOP);
			lblMessageLabel1.setLocation(new java.awt.Point(39,25));
			lblMessageLabel1.setSize(new java.awt.Dimension(430,20));
			lblMessageLabel1.setText("Version 1.0.0.0");
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.setSize(800, 600);
			mainPanel.add(getBtnClose(), null);
			mainPanel.add(lblMessageLabel1, null);
			mainPanel.add(getBtnExcelSelect(), null);
			mainPanel.add(jLabel, null);
			mainPanel.add(jLabel1, null);
			mainPanel.add(getBtnTableInfoSet(), null);
			mainPanel.add(jLabel3, null);
			mainPanel.add(getJButton(), null);
			mainPanel.add(jLabel2, null);
			mainPanel.add(getJButton2(), null);
			mainPanel.add(jLabel4, null);
			mainPanel.add(jLabel5, null);

			mainPanel.add(getJButton3(), null);
			
			JButton btnScriptList = new JButton();
			btnScriptList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ScriptListDialog scriptDialog = new ScriptListDialog();
					scriptDialog.setModal(true);
					scriptDialog.show();
				}
			});
			btnScriptList.setText("Script List");
			btnScriptList.setBounds(new Rectangle(40, 140, 110, 30));
			btnScriptList.setBounds(40, 240, 110, 30);
			mainPanel.add(btnScriptList);
			mainPanel.add(getBtnScenarioList());
			
			JButton btnOperationList = new JButton();
			btnOperationList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Utils.addWindow(SikuliIDE.getMain());
					OperationListDialog operationListDialog = new OperationListDialog();
					Utils.addWindow(operationListDialog);
					SikuliIDE.getMain().setVisible(false);
					operationListDialog.setVisible(true);
				}
			});
			btnOperationList.setText("Operation List");
			btnOperationList.setBounds(new Rectangle(40, 340, 110, 30));
			btnOperationList.setBounds(40, 390, 110, 30);
			mainPanel.add(btnOperationList);
		}
		return mainPanel;
	}

	/**
	 * This method initializes btnClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setPreferredSize(new java.awt.Dimension(70,30));
			btnClose.setLocation(new java.awt.Point(660,500));
			btnClose.setSize(new java.awt.Dimension(110,30));
			btnClose.setText("Close");
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return btnClose;
	}

	/**
	 * This method initializes btnExcelSelect	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnExcelSelect() {
		if (btnExcelSelect == null) {
			btnExcelSelect = new JButton();
			btnExcelSelect.setPreferredSize(new java.awt.Dimension(70,30));
			btnExcelSelect.setLocation(new java.awt.Point(40,90));
			btnExcelSelect.setSize(new java.awt.Dimension(110,30));
			btnExcelSelect.setText("Create Scripts");
			btnExcelSelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					actionExcelSelectPerformed();
				}
			});
		}
		return btnExcelSelect;
	}

	/**
	 *
	 */
	private void actionExcelSelectPerformed() {
		try {
			FileSelect jfc = new FileSelect();
			jfc.setVisible(true);
			int returnVal = jfc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (jfc.getSelectedFile().getName().indexOf(".xls") < 0) {
					JOptionPane.showMessageDialog(this, "Please select EXCEL files!");
					return;
				}				
				PrimaryExcelDialog excelDialog = new PrimaryExcelDialog(new File(jfc.getSelectedFile().getAbsolutePath()));
				excelDialog.setModal(true);
				excelDialog.show();
			}
		} catch (Exception e) {
			//
			logger.exception(e);
		}
	}

	/**
	 * This method initializes btnTableInfoSet	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnTableInfoSet() {
		if (btnTableInfoSet == null) {
			btnTableInfoSet = new JButton();
			btnTableInfoSet.setBounds(new Rectangle(40, 440, 110, 30));
			btnTableInfoSet.setText("tables info");
			btnTableInfoSet.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						ConnectionSetDialog excelDialog = new ConnectionSetDialog();
						excelDialog.setModal(true);
						excelDialog.show();
					} catch (Exception e1) {
						//
						logger.exception(e1);
					}
				}
			});
		}
		return btnTableInfoSet;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (btnPatternAdd == null) {
			btnPatternAdd = new JButton();
			btnPatternAdd.setBounds(new Rectangle(40, 140, 110, 30));
			btnPatternAdd.setText("Batch Pattern");
			btnPatternAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PatternListDialog patternDialog = new PatternListDialog();
					patternDialog.setModal(true);
					patternDialog.show();
				}
			});
		}
		return btnPatternAdd;
	}

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (btnParamSet == null) {
			btnParamSet = new JButton();
			btnParamSet.setBounds(new Rectangle(40, 190, 110, 30));
			btnParamSet.setText("Common cols");
			btnParamSet.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ParameterDialog parameterDialog = new ParameterDialog();
					parameterDialog.setModal(true);
					parameterDialog.show();				
				}
			});
		}
		return btnParamSet;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton3() {
		if (btnPageList == null) {
			btnPageList = new JButton();
			btnPageList.setBounds(new Rectangle(40, 290, 110, 30));
			btnPageList.setText("Page List");
			btnPageList.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Utils.addWindow(SikuliIDE.getMain());
					PageListDialog pageListDialog = new PageListDialog();
					Utils.addWindow(pageListDialog);
					SikuliIDE.getMain().setVisible(false);
					pageListDialog.show();
					}
			});
		}
		return btnPageList;
	}
	private JButton getBtnScenarioList() {
		if (btnScenarioList == null) {
			btnScenarioList = new JButton();
			btnScenarioList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Utils.addWindow(SikuliIDE.getMain());
					ScenarioListDialog scenarioListDialog = new ScenarioListDialog();
					Utils.addWindow(scenarioListDialog);
					SikuliIDE.getMain().setVisible(false);
					scenarioListDialog.show();
				}
			});
			btnScenarioList.setText("Scenario List");
			btnScenarioList.setBounds(new Rectangle(40, 340, 110, 30));
			btnScenarioList.setBounds(40, 340, 110, 30);
		}
		return btnScenarioList;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
