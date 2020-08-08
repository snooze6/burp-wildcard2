package com.snooze.burp.wildcard;

import burp.BurpExtender;
import burp.ITab;

import javax.swing.*;
import java.awt.*;

public class OptionsTab extends JPanel implements ITab {

    OptionsPane optionsTabPanel;
    JTabbedPane tabbedPane;

    public OptionsTab() {
        tabbedPane = new JTabbedPane();

        optionsTabPanel = new OptionsPane(tabbedPane,this);
        optionsTabPanel.setup();

        tabbedPane.addTab(Settings.tab_title,optionsTabPanel.panel1);
    }

    @Override
    public String getTabCaption() {
        //BurpExtender.getInstance().stdout(Settings.tab_title);
        return Settings.tab_title;
    }

    @Override
    public Component getUiComponent() {
//        return new JPanel();
        return tabbedPane;
    }

    public void reloadTab(){
        BurpExtender.getInstance().stdout("< First>");
        printTabs();
        BurpExtender.getInstance().stdout("</First>");

        if (tabbedPane.getTabCount()>1) {

            tabbedPane.remove(optionsTabPanel.panel1);

            for (int i = 0; i < tabbedPane.getTabCount() - 1; i++) {
                Component a = tabbedPane.getComponentAt(0);
                Component b = tabbedPane.getComponentAt(i);
                tabbedPane.setComponentAt(0, b);
                tabbedPane.setComponentAt(i, a);
            }

            tabbedPane.addTab(Settings.tab_title, optionsTabPanel.panel1);

            Component a = tabbedPane.getComponentAt(0);
            Component b = tabbedPane.getComponentAt(tabbedPane.getTabCount());
            tabbedPane.setComponentAt(0, b);
            tabbedPane.setComponentAt(tabbedPane.getTabCount(), a);
        }

        BurpExtender.getInstance().stdout("< Second>");
        printTabs();
        BurpExtender.getInstance().stdout("</Second>");
    }

    public void printTabs(){
        for (int i=0; i<tabbedPane.getTabCount(); i++){
            BurpExtender.getInstance().stdout(tabbedPane.getTitleAt(i));
        }
    }

    public void showAll(){
        this.optionsTabPanel.showAll();
    }
}
