package com.cg.servermonitorrest.resources;

import java.util.List;
import java.util.Set;

public class SharedVariables {

//	String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

	// initialized failed service list
	public static List<ServiceBean> failedServicesList;

	// initialized repeated failed service list
	public static List<ServiceBean> repeatedFailedServiceList;
	// initialized non repeated failed service list
	public static List<ServiceBean> nonRepeatedFailedServiceList;
	// shared invalid mail ids
	public static Set<String> invalidMailingIds;

	// initialized mailing list
	public static List<String> mailRecipients;

	// server services list
	public static List<String> serviceList;

	//services being restarted
	public static List<ServiceBean> restartingServices;
	
}
