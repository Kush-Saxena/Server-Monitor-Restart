package com.cg.servermonitorrest.services.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.ServiceTypeEnum;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.servicehelpers.AnalyzerServiceHelper;


public class AnalyzeServiceImplServiceWin implements IAnalyzeService {
	// set to shared variables
	private List<String> serviceList = SharedVariables.serviceList;
	
	@Autowired
	private AnalyzerServiceHelper analyzerHelper;

	static Logger logger = LogManager.getLogger();

	@Override
	public void analyze() {

		// init a beanList to hold the service beans
		List<ServiceBean> beanList = new ArrayList<>();

		for (String serviceName : serviceList) {

			logger.debug("Fetching services using query service: " + serviceName);
			ServiceBean fetchedBean = analyzerHelper.queryService(serviceName);
			// System.out.println("Bean = "+fetchedBean);
			beanList.add(fetchedBean);
		}

		// init a list to contain the failed/non running services
		List<ServiceBean> nonRunningServiceList = new ArrayList<>();

		// init a list to contain the repeatedly failed/non running services
		// this will be sent to the mailing service with severity
		List<ServiceBean> repeatedFailedServiceList = new ArrayList<>();

		// init a list to contain the repeatedly failed/non running services
		// this will be sent to the mailing service with severity
		List<ServiceBean> nonRepeatedFailedServiceList = new ArrayList<>();

		logger.debug("Building repeated and non repeated failed lists");
		for (ServiceBean bean : beanList) {
			if (bean.getServiceType() != ServiceTypeEnum.RUNNING) {
				nonRunningServiceList.add(bean);
				boolean checkIsRepeatedFailure = analyzerHelper.isRepeatedFaliure(bean);
				if (checkIsRepeatedFailure) {
					logger.debug("Repeated Faliure detected [CRITICAL] : " + bean.getServiceName());
					repeatedFailedServiceList.add(bean);
				} else {
					nonRepeatedFailedServiceList.add(bean);
				}
			}
		}

		if (nonRunningServiceList.size() > 0) {
			logger.debug("Writing failed process to file");
			analyzerHelper.writeFailedProcessesToFile(nonRunningServiceList);

			// writing all non running services to the file
			logger.debug("Commiting values to global variables");

			// commiting values to the shared variables to be used by other services
			SharedVariables.repeatedFailedServiceList = repeatedFailedServiceList;
			SharedVariables.nonRepeatedFailedServiceList = nonRepeatedFailedServiceList;

		}

	}
}
