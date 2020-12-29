package com.cg.servermonitorrest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.RunningModes;
import com.cg.servermonitorrest.services.analysis.AnalyzeServiceImplServiceUbuntu;
import com.cg.servermonitorrest.services.analysis.AnalyzeServiceImplServiceWin;
import com.cg.servermonitorrest.services.analysis.AnalyzeServiceImplStandaloneUbuntu;
import com.cg.servermonitorrest.services.analysis.AnalyzeServiceImplStandaloneWin;
import com.cg.servermonitorrest.services.analysis.IAnalyzeService;
import com.cg.servermonitorrest.services.monitoring.IMonitoringService;
import com.cg.servermonitorrest.services.monitoring.MonitoringServiceImplUbuntu;
import com.cg.servermonitorrest.services.monitoring.MonitoringServiceImplWin;
import com.cg.servermonitorrest.services.restarting.IRestartingService;
import com.cg.servermonitorrest.services.restarting.RestartingServiceImplServiceUbuntu;
import com.cg.servermonitorrest.services.restarting.RestartingServiceImplServiceWin;
import com.cg.servermonitorrest.services.restarting.RestartingServiceImplStandaloneUbuntu;
import com.cg.servermonitorrest.services.restarting.RestartingServiceImplStandaloneWin;

@SpringBootApplication
public class ServerMonitorRestApplication extends SpringBootServletInitializer {

	String platform = GlobalConstants.platform;
	RunningModes mode = GlobalConstants.runningMode;

	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext appContext = SpringApplication.run(ServerMonitorRestApplication.class, args);
//		Starter appStarter = appContext.getBean(Starter.class);
//		appStarter.startApp();
		appContext.getBean(IMonitoringService.class).initialize();

	}

	@Bean
	public IMonitoringService initMonitoringService() {
		IMonitoringService monitoringService = new MonitoringServiceImplUbuntu();

		if (platform.equalsIgnoreCase("Windows")) {
			logger.info("Running for Windows Environment");
			monitoringService = new MonitoringServiceImplWin();
		} else if (platform.equalsIgnoreCase("Ubuntu")) {
			logger.info("Running for Linux Environment");
			monitoringService = new MonitoringServiceImplUbuntu();
		}

		return monitoringService;
	}

	@Bean
	public IAnalyzeService initAnalysisService() {
		// default mode and platform
		IAnalyzeService analysisService = new AnalyzeServiceImplServiceUbuntu();
		if (platform.equalsIgnoreCase("Windows")) {
			if (mode.equals(RunningModes.StandaloneRunningMode)) {
				analysisService = new AnalyzeServiceImplStandaloneWin();

			} else if (mode.equals(RunningModes.ServiceRunningMode)) {
				analysisService = new AnalyzeServiceImplServiceWin();
			}
		} else if (platform.equalsIgnoreCase("Ubuntu")) {
			if (mode.equals(RunningModes.StandaloneRunningMode)) {
				analysisService = new AnalyzeServiceImplStandaloneUbuntu(); // no standalone mode for Ubuntu written
																			// AOY.

			} else if (mode.equals(RunningModes.ServiceRunningMode)) {
				analysisService = new AnalyzeServiceImplServiceUbuntu();
			}
		}

		return analysisService;

	}

	@Bean
	public IRestartingService initRestartingService() {
		// default mode and platform
		IRestartingService restartingService = new RestartingServiceImplServiceUbuntu();
		if (platform.equalsIgnoreCase("Windows")) {
			if (mode.equals(RunningModes.StandaloneRunningMode)) {
				restartingService = new RestartingServiceImplStandaloneWin();
			} else if (mode.equals(RunningModes.ServiceRunningMode)) {
				restartingService = new RestartingServiceImplServiceWin();

			}
		} else if (platform.equalsIgnoreCase("Ubuntu")) {
			if (mode.equals(RunningModes.StandaloneRunningMode)) {
				restartingService = new RestartingServiceImplStandaloneUbuntu(); // nothing written aoy
			} else if (mode.equals(RunningModes.ServiceRunningMode)) {
				restartingService = new RestartingServiceImplServiceUbuntu();
			}

		}

		return restartingService;
	}

}
