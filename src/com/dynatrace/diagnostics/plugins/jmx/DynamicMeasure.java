package com.dynatrace.diagnostics.plugins.jmx;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
import com.dynatrace.diagnostics.pdk.MonitorMeasure;

public class DynamicMeasure implements JvmMonitoringConstants{
	
	private static final Logger log = Logger.getLogger(DynamicMeasure.class.getName());
	
	public synchronized void populateDynamicMeasure(MonitorEnvironment env, String measureGroup, String baseMeasure, String measureName, Double d) {
		if (log.isLoggable(Level.INFO)) {
			log.info("Entering populateDynamicMeasure method");
		}
		Collection<MonitorMeasure> monitorMeasures = env.getMonitorMeasures(measureGroup, baseMeasure);
		for (MonitorMeasure subscribedMonitorMeasure : monitorMeasures) {
			MonitorMeasure dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, JMX_MEASURE_SPLIT_NAME, measureName);
			dynamicMeasure.setValue(d);
			log.info("populateDynamicMeasure method: measure group: " + measureGroup + ". BaseMeasure: " + baseMeasure + ". Measure Name: " + measureName + ". Value is: " + d);
		}
		log.info("populateDynamicMeasure method done");
	}
}