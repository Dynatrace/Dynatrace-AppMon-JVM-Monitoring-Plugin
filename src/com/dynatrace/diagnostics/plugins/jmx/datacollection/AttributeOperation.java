package com.dynatrace.diagnostics.plugins.jmx.datacollection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;

import java.util.logging.Logger;

public class AttributeOperation {

	private static final Logger log = Logger.getLogger(AttributeOperation.class.getName());

	protected static final Pattern CMD_LINE_ARGS_PATTERN = Pattern.compile("^([^=]+)(?:(?:\\=)(.+))?$");
	
    public Object doAttributeOperation(MBeanServerConnection mbsc, ObjectInstance instance, String command)
            throws Exception {
    	String cmd;
    	Matcher m = AttributeOperation.CMD_LINE_ARGS_PATTERN.matcher(command);
    	if (m == null || !m.matches()) {
        	log.warning("Failed to parse the command: " + command.toString());
            return null;
        }
        cmd = m.group(1);
    	try{
            return mbsc.getAttribute(instance.getObjectName(), cmd);
    	}catch(Exception e){
    		log.warning("Issue getting attribute: " + e);
    		return null;
    	}
    }
}
