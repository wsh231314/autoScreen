package org.app.co.jp.util;

import org.app.co.jp.ap.MainApplet;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class DomXMLUtils {
	
	public static void main(String []arg) {
//		//
//		String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
//		//
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			//UIManager.setLookAndFeel(systemLookAndFeel);
//			//
//			Font lblFont = new Font(null, Font.BOLD, 11);
//			Font btnFont = new Font(null, Font.BOLD, 10);
//			Font chkFont = new Font(null, Font.BOLD, 10);
//
//			//
//			Border lblBorder = BorderFactory.createLineBorder(Color.BLACK);
//
//			// panel
//			UIManager.put("Panel.background", new Color(200,255,255));
//
//			UIManager.put("Button.font", btnFont);
//			//UIManager.put("Button.background", new Color(236,198,236));
//
//			UIManager.put("Label.font", lblFont);
//			UIManager.put("Label.border", lblBorder);
//
//			UIManager.put("CheckBox.font", chkFont);
//			UIManager.put("CheckBox.background", new Color(200,255,255));
//
//			UIManager.put("ComboBox.font", chkFont);
//			UIManager.put("ComboBox.background", new Color(200,255,255));
//
//			//
//			UIManager.put("TextField.disabletextcolor", Color.GRAY);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		MainApplet applet = new MainApplet();
//		applet.setVisible(true);
		
		String strPath = "C:\\\\work\\\\worksoft\\\\eclipse\\\\workspace\\\\sikuli_ide\\\\excelApl\\\\scripts\\\\script_52\\\\script_52.sikuli\\\\script_copy.py";
		try {
			Utils.addOuterDealPython(strPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
