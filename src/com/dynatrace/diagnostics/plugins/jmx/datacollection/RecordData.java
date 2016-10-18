package com.dynatrace.diagnostics.plugins.jmx.datacollection;


import java.util.ArrayList;
import java.util.logging.Logger;

import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
import com.dynatrace.diagnostics.plugins.jmx.DynamicMeasure;	
import com.dynatrace.diagnostics.plugins.jmx.JvmMonitoringConstants;	
import com.dynatrace.diagnostics.plugins.jmx.variableholder.DataStats;

	public class RecordData implements JvmMonitoringConstants{

		private static final Logger log = Logger.getLogger(RecordData.class.getName());
	    
	    public void getWriteToDB(MonitorEnvironment envs, String Server, String Environment, ArrayList<DataStats> ad, ArrayList<DataStats> oldCopy) throws Exception {
	        String measureName;
	        DynamicMeasure testing = new DynamicMeasure();
	        int i=2;
	        try {
	            ArrayList<DataStats> list = ad;
	            for (DataStats fromStatic : list) {
	                String MBean = fromStatic.getMBeanName();
	                String Attribute = fromStatic.getAttName();
	                String SubAttribute = fromStatic.getSubAttName();
	                String ValueCheck = fromStatic.getValue();
	                Double value = Double.parseDouble(ValueCheck);
	                Long time = fromStatic.gettimeDateConverted(); 
	                /*MBean = MBean.replace(".", "-");
	                MBean = MBean.replace(" ", "");
	                MBean = MBean.replace(",", "-");
	                MBean = MBean.replace("]", "-");
	                MBean = MBean.replace("[", "-");
	                MBean = MBean.replace("=", "-");
	                MBean = MBean.replace(":", "-");
	                MBean = MBean.replace("//", "-");
	                MBean = MBean.replace("/", "-");*/
	                
	                Attribute = Attribute.replace(".", "-");

	                i=2;
	                if(MBean.contains("GarbageCollector") && Attribute.contains("CollectionTime")){
	                	if(MBean.contains("MarkSweepCompact")){
	                		//Old
	                		i=1;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("Copy")) {
	                		//young
	                		i=0;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("J9GC")) {
	                		//Old
	                		i=1;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("PSScavenge")) {
	                		//young
	                		i=0;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("PSMarkSweep")) {
	                		//old
	                		i=1;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("ParNew")) {
	                		//young
	                		i=0;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("ConcurrentMarkSweep")) {
	                		//old
	                		i=1;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPTIME_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("G1Young")) {
	                		//young
	                		i=0;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYTIME_METRIC, measureName, (double)value);
	                	}else if (MBean.contains("G1Mixed")) {
	                		//old
	                		i=1;
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPTIME_METRIC, measureName, (double)value);
	                	} 
	                	if(i==1){
	                		//Gather time in GC Old ....
	                		if(oldCopy != null){
	                			for (DataStats fromOldStatic : oldCopy) {
	                				String oldMBean = fromOldStatic.getMBeanName();
	                				String oldAttribute = fromOldStatic.getAttName();
	                				String ValueCheck2 = fromOldStatic.getValue();
	                				Double oldvalue = Double.parseDouble(ValueCheck2);
	                				Long oldTime = fromOldStatic.gettimeDateConverted();
	                				if(oldMBean.contains("GarbageCollector") && oldAttribute.contains("CollectionTime") && MBean.equalsIgnoreCase(oldMBean)){
		            					log.info("GC Info Time");
		            					Long totalTime = oldTime - time;
		            					log.info("GC Info Time old time" + oldTime.toString());
		            					log.info("GC Info Time current time" + time.toString());
		            					Double totalValue = oldvalue - value;
		            					log.info("Total GC Time");
		            					log.info("Total GC Time old" + oldvalue.toString());
		            					log.info("Total GC Time new" + value.toString());
		            					Double timeInGC = (totalValue / totalTime) * 100;
		            					log.info("Time in GC");
		            					log.info("Time in GC" + timeInGC.toString());
		            					measureName = Server+"|JVMMonitor|" + oldMBean + "|TimeInGC" + "|" + Environment;
		            					testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPGCTIME_METRIC, measureName, (double)timeInGC);
		            					break;
	                				}
	                			}
	                		}
		            	i=2;
	                	} else if(i==0){
	                		//Gather time in GC Young....
					        if(oldCopy != null){
			            		for (DataStats fromOldStatic : oldCopy) {
			            			String oldMBean = fromOldStatic.getMBeanName();
			            			String oldAttribute = fromOldStatic.getAttName();
			            			String oldValueCheck2 = fromOldStatic.getValue();
			            			Double oldvalue = Double.parseDouble(oldValueCheck2);
				                
			            			Long oldTime = fromOldStatic.gettimeDateConverted();
			            			
			            			if(oldMBean.contains("GarbageCollector") && oldAttribute.contains("CollectionTime") && MBean.equalsIgnoreCase(oldMBean)){
			            					Long totalTime = oldTime - time;
			            					Double totalValue = oldvalue - value;
			            					Double timeInGC = (totalValue / totalTime) * 100;
			            					//Long seconds = (totalTime / 1000); //time between checks.
			            					measureName = Server+"|JVMMonitor|" + oldMBean + "|TimeInGC" + "|" + Environment;
			            					testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYGCTIME_METRIC, measureName, (double)timeInGC);
			            					break;
			            			}
			            		}
			            	}
					        i=2;
	                	}
	                } else if(MBean.contains("GarbageCollector") && Attribute.contains("CollectionCount")){
	                	if(MBean.contains("MarkSweepCompact")){
	                		//Old
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("Copy")) {
	                		//young
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("J9GC")) {
	                		//Old
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("PSScavenge")) {
	                		//young
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("PSMarkSweep")) {
	                		//old
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("ParNew")) {
	                		//young
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("ConcurrentMarkSweep")) {
	                		//old
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOUNT_METRIC, measureName, (double)value);
	                	} else if (MBean.contains("G1Young")) {
	                		//young
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOUNT_METRIC, measureName, (double)value);
	                	}else if (MBean.contains("G1Mixed")) {
	                		//old
	                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOUNT_METRIC, measureName, (double)value);
	                	}
	                } else if (MBean.contains("GarbageCollector") && Attribute.contains("TotalCompacts")){
		                	if(MBean.contains("MarkSweepCompact")){
		                		//old
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("Copy")) {
		                		//young
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
						        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("J9GC")) {
		                		//old
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("PSScavenge")) {
		                		//young
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
						        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("PSMarkSweep")) {
		                		//old
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("ParNew")) {
		                		//young
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
						        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("ConcurrentMarkSweep")) {
		                		//old
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOMPACTS_METRIC, measureName, (double)value);
		                	} else if (MBean.contains("G1Young")) {
		                		//young
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
						        testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_COPYCOMPACTS_METRIC, measureName, (double)value);
		                	}else if (MBean.contains("G1Mixed")) {
		                		//old
		                		measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
					            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MARKSWEEPCOMPACTS_METRIC, measureName, (double)value);
		                	}	                
		            }  else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("HeapMemoryUsage") && SubAttribute.equalsIgnoreCase("used")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MEMORYUSED_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("NonHeapMemoryUsage") && SubAttribute.equalsIgnoreCase("used")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_NONMEMORYUSED_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("MaxHeapSizeLimit")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MAXHEAPSIZE_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("MaxHeapSizeLimit")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MAXHEAPSIZE_METRIC, measureName, (double)value);     
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("HeapMemoryUsage") && SubAttribute.equalsIgnoreCase("init")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MEMORYINIT_METRIC, measureName, (double)value);			            
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("HeapMemoryUsage") && SubAttribute.equalsIgnoreCase("committed")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MEMORYCOMMITTED_METRIC, measureName, (double)value);			            
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("HeapMemoryUsage") && SubAttribute.equalsIgnoreCase("max")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_MEMORYMAX_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("NonHeapMemoryUsage") && SubAttribute.equalsIgnoreCase("init")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_NONMEMORYINIT_METRIC, measureName, (double)value);			            
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("NonHeapMemoryUsage") && SubAttribute.equalsIgnoreCase("committed")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_NONMEMORYCOMMITTED_METRIC, measureName, (double)value);			            
	                } else if (MBean.contains("Memory") && Attribute.equalsIgnoreCase("NonHeapMemoryUsage") && SubAttribute.equalsIgnoreCase("max")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_NONMEMORYMAX_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Threading") && Attribute.equalsIgnoreCase("PeakThreadCount")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_THREADPEAK_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Threading") && Attribute.equalsIgnoreCase("TotalStartedThreadCount")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_THREADSTARTED_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Threading") && Attribute.equalsIgnoreCase("ThreadCount")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_THREADTOTAL_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Threading") && Attribute.equalsIgnoreCase("DaemonThreadCount")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_THREADDAEMON_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("ProcessCpuTime")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_OPCPUTIME_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("SystemLoadAverage")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_OPSYSLOADAVG_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("ProcessingCapacity")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_OPPROCCAP_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("ProcessCpuTimeByNS")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_OPPROCCPUTIMENS_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("AvailableProcessors")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_OPAVAILPROC_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("TotalSwapSpaceSize")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_TOTALSWAPSIZE_METRIC, measureName, (double)value);
	                } else if (MBean.contains("OperatingSystem") && Attribute.equalsIgnoreCase("FreeSwapSpaceSize")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_FREESWAPSIZE_METRIC, measureName, (double)value);
	                } else if (MBean.contains("Runtime") && Attribute.equalsIgnoreCase("Uptime")){
		                measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
			            testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_JVMUPTIME_METRIC, measureName, (double)value);
	                } else {
	                	//In case you monitor other MBeans/Attributes I don't account for.
	                	measureName = Server+"|JVMMonitor|" + MBean + "|" + Attribute + "|" + SubAttribute + "|" + Environment;
	                	testing.populateDynamicMeasure(envs, JVM_MONITOR_GROUP, JVM_DATA_METRIC, measureName, (double)value);
	                }
	            }
	        } catch (Exception e) {
	            log.warning("" + e);
	        }
	    }
}
