package org.app.co.jp.util.bean;

import org.app.co.jp.util.ConnectionUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileSelect extends JFileChooser {
	
	/**
	 *
	 */
	public int showOpenDialog(Component parent)  throws HeadlessException  {
		ConnectionUtil util = new ConnectionUtil();
		// 
		String sDirectory = util.getProperty("directory");
		File dirctory = new File(sDirectory);
		if (dirctory.exists() && dirctory.isDirectory()) {
			setCurrentDirectory(dirctory);
		}
		int sResult = super.showOpenDialog(parent);
		sDirectory = super.getCurrentDirectory().getPath();
		util.setProperty("directory", sDirectory);
		util.save();
		return sResult;
	}
	
	/**
	 *
	 */
	public int showSaveDialog(Component parent)  throws HeadlessException  {
		ConnectionUtil util = new ConnectionUtil();
		// 
		String sDirectory = util.getProperty("directory");
		File dirctory = new File(sDirectory);
		if (dirctory.exists() && dirctory.isDirectory()) {
			setCurrentDirectory(dirctory);
		}
		int sResult = super.showSaveDialog(parent);
		sDirectory = super.getCurrentDirectory().getPath();
		util.setProperty("directory", sDirectory);
		util.save();
		return sResult;
	}
}
