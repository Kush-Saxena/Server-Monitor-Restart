package com.cg.servermonitorrest.services.mailing;

import java.util.List;

import com.cg.servermonitorrest.resources.ServiceBean;

public interface IMailService {

	void reportToRecipients(List<ServiceBean> serviceList, boolean isSevere);
	
	void reportSuccessRestart(List<String> serviceNamesList);

}
