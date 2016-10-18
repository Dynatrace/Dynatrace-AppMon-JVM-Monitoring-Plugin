package com.dynatrace.diagnostics.plugins.jmx.datacollection;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;

import java.util.logging.Logger;

public class Options {

	private static final Logger log = Logger.getLogger(Options.class.getName());

    protected String[] listOptions(MBeanServerConnection mbsc, ObjectInstance instance, String[] aattributes)
            throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {

        int p = 0;
        MBeanInfo info = mbsc.getMBeanInfo(instance.getObjectName());
        MBeanAttributeInfo attributes[] = info.getAttributes();
        String getAtt[] = new String[attributes.length];
       if (attributes.length > 0) {
            for (int i = 0; i < attributes.length; i++) {
                boolean b = false;
                int length = aattributes.length;
                b = false;
                for (int m = 0; m < length; m++) {
                    b = attributes[i].toString().indexOf(aattributes[m].toString()) > 0;
                    if (b) {
                        break;
                    }
                }
                    if (b) {
                    	try{
                    		getAtt[p] = attributes[i].getName();
                    	}catch(Exception e){
                    		log.info("Issue setting attributes: " + e);
                    		continue;
                    	}
                        p++;	
                    	
                    }else {
                    	getAtt[p] = null;
                        p++;
                    }
            }
        }
        return getAtt;
    }
}
