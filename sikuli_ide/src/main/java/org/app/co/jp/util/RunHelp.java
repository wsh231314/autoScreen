package org.app.co.jp.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sikuli.basics.FileManager;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Runner;
import org.sikuli.script.Sikulix;
import org.sikuli.scriptrunner.IScriptRunner;
import org.sikuli.scriptrunner.ScriptingSupport;

public class RunHelp {

    private Thread _runningThread = null;

    public RunHelp() {
    	super();
    }

    public void runCurrentScript(String strScriptPath, String strEvidencePath) {
        
        final IScriptRunner[] srunners = new IScriptRunner[]{null};
        
        IScriptRunner srunner = ScriptingSupport.getRunner(null, "jython");
        
        // create the evidence folder TODO
        Date now = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        String ymd = date.format(now);
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");
        String hhmmss = time.format(now);
        strEvidencePath = strEvidencePath.concat("//").concat(ymd).concat("//").concat(ymd).concat(hhmmss).concat("//");
        File eviFolder = new File(strEvidencePath);
        if (!eviFolder.exists()) {
        	eviFolder.mkdirs();
        }
        String absoPath = FileManager.normalizeAbsolute(eviFolder.getAbsolutePath(), true).substring(1);
        
        addScriptCode(srunner);
        srunners[0] = srunner;
        
        final SubRun doRun = new SubRun(srunners, strScriptPath, absoPath);
        _runningThread = new Thread(doRun);
        _runningThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (System.out.checkError()) {
                    Sikulix.popError("System.out is broken (console output)!"
                            + "\nYou will not see any messages anymore!"
                            + "\nSave your work and restart the IDE!", "Fatal Error");
                }
                doRun.hasFinished(true);
                doRun.afterRun();
            }
        });
        _runningThread.start();
    }

    private class SubRun implements Runnable {
        private boolean finished = false;
        private IScriptRunner[] srunners = null;
        private String scriptFile = null;
        
        private String evidencePath = "";

        public SubRun(IScriptRunner[] srunners, String scriptFile, String evidencePath) {
        	this.evidencePath = evidencePath;
            this.srunners = srunners;
            this.scriptFile = scriptFile;
        }

        @Override
        public void run() {
            try {
            	File fScript = Runner.getScriptFile(new File(scriptFile));
            	fScript = new File(FileManager.normalizeAbsolute(fScript.getPath(), true));
            	ImagePath.setBundlePath(fScript.getParent());
                srunners[0].runScript(fScript, null, new String[]{evidencePath}, null);
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
            hasFinished(true);
            afterRun();
        }

        public synchronized boolean hasFinished(boolean state) {
            if (state) {
                finished = true;
            }
            return finished;
        }

        public void afterRun() {
            srunners[0].close();
            srunners[0] = null;
            Sikulix.cleanUp(0);
            _runningThread = null;
        }
    }

    protected void addScriptCode(IScriptRunner srunner) {
        srunner.execBefore(null);
        srunner.execBefore(new String[]{"Settings.setShowActions(Settings.FALSE)"});
    }
}
