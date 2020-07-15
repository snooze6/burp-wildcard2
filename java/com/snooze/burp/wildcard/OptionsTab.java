package com.snooze.burp.wildcard;

import burp.BurpExtender;
import burp.ITab;

import javax.swing.*;
import java.awt.*;

public class OptionsTab extends JPanel implements ITab {

    OptionsPane optionsTabPanel;

    public OptionsTab() {
        optionsTabPanel = new OptionsPane();
        optionsTabPanel.setup();
    }

    @Override
    public String getTabCaption() {
        return "aaa";
    }

    @Override
    public Component getUiComponent() {
        return optionsTabPanel.panel1;
    }
}
