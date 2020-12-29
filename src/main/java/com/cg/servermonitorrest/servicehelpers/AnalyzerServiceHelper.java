package com.cg.servermonitorrest.servicehelpers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.ServiceTypeEnum;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.util.AppUtilImpl;
import com.cg.servermonitorrest.util.IAppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AnalyzerServiceHelper {

	private static Logger logger = LogManager.getLogger();

	private IAppUtil appUtil = new AppUtilImpl();

	private ObjectMapper jsonMapper = new ObjectMapper();

	public boolean isRepeatedFaliure(ServiceBean sb) {

		List<ServiceBean> failedServicesInitList = SharedVariables.failedServicesList;
		// alt restarting

		for (ServiceBean bean : failedServicesInitList) {

			// this service was in the failed list before

			/*
			 * if (bean.getServiceName().equalsIgnoreCase(sb.getServiceName())) {
			 * 
			 * // this would give difference in hours long hourDiff = Math
			 * .abs((sb.getTimeCreated().getTime() - bean.getTimeCreated().getTime()) / (60
			 * * 60 * 1000));
			 * 
			 * if (hourDiff > GlobalConstants.allowedHourDifference) {
			 * 
			 * return true; } }
			 */

			if (bean.getServiceName().equalsIgnoreCase(sb.getServiceName())) {

				long timeDiff = Math.abs((sb.getTimeCreated().getTime() - bean.getTimeCreated().getTime()));

				// this would give difference in mins
				long minDiff = (timeDiff / (60 * 1000)) % 60;

//				long diffSeconds = timeDiff / 1000 % 60;  
//				long diffMinutes = timeDiff / (60 * 1000) % 60; 
//				long diffHours = timeDiff / (60 * 60 * 1000);
//				
				if (minDiff < GlobalConstants.allowedHourDifference) {

					return true;
				}
			}
		}

		return false;
	}

	public boolean writeFailedProcessesToFile(List<ServiceBean> beanList) {

		boolean isWriteSuccess = false;

		// getting old List
		List<ServiceBean> serviceList = SharedVariables.failedServicesList;

		try {

			// add new data to old list
			serviceList.addAll(beanList);

			// creating string in json
			String jsonString = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serviceList);

			// writing to file
			isWriteSuccess = appUtil.writeFiles(GlobalConstants.nonRunningProcessListFileName, jsonString);

		} catch (JsonProcessingException e) {
			logger.error("Error in writing JSON");
			// e.printStackTrace();
		}

		return isWriteSuccess;
	}

	// helper function to query services data
	public ServiceBean queryService(String serviceName) {

		ServiceBean sb = new ServiceBean();

		sb.setServiceID("000"); // some default arbitary processid for non-running service

		String commandToExec = "sc queryex " + serviceName;
		String res = AppUtilImpl.shellCommandExecute(commandToExec);

		String[] val = res.split("\n");
		for (String s : val) {
			if (s.contains("STATE")) {

				char state = s.charAt(s.indexOf(":") + 2);

				// 1 Stopped
				// 2 Starting
				// 3 Stopping
				// 4 Running

				// Setting default value as unknown
				ServiceTypeEnum serviceState = ServiceTypeEnum.UNKNOWN;
				switch (state) {
				case '1':
					serviceState = ServiceTypeEnum.STOPPED;
					break;

				case '2':
					serviceState = ServiceTypeEnum.START_PENDING;
					break;

				case '3':
					serviceState = ServiceTypeEnum.STOP_PENDING;
					break;

				case '4':
					serviceState = ServiceTypeEnum.RUNNING;
					break;
				}

				sb.setServiceName(serviceName);
				sb.setServiceType(serviceState);
			} else if (s.contains("PID")) {
				String processId = s.substring(s.indexOf(":") + 2, s.length());
				sb.setServiceID(processId);

			}

		}
		return sb;

	}

	// helper function to query services data
	public ServiceBean queryServiceLinux(String serviceName) {

		ServiceBean sb = new ServiceBean();

		sb.setServiceID("000"); // some default arbitary processid for non-running service

		String commandToExec = "sudo systemctl status " + serviceName + " .service";
		String res = AppUtilImpl.shellCommandExecute(commandToExec);

		String[] val = res.split("\n");
		for (String s : val) {
			if (s.contains("Active")) {

				String state = s.substring(s.indexOf(":") + 2, s.indexOf('(') - 1);

				// Setting default value as unknown
				ServiceTypeEnum serviceState = ServiceTypeEnum.UNKNOWN;
				switch (state) {
				case "inactive":
					serviceState = ServiceTypeEnum.STOPPED;
					break;

				case "active":
					serviceState = ServiceTypeEnum.RUNNING;
					break;
				}

				sb.setServiceName(serviceName);
				sb.setServiceType(serviceState);
			} else if (s.contains("PID")) {
				String processId = s.substring(s.indexOf(":") + 2, s.indexOf('(') - 1);
				sb.setServiceID(processId);

			}

		}
		return sb;

	}

}
