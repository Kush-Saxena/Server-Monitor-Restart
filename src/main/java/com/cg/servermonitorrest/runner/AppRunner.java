package com.cg.servermonitorrest.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.ServiceTypeEnum;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.servicehelpers.AnalyzerServiceHelper;
import com.cg.servermonitorrest.services.mailing.IMailService;
import com.cg.servermonitorrest.services.mailing.MailServiceImpl;
import com.cg.servermonitorrest.services.monitoring.IMonitoringService;

@Component
public class AppRunner {

	private static Logger logger = LogManager.getLogger();

	// Daily log archiving done
	// Java version checking and Proper specifications.

	// Creating instance of monitoring service, this is main service which calls
	// other service and handles all exceptions in them
	//Automatically handled while app bootstraps
	
	@Autowired
	private IMonitoringService monitoringService;

	public void runTasks() {

		logger.info("Starting Checks");

		// init function builds mailing list, failed services etc.
		monitoringService.initialize();

		// analyze function looks for services and looks for failed services.
		monitoringService.analyze();

		// restart function tries to restart the services.
		monitoringService.restart();

		// report function reports the data to clients and finished logging.
		monitoringService.report();

		// cleaup shared data
		monitoringService.cleanup();

		logger.info("Closing Application");
	}

	public void checkRestarts() {
		List<ServiceBean> serviceList = SharedVariables.restartingServices;
		HashMap<String, Boolean> statuses = new HashMap<String, Boolean>();

		for (ServiceBean service : serviceList) {

			logger.debug("Fetching service status using query service: " + service.getServiceName());

			AnalyzerServiceHelper analyzerHelper = new AnalyzerServiceHelper();
			ServiceBean fetchedBean = analyzerHelper.queryServiceLinux(service.getServiceName());
			if (fetchedBean.getServiceType() == ServiceTypeEnum.RUNNING) {
				statuses.put(fetchedBean.getServiceName(), true);
			} else {
				statuses.put(fetchedBean.getServiceName(), false);
			}
		}

		IMailService mailService = new MailServiceImpl();

		List<String> successList = new ArrayList<String>();
		for (Entry<String, Boolean> e : statuses.entrySet()) {
			if (e.getValue()) {
				successList.add(e.getKey());
				logger.info("Service Successfully Restarted: " + e.getKey());
			}

		}

		mailService.reportSuccessRestart(successList);
		SharedVariables.invalidMailingIds = new HashSet<String>();
	}

}