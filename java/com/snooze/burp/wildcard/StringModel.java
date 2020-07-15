package com.snooze.burp.wildcard;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

public class StringModel implements ListModel {

    ArrayList<String> data;
    ArrayList<ListDataListener> listeners = new ArrayList<>();

    public StringModel(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Object getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void add(String s){
        data.add(s);
        notifyListeners();
    }

    public void del(int i){
        data.remove(i);
        notifyListeners();
    }

    public void notifyListeners() {
        // no attempt at optimziation
        ListDataEvent le = new ListDataEvent(data, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
        for (int i = 0; i < listeners.size(); i++) {
            ((ListDataListener) listeners.get(i)).contentsChanged(le);
        }
    }
}
