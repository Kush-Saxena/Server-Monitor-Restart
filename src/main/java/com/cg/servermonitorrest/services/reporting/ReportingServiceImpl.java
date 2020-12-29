package com.cg.servermonitorrest.services.reporting;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.services.mailing.IMailService;

@Component
public class ReportingServiceImpl implements IReportService {

	@Autowired
	private IMailService mailService;

	private static Logger logger = LogManager.getLogger();

	@Override
	public void notifyClients() {

		List<ServiceBean> repeatedFailedServices = SharedVariables.repeatedFailedServiceList;
		List<ServiceBean> nonRepeatedFailedServices = SharedVariables.nonRepeatedFailedServiceList;

		// sending mail to recipients with severity
		// isSevere = true (Manual restart required)
		// isSevere = false (Auto restart)

		if (repeatedFailedServices.size() > 0) {
			mailService.reportToRecipients(repeatedFailedServices, true);
		}
		if (nonRepeatedFailedServices.size() > 0) {
			mailService.reportToRecipients(nonRepeatedFailedServices, false);
		}

		// log the invalid recipients
		logInvalidRecipientId();
	}

	// this function writes invalid email Id to log
	private void logInvalidRecipientId() {
		Set<String> invalidRecipientList = SharedVariables.invalidMailingIds;

		for (String recipient : invalidRecipientList) {

			logger.error("Invalid ID found while mailing: " + recipient);
		}

	}

}
