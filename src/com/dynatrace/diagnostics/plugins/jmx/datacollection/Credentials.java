package com.dynatrace.diagnostics.plugins.jmx.datacollection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.dynatrace.diagnostics.plugins.jmx.JvmMonitoring;

public class Credentials {

	private static final Logger log = Logger.getLogger(JvmMonitoring.class.getName());
	
    public Map<String, String[]> formatCredentials(String userpass) {
        Map<String, String[]> enviro = null;
        if (userpass == null || userpass.equals("-")) {
            return enviro;
        }
        int index = userpass.indexOf(':');
        if (index <= 0) {
        	log.warning("JVM monitoring error parsing password. Setting to null.");
        	return enviro;
        } else {
            String creds[] = {
                userpass.substring(0, index), userpass.substring(index + 1)
            };
            enviro = new HashMap<String, String[]>(1);
            enviro.put("jmx.remote.credentials", creds);
            return enviro;
        }
    }
}