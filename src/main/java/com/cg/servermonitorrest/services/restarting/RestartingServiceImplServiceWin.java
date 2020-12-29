package com.cg.servermonitorrest.services.restarting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.ServiceTypeEnum;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.util.AppUtilImpl;


public class RestartingServiceImplServiceWin implements IRestartingService {

	private static Logger logger = LogManager.getLogger();

	Map<String, String> serviceInfo = new HashMap<>();

	@Override
	public void restartServices() {

		List<ServiceBean> serviceList = SharedVariables.nonRepeatedFailedServiceList;

		for (ServiceBean sb : serviceList) {

			if (sb.getServiceType() == ServiceTypeEnum.STOPPED) {
				onlyRestart(sb.getServiceName());
			} else {
				stopAndStart(sb.getServiceName());
			}
		}

	}

	private static void stopAndStart(String serviceName) {

		logger.debug("Trying to restart service: " + serviceName);
		String commandToExec = "net stop " + serviceName + "&& net start " + serviceName;

		AppUtilImpl.shellCommandExecute(commandToExec);

	}

	private static void onlyRestart(String serviceName) {

		logger.debug("Trying to restart service: " + serviceName);
		String commandToExec = "net start " + serviceName;

		AppUtilImpl.shellCommandExecute(commandToExec);

	}
}
