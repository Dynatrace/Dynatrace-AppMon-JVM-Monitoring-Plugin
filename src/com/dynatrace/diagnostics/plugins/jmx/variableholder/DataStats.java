package com.dynatrace.diagnostics.plugins.jmx.variableholder;

public class DataStats {

    public Long gettimeDateConverted() {
        return timeDateConverted;
    }

    public void settimeDateConverted(Long timeDateConverted) {
        this.timeDateConverted = timeDateConverted;
    }

    public String getMBeanName() {
        return mbeanName;
    }

    public void setMBeanName(String mbeanName) {
        this.mbeanName = mbeanName;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getSubAttName() {
        return subAttName;
    }

    public void setSubAttName(String subAttName) {
        this.subAttName = subAttName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getbeanName() {
        return beanName;
    }

    public void setbeanName(String beanName) {
        this.beanName = beanName;

    }

    Long timeDateConverted;
    String mbeanName = null;
    String attName;
    String subAttName;
    String value;
    String beanName;

    @Override
    public String toString() {
        return mbeanName + " " + attName + " " + value + " " + timeDateConverted;
    }

    public DataStats(String input) {
        super();
        parse(input);
    }

    public DataStats() {
        super();
    }

    public void parse(String stuff) {
        String[] parts = stuff.split(" ");
        mbeanName = parts[0];
        attName = parts[1];
        value = parts[2];
        timeDateConverted = Long.parseLong(parts[3]);
    }
}

