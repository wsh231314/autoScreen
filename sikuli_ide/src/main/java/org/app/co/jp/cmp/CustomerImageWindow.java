/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */
package org.app.co.jp.cmp;

import org.app.co.jp.util.Utils;
import org.sikuli.basics.Debug;
import org.sikuli.script.Location;
import org.sikuli.script.ScreenImage;
import org.sikuli.script.ScreenUnion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerImageWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final String me = "PatternWindow: ";
    private CustomerImage _imgBtn;
    private PatternPaneScreenshot _screenshot;
    private PatternPaneTargetOffset _tarOffsetPane;
    private JTabbedPane tabPane;
    private JPanel paneTarget, panePreview;
    private JLabel[] msgApplied;
    private int tabSequence = 0;
    private static final int tabMax = 3;
    private ScreenImage _simg;
    boolean isFileOverwritten = false;
    String fileRenameOld;
    String fileRenameNew;
    Dimension pDim;

    public CustomerImageWindow(CustomerImage imgBtn, boolean exact,
                         float similarity, int numMatches) {
        init(imgBtn, exact, similarity, numMatches);
    }

    private void init(CustomerImage imgBtn, boolean exact, float similarity, int numMatches) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        _imgBtn = imgBtn;

        takeScreenshot();
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        GraphicsConfiguration gc = getGraphicsConfiguration();
        int pOff = 50;
        Point pLoc = new Point(pOff, pOff);
        if (gc == null) {
            pDim = new Dimension(900, 700);
        } else {
            pDim = getGraphicsConfiguration().getBounds().getSize();
            pDim.width = (int) ((pDim.width - 2 * pOff) * 0.95);
            pDim.height = (int) ((pDim.height - 2 * pOff) * 0.95);
            pLoc = getGraphicsConfiguration().getBounds().getLocation();
            pLoc.translate(pOff, pOff);
        }
        setPreferredSize(pDim);

        tabPane = new JTabbedPane();
        msgApplied = new JLabel[tabMax];

        tabSequence = 0;

        tabSequence++;
        msgApplied[tabSequence] = new JLabel();
        setMessageApplied(tabSequence, false);
        panePreview = createPreviewPanel();
        tabPane.addTab("Screen", panePreview);

        tabSequence++;
        msgApplied[tabSequence] = new JLabel();
        setMessageApplied(tabSequence, false);
        paneTarget = createTargetPanel();
        tabPane.addTab("Capture", paneTarget);

        c.add(tabPane, BorderLayout.CENTER);
        c.add(createButtons(), BorderLayout.SOUTH);
        c.doLayout();
        pack();
        try {
            _screenshot.setParameters(_imgBtn.getFilename(),
                    exact, similarity, numMatches);
        } catch (Exception e) {
            Debug.error(me + "Problem while setting up pattern pane\n%s", e.getMessage());
        }
        setLocation(pLoc);
    }

    void takeScreenshot() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        _simg = (new ScreenUnion()).getScreen().capture();
    }

    private JPanel createPreviewPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        _screenshot = new PatternPaneScreenshot(_simg, pDim, msgApplied[tabSequence]);
        createMarginBox(p, _screenshot);
        p.add(Box.createVerticalStrut(5));
        p.add(_screenshot.createControls());
        p.doLayout();
        return p;
    }

    private JPanel createTargetPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        _tarOffsetPane = new PatternPaneTargetOffset(
                _simg, _imgBtn.getFilename(), _imgBtn.getTargetOffset(), pDim, msgApplied[tabSequence]);
        createMarginBox(p, _tarOffsetPane);
        p.add(Box.createVerticalStrut(5));
        p.add(_tarOffsetPane.createControls());
        p.doLayout();
        return p;
    }

    private JComponent createButtons() {
        JPanel pane = new JPanel(new GridBagLayout());
        final JButton btnCancel = new JButton("cancel");
        btnCancel.addActionListener(new ActionCancel(this));
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 3;
        c.gridx = 0;
        c.insets = new Insets(5, 0, 10, 0);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 1;
        c.gridx = 2;
        pane.add(btnCancel, c);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                btnCancel.doClick();
            }
        });
        return pane;
    }

    private void createMarginBox(Container c, Component comp) {
        c.add(Box.createVerticalStrut(10));
        Box lrMargins = Box.createHorizontalBox();
        lrMargins.add(Box.createHorizontalStrut(10));
        lrMargins.add(comp);
        lrMargins.add(Box.createHorizontalStrut(10));
        c.add(lrMargins);
        c.add(Box.createVerticalStrut(10));
    }

    public void setMessageApplied(int i, boolean flag) {
        if (flag) {
            for (JLabel m : msgApplied) {
                m.setText("     (changed)");
            }
        } else {
            msgApplied[i].setText("     (          )");
        }
    }

    public void close() {
        _simg = null;
        _imgBtn.resetWindow();
    }

    public JTabbedPane getTabbedPane() {
        return tabPane;
    }

    public void setTargetOffset(Location offset) {
        if (offset != null) {
            _tarOffsetPane.setTarget(offset.x, offset.y);
        }
    }

    class ActionCancel implements ActionListener {

        private Window _parent;

        public ActionCancel(Window parent) {
            _parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            _imgBtn.getWindow().close();
            _parent.dispose();
            Utils.showAgainWindow();
        }
    }

}
