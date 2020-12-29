package com.cg.servermonitorrest.services.restarting;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.SharedVariables;


public class RestartingServiceImplStandaloneWin implements IRestartingService {

	private static Logger logger = LogManager.getLogger();


	@Override
	public void restartServices() {
		
		
		List<ServiceBean> serviceList = SharedVariables.nonRepeatedFailedServiceList;

	}

}
