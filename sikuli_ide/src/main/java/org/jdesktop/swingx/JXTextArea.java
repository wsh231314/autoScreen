package org.jdesktop.swingx;

import org.jdesktop.beans.JavaBean;
import org.jdesktop.swingx.prompt.BuddySupport;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JTextArea}, with integrated support for prompts.
 *
 * @author Peter Weishapl <petw@gmx.net>
 * @see PromptSupport
 * @see BuddySupport
 */
@JavaBean
public class JXTextArea extends JTextArea {
    public JXTextArea() {
        this(null);
    }

    public JXTextArea(String promptText) {
        this(promptText, null);
    }

    public JXTextArea(String promptText, Color promptForeground) {
        this(promptText, promptForeground, null);
    }

    public JXTextArea(String promptText, Color promptForeground,
                      Color promptBackground) {
        PromptSupport.init(promptText, promptForeground, promptBackground,
                this);
    }

    /**
     * @see PromptSupport#getFocusBehavior(javax.swing.text.JTextComponent)
     */
    public FocusBehavior getFocusBehavior() {
        return PromptSupport.getFocusBehavior(this);
    }

    /**
     * @see PromptSupport#getPrompt(javax.swing.text.JTextComponent)
     */
    public String getPrompt() {
        return PromptSupport.getPrompt(this);
    }

    /**
     * @see PromptSupport#getForeground(javax.swing.text.JTextComponent)
     */
    public Color getPromptForeground() {
        return PromptSupport.getForeground(this);
    }

    /**
     * @see PromptSupport#getForeground(javax.swing.text.JTextComponent)
     */
    public Color getPromptBackground() {
        return PromptSupport.getBackground(this);
    }

    /**
     * @see PromptSupport#getFontStyle(javax.swing.text.JTextComponent)
     */
    public Integer getPromptFontStyle() {
        return PromptSupport.getFontStyle(this);
    }

    /**
     * @see PromptSupport#getFocusBehavior(javax.swing.text.JTextComponent)
     */
    public void setFocusBehavior(FocusBehavior focusBehavior) {
        PromptSupport.setFocusBehavior(focusBehavior, this);
    }

    /**
     * @see PromptSupport#setPrompt(String, javax.swing.text.JTextComponent)
     */
    public void setPrompt(String labelText) {
        PromptSupport.setPrompt(labelText, this);
    }

    /**
     * @see PromptSupport#setForeground(Color, javax.swing.text.JTextComponent)
     */
    public void setPromptForeground(Color promptTextColor) {
        PromptSupport.setForeground(promptTextColor, this);
    }

    /**
     * @see PromptSupport#setBackground(Color, javax.swing.text.JTextComponent)
     */
    public void setPromptBackround(Color promptTextColor) {
        PromptSupport.setBackground(promptTextColor, this);
    }

    /**
     * @see PromptSupport#setFontStyle(Integer, javax.swing.text.JTextComponent)
     */
    public void setPromptFontStyle(Integer fontStyle) {
        PromptSupport.setFontStyle(fontStyle, this);
    }
}
