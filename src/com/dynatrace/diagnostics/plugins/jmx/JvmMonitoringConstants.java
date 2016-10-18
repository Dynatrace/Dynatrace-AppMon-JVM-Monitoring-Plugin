package com.dynatrace.diagnostics.plugins.jmx;

public interface JvmMonitoringConstants {
	// Plugin's configuration parameter's constants
	public static final String CONFIG_JMX_ENVIRONMENT = "environment";
	public static final String CONFIG_DT_SERVER = "dtServer";
	public static final String CONFIG_JMX_PASSWORD = "jmxPassword";
	public static final String CONFIG_SYSTEM_PROFILE = "systemProfile";
	public static final String CONFIG_METRICS = "metrics";
	public static final String CONFIG_AGENT_GROUPS = "agentGroups";
	public static final String CONFIG_MBEAN_GROUP = "mBeans";
	public static final String CONFIG_MBEAN_ATTRIBUTES = "attributes";
	public static final String CONFIG_SERVER_NAME = "serverName";
	
	// JVM Connection Group
	public static final String JVM_CONNECTION_GROUP = "JVM Connection Group";
	public static final String JVM_CONNECTION_METRIC = "JVM Connection Status";
	public static final String JVM_MONITORTIME_METRIC = "JVM Monitor Time";
	
	// JVM Monitor Group
	public static final String JVM_MONITOR_GROUP = "JVM Monitor Group";	
	public static final String JVM_DATA_METRIC = "JVM DATA";
	public static final String JVM_MARKSWEEPTIME_METRIC = "Old Collection Time";
	public static final String JVM_MARKSWEEPCOUNT_METRIC = "Old Collection Count";
	public static final String JVM_MARKSWEEPCOMPACTS_METRIC = "Old Total Compacts";
	public static final String JVM_MARKSWEEPGCTIME_METRIC = "Old Time In GC";
	public static final String JVM_COPYTIME_METRIC = "Young Collection Time";
	public static final String JVM_COPYCOUNT_METRIC = "Young Collection Count";
	public static final String JVM_COPYCOMPACTS_METRIC = "Young Total Compacts";	
	public static final String JVM_COPYGCTIME_METRIC = "Young Time In GC";
	public static final String JVM_MEMORYUSED_METRIC = "Heap Memory Used";
	public static final String JVM_MEMORYMAX_METRIC = "Heap Memory Max";
	public static final String JVM_MEMORYCOMMITTED_METRIC = "Heap Memory Committed";
	public static final String JVM_MEMORYINIT_METRIC = "Heap Memory Init";
	public static final String JVM_NONMEMORYUSED_METRIC = "NonHeap Memory Used";
	public static final String JVM_NONMEMORYMAX_METRIC = "NonHeap Memory Max";
	public static final String JVM_NONMEMORYCOMMITTED_METRIC = "NonHeap Memory Committed";
	public static final String JVM_NONMEMORYINIT_METRIC = "NonHeap Memory Init";
	public static final String JVM_MAXHEAPSIZE_METRIC = "Max Heap Size";
	public static final String JVM_THREADPEAK_METRIC = "Peak Thread Count";
	public static final String JVM_THREADSTARTED_METRIC = "Total Started ThreadCount";
	public static final String JVM_THREADTOTAL_METRIC = "Thread Count";
	public static final String JVM_THREADDAEMON_METRIC = "Daemon Thread Count";
	public static final String JVM_OPCPUTIME_METRIC = "Process Cpu Time";
	public static final String JVM_OPSYSLOADAVG_METRIC = "System Load Average";
	public static final String JVM_OPPROCCAP_METRIC = "Processing Capacity";
	public static final String JVM_OPPROCCPUTIMENS_METRIC = "Process Cpu Time By NS";
	public static final String JVM_OPAVAILPROC_METRIC = "Available Processors";
	public static final String JVM_FREESWAPSIZE_METRIC = "Free Swap Space Size";
	public static final String JVM_TOTALSWAPSIZE_METRIC = "Total Swap Space Size";
	public static final String JVM_JVMUPTIME_METRIC = "JVM Uptime";
	
	// Java Virtual Machine Metric Group
	public static final String JMX_MEASURE_SPLIT_NAME = "Measure Name";
}
