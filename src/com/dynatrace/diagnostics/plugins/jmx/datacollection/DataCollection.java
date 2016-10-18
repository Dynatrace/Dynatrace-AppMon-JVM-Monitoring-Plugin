package com.dynatrace.diagnostics.plugins.jmx.datacollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.util.logging.Logger;
import com.dynatrace.diagnostics.plugins.jmx.variableholder.DataStats;

public class DataCollection {

	private static final Logger log = Logger.getLogger(DataCollection.class.getName());
	private ArrayList<DataStats> test;
	
	public DataCollection(ArrayList<DataStats> test){
        this.test = test;
	}
	
    Options newOpt = new Options();
    SubCommand newSub = new SubCommand();
    private String NameOfBean = "";
    Object[] sendMail = null;
    Object[] sendBB = null;
    boolean sendEmail = false;
    boolean BB = false;

    public ArrayList<DataStats> doBean(MBeanServerConnection mbsc, ObjectInstance instance, String[] attributes, String BeanName)
            throws Exception {
    	
            String[] newOptions = newOpt.listOptions(mbsc, instance, attributes);
            int p = newOptions.length;
            
            for (int i = 0; i < p; i++) {
                test = newSub.doSubCommand(mbsc, instance, newOptions[i], BeanName);
            }
        return test;
    }

    public ArrayList<DataStats> execute(MBeanServerConnection mbsc, String[] MBeans, String[] attributes)
            throws Exception {
        
        String beanName = null;
        ObjectName objName = beanName != null && beanName.length() > 0 ? new ObjectName(beanName) : null;
        Set<?> beans = mbsc.queryMBeans(objName, null);
        for (Iterator<?> i = beans.iterator(); i.hasNext();) {
            boolean b = false;
            Object obj = i.next();
            if (obj instanceof ObjectInstance) {
                String testing2 = ((ObjectInstance) obj).getObjectName().getCanonicalName();
                int length = MBeans.length;
                b = false;
                for (int m = 0; m < length; m++) {
                    b = testing2.equalsIgnoreCase(MBeans[m].toString()) || testing2.indexOf(MBeans[m].toString()) > 0;
                    if (b) {
                        NameOfBean = MBeans[m].toString();
                        break;
                    }
                }
                 
                if (b) {
                    log.info(testing2);
                    ObjectName objName2 = testing2 != null && testing2.length() > 0 ? new ObjectName(testing2) : null;
                    Set<?> beans2 = mbsc.queryMBeans(objName2, null);
                    ObjectInstance instance = (ObjectInstance) beans2.iterator().next();
                    doBean(mbsc, instance, attributes, NameOfBean);
                }                
            } else {
                log.warning((new StringBuilder()).append("Unexpected object type: ").append(obj).toString());
            }
        }
        return test;
    }
}
