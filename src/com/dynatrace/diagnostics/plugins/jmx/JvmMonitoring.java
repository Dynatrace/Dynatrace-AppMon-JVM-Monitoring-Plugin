
package com.dynatrace.diagnostics.plugins.jmx;

import com.dynatrace.diagnostics.pdk.*;
import com.dynatrace.diagnostics.plugins.jmx.datacollection.Credentials;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.dynatrace.diagnostics.plugins.jmx.variableholder.DataStats;
import com.dynatrace.diagnostics.plugins.jmx.variableholder.DateTime;

public class JvmMonitoring implements Monitor, JvmMonitoringConstants {

	private static final Logger log = Logger.getLogger(JvmMonitoring.class.getName());
	
	private JvmMonitoringProperties properties;
	private MBeanServerConnection mbsc = null;
	private JMXServiceURL rmiurl = null;
	private JMXConnector jmxc = null;
	private ArrayList<DataStats> oldCopy = null;
	
	private String MBeans;
	private String attributes;
	private String[] bean;
	private String[] attribute;
	private String environment;
	private String serverName;
	private String userpass;
	private String hostname;
	private Credentials datacollection = new Credentials();
	
	
	
	public static boolean isEmptyOrBlank(String str) {
		return str == null || str.trim().isEmpty();
		}
	
	@Override
	public Status setup(MonitorEnvironment env) throws Exception {
		if (log.isLoggable(Level.INFO)) {
			log.info("Entering setup method");
		}
		String s;
		properties = new JvmMonitoringProperties();
		
		// set plugin's configuration parameters
		properties.setJmxEnvironment(env.getConfigString(CONFIG_JMX_ENVIRONMENT).trim());
		properties.setDtServer(env.getConfigString(CONFIG_DT_SERVER).trim());
		properties.setJmxPassword(env.getConfigString(CONFIG_JMX_PASSWORD));
		properties.setSystemProfile(env.getConfigString(CONFIG_SYSTEM_PROFILE).trim());
		properties.setMetrics((s = env.getConfigString(CONFIG_METRICS)) == null ? new String[0] : s.trim().split(";"));
		properties.setAgentGroups((s = env.getConfigString(CONFIG_AGENT_GROUPS)) == null ? new String[0] : s.trim().split(";"));
		properties.setserverName(env.getConfigString(CONFIG_SERVER_NAME).trim());
		properties.setMbeans(env.getConfigString(CONFIG_MBEAN_GROUP).trim());
		 
		
		
		MBeans=properties.getMbeans();
		bean = MBeans.split("\\s+");
		properties.setAttributes(env.getConfigString(CONFIG_MBEAN_ATTRIBUTES).trim());
		attributes=properties.getAttributes();
    	attribute = attributes.split("\\s+");
		environment=properties.getJmxEnvironment();
		serverName=properties.getserverName();
		
		if (isEmptyOrBlank(MBeans)){
			//MBeans="nothingWasEntered";
			log.warning("No MBean data was entered.");
			return new Status(Status.StatusCode.ErrorInternal, "No MBean data was entered. Validate configuration.");
		}
		if (isEmptyOrBlank(attributes)){
			//attributes="nothingWasEntered";
			log.warning("No Attributes data was entered.");
			return new Status(Status.StatusCode.ErrorInternal, "No Attribute data was entered. Validate configuration.");
		}		
		
		
		//Credentials datacollection = new Credentials();
		hostname = properties.getDtServer();
		userpass = properties.getJmxPassword();

        rmiurl = new JMXServiceURL((new StringBuilder()).append("service:jmx:rmi://").append(hostname).append("/jndi/rmi://").append(hostname).append("/jmxrmi").toString());

        try {
            jmxc = JMXConnectorFactory.connect(rmiurl, datacollection.formatCredentials(userpass));
        
        } catch (Exception e) {
        	jmxc = null; 
        }
       try{
    	   if(jmxc != null){
    		   mbsc = jmxc.getMBeanServerConnection();
    	   }else {
    		   mbsc = null;
    		   return new Status(Status.StatusCode.PartialSuccess, "There was an error connecting to JMX server. Could be formatting of the user password and/or configuration setup. Please validate the configurations being used"
       			+ " and JMX server is running.");
    	   }
       } catch (Exception e) {
    	 //If there is an error we need to handle it here.
    	   mbsc = null;
    	   return new Status(Status.StatusCode.PartialSuccess, "Unable to connect to the MBean server at: " + hostname + ". Please validate it is running and connection"
    	   		+ " details are correct.");
       }
		return new Status(Status.StatusCode.Success);
	}
	
	
	@Override
	public Status execute(MonitorEnvironment env) throws Exception {
		DynamicMeasure testing = new DynamicMeasure();
		
		if (mbsc == null){
			try{
				try {
		            jmxc = JMXConnectorFactory.connect(rmiurl, datacollection.formatCredentials(userpass));
		        } catch (Exception e) {
		        	jmxc = null;  
		        	String measureName = serverName+"|JVMConnection|"+environment;
					Float f = (float) 0;
					testing.populateDynamicMeasure(env, JVM_CONNECTION_GROUP, JVM_CONNECTION_METRIC, measureName, (double)f);
		        	
		        	return new Status(Status.StatusCode.PartialSuccess, "There was an error connecting to JMX server. Could be formatting of the user password and/or configuration setup. Please validate the configurations being used"
		        			+ " and target JMX server is running.");

		        }
		    	   mbsc = jmxc.getMBeanServerConnection();
		    	   
		       } catch (Exception e) {
		    	    String measureName = serverName+"|JVMConnection|"+environment;
					Float f = (float) 0;
					testing.populateDynamicMeasure(env, JVM_CONNECTION_GROUP, JVM_CONNECTION_METRIC, measureName, (double)f);
		    	    mbsc = null;
		    	    return new Status(Status.StatusCode.PartialSuccess, "Unable to connect to: " + hostname + ". RMI URL string is: " + rmiurl
		    			   + ". ");
		       }
		}
		
		if(mbsc != null){
			
			long webMonitorStartTime = DateTime.getDateTime();
			
			ArrayList<DataStats> thwd = null;
			com.dynatrace.diagnostics.plugins.jmx.datacollection.DataCollection client = new com.dynatrace.diagnostics.plugins.jmx.datacollection.DataCollection(thwd);
			
			try{
				
			  thwd = client.execute(mbsc, bean, attribute);
			}catch (Exception e){
				mbsc = null;
				String measureName = serverName+"|JVMConnection|"+environment;
				Float f = (float) 0;
				testing.populateDynamicMeasure(env, JVM_CONNECTION_GROUP, JVM_CONNECTION_METRIC, measureName, (double)f);
				log.info("Exception: " + e);
				return new Status(Status.StatusCode.PartialSuccess, "Lost connection to: " + hostname + ". We will try to establish this connection on next run.");
			}
			
			com.dynatrace.diagnostics.plugins.jmx.datacollection.RecordData record = new com.dynatrace.diagnostics.plugins.jmx.datacollection.RecordData();
			
			try{
				record.getWriteToDB(env, serverName, environment, thwd, oldCopy);
			} catch (Exception e){
				log.info("Error writting data to DT: " + e.toString());
			}
			
			oldCopy=thwd;
			
			//How long it took to monitor.
			long webPerfEndTime = DateTime.getDateTime();
			long WebtotalPerfTime = (webPerfEndTime - webMonitorStartTime); 
			String measureName = serverName+"|MonitorJVMTime|"+environment;
			testing.populateDynamicMeasure(env, JVM_CONNECTION_GROUP, JVM_MONITORTIME_METRIC, measureName, (double)WebtotalPerfTime);			
		
			
		
	}
	    String measureName = serverName+"|JVMConnection|"+environment;
		Float f = (float) 1;
		testing.populateDynamicMeasure(env, JVM_CONNECTION_GROUP, JVM_CONNECTION_METRIC, measureName, (double)f);
		
		return new Status(Status.StatusCode.Success, "Monitoring completed without know issue.");
	}

	
	@Override
	public void teardown(MonitorEnvironment env) throws Exception {
		if (log.isLoggable(Level.INFO)) {
			log.info("Entering teardown method");
		}
		
		if (jmxc != null) {
			jmxc.close();
		}

	}
}
