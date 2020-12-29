package com.cg.servermonitorrest.services.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cg.servermonitorrest.resources.ProcessDumpBean;
import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.ServiceTypeEnum;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.servicehelpers.AnalyzerServiceHelper;
import com.cg.servermonitorrest.util.AppUtilImpl;
import com.cg.servermonitorrest.util.IAppUtil;

public class AnalyzeServiceImplStandaloneWin implements IAnalyzeService {

	private AnalyzerServiceHelper analyzerHelper;

	@Autowired
	private IAppUtil appUtil;

	static Logger logger = LogManager.getLogger();


	@Override
	public void analyze() {

		// command to execute in case of standalone mode
		String commandToExec = "wmic process where \"commandline like '%tomcat%' and name='java.exe'\"";

		// result in form of string for the executed command
		String commandResult = AppUtilImpl.shellCommandExecute(commandToExec);

		// System.out.println(commandResult);

		// converting String result to process Beans
		List<ProcessDumpBean> pBeanList = appUtil.buildProcessBean(commandResult);

		if (pBeanList.size() > 0) {

			logger.info("Process Found: " + pBeanList.size());

			// init a beanList to hold the service beans
			List<ServiceBean> beanList = new ArrayList<>();

			// loop to convert all processdumpbeans to servicebeans
			logger.debug("Converting all process beans to service beans");
			for (ProcessDumpBean pBean : pBeanList) {
				// for each process bean, executing an internal command to get running status
				// and convert it to a service bean.
				ServiceBean sb = AppUtilImpl.processBeanToServiceBean(pBean);
				beanList.add(sb);
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
					if (analyzerHelper.isRepeatedFaliure(bean)) {
						repeatedFailedServiceList.add(bean);
					} else {
						nonRepeatedFailedServiceList.add(bean);
					}
				}
			}

			logger.debug("Writing failed process to file");
			// writing all non running services to the file
			analyzerHelper.writeFailedProcessesToFile(nonRunningServiceList);

			logger.debug("Commiting values to global variables");
			// commiting values to the shared variables to be used by other services
			SharedVariables.repeatedFailedServiceList = repeatedFailedServiceList;
			SharedVariables.nonRepeatedFailedServiceList = nonRepeatedFailedServiceList;

		} else {
			logger.info("No Apps Found");
		}

	}

}
