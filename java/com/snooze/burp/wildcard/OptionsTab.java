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
        BurpExtender.getInstance().stdout(Settings.tab_title);
        return Settings.tab_title;
    }

    @Override
    public Component getUiComponent() {
//        return new JPanel();
        return optionsTabPanel.panel1;
    }
}
