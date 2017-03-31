/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */

package org.sikuli.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
//import java.io.FilenameFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.sikuli.basics.Debug;
import org.sikuli.basics.PreferencesUser;
import org.sikuli.basics.Settings;
import org.sikuli.script.Sikulix;

public class SikulixFileChooser {
  static final int FILES = JFileChooser.FILES_ONLY;
  static final int DIRS = JFileChooser.DIRECTORIES_ONLY;
  static final int DIRSANDFILES = JFileChooser.FILES_AND_DIRECTORIES;
  static final int SAVE = FileDialog.SAVE;
  static final int LOAD = FileDialog.LOAD;
  Frame _parent;
  boolean accessingAsFile = false;
  boolean loadingImage = false;

  public SikulixFileChooser(Frame parent) {
    _parent = parent;
  }

  public SikulixFileChooser(Frame parent, boolean accessingAsFile) {
    _parent = parent;
    this.accessingAsFile = accessingAsFile;
  }

  private boolean isGeneric() {
    return (_parent.getWidth() == 1 && _parent.getHeight() == 1);
  }

  public File show(String title) {
    File ret = showFileChooser(title, LOAD, DIRSANDFILES);
    return ret;
  }

  public File load() {
    String type = "Sikuli Script (*.sikuli, *.skl)";
    String title = "Open a Sikuli Script";
    File ret = showFileChooser(title, LOAD, DIRSANDFILES, new SikulixFileFilter(type, "o"));
    return ret;
  }

  public File save() {
    String type = "Sikuli Script (*.sikuli)";
    String title = "Save a Sikuli Script";
    File ret = showFileChooser(title, SAVE, DIRS, new SikulixFileFilter(type, "s"));
    return ret;
  }

  public File export() {
    String type = "Sikuli packed Script (*.skl)";
    String title = "Export as Sikuli packed Script";
    File ret = showFileChooser(title, SAVE, FILES, new SikulixFileFilter(type, "e"));
    return ret;
  }

  public File loadImage() {
    loadingImage = true;
    File ret = showFileChooser("Load Image File", LOAD, FILES,
            new FileNameExtensionFilter("Image files (jpg, png)", "jpg", "jpeg", "png"));
    return ret;
  }

  private File showFileChooser(String title, int mode, int selectionMode, Object... filters) {
    String last_dir = PreferencesUser.getInstance().get("LAST_OPEN_DIR", "");
    Debug.log(3,"showFileChooser: %s at %s", title.split(" ")[0], last_dir);
    JFileChooser fchooser = null;
    File fileChoosen = null;
    while (true) {
      fchooser = new JFileChooser();
      if (!last_dir.isEmpty()) {
        fchooser.setCurrentDirectory(new File(last_dir));
      }
      fchooser.setSelectedFile(null);
      fchooser.setDialogTitle(title);
      boolean shouldTraverse = false;
      String btnApprove = "Select";
      if (isGeneric()) {
        fchooser.setFileSelectionMode(DIRSANDFILES);
        fchooser.setAcceptAllFileFilterUsed(true);
        shouldTraverse = true;
      } else {
        if (Settings.isMac() && Settings.isJava7() && selectionMode == DIRS) {
          selectionMode = DIRSANDFILES;
        }
        fchooser.setFileSelectionMode(selectionMode);
        if (mode == FileDialog.SAVE) {
          fchooser.setDialogType(JFileChooser.SAVE_DIALOG);
          btnApprove = "Save";
        }
        if (filters.length == 0) {
          fchooser.setAcceptAllFileFilterUsed(true);
          shouldTraverse = true;
        } else {
          fchooser.setAcceptAllFileFilterUsed(false);
          for (Object filter : filters) {
            if (filter instanceof SikulixFileFilter) {
              fchooser.addChoosableFileFilter((SikulixFileFilter) filter);
            } else {
              fchooser.setFileFilter((FileNameExtensionFilter) filter);
              shouldTraverse = true;
            }
          }
        }
      }
      if (shouldTraverse && Settings.isMac()) {
        fchooser.putClientProperty("JFileChooser.packageIsTraversable", "always");
      }
      if (fchooser.showDialog(_parent, btnApprove) != JFileChooser.APPROVE_OPTION) {
        return null;
      }
      fileChoosen = fchooser.getSelectedFile();
      // folders must contain a valid scriptfile
      if (!isGeneric() && mode == FileDialog.LOAD && !isValidScript(fileChoosen)) {
        Sikulix.popError("Folder not a valid SikuliX script\nTry again.");
        last_dir = fileChoosen.getParentFile().getAbsolutePath();
        continue;
      }
      break;
    }
    String lastDir = fileChoosen.getParent();
    if (null == lastDir) {
      lastDir = fileChoosen.getAbsolutePath();
    }
    PreferencesUser.getInstance().put("LAST_OPEN_DIR", lastDir);
    return fileChoosen;
  }

  private boolean isValidScript(File f) {
    String[] endings = new String[]{".py", ".rb", ".js"};
    String fName = f.getName();
    if (loadingImage || fName.endsWith(".skl")) {
      return true;
    }
    if (fName.endsWith(".sikuli")) {
      fName = fName.substring(0, fName.length() - 7);
    }
    boolean valid = false;
    for (String ending : endings) {
      if (new File(f, fName + ending).exists()) {
        return true;
      }
    }
    return false;
  }

  private static boolean isExt(String fName, String givenExt) {
    int i = fName.lastIndexOf('.');
    if (i > 0) {
      if (fName.substring(i + 1).toLowerCase().equals(givenExt)) {
        return true;
      }
    }
    return false;
  }

  class SikulixFileFilter extends FileFilter {

    private String _type, _desc;

    public SikulixFileFilter(String desc, String type) {
      _type = type;
      _desc = desc;
    }

    @Override
    public boolean accept(File f) {
      if ("o".equals(_type) && (isExt(f.getName(), "sikuli") || isExt(f.getName(), "skl"))) {
        return true;
      }
      if ("s".equals(_type) && isExt(f.getName(), "sikuli")) {
        return true;
      }
      if ("e".equals(_type)) {
        if (isExt(f.getName(), "skl")) {
          return true;
        }
        if (Settings.isMac() && isExt(f.getName(), "sikuli")) {
          return false;
        }
      }
      if (f.isDirectory()) {
        return true;
      }
      return false;
    }

    @Override
    public String getDescription() {
      return _desc;
    }
  }
}
