package com.dynatrace.diagnostics.plugins.jmx.variableholder;

import java.util.*;

public class DataTimeHolder {

    private ArrayList<DataStats> holder = new ArrayList<DataStats>();

    public synchronized ArrayList<DataStats> getArrayList() {
        return holder;
    }

    public synchronized void setArrayList(ArrayList<DataStats> obj) throws Exception {
        holder = obj;
    }

    public synchronized void addServerStats(DataStats parm) {
        holder.add(parm);
    }

    public synchronized void addServerStatsList(ArrayList<DataStats> obj) {
        holder.addAll(obj);
    }

    public void Clearvalue() throws Exception {
        holder.clear();
    }
}