package org.app.co.jp.ap;

import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.GridUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class TableInfoDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JPanel excelSheet = null;
	private JButton jButton = null;
	
	private File initExcelFile;
	private JButton preButton = null;
	private JButton afterButton = null;
	private JLabel detailNo = null;
	private JLabel detailColumn = null;
	GridUtils grid = null;
	private JLabel titlePatternId = null;
	private JLabel titlePatternName = null;
	private JLabel titleSelect = null;
	private JLabel pageInfoLbl = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	private JLabel titleDeal = null;
	private JLabel titlePatternType = null;
	private JLabel detailConstants = null;
	private JLabel tableId = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel tableName = null;
	private JLabel tableComment = null;
	private JLabel detailRelation = null;
	private JLabel detailPattern = null;
	private JLabel detailLength = null;
	private JLabel detailRadom = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel7 = null;
	
	private String strTableId = "";
	private String strTableName = "";
	private String strComment = "";
	private List propertyList = new ArrayList();
	/**
	 * This method initializes 
	 * 
	 */
	public TableInfoDialog(String strTableId, String strTableName, String strComment, Map propertyMap) {
		super();
		try {
			this.strTableId = strTableId;
			this.strTableName = strTableName;
			this.strComment = strComment;
			//
			if (propertyMap != null && !propertyMap.isEmpty()) {
				//
				int i = 1;
				Iterator iterator = propertyMap.keySet().iterator();
				while (iterator.hasNext()) {
					Map propertyColumnMap = new HashMap();
					String key = iterator.next().toString();
					//
					Map keyMap = (Map)propertyMap.get(key);
					// No
					propertyColumnMap.put("NO", String.valueOf(i));
					propertyColumnMap.put("COLUMN_NAME", key);
					propertyColumnMap.putAll((Map)((HashMap)keyMap).clone());
					//
					propertyList.add(propertyColumnMap);
					//
					i++;
				}
			}
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
        this.setSize(new java.awt.Dimension(800,600));
        this.setContentPane(this.getJPanel());
        List title = new ArrayList();
        title.add("No");
        title.add("Column");
        title.add("Fixed Value");
        title.add("Relation");
        title.add("Pattern");
        title.add("Radom");
        title.add("Length");

        List componentList = new ArrayList();
        componentList.add(detailNo);
        componentList.add(detailColumn);
        componentList.add(detailConstants);
        componentList.add(detailRelation);
        componentList.add(detailPattern);
        componentList.add(detailRadom);
        componentList.add(detailLength);
        String []arrColumn = {"NO", "COLUMN_NAME", "COLUMN_FIRM", "COLUMN_FK", "COLUMN_PREFIX", "COLUMN_RADOM", "COLUMN_LENGTH"};
        String []arrTitle = {"NO", "COLUMN_NAME", "COLUMN_FIRM", "COLUMN_FK", "COLUMN_PREFIX", "COLUMN_RADOM", "COLUMN_LENGTH"};
        grid = new GridUtils(excelSheet, title, componentList, arrColumn, preButton, afterButton, 15, arrTitle);
        grid.setPageInfo(pageInfoLbl);
        
        grid.setData(propertyList);
        
        tableId.setText(strTableId);
        tableName.setText(strTableName);
        tableComment.setText(strComment);
        
        setTitle("Table Definition Information");
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel7 = new JLabel();
			jLabel7.setBounds(new java.awt.Rectangle(728,65,45,20));
			jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel7.setText("Length");
			jLabel7.setBackground(new Color(255, 204, 204));
			jLabel2 = new JLabel();
			jLabel2.setBounds(new java.awt.Rectangle(667,65,60,20));
			jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel2.setText("Radom");
			jLabel2.setBackground(new Color(255, 204, 204));
			tableComment = new JLabel();
			tableComment.setBounds(new java.awt.Rectangle(298,29,300,20));
			tableComment.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			tableName = new JLabel();
			tableName.setBounds(new java.awt.Rectangle(99,29,200,20));
			tableName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jLabel4 = new JLabel();
			jLabel4.setBounds(new java.awt.Rectangle(298,10,300,20));
			jLabel4.setText("Comment");
			jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jLabel3 = new JLabel();
			jLabel3.setBounds(new java.awt.Rectangle(99,10,200,20));
			jLabel3.setText("Table Name");
			jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			tableId = new JLabel();
			tableId.setBounds(new java.awt.Rectangle(10,29,90,20));
			tableId.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			titlePatternType = new JLabel();
			titlePatternType.setBounds(new java.awt.Rectangle(214,65,130,20));
			titlePatternType.setHorizontalAlignment(SwingConstants.CENTER);
			titlePatternType.setText("Fixed Value");
			titleDeal = new JLabel();
			titleDeal.setBounds(new java.awt.Rectangle(576,65,90,20));
			titleDeal.setHorizontalAlignment(SwingConstants.CENTER);
			titleDeal.setText("Pattern");
			titleDeal.setBackground(new Color(255, 204, 204));
			pageInfoLbl = new JLabel();
			pageInfoLbl.setBounds(new java.awt.Rectangle(224,491,315,30));
			pageInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
			pageInfoLbl.setText("JLabel");
			titleSelect = new JLabel();
			titleSelect.setBounds(new java.awt.Rectangle(345,65,230,20));
			titleSelect.setHorizontalAlignment(SwingConstants.CENTER);
			titleSelect.setBackground(new Color(255,204,204));
			titleSelect.setText("Relation");
			titlePatternName = new JLabel();
			titlePatternName.setBounds(new java.awt.Rectangle(63,65,150,20));
			titlePatternName.setHorizontalAlignment(SwingConstants.CENTER);
			titlePatternName.setText("Column");
			titlePatternId = new JLabel();
			titlePatternId.setBounds(new java.awt.Rectangle(10,65,50,20));
			titlePatternId.setHorizontalAlignment(SwingConstants.CENTER);
			titlePatternId.setText("No");
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(10,10,90,20));
			jLabel.setText("ID");
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(this.getJButton(), null);
			jPanel.add(getExcelSheet(), null);
			jPanel.add(getPreButton(), null);
			jPanel.add(getAfterButton(), null);
			jPanel.add(titlePatternId, null);
			jPanel.add(titlePatternName, null);
			jPanel.add(titleSelect, null);
			jPanel.add(pageInfoLbl, null);
			jPanel.add(titleDeal, null);
			jPanel.add(titlePatternType, null);
			jPanel.add(tableId, null);
			jPanel.add(jLabel3, null);
			jPanel.add(jLabel4, null);
			jPanel.add(tableName, null);
			jPanel.add(tableComment, null);
			jPanel.add(jLabel2, null);
			jPanel.add(jLabel7, null);
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
			detailRadom = new JLabel();
			detailRadom.setBounds(new java.awt.Rectangle(658,0,60,20));
			detailRadom.setText("");
			detailLength = new JLabel();
			detailLength.setBounds(new java.awt.Rectangle(719,0,45,20));
			detailLength.setText("");
			detailPattern = new JLabel();
			detailPattern.setBounds(new java.awt.Rectangle(567,0,90,20));
			detailPattern.setText("");
			detailRelation = new JLabel();
			detailRelation.setBounds(new java.awt.Rectangle(336,0,230,20));
			detailRelation.setText("");
			detailConstants = new JLabel();
			detailConstants.setBounds(new java.awt.Rectangle(205,0,130,20));
			detailConstants.setText("");
			detailColumn = new JLabel();
			detailColumn.setBounds(new java.awt.Rectangle(54,0,150,20));
			detailColumn.setText("JLabel");
			detailNo = new JLabel();
			detailNo.setBounds(new java.awt.Rectangle(3,0,50,20));
			detailNo.setText("JLabel");
			excelSheet = new JPanel();
			excelSheet.setBounds(new java.awt.Rectangle(10,85,770,399));
			excelSheet.setLayout(null);
			excelSheet.add(detailNo, null);
			excelSheet.add(detailColumn, null);
			excelSheet.add(detailConstants, null);
			excelSheet.add(detailRelation, null);
			excelSheet.add(detailPattern, null);
			excelSheet.add(detailLength, null);
			excelSheet.add(detailRadom, null);
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
			jButton.setSize(new java.awt.Dimension(90,30));
			jButton.setLocation(new java.awt.Point(690,526));
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
	 * This method initializes preButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPreButton() {
		if (preButton == null) {
			preButton = new JButton();
			preButton.setText("Prev Page");
			preButton.setSize(new java.awt.Dimension(90,30));
			preButton.setLocation(new java.awt.Point(11,491));
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
			afterButton.setLocation(new java.awt.Point(690,491));
			afterButton.setText("Next Page");
			afterButton.setSize(new java.awt.Dimension(90,30));
		}
		return afterButton;
	}
}  //  @jve:decl-index=0:visual-constraint="6,6"
