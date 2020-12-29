package com.cg.servermonitorrest.services.cleanup;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.SharedVariables;

@Component
public class CleanupService {

	public static void cleanLists() {
		SharedVariables.failedServicesList = new ArrayList<ServiceBean>();
		SharedVariables.repeatedFailedServiceList = new ArrayList<ServiceBean>();
		SharedVariables.nonRepeatedFailedServiceList = new ArrayList<ServiceBean>();
	}

}
