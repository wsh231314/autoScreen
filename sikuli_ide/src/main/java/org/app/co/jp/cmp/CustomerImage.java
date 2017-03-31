/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */
package org.app.co.jp.cmp;

import org.app.co.jp.dao.PageListDao;
import org.app.co.jp.util.Utils;
import org.sikuli.basics.Debug;
import org.sikuli.ide.EditorPatternLabel;
import org.sikuli.script.Image;
import org.sikuli.script.Location;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

public class CustomerImage extends JButton implements ActionListener, Serializable, MouseListener {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_NUM_MATCHES = 50;
    static final float DEFAULT_SIMILARITY = 0.7f;
    private String _imgFilename, _thumbFname, _imgFilenameSaved;
    private Image _image;
    private float _similarity, _similaritySaved;
    private int _numMatches = DEFAULT_NUM_MATCHES;
    private boolean _exact, _exactSaved;
    private Location _offset = new Location(0, 0), _offsetSaved;
    private int _imgW, _imgH;
    private float _scale = 1f;
    private static CustomerImageWindow pwin = null;
    private static Font textFont = new Font("arial", Font.BOLD, 12);


    public CustomerImage(String imgFilename) {
        this._image = null;
        init(imgFilename);
    }

    private void init(String imgFilename) {
        _exact = false;
        _similarity = DEFAULT_SIMILARITY;
        _numMatches = DEFAULT_NUM_MATCHES;
        setFilename(imgFilename);
        setMargin(new Insets(0, 0, 0, 0));
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addActionListener(this);
        this.setBorder(null);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        setButtonText();
    }

    public BufferedImage createThumbnailImage(int maxHeight) {
        return createThumbnailImage(_imgFilename, maxHeight);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (!Utils.isFileExists(_imgFilename)) {
    		return;
    	}
    	Utils.hidenWindow();
        if (pwin == null) {
            _offsetSaved = new Location(_offset);
            _similaritySaved = _similarity;
            _exactSaved = _similarity >= 0.99f;
            _imgFilenameSaved = _imgFilename.substring(0);
            pwin = new CustomerImageWindow(this, _exactSaved, _similarity, _numMatches);
            pwin.setTargetOffset(_offset);
        }
        pwin.setVisible(true);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    public CustomerImageWindow getWindow() {
        return pwin;
    }

    public void resetWindow() {
        pwin = null;
    }

    public String getFilename() {
            return _imgFilename;
    }

    public void setFilename(String fileName) {
        _imgFilename = fileName;
        if (Utils.isFileExists(fileName)) {
        	
			PageListDao dao = new PageListDao();
			String path = dao.getPagePath("work");

			_imgFilename = path.concat((new File(fileName)).getName());
			
			if (Utils.isFileExists(_imgFilename)) {
				setIcon(new ImageIcon(createThumbnailImage(_imgFilename, 40)));
			}
        	
        }
        setButtonText();
    }

    private BufferedImage createThumbnailImage(String imgFname, int maxHeight) {
        try {
            BufferedImage img;
            if (_image != null) {
                img = _image.get();
            } else {

//TODO ????        Debug.error("EditorPatternButton: createThumbnailImage: not using Image for: " + imgFname);
                img = ImageIO.read(new File(imgFname));
            }
            int w = img.getWidth(null), h = img.getHeight(null);
            _imgW = w;
            _imgH = h;
            if (maxHeight == 0 || maxHeight >= h) {
                return img;
            }
            _scale = (float) maxHeight / h;
            w *= _scale;
            h *= _scale;
            h = (int) h;
            BufferedImage thumb = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = thumb.createGraphics();
            g2d.drawImage(img, 0, 0, w, h, null);
            g2d.dispose();
            return thumb;
        } catch (IOException e) {
            Debug.error("Can't read file: " + e.getMessage());
            return null;
        }
    }

    public boolean setParameters(boolean exact, float similarity, int numMatches) {
        boolean dirty = false;
        Debug.log(3, "ThumbButtonLabel: setParameters: " + exact + "," + similarity + "," + numMatches);
        dirty |= setExact(exact);
        dirty |= setSimilarity(similarity);
        setButtonText();
        return dirty;
    }

    public void resetParameters() {
        setFilename(_imgFilenameSaved);
        setParameters(_exactSaved, _similaritySaved, DEFAULT_NUM_MATCHES);
        setTargetOffset(_offsetSaved);
    }

    public boolean setExact(boolean exact) {
        if (_exact != exact) {
            _exact = exact;
            return true;
        }
        return false;
    }

    public boolean setSimilarity(float val) {
        float sim;
        if (val < 0) {
            sim = 0;
        } else if (val >= 1) {
            sim = 0.99f;
        } else {
            sim = val;
        }
        if (sim != _similarity) {
            _similarity = sim;
            return true;
        }
        return false;
    }

    public float getSimilarity() {
        return _similarity;
    }

    public boolean setTargetOffset(Location offset) {
        Debug.log(3, "ThumbButtonLabel: setTargetOffset: " + offset.toStringShort());
        if (!_offset.equals(offset)) {
            _offset = offset;
            setButtonText();
            return true;
        }
        return false;
    }

    public Location getTargetOffset() {
        return _offset;
    }

    public String getFileName() {
        return _imgFilename;
    }

    //<editor-fold defaultstate="collapsed" desc="paint button">
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawText(g2d);
        if (useThumbnail()) {
            g2d.setColor(new Color(0, 128, 128, 128));
            g2d.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 5, 5);
        }
    }

    private void drawText(Graphics2D g2d) {
        String strSim = null, strOffset = null;
        if (_similarity != DEFAULT_SIMILARITY) {
            if (_exact) {
                strSim = "99";
            } else {
                strSim = String.format("%d", (int) (_similarity * 100));
            }
        }
        if (_offset != null && (_offset.x != 0 || _offset.y != 0)) {
            strOffset = _offset.toStringShort();
        }
        if (strOffset == null && strSim == null) {
            return;
        }

        g2d.getFontMetrics().getMaxAscent();
        final int x = getWidth(), y = 0;
        drawText(g2d, strSim, x, y);
        if (_offset != null) {
            drawCross(g2d);
        }
    }

    private void drawText(Graphics2D g2d, String str, int x, int y) {
        if (str == null) {
            return;
        }
        final int w = g2d.getFontMetrics().stringWidth(str);
        final int fontH = g2d.getFontMetrics().getMaxAscent();
        final int borderW = 3;
        g2d.setFont(textFont);
        g2d.setColor(new Color(0, 128, 0, 128));
        g2d.fillRoundRect(x - borderW * 2 - w - 1, y, w + borderW * 2 + 1, fontH + borderW * 2 + 1, 3, 3);
        g2d.setColor(Color.white);
        g2d.drawString(str, x - w - 3, y + fontH + 3);
    }

    private void drawCross(Graphics2D g2d) {
        int x, y;
        final String cross = "+";
        final int w = g2d.getFontMetrics().stringWidth(cross);
        final int h = g2d.getFontMetrics().getMaxAscent();
        if (_offset.x > _imgW / 2) {
            x = getWidth() - w;
        } else if (_offset.x < -_imgW / 2) {
            x = 0;
        } else {
            x = (int) (getWidth() / 2 + _offset.x * _scale - w / 2);
        }
        if (_offset.y > _imgH / 2) {
            y = getHeight() + h / 2 - 3;
        } else if (_offset.y < -_imgH / 2) {
            y = h / 2 + 2;
        } else {
            y = (int) (getHeight() / 2 + _offset.y * _scale + h / 2);
        }
        g2d.setFont(textFont);
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.drawString(cross, x + 1, y + 1);
        g2d.setColor(new Color(255, 0, 0, 180));
        g2d.drawString(cross, x, y);
    }

    private boolean useThumbnail() {
        return !_imgFilename.equals(_thumbFname);
    }
    //</editor-fold>

    @Override
    public String toString() {
        return this.getPatternString(_imgFilename, _similarity, _offset, _image);
    }

    private void setButtonText() {
    	setToolTipText(toString());
    }

    //<editor-fold defaultstate="collapsed" desc="mouse events not used">
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }
    
    public String getPatternString(String ifn, float sim, Location off, Image img) {
        if (ifn == null) {
            return "\"" + EditorPatternLabel.CAPTURE + "\"";
        }
//        String imgName = new File(ifn).getName();
//        if (img != null) {
//            imgName = img.getName();
//        }
        String imgName = ifn;
        String pat = "Pattern(\"" + imgName + "\")";
        String ret = "";
        if (sim > 0) {
            if (sim >= 0.99F) {
                ret += ".exact()";
            } else if (sim != 0.7F) {
                ret += String.format(Locale.ENGLISH, ".similar(%.2f)", sim);
            }
        }
        if (off != null && (off.x != 0 || off.y != 0)) {
            ret += ".targetOffset(" + off.x + "," + off.y + ")";
        }
        if (!ret.equals("")) {
            ret = pat + ret;
        } else {
            ret = "\"" + imgName + "\"";
        }
        return ret;
    }
    
    public void setParameterByString(String strValue) {
        if (!strValue.startsWith("Pattern")) {
            return;
        }
        String[] tokens = strValue.split("\\)\\s*\\.?");
        for (String tok : tokens) {
            //System.out.println("token: " + tok);
            if (tok.startsWith("exact")) {
            	_similarity = 0.99f;
            	setExact(true);
            } else if (tok.startsWith("similar")) {
                String strArg = tok.substring(tok.lastIndexOf("(") + 1);
                try {
                    setSimilarity(Float.valueOf(strArg));
                } catch (NumberFormatException e) {
                    return;
                }
            } else if (tok.startsWith("firstN")) { // FIXME: replace with limit/max
                String strArg = tok.substring(tok.lastIndexOf("(") + 1);
                _numMatches = Integer.valueOf(strArg);
            } else if (tok.startsWith("targetOffset")) {
                String strArg = tok.substring(tok.lastIndexOf("(") + 1);
                String[] args = strArg.split(",");
                try {
                    Location offset = new Location(0, 0);
                    offset.x = Integer.valueOf(args[0]);
                    offset.y = Integer.valueOf(args[1]);
                    setTargetOffset(offset);
                } catch (NumberFormatException e) {
                    return;
                }
            }
        }
        setButtonText();
    }
    //</editor-fold>
}