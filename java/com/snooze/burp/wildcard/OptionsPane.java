package com.snooze.burp.wildcard;

import burp.BurpExtender;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
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

    public OptionsPane() {
    }

    public void setup(){

        tabManager = new TabManager();
        Settings.tab_manager = tabManager;

        // Automagically refresh tabs
        panel1.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadConf();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                loadConf();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                loadConf();
            }
        });

        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BurpExtender.getInstance().stdout("[Add click]");

                int i = list_shown.getSelectedIndex();
                tabManager.hide(i);
                saveConf();
                hideTabs();
            }
        });

        btn_del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BurpExtender.getInstance().stdout("[Add click]");

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
            }
        });

        // Manually refresh tabs
        btn_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadTabs();
            }
        });

    }

    public ArrayList<String> getTabs(){
        ArrayList<String> tabs = new ArrayList<String>();

        JTabbedPane parent = (JTabbedPane) panel1.getParent();

        if (parent!=null) {
            for (int i = 0; i < parent.getTabCount(); i++) {
                String tabName = null;
                try {
                    tabName = parent.getTitleAt(i);
                    tabs.add(tabName);
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

    }

    public void saveConf(){
        BurpExtenderCallbacks.callbacks.saveExtensionSetting("com.snooze.burp.wildcard.hidden", Settings.tab_manager.toString());
    }

    public void loadConf(){

        String hidden = BurpExtenderCallbacks.callbacks.loadExtensionSetting("com.snooze.burp.wildcard.hidden");
        Settings.tab_manager.fromString(hidden,getTabs());

        textField1.setText(Settings.tab_title);

        BurpExtender.getInstance().stdout("Hidden: "+hidden);

        list_all.setModel(tabManager.model_all);
        list_hidden.setModel(tabManager.model_hidden);
        list_shown.setModel(tabManager.model_shown);

//        reloadTabs();
    }
}
