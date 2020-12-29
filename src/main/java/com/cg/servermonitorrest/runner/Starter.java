package com.cg.servermonitorrest.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.SharedVariables;

@Component
public class Starter {

	private static Logger logger = LogManager.getLogger();

	@Autowired
	private AppRunner appRunner;

//	public static void main(String[] args) throws InterruptedException {
//
//		while (true) {
//			long startTime = System.currentTimeMillis();
//			logger.info("App Starting");
//			appRunner.runTasks();
//			logger.info("App Completed in :"+(System.currentTimeMillis()-startTime)+" milliseconds");
//			if (SharedVariables.restartingServices.size() > 0) {
//				logger.info(
//						"App Will check for successful restarts in " + GlobalConstants.RestartCheckTime + " minutes");
//				Thread.sleep((long) (GlobalConstants.RestartCheckTime * 60 * 1000));
//				logger.info("Checking for successful restarts");
//				appRunner.checkRestarts();
//			}
//			logger.info("App will restart in " + GlobalConstants.AppRerunMinutes + " minutes");
//			Thread.sleep((long) (GlobalConstants.AppRerunMinutes * 60 * 1000));
//		}
//	}

	public void startApp() throws InterruptedException {
		while (true) {
			long startTime = System.currentTimeMillis();
			logger.info("App Starting");
			appRunner.runTasks();
			logger.info("App Completed in :" + (System.currentTimeMillis() - startTime) + " milliseconds");
			if (SharedVariables.restartingServices.size() > 0) {
				logger.info(
						"App Will check for successful restarts in " + GlobalConstants.RestartCheckTime + " minutes");
				Thread.sleep((long) (GlobalConstants.RestartCheckTime * 60 * 1000));
				logger.info("Checking for successful restarts");
				appRunner.checkRestarts();
			}
			logger.info("App will restart in " + GlobalConstants.AppRerunMinutes + " minutes");
			Thread.sleep((long) (GlobalConstants.AppRerunMinutes * 60 * 1000));
		}
	}

}
