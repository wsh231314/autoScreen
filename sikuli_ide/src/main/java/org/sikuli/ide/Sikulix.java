/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */

package org.sikuli.ide;

import javax.swing.*;
import java.security.CodeSource;

public class Sikulix {

    public static void main(String[] args) {
        String jarName = "";

        CodeSource codeSrc = SikuliIDE.class.getProtectionDomain().getCodeSource();
        if (codeSrc != null && codeSrc.getLocation() != null) {
            jarName = codeSrc.getLocation().getPath();
        }

        if (jarName.contains("sikulixsetupIDE")) {
            JOptionPane.showMessageDialog(null, "Not useable!\nRun setup first!",
                    "sikulixsetupIDE", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
//    Screen.ignorePrimaryAtCapture = true;
//    Settings.TraceLogs = true;
        SikuliIDE.runAlone(args);
    }
}
