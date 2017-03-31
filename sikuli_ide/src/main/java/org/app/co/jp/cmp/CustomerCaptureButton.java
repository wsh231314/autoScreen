/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */
package org.app.co.jp.cmp;

import org.app.co.jp.dao.PageListDao;
import org.app.co.jp.util.Utils;
import org.sikuli.basics.Debug;
import org.sikuli.basics.FileManager;
import org.sikuli.basics.PreferencesUser;
import org.sikuli.basics.Settings;
import org.sikuli.ide.PatternPaneNaming;
import org.sikuli.ide.SikuliIDE;
import org.sikuli.script.*;
import org.sikuli.util.EventObserver;
import org.sikuli.util.EventSubject;
import org.sikuli.util.OverlayCapturePrompt;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class CustomerCaptureButton extends JButton implements ActionListener, Cloneable, EventObserver {

	private static final long serialVersionUID = 1L;
    protected Element _line;
    private CustomerImageButton _imgBtn = null;
    String _imgFilePath = null;

    public static boolean debugTrace = false;

    public CustomerCaptureButton(CustomerImageButton imgBtn, String imagePath) {
        super();
        URL imageURL = SikuliIDE.class.getResource("/icons/capture-small.png");
        
        ImageIcon icon = new ImageIcon(imageURL);
        
        setIcon(icon);
        addActionListener(this);
        
		PageListDao dao = new PageListDao();
		String path = dao.getPagePath("work");

		_imgFilePath = path.concat((new File(imagePath)).getName());
        
        _imgBtn = imgBtn;
        
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setBorder(null);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        captureWithAutoDelay();
    }

    public void captureWithAutoDelay() {
        PreferencesUser pref = PreferencesUser.getInstance();
        int delay = (int) (pref.getCaptureDelay() * 1000.0) + 1;
        capture(delay);
    }

    IScreen defaultScreen = null;
    ScreenImage sImgNonLocal = null;

    public void capture(final int delay) {
    	
    	Utils.hidenWindow();
    	
    	RunTime.pause(((float) delay) / 1000);
    	
    	Screen.doPrompt("Select an image", this);
    }

    @Override
    public void update(EventSubject es) {

        ScreenImage simg = null;
        OverlayCapturePrompt ocp = null;
        if (null == es) {
            simg = sImgNonLocal;
        } else {
            ocp = (OverlayCapturePrompt) es;
            simg = ocp.getSelection();
            Screen.closePrompt();
        }
        try {
        	
        	File tempFile = new File(_imgFilePath);
        	
        	String filename = tempFile.getName();
        	
        	Settings.OverwriteImages = true;
        	
        	String strImagePath = FileManager.saveImage(simg.getImage(), filename, tempFile.getParent());
        	
        	// TODO
        	_imgBtn.setFilename(strImagePath);
        	
        	// TODO SAVE IMAGE FILE
        	
        	if (ocp != null) {
        		Screen.resetPrompt(ocp);
        	}
        } finally {
        	Utils.showAgainWindow();
        }
        
        
    }

    @Override
    public String toString() {
        return "\"__CLICK-TO-CAPTURE__\"";
    }
}
