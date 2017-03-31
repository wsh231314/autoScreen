package org.app.co.jp.ap;

import org.app.co.jp.com.CommonConstant;
import org.app.co.jp.dao.TableDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.Check;
import org.app.co.jp.util.bean.DefaultComboBoxModel;
import org.app.co.jp.util.bean.SelectBean;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class TableDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JButton btnTableCreate = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JLabel lblTable = null;
	BasicLogger logger = BasicLogger.getLogger();
	private JTextField tableName = null;
	private String strTableName = "";
	private String strMode = "";
	private String strTableId = "";
	
	private JButton btnConfirm = null;
	private JPanel tablePanel = null;
	private JLabel columnLbl = null;
	private JComboBox columnComBox = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JTextField txtKoteiTi = null;
	private JTextField txtKanrenKankei = null;
	private JTextField txtPattern = null;
	private JTextField txtRadom = null;
	private JTextField txtLength = null;
	
	private Map propertyMap = new LinkedHashMap();
	
	private List columnList = null;
	
	private boolean blnConfirm = false;
	
	private String strOlderValue = "";
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	private JTextField txtTableComment = null;
	private JTextField txtTableNumber = null;
	private Map tableMap = null;
	private JButton btnDetail = null;
	/**
	 * This method initializes 
	 * 
	 */
	public TableDialog(String strTableId, String strTableName, String strMode) {
		super();
		try {
			this.strTableId = strTableId;
			this.strTableName = strTableName;
			this.strMode = strMode;
			initialize();
		} catch(Exception e) {
			logger.exception(e);
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(650,450));
        this.setContentPane(getJPanel());
        
        blnConfirm = false;
        
        setDisplayData();
       
		if (CommonConstant.MODE_NEW.equals(strMode)) {
			btnTableCreate.setText("Create");
		} else if (CommonConstant.MODE_COPY.equals(strMode)) {
			btnTableCreate.setText("Copy");
		} else {
			btnTableCreate.setText("Update");
		}
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel7 = new JLabel();
			jLabel7.setBounds(new java.awt.Rectangle(10,100,80,20));
			jLabel7.setText("Rec Num");
			jLabel6 = new JLabel();
			jLabel6.setBounds(new java.awt.Rectangle(10,70,80,20));
			jLabel6.setText("Comment");
			lblTable = new JLabel();
			lblTable.setBounds(new java.awt.Rectangle(10,40,80,20));
			lblTable.setText("Table");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,500,24));
			jLabel.setText("Input the table's definition");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getBtnTableCreate(), null);
			jPanel.add(getJButton(), null);
			jPanel.add(lblTable, null);
			jPanel.add(getSearchPatternName(), null);
			jPanel.add(getBtnConfirm(), null);
			jPanel.add(getTablePanel(), null);
			jPanel.add(jLabel6, null);
			jPanel.add(jLabel7, null);
			jPanel.add(getTxtTableComment(), null);
			jPanel.add(getTxtTableNumber(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes btnExcelCreate	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnTableCreate() {
		if (btnTableCreate == null) {
			btnTableCreate = new JButton();
			btnTableCreate.setText("Confirm");
			btnTableCreate.setSize(new java.awt.Dimension(150,30));
			btnTableCreate.setLocation(new java.awt.Point(10,370));
			btnTableCreate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						if (Check.isNull(txtTableComment.getText().trim())) {
							JOptionPane.showMessageDialog(TableDialog.this, "Please input the comment!");
							return;
						}
						if (Check.isNull(txtTableNumber.getText().trim())) {
							JOptionPane.showMessageDialog(TableDialog.this, "Please input the record number!");
							return;
						}
						try {
							Integer.parseInt(txtTableNumber.getText().trim());
						} catch(Exception e1) {
							JOptionPane.showMessageDialog(TableDialog.this, "The record number is not right!");
							return;
						}
						if (!Check.isNull(strOlderValue)) {
							Map selectMap = (Map)propertyMap.get(strOlderValue);
							//
							selectMap.put("COLUMN_FIRM", txtKoteiTi.getText().trim());
							selectMap.put("COLUMN_FK", txtKanrenKankei.getText().trim());
							selectMap.put("COLUMN_PREFIX", txtPattern.getText().trim());
							selectMap.put("COLUMN_RADOM", txtRadom.getText().trim());
							selectMap.put("COLUMN_LENGTH", txtLength.getText().trim());
						}
						blnConfirm = true;
						hide();
					} catch(Exception e1) {
						JOptionPane.showMessageDialog(TableDialog.this, "Failed!");
						logger.exception(e1);
					}
				}
			});
		}
		return btnTableCreate;
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
			jButton.setLocation(new java.awt.Point(540,370));
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
	 * This method initializes searchPatternName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchPatternName() {
		if (tableName == null) {
			tableName = new JTextField();
			tableName.setBounds(new java.awt.Rectangle(100,40,200,20));
		}
		return tableName;
	}

	/**
	 *
	 */
	private void setDisplayData() {
		propertyClear();
		if (CommonConstant.MODE_NEW.equals(strMode)) {
			setDisplayControl(CommonConstant.MODE_NEW);
		} else {
			setDisplayControl(CommonConstant.MODE_UPDATE);
			tableName.setText(strTableName);
			//
			setColumnCobValue();
		}
	}
	
	/**
	 *
	 */
	private void setDisplayControl(String sMode) {
		if (CommonConstant.MODE_NEW.equals(sMode)) {
			tableName.setEditable(true);
			btnConfirm.setEnabled(true);
			txtTableComment.setEditable(false);
			txtTableNumber.setEditable(false);
			columnComBox.setEnabled(false);
			
			txtKoteiTi.setEditable(false);
			txtKanrenKankei.setEditable(false);
			txtPattern.setEditable(false);
			txtRadom.setEditable(false);
			txtLength.setEditable(false);
			btnTableCreate.setEnabled(false);
			btnDetail.setEnabled(false);
		} else {
			tableName.setEditable(false);
			btnConfirm.setEnabled(false);
			
			txtTableComment.setEditable(true);
			txtTableNumber.setEditable(true);
			columnComBox.setEnabled(true);
			
			txtKoteiTi.setEditable(true);
			txtKanrenKankei.setEditable(true);
			txtPattern.setEditable(true);
			txtRadom.setEditable(true);
			txtLength.setEditable(true);
			btnTableCreate.setEnabled(true);
			btnDetail.setEnabled(true);
		}
	}
	
	/**
	 *
	 */
	private void propertyClear() {
		txtKoteiTi.setText("");
		txtKanrenKankei.setText("");
		txtPattern.setText("");
		txtRadom.setText("");
		txtLength.setText("");
	}
	
	
	/**
	 * This method initializes btnConfirm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnConfirm() {
		if (btnConfirm == null) {
			btnConfirm = new JButton();
			btnConfirm.setBounds(new java.awt.Rectangle(310,40,70,20));
			btnConfirm.setText("Confirm");
			btnConfirm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (setColumnCobValue()) {
						setDisplayControl(CommonConstant.MODE_UPDATE);
						for (int i = 0; i < columnList.size(); i++) {
							Map map = new HashMap();
							//
							map.put("COLUMN_FIRM", "");
							map.put("COLUMN_FK", "");
							map.put("COLUMN_PREFIX", "");
							map.put("COLUMN_RADOM", "");
							map.put("COLUMN_LENGTH", "");
							//
							propertyMap.put(columnList.get(i).toString(), map);
						}
					} 
				}
			});
		}
		return btnConfirm;
	}

	/**
	 * This method initializes tablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTablePanel() {
		if (tablePanel == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(new java.awt.Rectangle(10,164,110,20));
			jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel5.setText("Length");
			jLabel4 = new JLabel();
			jLabel4.setBounds(new java.awt.Rectangle(10,134,110,20));
			jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel4.setText("Radom");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new java.awt.Rectangle(10,104,110,20));
			jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel3.setText("Patten");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new java.awt.Rectangle(10,74,110,20));
			jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel2.setText("Relation");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new java.awt.Rectangle(10,44,110,20));
			jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel1.setText("Fixed value");
			columnLbl = new JLabel();
			columnLbl.setBounds(new java.awt.Rectangle(0,10,300,20));
			columnLbl.setText("Please select the column");
			tablePanel = new JPanel();
			tablePanel.setLayout(null);
			tablePanel.setBounds(new java.awt.Rectangle(10,130,620,220));
			tablePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			tablePanel.add(columnLbl, null);
			tablePanel.add(getColumnComBox(), null);
			tablePanel.add(jLabel1, null);
			tablePanel.add(jLabel2, null);
			tablePanel.add(jLabel3, null);
			tablePanel.add(jLabel4, null);
			tablePanel.add(jLabel5, null);
			tablePanel.add(getTxtKoteiTi(), null);
			tablePanel.add(getTxtKanrenKankei(), null);
			tablePanel.add(getTxtPattern(), null);
			tablePanel.add(getTxtRadom(), null);
			tablePanel.add(getTxtLength(), null);
			tablePanel.add(getBtnDetail(), null);
		}
		return tablePanel;
	}

	/**
	 * This method initializes columnComBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getColumnComBox() {
		if (columnComBox == null) {
			columnComBox = new JComboBox();
			columnComBox.setBounds(new java.awt.Rectangle(310,10,300,20));
			columnComBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SelectBean bean = (SelectBean)columnComBox.getSelectedItem();
					if (!Check.isNull(strOlderValue)) {
						Map selectMap = (Map)propertyMap.get(strOlderValue);
						//
						selectMap.put("COLUMN_FIRM", txtKoteiTi.getText().trim());
						selectMap.put("COLUMN_FK", txtKanrenKankei.getText().trim());
						selectMap.put("COLUMN_PREFIX", txtPattern.getText().trim());
						selectMap.put("COLUMN_RADOM", txtRadom.getText().trim());
						selectMap.put("COLUMN_LENGTH", txtLength.getText().trim());
					}
					strOlderValue = bean.getCode();
					
					if (Check.isNull(bean.getCode())) {
						txtKoteiTi.setText("");
						txtKanrenKankei.setText("");
						txtPattern.setText("");
						txtRadom.setText("");
						txtLength.setText("");
					} else {
						Map selectMap = (Map)propertyMap.get(bean.getCode());
						//
						txtKoteiTi.setText((String)selectMap.get("COLUMN_FIRM"));
						txtKanrenKankei.setText((String)selectMap.get("COLUMN_FK"));
						txtPattern.setText((String)selectMap.get("COLUMN_PREFIX"));
						txtRadom.setText((String)selectMap.get("COLUMN_RADOM"));
						txtLength.setText((String)selectMap.get("COLUMN_LENGTH"));
					}
				}
			});
		}
		return columnComBox;
	}

	/**
	 * This method initializes txtKoteiTi	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtKoteiTi() {
		if (txtKoteiTi == null) {
			txtKoteiTi = new JTextField();
			txtKoteiTi.setBounds(new java.awt.Rectangle(130,44,400,20));
		}
		return txtKoteiTi;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtKanrenKankei() {
		if (txtKanrenKankei == null) {
			txtKanrenKankei = new JTextField();
			txtKanrenKankei.setBounds(new java.awt.Rectangle(130,74,400,20));
			txtKanrenKankei.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					if (e.isTemporary()) {
						return;
					}
					txtKanrenKankei.setText(txtKanrenKankei.getText().toUpperCase());
					if (!Check.isNull(txtKanrenKankei.getText().trim())) {
						// 
						String sKey = txtKanrenKankei.getText().trim();
						if (sKey.indexOf(".") <= 0) {
							txtKanrenKankei.requestFocus();
							JOptionPane.showMessageDialog(TableDialog.this, "Relation format is {TABLE.KEY[PATTERN_ID]} or {TABLE.KEY} !");
							return;
						}
						TableDao dao = new TableDao();
						String strTempTable = sKey.substring(0, sKey.indexOf("."));
						List columnList = dao.getColumns(strTempTable);
						if (columnList == null || columnList.isEmpty()) {
							txtKanrenKankei.requestFocus();
							JOptionPane.showMessageDialog(TableDialog.this, "Table is not exists!");
							return;
						}
						String strTempColumn = "";
						if (sKey.indexOf("[") > 0) {
							strTempColumn = sKey.substring(sKey.indexOf(".") + 1, sKey.indexOf("["));
						} else {
							strTempColumn = sKey.substring(sKey.indexOf(".") + 1);
						}
						boolean blnCheck = dao.checkColumn(strTempTable, strTempColumn);
						if (!blnCheck) {
							txtKanrenKankei.requestFocus();
							JOptionPane.showMessageDialog(TableDialog.this, "Column is not right!");
							return;
						}
						if (sKey.indexOf("[") > 0 && sKey.indexOf("]") > 0) {
							String strTempPattern = sKey.substring(sKey.indexOf("[") + 1, sKey.indexOf("]"));
							//
							if (!tableMap.containsKey(strTempTable)) {
								txtKanrenKankei.requestFocus();
								JOptionPane.showMessageDialog(TableDialog.this, "pattern id is not Ok!");
								return;
							} else {
								Set keyMap = (Set)tableMap.get(strTempTable);
								if (!keyMap.contains(strTempPattern)) {
									txtKanrenKankei.requestFocus();
									JOptionPane.showMessageDialog(TableDialog.this, "Pattern ID is not right!");
									return;
								}
							}
						}
					}
				}
			});
		}
		return txtKanrenKankei;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtPattern() {
		if (txtPattern == null) {
			txtPattern = new JTextField();
			txtPattern.setBounds(new java.awt.Rectangle(130,104,400,20));
		}
		return txtPattern;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtRadom() {
		if (txtRadom == null) {
			txtRadom = new JTextField();
			txtRadom.setBounds(new java.awt.Rectangle(130,134,400,20));
		}
		return txtRadom;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtLength() {
		if (txtLength == null) {
			txtLength = new JTextField();
			txtLength.setBounds(new java.awt.Rectangle(130,164,400,20));
		}
		return txtLength;
	}
	
	/**
	 */
	private boolean setColumnCobValue() {
		//
		TableDao dao = new TableDao();
		String strName = tableName.getText().trim();
		tableName.setText(strName.toUpperCase());
		Vector selectList = new Vector();
		
		List columnList = dao.getColumns(strName) ;
		if (columnList.isEmpty()) {
			JOptionPane.showMessageDialog(TableDialog.this, "Table is not exists!");
			return false;
		} else {
			SelectBean blank = new SelectBean();
			selectList.add(blank);
			for (int i = 0; i < columnList.size(); i++) {
				SelectBean bean = new SelectBean();
				bean.setCode(columnList.get(i).toString());
				bean.setName(columnList.get(i).toString());
				selectList.add(bean);
			}
			this.columnList = columnList;
			// 
			DefaultComboBoxModel modal = new DefaultComboBoxModel(selectList);
			// 
			columnComBox.setModel(modal);
		}
		return true;
	}

	/**
	 */
	public boolean isBlnConfirm() {
		return blnConfirm;
	}

	/**
	 */
	public void setBlnConfirm(boolean blnConfirm) {
		this.blnConfirm = blnConfirm;
	}

	/**
	 */
	public Map getPropertyMap() {
		return propertyMap;
	}

	/**
	 */
	public void setPropertyMap(Map propertyMap) {
		this.propertyMap = propertyMap;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtTableComment() {
		if (txtTableComment == null) {
			txtTableComment = new JTextField();
			txtTableComment.setBounds(new java.awt.Rectangle(100,70,400,20));
		}
		return txtTableComment;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtTableNumber() {
		if (txtTableNumber == null) {
			txtTableNumber = new JTextField();
			txtTableNumber.setBounds(new java.awt.Rectangle(100,100,70,20));
		}
		return txtTableNumber;
	}
	
	public String getNumber() {
		return txtTableNumber.getText().trim();
	}
	
	public String getComment() {
		return txtTableComment.getText().trim();
	}
	
	public String getName() {
		return tableName.getText().trim();
	}
	
	public void setCommentStr(String comment) {
		txtTableComment.setText(comment);
	}
	
	public void setNumberStr(String name) {
		txtTableNumber.setText(name);
	}
	
	public void setTableMap(Map pMap) {
		tableMap =pMap;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDetail() {
		if (btnDetail == null) {
			btnDetail = new JButton();
			btnDetail.setText("Detail");
			btnDetail.setBounds(new java.awt.Rectangle(526,195,86,19));
			btnDetail.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						if (!Check.isNull(strOlderValue)) {
							Map selectMap = (Map)propertyMap.get(strOlderValue);
							//
							selectMap.put("COLUMN_FIRM", txtKoteiTi.getText().trim());
							selectMap.put("COLUMN_FK", txtKanrenKankei.getText().trim());
							selectMap.put("COLUMN_PREFIX", txtPattern.getText().trim());
							selectMap.put("COLUMN_RADOM", txtRadom.getText().trim());
							selectMap.put("COLUMN_LENGTH", txtLength.getText().trim());
						}
						TableInfoDialog dialog = new TableInfoDialog(strTableId, strTableName, txtTableComment.getText(), propertyMap);
						dialog.setModal(true);
						dialog.show();
					} catch (Exception e1) {
						//
						logger.exception(e1);
					}
				}
			});
		}
		return btnDetail;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
