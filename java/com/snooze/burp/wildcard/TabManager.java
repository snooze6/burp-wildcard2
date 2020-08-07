package com.snooze.burp.wildcard;

import burp.BurpExtender;

import java.util.ArrayList;
import java.util.Arrays;

public class TabManager {
    ArrayList<String> shown, hidden, all;
    StringModel model_shown, model_hidden, model_all;

    public static String SEPARATOR = ",";

    public TabManager() {
        this.setShown(new ArrayList<String>());
        this.setHidden(new ArrayList<String>());
        this.setAll(new ArrayList<String>());
    }

    public TabManager(ArrayList<String> shown, ArrayList<String> hidden, ArrayList<String> all) {
        this.setShown(shown);
        this.setHidden(hidden);
        this.setAll(all);
    }

    public void populate(ArrayList<String> tabs){
        boolean notify = false;
        ArrayList<String> newtabs = new ArrayList<String>();

        for (String tab:tabs){
            boolean found = false;

            for (String saved_tab:all){
                if (saved_tab.equals(tab)){
                    found = true;
                    break;
                }
            }

            if (!found){
                this.all.add(tab);
                this.shown.add(tab);
                notify = true;
            }
        }

        if (notify) {
            this.model_shown.notifyListeners();
            this.model_all.notifyListeners();

            for (String tab:newtabs){
                BurpExtender.getInstance().stdout("[New]: "+tab);
            }
        }
    }

    public void reload(){
        ArrayList<String> arr1 = new ArrayList<String>(Arrays.asList("1","2","3"));
        ArrayList<String> arr2 = new ArrayList<String>(Arrays.asList("A","B","C"));
        ArrayList<String> arr3 = new ArrayList<String>();
        {
            for (String s:arr1){
                arr3.add(s);
            }
            for (String s:arr2){
                arr3.add(s);
            }
        }
        this.setShown(arr1);
        this.setHidden(arr2);
        this.setAll(arr3);
    }

    public void refresh(ArrayList<String> tabs){
        for (String tab:tabs){
          if (!tabExists(tab)){
              all.add(tab);
          }
        }
    }

    private boolean tabExists(String tab){
        for (String s:all){
            if (s.equals(tab)){
                return true;
            }
        }
        return false;
    }

    public void hide(int i){
        String s = shown.get(i);
        hidden.add(s);
        shown.remove(i);

        BurpExtender.getInstance().stdout("Hiding: "+s);

        model_hidden.notifyListeners();
        model_shown.notifyListeners();
    }

    public void show(int i){
        String s = hidden.get(i);
        shown.add(s);
        hidden.remove(i);

        BurpExtender.getInstance().stdout("Show: "+s);

        model_hidden.notifyListeners();
        model_shown.notifyListeners();
    }


    public ArrayList<String> getShown() {
        return shown;
    }

    public ArrayList<String> getHidden() {
        return hidden;
    }

    public ArrayList<String> getAll() {
        return all;
    }

    public TabManager setShown(ArrayList<String> shown) {
        this.shown = shown;
        model_shown = new StringModel(shown);
        return this;
    }

    public TabManager setHidden(ArrayList<String> hidden) {
        this.hidden = hidden;
        model_hidden = new StringModel(hidden);
        return this;
    }

    public TabManager setAll(ArrayList<String> all) {
        this.all = all;
        model_all = new StringModel(all);
        return this;
    }

    @Override
    public String toString() {
        String s = "";
        if (this.hidden.size()>0) {
            for (String tab : this.hidden) {
                s += tab + SEPARATOR;
            }
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public void fromString(String s, ArrayList<String> all){
        if (all!=null) {
            this.setAll(all);

            ArrayList<String> hid = new ArrayList<String>();
            String[] split = s.split(SEPARATOR);
            for (String sp : split) {
                hid.add(sp);
            }
            this.setHidden(hid);

            ArrayList<String> show = new ArrayList<String>();
            for (String tab : all) {
                boolean found = false;
                for (String hidden_tab : hidden) {
                    if (hidden_tab.equals(tab)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    show.add(tab);
                }
            }
            this.setShown(show);
        }
    }
}
