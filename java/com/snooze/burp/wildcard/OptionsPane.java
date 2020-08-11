package com.snooze.burp.wildcard;

import burp.BurpExtender;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OptionsPane {

    public JPanel panel1;
    private JTextField textField1;
    private JList list_shown;
    private JList list_hidden;
    private JList list_all;
    private JButton btn_save;
    private JButton btn_refresh;
    private JButton btn_add;
    private JButton btn_del;
    private TabManager tabManager;
    private JTabbedPane tabbedPane;
    private OptionsTab optionsTab;
    private boolean init;

    public OptionsPane(JTabbedPane tabbedPane, OptionsTab optionsTab) {
        this.tabbedPane = tabbedPane;
        this.optionsTab = optionsTab;
    }

    public void setup(){

        tabManager = new TabManager();
        Settings.tab_manager = tabManager;

        // Automagically refresh tabs
        panel1.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
//                loadConf();
//                //hideTabs();
//                BurpExtender.getInstance().stdout("AncestorAdded");
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
//                loadConf();
//                //hideTabs();
//                BurpExtender.getInstance().stdout("AncestorRemoved");
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                if (!init) {
                    loadConf();
                    hideTabs();
                    BurpExtender.getInstance().stdout("AncestorMoved");

                    init = false;
                }
            }
        });

        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //BurpExtender.getInstance().stdout("[Add click]");

                int i = list_shown.getSelectedIndex();
                tabManager.hide(i);
                saveConf();
                hideTabs();
            }
        });

        btn_del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //BurpExtender.getInstance().stdout("[Del click]");

                int i = list_hidden.getSelectedIndex();
                tabManager.show(i);
                saveConf();
                hideTabs();
            }
        });

        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.tab_title = textField1.getText();
                BurpExtender.getInstance().changeName();
                optionsTab.reloadTab();
                hideTabs();
            }
        });

        // Manually refresh tabs
        btn_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAll();
                reloadTabs();
            }
        });

    }

    public ArrayList<String> getTabs(){
        ArrayList<String> tabs = new ArrayList<String>();

        JTabbedPane parent = (JTabbedPane) tabbedPane.getParent();

        BurpExtender.getInstance().stdout("[Getting tabs]");

        if (parent!=null) {
            for (int i = 0; i < parent.getTabCount(); i++) {
                String tabName = null;
                try {
                    tabName = parent.getTitleAt(i);
                    BurpExtender.getInstance().stdout(tabName);
                    if (!tabName.equals(Settings.tab_title)) {
                        tabs.add(tabName);
                    }
                } catch (Exception ex) {
                    // do nothing
                }
            }

            return tabs;
        } else {
            BurpExtender.getInstance().stdout("Parent is null");
            return null;
        }
    }

    public void reloadTabs(){
        ArrayList<String> tabs = getTabs();
        if (tabs != null){
            tabManager.populate(tabs);
        }
    }

    public void hideTabs(){
        ArrayList<String> tabs = new ArrayList<String>();

        //BurpExtender.getInstance().stdout("< hideTabs>");

        JTabbedPane parent = (JTabbedPane) tabbedPane.getParent();

        if (parent!=null) {
            // Hide
            for (int i = 0; i < parent.getTabCount(); i++) {
                String tabName = null;
                try {
                    tabName = parent.getTitleAt(i);

                    if (tabManager.hidden.contains(tabName)){
                        //BurpExtender.getInstance().stdout("[Hiding] "+tabName);

                        Component c = parent.getComponentAt(i);
                        tabbedPane.addTab(tabName, c);

                    } else {
                        //BurpExtender.getInstance().stdout("[Not hidden] "+tabName);
                    }

                } catch (Exception ex) {
                    // do nothing
                }
            }

            //BurpExtender.getInstance().stdout("-- --");
            //BurpExtender.getInstance().stdout(tabManager.shown.toString());
            //BurpExtender.getInstance().stdout(tabManager.hidden.toString());
            //BurpExtender.getInstance().stdout("Tabs: "+tabbedPane.getTabCount());

            //Unhide
            ArrayList<Component> cs = new ArrayList<>();
            ArrayList<String> cs_str = new ArrayList<>();

            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                String tabName = null;
                try {
                    tabName = tabbedPane.getTitleAt(i);
                    //BurpExtender.getInstance().stdout(tabName);

                    if (tabManager.shown.contains(tabName)){
                        //BurpExtender.getInstance().stdout("[Showing] "+tabName);

                        cs.add(tabbedPane.getComponentAt(i));
                        cs_str.add(tabName);

                    } else {
                        //BurpExtender.getInstance().stdout("[Not shown] "+tabName);
                    }

                } catch (Exception ex) {
                    //BurpExtender.getInstance().stdout("Exception caught");
                    //ex.printStackTrace();
                }
            }
            for (int i=0; i<cs.size(); i++){
                parent.addTab(cs_str.get(i),cs.get(i));
            }
        } else {
            BurpExtender.getInstance().stdout("Parent is null");
        }

        //BurpExtender.getInstance().stdout("</hideTabs>");
    }

    public void saveConf(){
        BurpExtenderCallbacks.callbacks.saveExtensionSetting("com.snooze.burp.wildcard.hidden", Settings.tab_manager.toString());
    }

    public void loadConf(){

        String hidden = BurpExtenderCallbacks.callbacks.loadExtensionSetting("com.snooze.burp.wildcard.hidden");
        Settings.tab_manager.fromString(hidden,getTabs());

        textField1.setText(Settings.tab_title);

        list_all.setModel(tabManager.model_all);
        list_hidden.setModel(tabManager.model_hidden);
        list_shown.setModel(tabManager.model_shown);
    }

    public void showAll(){
        tabManager.showAll();
        hideTabs();
    }
}
