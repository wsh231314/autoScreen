/*
 * Copyright (c) 2010-2016, Sikuli.org, sikulix.com
 * Released under the MIT License.
 *
 */

package org.sikuli.util;

/**
 * This interface provides script language specific methods which are non a part
 * of ScriptRunner classes.
 *
 * These methods may be used both in case of a script running from IDE or from
 * sikulixapi library without IDE.
 */
public interface JLangHelperInterface {
    /**
     * Run callback for observers.
     *
     * @param args
     *            is array for two elements. First is a callback object. Second
     *            is an event.
     * @return true if the callback was ran correctly. False in other cases.
     */
    public boolean runObserveCallback(Object[] args);
}
