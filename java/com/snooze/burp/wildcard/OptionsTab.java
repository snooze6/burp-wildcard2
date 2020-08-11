package com.snooze.burp.wildcard;

import burp.BurpExtender;
import burp.ITab;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

        tabbedPane.remove(optionsTabPanel.panel1);

        ArrayList<Component> cs = new ArrayList<>();
        ArrayList<String> cs_str = new ArrayList<>();

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            cs.add(tabbedPane.getComponentAt(i));
            cs_str.add(tabbedPane.getTitleAt(i));
        }

        //BurpExtender.getInstance().stdout("Tabs: "+cs_str.toString());

        for (int i = 0; i < cs.size(); i++) {
            tabbedPane.remove(cs.get(i));
            //BurpExtender.getInstance().stdout("Removing: "+cs_str.get(i));
        }

        tabbedPane.add(Settings.tab_title, optionsTabPanel.panel1);

        for (int i = 0; i <cs.size(); i++) {
            tabbedPane.add(cs_str.get(i), cs.get(i));
            //BurpExtender.getInstance().stdout("Adding: "+cs_str.get(i));
        }
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
