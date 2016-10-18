package com.dynatrace.diagnostics.plugins.jmx.datacollection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanFeatureInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import java.util.logging.Logger;

import com.dynatrace.diagnostics.plugins.jmx.variableholder.DataStats;
import com.dynatrace.diagnostics.plugins.jmx.variableholder.DateTime;
import com.dynatrace.diagnostics.plugins.jmx.variableholder.DataTimeHolder;

public class SubCommand {

    private final ArrayList<DataStats> statsList = new ArrayList<DataStats>();
    
    DataTimeHolder ad = new DataTimeHolder();
    
	private ArrayList<DataStats> test;
    
    private static final Logger log = Logger.getLogger(SubCommand.class.getName());

    public boolean isFeatureInfo(MBeanFeatureInfo infos[], String cmd) {
        return getFeatureInfo(infos, cmd) != null;
    }

    protected String addNameToBuffer(StringBuffer buffer, String indent, String name) {
        if (name == null || name.length() == 0) {
            return indent;
        } else {
            buffer.append(indent);
            buffer.append(name);
            buffer.append(":\n");
            return (new StringBuilder()).append(indent).append(" ").toString();
        }
    }

    protected StringBuffer recurseCompositeData(StringBuffer buffer, String indent, String name, CompositeData data) {
        indent = addNameToBuffer(buffer, indent, name);
        for (Iterator<?> i = data.getCompositeType().keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            Object o = data.get(key);
            if (o instanceof CompositeData) {
                recurseCompositeData(buffer, (new StringBuilder()).append(indent).append(" ").toString(), key, (CompositeData) o);
            } else if (o instanceof TabularData) {
                recurseTabularData(buffer, indent, key, (TabularData) o);
            } else {
                buffer.append(indent);
                buffer.append(key);
                buffer.append(": ");
                buffer.append(o);
                buffer.append("\n");
            }
        }
        return buffer;
    }

    protected StringBuffer recurseTabularData(StringBuffer buffer, String indent, String name, TabularData data) {
        addNameToBuffer(buffer, indent, name);
        Collection<?> c = data.values();
        for (Iterator<?> i = c.iterator(); i.hasNext();) {
            Object obj = i.next();
            if (obj instanceof CompositeData) {
                recurseCompositeData(buffer, (new StringBuilder()).append(indent).append(" ").toString(), "", (CompositeData) obj);
            } else if (obj instanceof TabularData) {
                recurseTabularData(buffer, indent, "", (TabularData) obj);
            } else {
                buffer.append(obj);
            }
        }
        return buffer;
    }

    public MBeanFeatureInfo getFeatureInfo(MBeanFeatureInfo infos[], String cmd) {
        int index = cmd.indexOf('=');
        String name = index <= 0 ? cmd : cmd.substring(0, index);
        for (int i = 0; i < infos.length; i++) {
            if (infos[i].getName().equals(name)) {
                return infos[i];
            }
        }
        return null;
    }

    public ArrayList<DataStats> doSubCommand(MBeanServerConnection mbsc, ObjectInstance instance, String subCommand, String BeanName)
            throws Exception {
        MBeanAttributeInfo attributeInfo[] = mbsc.getMBeanInfo(instance.getObjectName()).getAttributes();
        MBeanOperationInfo operationInfo[] = mbsc.getMBeanInfo(instance.getObjectName()).getOperations();
        Object result = null;
        if (subCommand != null) {
            AttributeOperation ao = new AttributeOperation();
            if (Character.isUpperCase(subCommand.charAt(0))  && !isFeatureInfo(operationInfo, subCommand) && isFeatureInfo(attributeInfo, subCommand)) {
                    result = ao.doAttributeOperation(mbsc, instance, subCommand);
            } else {
            	result = ao.doAttributeOperation(mbsc, instance, subCommand);
            }
            if (result instanceof CompositeData) {
                result = recurseCompositeData(new StringBuffer("\n"), "", "", (CompositeData) result);
                
            } else if (result instanceof TabularData) {
                result = recurseTabularData(new StringBuffer("\n"), "", "", (TabularData) result);
                
            } else if (result instanceof String[]) {
                String strs[] = (String[]) (String[]) result;
                StringBuffer buffer = new StringBuffer("\n");
                for (int i = 0; i < strs.length; i++) {
                    buffer.append(strs[i]);
                    buffer.append("\n");
                }
                result = buffer;
            }
                if (result != null) {
                    String str = result.toString().replace("\n", " ");
                    String rt = str.replace(": ", " ");
                    String[] tokens = rt.split(" ");
                    DataStats stats = new DataStats();
                    stats.setbeanName(BeanName);
                    stats.setMBeanName(instance.toString().replaceAll(" ", ""));
                    stats.setAttName(subCommand);
                    long time = DateTime.getDateTime();
                    stats.settimeDateConverted(time);
                    int p = 0;
                    int n = 1;
                    for (String t : tokens) {

                        if (n == 0) {
                            stats = new DataStats();
                            stats.setMBeanName(instance.toString());
                            stats.setAttName(subCommand);
                            time = DateTime.getDateTime();
                            stats.settimeDateConverted(time);
                            n++;
                        }
                        if (t.equals("")) {
                            //Do nothing. We don't want empty space being saved off.
                        } else {
                        	log.info(t);
                            if (p == 0) {

                                try {
                                    try {
                                        Long.parseLong(t);
                                    } catch (Exception pp) {
                                        Float.parseFloat(t);
                                    }
                                    stats.setSubAttName("Count");
                                    if (subCommand.equalsIgnoreCase("Uptime")) {
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        Float val = Float.parseFloat(t);
                                        val = (((val / 1000) / 60));
                                        String r = df.format(val);
                                        stats.setValue(r);
                                    } else {
                                        if (Float.parseFloat(t) < 0) {
                                            t = "0";
                                        }
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        float f = Float.parseFloat(t);
                                        String r = df.format(f);
                                        stats.setValue(r);
                                    }
                                } catch (Exception e) {
                                    stats.setSubAttName(t);
                                }
                                if (p == 0) {
                                    statsList.add(stats);
                                    ad.addServerStatsList(statsList);
                                    statsList.clear();
                                }
                                p++;
                            } else if (p == 1) {
                                if (subCommand.equalsIgnoreCase("Uptime")) {
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    Float val = Float.parseFloat(t);
                                    val = (((val / 1000) / 60) / 60);
                                    String r = df.format(val);
                                    stats.setValue(r);
                                } else {
                                    if (Float.parseFloat(t) < 0) {
                                        t = "0";
                                    }
                                    DecimalFormat df = new DecimalFormat("#.##");

                                    float f = Float.parseFloat(t);
                                    String r = df.format(f);
                                    stats.setValue(r);
                                    p = 0;
                                    n = 0;
                                }
                            }
                        }
                    }

                }
        }
        test = ad.getArrayList();
        return test;
    }
}
