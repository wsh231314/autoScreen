package org.app.co.jp.ap;

import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.PageListDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.Utils;
import org.sikuli.ide.SikuliIDE;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PageListDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel excelSheet = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel pageIdInit;
	private JLabel lblPageSelect;
	private JButton pageSelect = null;
	private JLabel pageNameInit;
	GridUtils grid = null;
	private JLabel titlePageId;
	private JLabel titlePageName;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField searchPageName = null;
	private JButton addPage = null;
	private JLabel titleDeal = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JButton jButton4 = null;
	public List<String> _console;
	
	/**
	 * This method initializes 
	 * 
	 */
	public PageListDialog() {
		super();
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
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
        title.add("Page ID");
        title.add("Page Name");
        title.add("Detail");
        title.add("Copy");
		title.add("Del");

        List<JComponent> componentList = new ArrayList<JComponent>();
        componentList.add(pageIdInit);
        componentList.add(pageNameInit);
        componentList.add(jButton2);
        componentList.add(jButton3);
        componentList.add(jButton4);
        String []arrColumn = {"PAGE_ID", "PAGE_NAME", "DEAL_1", "DEAL_2", "DEAL_3"};
        String []arrTitle = {"PAGE_ID", "PAGE_NAME", "DEAL_1"};
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 12, arrTitle);
        grid.setPageInfo(pageInfoLbl);
        
        searchDetailList();
        
        setTitle("Page List Page");
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
			titlePageName = new JLabel();
			titlePageName.setBounds(new Rectangle(110, 75, 460, 20));
			titlePageName.setHorizontalAlignment(SwingConstants.CENTER);
			titlePageName.setBackground(new Color(255,204,204));
			titlePageName.setText("Page Name");
			titlePageId = new JLabel();
			titlePageId.setBounds(new java.awt.Rectangle(11,75,100,22));
			titlePageId.setHorizontalAlignment(SwingConstants.CENTER);
			titlePageId.setText("Page ID");
			lblPageSelect = new JLabel();
			lblPageSelect.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblPageSelect.setText("Page Name");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(10, 10, 500, 24));
			jLabel.setText("You can create the page's component's image at this page");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getJButton(), null);
			jPanel.add(getExcelSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblPageSelect, null);
			jPanel.add(getPageSelect(), null);
			jPanel.add(titlePageId, null);
			jPanel.add(titlePageName, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getSearchPageName(), null);
			jPanel.add(getAddPage(), null);
			jPanel.add(titleDeal, null);
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
			pageNameInit = new JLabel();
			pageNameInit.setBounds(new Rectangle(100, 0, 457, 20));
			pageNameInit.setText("JLabel");
			pageIdInit = new JLabel();
			pageIdInit.setBounds(new java.awt.Rectangle(3,0,100,20));
			pageIdInit.setText("JLabel");
			excelSheet = new JPanel();
			excelSheet.setBounds(new java.awt.Rectangle(10,95,770,300));
			excelSheet.setLayout(null);
			excelSheet.add(pageIdInit, null);
			excelSheet.add(pageNameInit, null);
			excelSheet.add(getJButton2(), null);
			excelSheet.add(getJButton3(), null);
			excelSheet.add(getJButton4(), null);
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
			jButton.setLocation(new java.awt.Point(690,500));
			jButton.addActionListener(new java.awt.event.ActionListener() {
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
	private JButton getPageSelect() {
		if (pageSelect == null) {
			pageSelect = new JButton();
			pageSelect.setText("Search");
			pageSelect.setLocation(new java.awt.Point(236,40));
			pageSelect.setSize(new Dimension(110,20));
			pageSelect.setPreferredSize(new Dimension(70, 30));
			pageSelect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						searchDetailList();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(PageListDialog.this, "Search Failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return pageSelect;
	}

	/**
	 * This method initializes searchPatternName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchPageName() {
		if (searchPageName == null) {
			searchPageName = new JTextField();
			searchPageName.setBounds(new java.awt.Rectangle(100,40,130,20));
		}
		return searchPageName;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddPage() {
		if (addPage == null) {
			addPage = new JButton();
			addPage.setBounds(new java.awt.Rectangle(670,40,110,20));
			addPage.setText("Add Page");
			addPage.setPreferredSize(new Dimension(70, 30));
			addPage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PageDialog dialog = new PageDialog(PageListDialog.this, "",  "", CommonConstant.MODE_NEW);
					setVisible(false);
					Utils.addWindow(dialog);
					dialog.show();
				}
			});
		}
		return addPage;
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
						if (e.getSource().equals(((List)compList.get(i)).get(2))) {
							iRow = i;
						}
					}
					String strPageId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strPageName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					PageDialog dialog = new PageDialog(PageListDialog.this, strPageId, strPageName, CommonConstant.MODE_UPDATE);
					setVisible(false);
					Utils.addWindow(dialog);
					dialog.show();
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
						if (e.getSource().equals(((List)compList.get(i)).get(3))) {
							iRow = i;
						}
					}
					String strPageId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					String strPageName = ((JLabel)((List)compList.get(iRow)).get(1)).getText();
					PageDialog dialog = new PageDialog(PageListDialog.this, strPageId, strPageName, CommonConstant.MODE_COPY);
					setVisible(false);
					Utils.addWindow(dialog);
					dialog.show();
				}
			});
		}
		return jButton3;
	}
	
	/**
	 *
	 */
	public void searchDetailList() {
		PageListDao dao = new PageListDao();
		String strSearchTxt = searchPageName.getText().trim();
		List<Map<String, String>> list = dao.searchList(strSearchTxt);
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
					int iResult = JOptionPane.showConfirmDialog(PageListDialog.this, "Do you want to delete?");
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
					String strPageId = ((JLabel)((List)compList.get(iRow)).get(0)).getText();
					
					PageListDao dao = new PageListDao();
					dao.deleteByList(strPageId);
			        searchDetailList();
				}
			});
		}
		return jButton4;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
