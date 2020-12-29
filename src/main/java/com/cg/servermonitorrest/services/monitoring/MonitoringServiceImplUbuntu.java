package com.cg.servermonitorrest.services.monitoring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cg.servermonitorrest.services.analysis.IAnalyzeService;
import com.cg.servermonitorrest.services.cleanup.CleanupService;
import com.cg.servermonitorrest.services.initialization.IInitializeService;
import com.cg.servermonitorrest.services.reporting.IReportService;
import com.cg.servermonitorrest.services.restarting.IRestartingService;

public class MonitoringServiceImplUbuntu implements IMonitoringService {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	private IInitializeService initService;

	// their implementation is based on type of Startup Constant Parameter and they are initialized while app bootstraps.
	@Autowired
	private IAnalyzeService analysisService;
	
	@Autowired
	private IRestartingService restartingService;
	
	@Autowired
	private IReportService reportingService;

	@Override
	public void initialize() {
		logger.info("Initialization begins.");

		// build initial list of services to scan
		initService.initServiceList();

		// build recipient list in mailing service.
		initService.buildMailingList();

		// build failing services list
		initService.initFailedServicesList();

		logger.info("Initialization ended.");
	}

	@Override
	public void analyze() {
		logger.info("Analysis Begins.");

		// beginning analysis
		analysisService.analyze();

		logger.info("Analysis Ended.");
	}

	public void restart() {

		logger.info("Restarting Begins.");

		restartingService.restartServices();

		logger.info("Restarting Ends.");

	}

	public void report() {

		logger.info("Reporting Begins.");

		reportingService.notifyClients();

		logger.info("Reporting Ends.");
	}

	@Override
	public void cleanup() {
		logger.info("Cleanup Begins.");

		CleanupService.cleanLists();

		logger.info("Cleanup Ends.");

	}

}
