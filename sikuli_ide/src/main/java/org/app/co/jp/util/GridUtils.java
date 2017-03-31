package org.app.co.jp.util;

import org.app.co.jp.cmp.CustomerCaptureButton;
import org.app.co.jp.cmp.CustomerImage;
import org.app.co.jp.cmp.CustomerImageButton;
import org.app.co.jp.util.bean.DefaultComboBoxModel;
import org.app.co.jp.util.bean.SelectBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridUtils {
	
	JPanel jPanel = null;
	JButton preBtn = null;
	JButton afterBtn = null;
	int iPageSize = 10;
	
	List<Object> valueList = new ArrayList<Object>();
	List componentList = new ArrayList();
	
	List lstInitComp = null;
	List lstInitTitle = null;
	
	int iHeight = 0;
	int iPage = 0;
	/** */
	String[] arrColumn = null;
	/** */
	String[] titleArray = null;
	
	JLabel pageInfo = null;
	
	JButton selectAllButton = null;
	String strSelectKey = "";
	
	private int cellspace = 3;
	
	public GridUtils (JPanel jPanel, List lstInitTitle, List lstInitComp, String[] strArray, JButton preBtn, JButton afterBtn, String[] titleArray) {
		this.jPanel = jPanel;
		this.lstInitTitle = lstInitTitle;
		setInitComponent(lstInitComp);
		this.arrColumn = strArray;
		this.preBtn = preBtn;
		this.afterBtn = afterBtn;
		this.titleArray = titleArray;
		setTitle();
		preBtn.setEnabled(false);
		afterBtn.setEnabled(false);
		addActionListener();
		setTitleBorder();
	}
	
	public GridUtils (JPanel jPanel, List lstInitTitle, List lstInitComp, String[] strArray, JButton preBtn, JButton afterBtn, int iPageSize, String[] titleArray) {
		this.jPanel = jPanel;
		this.lstInitTitle = lstInitTitle;
		setInitComponent(lstInitComp);
		this.arrColumn = strArray;
		this.preBtn = preBtn;
		this.afterBtn = afterBtn;
		this.iPageSize = iPageSize;
		this.titleArray = titleArray;
		setTitle();
		preBtn.setEnabled(false);
		afterBtn.setEnabled(false);
		addActionListener();
		setTitleBorder();
	}
	
	/**
	 *
	 */
	private void setTitle() {
		for (int i= 0; i < lstInitComp.size(); i++) {
			JComponent jcp = (JComponent)lstInitComp.get(i);
			jcp.setLocation((int)jcp.getLocation().getX(), (int)jcp.getLocation().getY() + 3);
			JLabel label = new JLabel();
			label.setSize(jcp.getSize());
			if (jcp.getSize().height > iHeight) {
				iHeight = jcp.getSize().height;
			}
			label.setLocation(jcp.getLocation());
		}
	}
	
	/**
	 *
	 * @param list
	 */
	public void setData(List list) {
		valueList = list;
		iPage = 0;
		showDetail();
		setPageBtn();
		setPageInfo();
		//
		setBorder();
	}
	
	/**
	 *
	 */
	private void showDetail() {
		jPanel.removeAll();
		jPanel.repaint();
		if (componentList == null) {
			componentList = new ArrayList();
		} else {
			componentList.clear();
		}
		if (valueList != null && valueList.size() > 0) {

			int iStart = getStartPos();
			int iEnd = getEndPos();
			int  iRow = 0;
			for (int i = iStart; i <= iEnd; i++) {
				Map valueMap = (Map)valueList.get(i);
				List compList = new ArrayList();
				CustomerImageButton imgButton = null;
				for (int m = 0; m < lstInitComp.size(); m++) {
					JComponent jcp = (JComponent)lstInitComp.get(m);
					if (jcp instanceof JLabel) {
						JLabel label = new JLabel();
						label.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						label.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						String value = (String)valueMap.get(arrColumn[m]);
						label.setText(value);
						label.setFont(jcp.getFont());
						label.setHorizontalAlignment(((JLabel) jcp).getHorizontalAlignment());
						jPanel.add(label);
						compList.add(label);
					} else if (jcp instanceof JTextField) {
						JTextField text = new JTextField();
						text.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						text.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						String value = (String)valueMap.get(arrColumn[m]);
						text.setText(value);
						ActionListener []listeners = ((JTextField)jcp).getActionListeners();
						for (int n = 0; n < listeners.length; n++) {
							text.addActionListener(listeners[n]);
						}
						jPanel.add(text);
						compList.add(text);
					} else if (jcp instanceof CustomerImageButton) {
						String value = (String)valueMap.get(arrColumn[m]);
						String path = (String)valueMap.get("file_path");
						// 
						imgButton = new CustomerImageButton(path);
						
						imgButton.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						imgButton.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						imgButton.setParameterByString(value);
						jPanel.add(imgButton);
						compList.add(imgButton);
					} else if (jcp instanceof CustomerImage) {
						String value = (String)valueMap.get(arrColumn[m]);
						String path = (String)valueMap.get("file_path");
						// 
						CustomerImage img = new CustomerImage(path);
						
						img.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						img.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						img.setParameterByString(value);
						jPanel.add(img);
						compList.add(img);
					}    else if (jcp instanceof CustomerCaptureButton) {
						String path = (String)valueMap.get("file_path");
						// 
						CustomerCaptureButton button = new CustomerCaptureButton(imgButton, path);
						
						button.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						button.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						jPanel.add(button);
						compList.add(button);
					} else if (jcp instanceof JButton) {
						JButton button = new JButton();
						button.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						button.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						String value = (String)valueMap.get(arrColumn[m]);
						button.setText(value);
						button.setFont(jcp.getFont());
						button.setHorizontalAlignment(((JButton) jcp).getHorizontalAlignment());
						ActionListener []listeners = ((JButton)jcp).getActionListeners();
						for (int n = 0; n < listeners.length; n++) {
							button.addActionListener(listeners[n]);
						}
						jPanel.add(button);
						compList.add(button);
					} else if (jcp instanceof JRadioButton) {
						JRadioButton button = new JRadioButton();
						button.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						button.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						String value = (String)valueMap.get(arrColumn[m]);
						Boolean blnValue = Boolean.valueOf(value);
						button.setSelected(blnValue.booleanValue());
						ActionListener []listeners = ((JRadioButton)jcp).getActionListeners();
						for (int n = 0; n < listeners.length; n++) {
							button.addActionListener(listeners[n]);
						}
						jPanel.add(button);
						compList.add(button);
					} else if (jcp instanceof JCheckBox) {
						JCheckBox button = new JCheckBox();
						button.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						button.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						String value = (String)valueMap.get(arrColumn[m]);
						Boolean blnValue = Boolean.valueOf(value);
						button.setSelected(blnValue.booleanValue());
						button.setHorizontalAlignment(((JCheckBox) jcp).getHorizontalAlignment());
						ActionListener []listeners = ((JCheckBox)jcp).getActionListeners();
						for (int n = 0; n < listeners.length; n++) {
							button.addActionListener(listeners[n]);
						}
						jPanel.add(button);
						compList.add(button);
					} else if (jcp instanceof JComboBox) {
						JComboBox box = new JComboBox();
						box.setSize((int)(jcp.getSize().getWidth()) -3, (int)(jcp.getSize().getHeight()));
						box.setLocation((int)(jcp.getLocation().getX()), (int)(jcp.getLocation().getY() + iRow * (iHeight + cellspace)));
						DefaultComboBoxModel modal = (DefaultComboBoxModel)valueMap.get(arrColumn[m] + "_FOR_SELECT");
						box.setModel(modal);
						String value = (String)valueMap.get(arrColumn[m]);
						SelectBean select = null;
						for (int n = 0; n < modal.getSize(); n++) {
							SelectBean temp = (SelectBean)modal.getElementAt(n);
							if (temp.getCode().equals(value)) {
								select = temp;
								break;
							}
						}
						ActionListener []listeners = ((JComboBox)jcp).getActionListeners();
						for (int n = 0; n < listeners.length; n++) {
							box.addActionListener(listeners[n]);
						}
						box.setSelectedItem(select);
						jPanel.add(box);
						compList.add(box);
					} 
				}
				iRow++;
				componentList.add(compList);
			}
		}
	}
	
	/**
	 * 
	 *
	 */
	private void setBorder() {
		int height = iHeight;
		int min = getMinHeight();
		int max = getMaxWidth();
		
		for (int i = 0; i < componentList.size(); i++) {
			for (int j = 0; j < lstInitComp.size(); j++) {
				// 
				JComponent jcp = (JComponent)lstInitComp.get(j);
				int x = jcp.getX();
				//
				if (j == 0) {
					if (i == 0) {
						JSeparator first = new JSeparator(SwingConstants.HORIZONTAL);
						first.setBounds(x - 3, min + ((cellspace + height) * i - 3), max - x + 4, 2);
						first.setBackground(Color.BLACK);
//						jPanel.add(first);
					}
					//
					JSeparator jsh = new JSeparator(SwingConstants.HORIZONTAL);
					jsh.setBounds(x - 3, min + ((cellspace + height) * (i + 1) - 3), max - x + 4, 2);
					jsh.setBackground(Color.BLACK);
					jPanel.add(jsh);
				}
				//
				if (j == 0) {
					JSeparator jsh = new JSeparator(SwingConstants.VERTICAL);
					jsh.setBounds(max + 1, min + ((cellspace + height) * i - 3), 2, cellspace + height + 2);
					jsh.setBackground(Color.BLACK);
					jPanel.add(jsh);
				}
				if (checkColumn(j)) {
					JSeparator jsh = new JSeparator(SwingConstants.VERTICAL);
					jsh.setBounds(x - 3, min + ((cellspace + height) * i - 3), 2, cellspace + height + 2);
					jsh.setBackground(Color.BLACK);
					jPanel.add(jsh);
				}
			}
		}
		
	}
	
	/**
	 *
	 * @return
	 */
	private int getMinHeight() {
		int iTempHeight = 0;
		for (int i = 0; i < lstInitComp.size(); i++) {
			JComponent jcp = (JComponent)lstInitComp.get(i);
			if (i == 0) {
				iTempHeight = jcp.getY();
			}
			//
			if (iTempHeight > jcp.getY()) {
				iTempHeight = jcp.getY();
			}
		}
		return iTempHeight;
	}
	
	/**
	 *
	 * @return
	 */
	private int getMaxWidth() {
		int iTempWidth = 0;
		for (int i = 0; i < lstInitComp.size(); i++) {
			JComponent jcp = (JComponent)lstInitComp.get(i);
			if (i == 0) {
				iTempWidth = jcp.getX() + jcp.getWidth();
			}
			//
			if (iTempWidth < (jcp.getX() + jcp.getWidth())) {
				iTempWidth = jcp.getX() + jcp.getWidth();
			}
		}
		return iTempWidth;
	}
	
	/**
	 *
	 */
	private void setPageBtn() {
		int iStart = getStartPos();
		int iEnd = getEndPos();
		//
		if (iStart == 0) {
			preBtn.setEnabled(false);
		} else {
			preBtn.setEnabled(true);
		}
		//
		if (iEnd == (valueList.size() - 1)) {
			afterBtn.setEnabled(false);
		} else {
			afterBtn.setEnabled(true);
		}
	}
	
	/**
	 *
	 */
	private int getStartPos() {
		return iPageSize * iPage;
	}
	
	/**
	 *
	 */
	private void setInitComponent(List initCompList) {
		if (lstInitComp == null) {
			lstInitComp = new ArrayList();
		} else {
			lstInitComp.clear();
		}
		for (int i= 0; i < initCompList.size(); i++) {
			JComponent jcp = (JComponent)initCompList.get(i);

			if (jcp instanceof JLabel) {
				JLabel label = new JLabel();
				label.setSize(jcp.getSize());
				label.setLocation(jcp.getLocation());
				label.setFont(jcp.getFont());
				lstInitComp.add(label);
			} else if (jcp instanceof JTextField) {
				JTextField text = new JTextField();
				text.setSize(jcp.getSize());
				text.setLocation(jcp.getLocation());
				text.setFont(jcp.getFont());
				ActionListener []listeners = ((JTextField)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					text.addActionListener(listeners[m]);
				}
				lstInitComp.add(text);
			} else if (jcp instanceof CustomerImageButton) {
				CustomerImageButton button = new CustomerImageButton("");
				
				button.setSize(jcp.getSize());
				button.setLocation(jcp.getLocation());
				button.setFont(jcp.getFont());
				ActionListener []listeners = ((JButton)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					button.addActionListener(listeners[m]);
				}
				lstInitComp.add(button);
			}   else if (jcp instanceof CustomerImage) {
				CustomerImage button = new CustomerImage("");
				
				button.setSize(jcp.getSize());
				button.setLocation(jcp.getLocation());
				button.setFont(jcp.getFont());
				ActionListener []listeners = ((JButton)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					button.addActionListener(listeners[m]);
				}
				lstInitComp.add(button);
			}  else if (jcp instanceof CustomerCaptureButton) {
				CustomerImageButton imgButton = null;
				// 
				CustomerCaptureButton button = new CustomerCaptureButton(imgButton, "");
				
				button.setSize(jcp.getSize());
				button.setLocation(jcp.getLocation());
				button.setFont(jcp.getFont());
				ActionListener []listeners = ((JButton)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					button.addActionListener(listeners[m]);
				}
				lstInitComp.add(button);
			} else if (jcp instanceof JButton) {
				JButton button = new JButton();
				button.setSize(jcp.getSize());
				button.setLocation(jcp.getLocation());
				button.setFont(jcp.getFont());
				ActionListener []listeners = ((JButton)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					button.addActionListener(listeners[m]);
				}
				lstInitComp.add(button);
			} else if (jcp instanceof JRadioButton) {
				JRadioButton button = new JRadioButton();
				button.setSize(jcp.getSize());
				button.setLocation(jcp.getLocation());
				button.setFont(jcp.getFont());
				ActionListener []listeners = ((JRadioButton)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					button.addActionListener(listeners[m]);
				}
				lstInitComp.add(button);
			} else if (jcp instanceof JCheckBox) {
				JCheckBox button = new JCheckBox();
				button.setSize(jcp.getSize());
				button.setLocation(jcp.getLocation());
				button.setFont(jcp.getFont());
				ActionListener []listeners = ((JCheckBox)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					button.addActionListener(listeners[m]);
				}
				lstInitComp.add(button);
			} else if (jcp instanceof JComboBox) {
				JComboBox box = new JComboBox();
				box.setSize(jcp.getSize());
				box.setLocation(jcp.getLocation());
				box.setFont(jcp.getFont());
				ActionListener []listeners = ((JComboBox)jcp).getActionListeners();
				for (int m = 0; m < listeners.length; m ++) {
					box.addActionListener(listeners[m]);
				}
				lstInitComp.add(box);
			} 
		}
		jPanel.removeAll();
	}
	
	/**
	 *
	 */
	public void freshData() {
		if (valueList != null && valueList.size() > 0) {
			int iStart = getStartPos();
			int iEnd = getEndPos();
			int  iRow = 0;
			for (int i = iStart; i <= iEnd; i++) {
				Map valueMap = (Map)valueList.get(i);
				List compList = (List)componentList.get(iRow);
				for (int m = 0; m < compList.size(); m++) {
					JComponent jcp = (JComponent)compList.get(m);
					
					if (jcp instanceof JLabel) {
						JLabel label = (JLabel)jcp;
						String value = label.getText();
						valueMap.put(arrColumn[m], value);
					} else if (jcp instanceof JTextField) {
						JTextField text = (JTextField)jcp;
						String value = text.getText();
						valueMap.put(arrColumn[m], value);
					} else if (jcp instanceof CustomerImageButton) {
						CustomerImageButton button = (CustomerImageButton)jcp;
						String value = button.toString();
						valueMap.put(arrColumn[m], value);
						
						//
						String file_path = button.getFileName();
						valueMap.put("file_path", file_path);
					}  else if (jcp instanceof JButton) {

					} else if (jcp instanceof JRadioButton) {
						JRadioButton button = (JRadioButton)jcp;
						String value = String.valueOf(button.isSelected());
						valueMap.put(arrColumn[m], value);
					} else if (jcp instanceof JCheckBox) {
						JCheckBox check = (JCheckBox)jcp;
						String value = String.valueOf(check.isSelected());
						valueMap.put(arrColumn[m], value);
					} else if (jcp instanceof JComboBox) {
						JComboBox combo = (JComboBox)jcp;
						SelectBean value = (SelectBean)combo.getSelectedItem();
						valueMap.put(arrColumn[m], value.getCode());
					} 
				}
				iRow++;
			}
		}
	}
	
	/**
	 *
	 */
	private int getEndPos() {
		int iResult = iPageSize * (iPage + 1) - 1;
		if (iResult > (valueList.size() - 1)) {
			iResult = valueList.size() - 1;
		}
		return iResult;
	}
	
	/**
	 *
	 */
	private void addActionListener() {
		preBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				freshData();
				iPage--;
				showDetail();
				setPageBtn();
				setPageInfo();
				setBorder();
			}
		});
		afterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				freshData();
				iPage++;
				showDetail();
				setPageBtn();
				setPageInfo();
				//
				setBorder();
			}
		});
	}
	
	/**
	 *
	 */
	private void setPageInfo() {
		if (pageInfo != null) {
			String nowPage = String.valueOf(iPage + 1);
			int iTotalPage = ((valueList.size() - 1)/ iPageSize) + 1;
			String strTotalPage = String.valueOf(iTotalPage);
			String strMessage = nowPage + " / " + strTotalPage;
			pageInfo.setText(strMessage);
		}
	}
	
	/**
	 */
	public int getDataRow(int iRow) {
		int iDataRow = 0;
		//
		iDataRow = iPage * iPageSize + iRow;
		
		return iDataRow;
	}
	
	/**
	 *
	 */
	public void setPageNo(int iPageNo) {
		int iTotalPage = ((valueList.size() - 1)/ iPageSize) + 1;
		
		if (iPageNo > iTotalPage) {
			JOptionPane.showMessageDialog(jPanel.getParent(), "Page No is greater than max page!");
		}
		freshData();
		iPage = iPageNo;
		showDetail();
		setPageBtn();
		setPageInfo();
		//
		setBorder();
	}
	
	/**
	 *
	 */
	public int getPageNo() {
		return iPage;
	}
	
	/**
	 *
	 */
	public int getMaxPageNo() {
		return ((valueList.size() - 1)/ iPageSize);
	}

	/**
	 */
	public List getComponentList() {
		return componentList;
	}

	/**
	 */
	public void setComponentList(List componentList) {
		this.componentList = componentList;
	}

	/**
	 */
	public List getValueList() {
		return valueList;
	}

	/**
	 */
	public void setValueList(List valueList) {
		this.valueList = valueList;
	}

	/**
	 */
	public JLabel getPageInfo() {
		return pageInfo;
	}

	/**
	 */
	public void setPageInfo(JLabel pageInfo) {
		this.pageInfo = pageInfo;
	}

	/**
	 */
	public JButton getSelectAllButton() {
		return selectAllButton;
	}

	/**
	 */
	public void setSelectAllButton(JButton selectAllButton, String strSelectKey) {
		this.selectAllButton = selectAllButton;
		this.strSelectKey = strSelectKey;
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				freshData();
				iPage--;
				showDetail();
				setPageBtn();
				setPageInfo();
				//
				setBorder();
			}
		});
	}
	
	public void setTitleBorder() {
		// 
		int iInitX = 0;
		//
		int iInitY = 0;
		iInitX = (int)jPanel.getLocation().getX();
		iInitY = (int)jPanel.getLocation().getY();
		Container jParent = jPanel.getParent();
		int height = iHeight;
		int min = iInitY - iHeight - getMinHeight();
		int max = getMaxWidth();
		
		for (int j = 0; j < lstInitComp.size(); j++) {
			// 
			JComponent jcp = (JComponent) lstInitComp.get(j);
			int x = jcp.getX();
			//
			if (j == 0) {
				JSeparator first = new JSeparator(SwingConstants.HORIZONTAL);
				first.setBounds(iInitX + x - 3, min - 3, max - x + 4, 2);
				first.setBackground(Color.BLACK);
				jParent.add(first);
				//
				JSeparator jsh = new JSeparator(SwingConstants.HORIZONTAL);
				jsh.setBounds(iInitX + x - 3, min + cellspace + height - 3, max
						- x + 4, 2);
				jsh.setBackground(Color.BLACK);
				jParent.add(jsh);
			}
			//
			if (j == 0) {
				JSeparator jsh = new JSeparator(SwingConstants.VERTICAL);
				jsh.setBounds(iInitX + max + 1, min - 3, 3, cellspace + height
						+ 2);
				jsh.setBackground(Color.BLACK);
				jParent.add(jsh);
			}
			if (checkColumn(j)) {
				JSeparator jsh = new JSeparator(SwingConstants.VERTICAL);
				jsh.setBounds(iInitX + x - 3, min - 3, 3, cellspace + height
						+ 2);
				jsh.setBackground(Color.BLACK);
				jParent.add(jsh);
			}
		}
	}
	
	/**
	 */
	private boolean checkColumn(int iColumn) {
		boolean blnResult = false;
		if (iColumn >= arrColumn.length) {
			return false;
		} else {
			String strColumnTemp = arrColumn[iColumn];
			for (int i = 0; i < titleArray.length; i++) {
				if (strColumnTemp.equals(titleArray[i])) {
					return true;
				}
			}
		}
		
		return blnResult;
	}
}
