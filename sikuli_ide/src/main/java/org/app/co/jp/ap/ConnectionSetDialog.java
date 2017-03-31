package org.app.co.jp.ap;

import org.app.co.jp.com.ComDao;
import org.app.co.jp.util.BasicLogger;
import org.app.co.jp.util.ConnectionUtil;
import org.app.co.jp.util.ExcelUtils;
import org.app.co.jp.util.XMLUtils;

import javax.swing.*;

public class ConnectionSetDialog extends JDialog {

	private JPanel jPanel = null;
	private JLabel lUser = null;
	private JLabel lPassword = null;
	private JLabel lUrl = null;
	private JLabel lSchema = null;
	private JButton btnImport = null;
	private JButton btnChange = null;
	private JButton btnTestUrl = null;
	private JTextField txtUser = null;
	private JTextField txtPassword = null;
	private JTextField txtSchema = null;
	private JTextField txtUrl = null;
	private JButton btnSet = null;
	private JButton btnCancel = null;
	private JButton btnClose = null;
	
	BasicLogger logger = BasicLogger.getLogger();
	/**
	 * This method initializes 
	 * 
	 */
	public ConnectionSetDialog() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(600,300));
        this.setContentPane(getJPanel());
        // title
        this.setTitle("Connection Setting");
        //display
        setDisplay();
        // page status
        setStatus(1);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			lSchema = new JLabel();
			lSchema.setBounds(new java.awt.Rectangle(30,90,100,20));
			lSchema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lSchema.setText("Schema");
			lUrl = new JLabel();
			lUrl.setBounds(new java.awt.Rectangle(30,120,100,20));
			lUrl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lUrl.setText("URL");
			lPassword = new JLabel();
			lPassword.setBounds(new java.awt.Rectangle(30,60,100,20));
			lPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lPassword.setText("Password");
			lUser = new JLabel();
			lUser.setText("User ID");
			lUser.setSize(new java.awt.Dimension(100,20));
			lUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lUser.setLocation(new java.awt.Point(30,30));
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(lUser, null);
			jPanel.add(lPassword, null);
			jPanel.add(lUrl, null);
			jPanel.add(lSchema, null);
			jPanel.add(getBtnImport(), null);
			jPanel.add(getBtnChange(), null);
			jPanel.add(getBtnTestUrl(), null);
			jPanel.add(getTxtUser(), null);
			jPanel.add(getTxtPassword(), null);
			jPanel.add(getTxtSchema(), null);
			jPanel.add(getTxtUrl(), null);
			jPanel.add(getBtnSet(), null);
			jPanel.add(getBtnCancel(), null);
			jPanel.add(getBtnClose(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes btnImport	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton();
			btnImport.setLocation(new java.awt.Point(30,175));
			btnImport.setText("Export");
			btnImport.setSize(new java.awt.Dimension(70,25));
			btnImport.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					XMLUtils util = new XMLUtils();
					try {
						int iResult = JOptionPane.showConfirmDialog(ConnectionSetDialog.this, "�e�[�u���̒�`���𓱏o���܂����H�������Ԃ��K�v�ɂ���܂��B");
						if (iResult == JOptionPane.OK_OPTION) {
							util.createTablesXml(true);
							JOptionPane.showMessageDialog(ConnectionSetDialog.this, "���o�������ł����I");
						}
					} catch (Exception e1) {
						logger.exception(e1);
						JOptionPane.showMessageDialog(ConnectionSetDialog.this, e1.getMessage());
					}
				}
			});
		}
		return btnImport;
	}

	/**
	 * This method initializes btnChange	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnChange() {
		if (btnChange == null) {
			btnChange = new JButton();
			btnChange.setSize(new java.awt.Dimension(90,25));
			btnChange.setText("Set");
			btnChange.setLocation(new java.awt.Point(120,175));
			btnChange.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setStatus(2);
				}
			});
		}
		return btnChange;
	}

	/**
	 * This method initializes btnTestUrl	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnTestUrl() {
		if (btnTestUrl == null) {
			btnTestUrl = new JButton();
			btnTestUrl.setLocation(new java.awt.Point(480,120));
			btnTestUrl.setText("TEST");
			btnTestUrl.setSize(new java.awt.Dimension(70,20));
			btnTestUrl.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (checkValid()) {
						JOptionPane.showMessageDialog(ConnectionSetDialog.this, "�A���������I");
					}
				}
			});
		}
		return btnTestUrl;
	}

	/**
	 * This method initializes txtUser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtUser() {
		if (txtUser == null) {
			txtUser = new JTextField();
			txtUser.setLocation(new java.awt.Point(140,30));
			txtUser.setSize(new java.awt.Dimension(150,20));
		}
		return txtUser;
	}

	/**
	 * This method initializes txtPassword	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtPassword() {
		if (txtPassword == null) {
			txtPassword = new JTextField();
			txtPassword.setBounds(new java.awt.Rectangle(140,60,150,20));
		}
		return txtPassword;
	}

	/**
	 * This method initializes txtSchema	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSchema() {
		if (txtSchema == null) {
			txtSchema = new JTextField();
			txtSchema.setBounds(new java.awt.Rectangle(140,90,150,20));
		}
		return txtSchema;
	}

	/**
	 * This method initializes txtUrl	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtUrl() {
		if (txtUrl == null) {
			txtUrl = new JTextField();
			txtUrl.setBounds(new java.awt.Rectangle(140,120,330,20));
		}
		return txtUrl;
	}

	/**
	 * This method initializes btnSet	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSet() {
		if (btnSet == null) {
			btnSet = new JButton();
			btnSet.setSize(new java.awt.Dimension(70,25));
			btnSet.setText("Change");
			btnSet.setLocation(new java.awt.Point(220,175));
			btnSet.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (checkValid()) {
						ConnectionUtil util = new ConnectionUtil();
				        util.setProperty("user", txtUser.getText());
				        util.setProperty("password", txtPassword.getText());
				        util.setProperty("schema", txtSchema.getText());
				        // URL
				        util.setProperty("url", txtUrl.getText());
				        try {
				        	util.save();
				        	JOptionPane.showMessageDialog(ConnectionSetDialog.this, "Saved!!");
				        	setStatus(1);
				        } catch(Exception e1) {
				        	logger.exception(e1);
				        	JOptionPane.showMessageDialog(ConnectionSetDialog.this, e1.getMessage());
				        }
					}
				}
			});
		}
		return btnSet;
	}
	
	private void setDisplay() {
        ConnectionUtil util = new ConnectionUtil();
        
        txtUser.setText(util.getProperty("user"));
        txtPassword.setText(util.getProperty("password"));
        txtSchema.setText(util.getProperty("schema"));
        txtUrl.setText(util.getProperty("url"));}
	
	/**
	 */
	private void setStatus(int iMode) {
		if (iMode == 1) {
	        txtUser.setEditable(false);
	        txtPassword.setEditable(false);
	        txtSchema.setEditable(false);
	        txtUrl.setEditable(false);
	        btnSet.setVisible(false);
	        btnCancel.setVisible(false);
	        btnImport.setVisible(true);
	        btnChange.setVisible(true);
		} else {
	        txtUser.setEditable(true);
	        txtPassword.setEditable(true);
	        txtSchema.setEditable(true);
	        txtUrl.setEditable(true);
	        btnSet.setVisible(true);
	        btnCancel.setVisible(true);
	        btnImport.setVisible(false);
	        btnChange.setVisible(false);
		}
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setBounds(new java.awt.Rectangle(300,175,70,25));
			btnCancel.setText("Cancel");
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setDisplay();
					setStatus(1);
				}
			});
		}
		return btnCancel;
	}

	/**
	 * This method initializes btnClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setBounds(new java.awt.Rectangle(480,175,70,25));
			btnClose.setText("Closed");
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ConnectionSetDialog.this.setVisible(false);
				}
			});
		}
		return btnClose;
	}
	
	/**
	 *
	 */
	private boolean checkValid() {
		
		ExcelUtils utils = new ExcelUtils();
		
		if (utils.isNullOrBlankString(txtUser.getText())) {
			JOptionPane.showMessageDialog(ConnectionSetDialog.this, "Please input the user ID!");
			return false;
		}
		if (utils.isNullOrBlankString(txtPassword.getText())) {
			JOptionPane.showMessageDialog(ConnectionSetDialog.this, "Please input the password!");
			return false;
		}
		if (utils.isNullOrBlankString(txtSchema.getText())) {
			JOptionPane.showMessageDialog(ConnectionSetDialog.this, "Please input the schema!");
			return false;
		}
		// URL
		if (utils.isNullOrBlankString(txtUrl.getText())) {
			JOptionPane.showMessageDialog(ConnectionSetDialog.this, "Please input the url!");
			return false;
		}
		
		ComDao dao = new ComDao();
	    // 
		String sErrMsg = dao.checkUrl(txtUser.getText(), txtPassword.getText(), txtSchema.getText(), txtUrl.getText());
		if (!utils.isNullOrBlankString(sErrMsg)) {
			JOptionPane.showMessageDialog(ConnectionSetDialog.this, sErrMsg);
			return false;
		}
		
		return true;
	}

}  //  @jve:decl-index=0:visual-constraint="6,6"
