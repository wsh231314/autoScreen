package org.app.co.jp.ap;

import org.app.co.jp.cmp.CustomerImage;
import org.app.co.jp.dao.PageDao;
import org.app.co.jp.dao.PageListDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.GridUtils;
import org.app.co.jp.util.Utils;
import org.app.co.jp.util.bean.DefaultComboBoxModel;
import org.app.co.jp.util.bean.SelectBean;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemEvent;

public class PageSelectDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel pageSheet = null;
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
	private CustomerImage imageButton  = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JLabel titleFieldImage;
	private JLabel recordNumInit = null;
	private JCheckBox checkInit;
	private JLabel filedNameInit;
	private JLabel fieldTypeInit;
	
	private JLabel titleSelect;
	
	private JComboBox<DefaultComboBoxModel> pageIdSelect;
	
	private List<Map<String, String>> returnValue = new ArrayList<Map<String, String>>();
	
	ScenarioDialog _parent;
	
	/**
	 * This method initializes 
	 * 
	 */
	public PageSelectDialog(ScenarioDialog parent) {
		super();
		
		_parent = parent;
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				_parent.addSelectFields(PageSelectDialog.this);
				Utils.removeWindow(PageSelectDialog.this);
				_parent.setVisible(true);
			}
		});
		
		try {
			initialize();
			returnValue.clear();
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

        List<String> title = new ArrayList<String>();
        title.add("Select");
        title.add("Field Id");
        title.add("Field Name");
        title.add("Field Type");
        title.add("Image");

        List<JComponent> componentList = new ArrayList<JComponent>();
        componentList.add(checkInit);
        componentList.add(fieldIdInit);
        componentList.add(filedNameInit);
        componentList.add(fieldTypeInit);
        componentList.add(imageButton);

        String []arrColumn = {"SELECT", "FIELD_ID", "FIELD_NAME", "FIELD_TYPE", "FIELD_VALUE"};
        String []arrTitle = {"SELECT", "FIELD_ID", "FIELD_NAME", "FIELD_TYPE", "FIELD_VALUE"};
        // init grid
        grid = new GridUtils(pageSheet, title, componentList, arrColumn, preButton, afterButton, 5, arrTitle);
        // set title
        grid.setPageInfo(pageInfoLbl);
        
        searchDetailList();
        
        initComboBox();
        
        setTitle("Page Select page");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			titleFieldImage = new JLabel();
			titleFieldImage.setBounds(new Rectangle(480, 75, 280, 60));
			titleFieldImage.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldImage.setText("Image");
			titleFieldImage.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,480,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("");
			titleFieldName = new JLabel();
			titleFieldName.setBounds(new Rectangle(190, 75, 170, 60));
			titleFieldName.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldName.setBackground(new Color(255,204,204));
			titleFieldName.setText("Field name");
			titleFieldId = new JLabel();
			titleFieldId.setBounds(new Rectangle(90, 75, 70, 60));
			titleFieldId.setHorizontalAlignment(SwingConstants.CENTER);
			titleFieldId.setBackground(new Color(255,160,204));
			titleFieldId.setText("Field ID");
			lblPageName = new JLabel();
			lblPageName.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblPageName.setText("Page Name");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,500,24));
			jLabel.setText("Select the field you want to add");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getJButton(), null);
			jPanel.add(getPageSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(lblPageName, null);
			jPanel.add(titleFieldId, null);
			jPanel.add(titleFieldName, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(titleFieldImage, null);
			
			JLabel titleFiledType = new JLabel();
			titleFiledType.setText("Field Type");
			titleFiledType.setHorizontalAlignment(SwingConstants.CENTER);
			titleFiledType.setBounds(new Rectangle(10, 75, 90, 60));
			titleFiledType.setBackground(new Color(255, 160, 204));
			titleFiledType.setBounds(360, 75, 120, 60);
			jPanel.add(titleFiledType);
			
			JButton btnSelect = new JButton();
			btnSelect.addActionListener(new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					returnValue.clear();
					grid.freshData();
					List<Map<String, String>> valueList = grid.getValueList();
					
					SelectBean bean = (SelectBean)pageIdSelect.getSelectedItem();
					
					for (Map<String, String> map : valueList) {
						String strSelect = map.get("SELECT");
						
						if (Boolean.valueOf(strSelect)) {
							Map<String, String> temp = new HashMap<String, String>(map);
							temp.put("PAGE_ID", bean.getCode());
							String strName = bean.getName();
							
							if (strName.indexOf(":") > 0 && strName.length() > strName.indexOf(":") + 2) {
								strName = strName.substring(strName.indexOf(":") + 2);
							}
							temp.put("PAGE_NAME", strName);
							returnValue.add(temp);
						}
						
					}
					if (returnValue.size() > 0) {
						setVisible(false);
						_parent.addSelectFields(PageSelectDialog.this);
						Utils.removeWindow(PageSelectDialog.this);
						_parent.setVisible(true);
					
					} else {
						JOptionPane.showMessageDialog(PageSelectDialog.this, "Plese select field!");
					}
				}
				
			});
			btnSelect.setText("Select");
			btnSelect.setSize(new Dimension(90, 30));
			btnSelect.setLocation(new Point(10, 480));
			btnSelect.setBounds(10, 520, 90, 30);
			jPanel.add(btnSelect);
			
			titleSelect = new JLabel();
			titleSelect.setText("Select");
			titleSelect.setHorizontalAlignment(SwingConstants.CENTER);
			titleSelect.setBounds(new Rectangle(90, 75, 70, 60));
			titleSelect.setBackground(new Color(255, 160, 204));
			titleSelect.setBounds(10, 75, 80, 60);
			jPanel.add(titleSelect);
			
			pageIdSelect = new JComboBox<DefaultComboBoxModel>();
			pageIdSelect.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					searchDetailList();
				}
			});
			pageIdSelect.setBounds(120, 40, 300, 20);
			jPanel.add(pageIdSelect);
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
			fieldIdInit.setBounds(new Rectangle(83, 20, 87, 20));
			fieldIdInit.setText("JLabel");
			pageSheet = new JPanel();
			pageSheet.setBounds(new Rectangle(10, 135, 770, 340));
			pageSheet.setLayout(null);
			pageSheet.add(fieldIdInit, null);
			
			imageButton = new CustomerImage("");
			imageButton.setVerticalAlignment(SwingConstants.CENTER);
			imageButton.setHorizontalAlignment(SwingConstants.CENTER);
			imageButton.setBounds(new Rectangle(480, 0, 207, 60));
			pageSheet.add(imageButton);
			pageSheet.add(recordNumInit, null);
			

			
			checkInit = new JCheckBox("");
			checkInit.setHorizontalAlignment(SwingConstants.CENTER);
			checkInit.setBounds(3, 20, 70, 20);
			pageSheet.add(checkInit);
			
			filedNameInit = new JLabel();
			filedNameInit.setText("JLabel");
			filedNameInit.setBounds(new Rectangle(83, 20, 87, 20));
			filedNameInit.setBounds(180, 20, 140, 20);
			pageSheet.add(filedNameInit);
			
			fieldTypeInit = new JLabel();
			fieldTypeInit.setText("JLabel");
			fieldTypeInit.setBounds(new Rectangle(83, 20, 87, 20));
			fieldTypeInit.setBounds(348, 20, 100, 20);
			pageSheet.add(fieldTypeInit);
		}
		return pageSheet;
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
					setVisible(false);
					_parent.addSelectFields(PageSelectDialog.this);
					Utils.removeWindow(PageSelectDialog.this);
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
	 *
	 */
	@SuppressWarnings("rawtypes")
	private void searchDetailList() {
		
		String strPageId = getSelectPageId();
		
		if (!Utils.isEmpty(strPageId)) {
			PageDao dao = new PageDao();
			List list = dao.searchListById(strPageId);
			grid.setData(list);
		} else {
			List emptyList = new ArrayList();
			grid.setData(emptyList);
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private String getSelectPageId () {
		String strSelectPageId = "";
		SelectBean bean = (SelectBean)pageIdSelect.getSelectedItem();
		
		if (bean != null) {
			strSelectPageId = bean.getCode();
		}
		
		return strSelectPageId;
	}
	
	@SuppressWarnings("unchecked")
	private void initComboBox () {
		
		Vector<SelectBean> selectList = new Vector<SelectBean>();
		SelectBean blank = new SelectBean();
		selectList.add(blank);
		
		PageListDao listDao = new PageListDao();
		
		List<Map<String, String>>  result = listDao.searchList("");
		for (Map<String, String> map : result) {
			SelectBean bean = new SelectBean();
			String strId = map.get("PAGE_ID");
			String strName = map.get("PAGE_NAME");
			
			bean.setCode(strId);
			bean.setName(strId.concat(" : ").concat(strName));
			selectList.add(bean);
		}
		
		DefaultComboBoxModel model = new DefaultComboBoxModel(selectList);
		
		pageIdSelect.setModel(model);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public List<Map<String, String>> getReturnValue() {
		return returnValue;
	}
	
}  //  @jve:decl-index=0:visual-constraint="6,6"
