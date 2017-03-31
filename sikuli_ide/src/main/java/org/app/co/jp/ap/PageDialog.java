package org.app.co.jp.ap;

import org.app.co.jp.cmp.CustomerCaptureButton;
import org.app.co.jp.cmp.CustomerImageButton;
import org.app.co.jp.com.ComDao;
import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.PageDao;
import org.app.co.jp.dao.PageListDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PageDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel pageSheet = null;
	private JButton btnConfirm = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel fieldIdInit;
	private JLabel lblPageName;
	public GridUtils grid = null;
	private JLabel titleFieldId;
	private JLabel titleFieldName;
	private JLabel pageInfoLbl = null;
	CustomerImageButton imageButton  = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField pageName = null;
	private JLabel titleDeal = null;
	private CustomerCaptureButton btnCapture = null;
	private JLabel titleFieldImage;
	private JLabel patternId = null;
	private JButton btnDeleteRow = null;
	private JLabel recordNumInit = null;
	private JButton addRecord = null;
	
	private String strPageId = "";
	private String strMode = "";
	private String strPageName = "";
	
	private PageListDialog _parent;
	
	private Map tableMap = new HashMap();
	
	private List deleteList = new ArrayList();
	private JTextField fieldNameInit;
	private JComboBox fieldTypeInit;
	
	private String workFolder = "";
	
	/**
	 * This method initializes 
	 * 
	 */
	public PageDialog(PageListDialog parent, String strPageId, String strPageName, String strMode) {
		super();
		try {
			this.strPageId = strPageId;
			this.strMode = strMode;
			this._parent = parent;
			this.strPageName = strPageName;
			
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					hide();
					_parent.setVisible(true);
					_parent.searchDetailList();
				}
			});

			// TODO init work folder
			initWorkFolder(strPageId);
			
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
        title.add("Field Id");
        title.add("Field Name");
        title.add("Field Type");
        title.add("Image");
        title.add("capture");
        title.add("Del");

        List componentList = new ArrayList();
        componentList.add(fieldIdInit);
        componentList.add(fieldNameInit);
        componentList.add(fieldTypeInit);
        componentList.add(imageButton);
        componentList.add(btnCapture);
        componentList.add(btnDeleteRow);

        String []arrColumn = {"FIELD_ID", "FIELD_NAME", "FIELD_TYPE", "FIELD_VALUE", "CAPTURE", "DEAL_1"};
        String []arrTitle = {"FIELD_ID", "FIELD_NAME", "FIELD_TYPE", "FIELD_VALUE", "DEAL_1"};
        // init grid
        grid = new GridUtils(pageSheet, title, componentList, arrColumn, preButton, afterButton, 5, arrTitle);
        // set title
        grid.setPageInfo(pageInfoLbl);
        
        searchDetailList();
        
        setTitle("Page create page");
        
		if (CommonConstant.MODE_NEW.equals(strMode) || CommonConstant.MODE_COPY.equals(strMode)) {
			// confirm button
			btnConfirm.setText("Create");
		} else {
			btnConfirm.setText("Update");
		}
		
		if (strMode.equals(CommonConstant.MODE_NEW) ||
				strMode.equals(CommonConstant.MODE_COPY)
				) {
			
			// old page
			String strFromPageId = strPageId;
			
			ComDao comDao = new ComDao();
			strPageId = "PAGE".concat("_").concat(comDao.getPageSeq(CommonConstant.PATTERN_CUSTOMER));
			
			if (strMode.equals(CommonConstant.MODE_COPY)) {
				comDao.copyPageInfo(strFromPageId, strPageId, CommonConstant.PATTERN_CUSTOMER);
			}
		}
		
		pageName.setText(strPageName);
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
			titleFieldImage = new JLabel();
			titleFieldImage.setBounds(new Rectangle(370, 75, 280, 60));
			titleFieldImage.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldImage.setText("Image");
			titleFieldImage.setBackground(new Color(255, 204, 204));
			titleDeal = new JLabel();
			titleDeal.setBounds(new Rectangle(655, 75, 120, 60));
			titleDeal.setHorizontalAlignment(SwingConstants.CENTER);
			titleDeal.setText("Operation");
			titleDeal.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,480,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("");
			titleFieldName = new JLabel();
			titleFieldName.setBounds(new Rectangle(80, 75, 170, 60));
			titleFieldName.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldName.setBackground(new Color(255,204,204));
			titleFieldName.setText("Field name");
			titleFieldId = new JLabel();
			titleFieldId.setBounds(new Rectangle(10, 75, 70, 60));
			titleFieldId.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldId.setBackground(new Color(255,160,204));
			titleFieldId.setText("Field ID");
			lblPageName = new JLabel();
			lblPageName.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblPageName.setText("Page Name");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,500,24));
			jLabel.setText("Input Page's field and take the image.");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getBtnConfirm(), null);
			jPanel.add(getJButton(), null);
			jPanel.add(getPageSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblPageName, null);
			jPanel.add(titleFieldId, null);
			jPanel.add(titleFieldName, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(getSearchPatternName(), null);
			jPanel.add(titleDeal, null);
			jPanel.add(titleFieldImage, null);
			jPanel.add(patternId, null);
			jPanel.add(getAddRecord(), null);
			
			JLabel titleFiledType = new JLabel();
			titleFiledType.setText("Field ID");
			titleFiledType.setHorizontalAlignment(SwingConstants.CENTER);
			titleFiledType.setBounds(new Rectangle(10, 75, 90, 60));
			titleFiledType.setBackground(new Color(255, 160, 204));
			titleFiledType.setBounds(250, 75, 120, 60);
			jPanel.add(titleFiledType);
		}
		return jPanel;
	}

	/**
	 * This method initializes excelSheet	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPageSheet() {
		if (pageSheet == null) {
			recordNumInit = new JLabel();
			recordNumInit.setBounds(new java.awt.Rectangle(497,0,70,20));
			recordNumInit.setText("");
			recordNumInit.setBackground(new Color(255, 204, 204));
			fieldIdInit = new JLabel();
			fieldIdInit.setBounds(new Rectangle(3, 21, 87, 20));
			fieldIdInit.setText("JLabel");
			pageSheet = new JPanel();
			pageSheet.setBounds(new Rectangle(10, 135, 770, 340));
			pageSheet.setLayout(null);
			pageSheet.add(fieldIdInit, null);
			
			imageButton = new CustomerImageButton("");
			imageButton.setVerticalAlignment(SwingConstants.CENTER);
			imageButton.setHorizontalAlignment(SwingConstants.CENTER);
			imageButton.setBounds(new Rectangle(370, 0, 207, 60));
			pageSheet.add(imageButton);
			
			pageSheet.add(getBtnCapture(), null);
			pageSheet.add(getBtnDeleteRow(), null);
			pageSheet.add(recordNumInit, null);
			
			fieldNameInit = new JTextField();
			fieldNameInit.setBounds(70, 21, 160, 20);
			pageSheet.add(fieldNameInit);
			fieldNameInit.setColumns(10);
			
			fieldTypeInit = new JComboBox();
			fieldTypeInit.setBounds(240, 21, 110, 20);
			pageSheet.add(fieldTypeInit);
		}
		return pageSheet;
	}

	/**
	 * This method initializes btnExcelCreate	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnConfirm() {
		if (btnConfirm == null) {
			btnConfirm = new JButton();
			btnConfirm.setText("Confirm");
			btnConfirm.setSize(new Dimension(90,30));
			btnConfirm.setLocation(new java.awt.Point(10,520));
			btnConfirm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						PageDao dao = new PageDao();
						grid.freshData();
						List valueList = grid.getValueList();
						if ((valueList == null || valueList.size() == 0) && deleteList.size() == 0) {
							JOptionPane.showMessageDialog(PageDialog.this, "Please add data field!");
							return;
						}
						
						String strMessage = checkBeforeConfirm(valueList);
						
						if (!Utils.isEmpty(strMessage)) {
							JOptionPane.showMessageDialog(PageDialog.this, strMessage);
							return;
						}
						
						if (deleteList.size() > 0){
							int iResult = JOptionPane.showConfirmDialog(PageDialog.this, "We will delete some field definition. Is it OK?");
							if (iResult != JOptionPane.YES_OPTION) {
								return;
							}
							dao.deleteByList(deleteList);
						}
						

						
						if (valueList != null && valueList.size() > 0) {
							PageDao patternDao = new PageDao();
							patternDao.createByList(valueList, strPageId, pageName.getText().trim());
							
							// complete the edit
							finishWorkFolder(strPageId);
						}
						JOptionPane.showMessageDialog(PageDialog.this, "Completed!");
						hide();
						_parent.setVisible(true);
						_parent.searchDetailList();
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(PageDialog.this, "Failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return btnConfirm;
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
					_parent.setVisible(true);
					_parent.searchDetailList();
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
		if (pageName == null) {
			pageName = new JTextField();
			pageName.setBounds(new java.awt.Rectangle(195,40,462,20));
		}
		return pageName;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCapture() {
		if (btnCapture == null) {
			btnCapture = new CustomerCaptureButton(imageButton, imageButton.getFilename());
			btnCapture.setBounds(new Rectangle(585, 35, 70, 20));
			btnCapture.setVerticalAlignment(SwingConstants.CENTER);
			btnCapture.setHorizontalAlignment(SwingConstants.CENTER);
			btnCapture.setPreferredSize(new Dimension(70, 30));
		}
		return btnCapture;
	}
	
	/**
	 *
	 */
	private void searchDetailList() {
		if (strPageId != null && !strPageId.equals("")) {
			PageDao dao = new PageDao();
			
			List list = dao.searchList();
			grid.setData(list);
			
		}
	}
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDeleteRow() {
		if (btnDeleteRow == null) {
			btnDeleteRow = new JButton();
			btnDeleteRow.setBounds(new Rectangle(680, 21, 60, 20));
			btnDeleteRow.setPreferredSize(new Dimension(70, 30));
			btnDeleteRow.setText("Del");
			btnDeleteRow.setFont(new Font("Dialog", Font.BOLD, 10));
			btnDeleteRow.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int iResult = JOptionPane.showConfirmDialog(PageDialog.this, "Do you want to delete?");
					if (iResult != JOptionPane.YES_OPTION) {
						return;
					}
					int iRow = 0;
					List compList = grid.getComponentList();
					for (int i = 0; i < compList.size(); i++) {
						if (e.getSource().equals(((List)compList.get(i)).get(5))) {
							iRow = i;
						}
					}
					// 
					int dateRow = grid.getDataRow(iRow);
					List dataList = grid.getValueList();
					Map deleteMap = (Map)dataList.get(dateRow);
					//
					String strId = (String)deleteMap.get("FIELD_ID");
					if (!Check.isNull(strId)) {
						deleteList.add(deleteMap);
					}
					dataList.remove(dateRow);
					grid.setData(dataList);
				}
			});
		}
		return btnDeleteRow;
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
					List dataList = grid.getValueList();
					Map updateMap = new HashMap();

					ComDao comDao = new ComDao(); 
					String filedId = "FIELD_".concat(comDao.getFieldSeq(strPageId, CommonConstant.PATTERN_CUSTOMER));

					PageListDao dao = new PageListDao();
					String path = dao.getPagePath("work");

					String fileName = filedId.concat(".png");

					updateMap.put("FIELD_ID", filedId);
					updateMap.put("FIELD_NAME", "");
					updateMap.put("FIELD_TYPE", "");
					updateMap.put("FIELD_TYPE_FOR_SELECT", Utils.getComponentList());
					updateMap.put("FIELD_VALUE", fileName);
					updateMap.put("CAPTURE", "");
					updateMap.put("DEAL_1", "Del");
					updateMap.put("file_path", path.concat(fileName));

					dataList.add(updateMap);
					grid.setData(dataList);
				}
			});
		}
		return addRecord;
	}
	
	
	public void initWorkFolder(String strPageId) throws FileNotFoundException, IOException {
		PageListDao dao = new PageListDao();
		String path = dao.getPagePath("work");
		
		File folder = new File(path);
		
		if (!folder.exists()) {
			folder.mkdirs();
		} else {
			Utils.deleteDirectory(path);
			folder.mkdirs();
		}
		
		workFolder = path;
		
		if (!Utils.isEmpty(strPageId)) {
			String pageIdFolder =  dao.getPagePath(strPageId);
			Utils.copyFileByFolder(pageIdFolder, path, "", "");
		}
	}

	public void finishWorkFolder(String strPageId) throws FileNotFoundException, IOException {
		PageListDao dao = new PageListDao();
		String path = dao.getPagePath("work");
		
		String pageIdFolder =  dao.getPagePath(strPageId);
		
		File folder = new File(pageIdFolder);
		
		if (!folder.exists()) {
			folder.mkdirs();
		} else {
			Utils.deleteDirectory(pageIdFolder);
			folder.mkdirs();
		}

		Utils.copyFileByFolder(path, pageIdFolder, "", "");
	}
	
	private String checkBeforeConfirm(List resultList) {
		String strMessage = "";
		for (Object obj : resultList) {
			Map map = (Map)obj;
			String strId = (String)map.get("FIELD_ID");
			String strName = (String)map.get("FIELD_NAME");
			String strType = (String)map.get("FIELD_TYPE");
			String filePath = (String)map.get("file_path");
			
			if (Utils.isEmpty(strName)) {
				strMessage = "You must input the FIELD NAME at: ".concat(strId);
				break;
			}
			if (Utils.isEmpty(strType)) {
				strMessage = "You must input the FIELD TYPE at: ".concat(strId);
				break;
			}
			if (!(new File(filePath)).exists()) {
				strMessage = "You must take the picture for : ".concat(strId);
				break;
			}
		}
		return strMessage;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
