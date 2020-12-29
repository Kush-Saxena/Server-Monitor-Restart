package com.cg.servermonitorrest.services.mailing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.util.AppUtilImpl;

@Component
public class MailServiceImpl implements IMailService {

	private static Logger logger = LogManager.getLogger();

	private static String mailFromId = GlobalConstants.mailFrom;

	@Autowired
	private static JavaMailSender mailSender;

	private static void sendMail(List<String> recipientList, String messageBody) {
		

		SimpleMailMessage msg = new SimpleMailMessage();
		String[] recipientArr = recipientList.toArray(new String[recipientList.size()]);
		msg.setTo(recipientArr);
		msg.setFrom(mailFromId);
		msg.setSubject("This is the Subject Line!");
		msg.setText(messageBody);
		logger.info("Sending mail");
		//mailSender.send(msg);
		logger.info("Mail Sent");

	}

	@Override
	public void reportToRecipients(List<ServiceBean> serviceList, boolean isSevere) {

		List<String> recipientsList = SharedVariables.mailRecipients;
		List<String> validMailList = new ArrayList<>();
		List<String> invalidMailIdList = new ArrayList<>();

		// loop to find valid and invalid mail ids.
		// report invalid lists back.
		for (String mailId : recipientsList) {

			if (AppUtilImpl.isMailIdValidFormat(mailId)) {
				validMailList.add(mailId);
			} else {
				invalidMailIdList.add(mailId);
			}
		}

		String messageSub = GlobalConstants.noSevereMessage;

		if (isSevere) {
			messageSub = GlobalConstants.severeMessage;
		}

		// a string containing the message

		StringBuilder message = new StringBuilder(messageSub);

		message.append("\n").append("Service List: ").append("\n");

		for (ServiceBean bean : serviceList) {
			message.append(bean.getServiceName()).append("\n");
		}

		// sending mail
		try {
			sendMail(validMailList, message.toString());
		} catch (Exception e) {
			logger.error("Some error occured while sending mail");
			// TODO: handle exception
		}

		// set invalid ids in a shared variables
		SharedVariables.invalidMailingIds = new HashSet<>(invalidMailIdList);

	}

	@Override
	public void reportSuccessRestart(List<String> serviceNamesList) {

		List<String> recipientsList = SharedVariables.mailRecipients;
		List<String> validMailList = new ArrayList<>();

		// loop to find valid and invalid mail ids.
		// report invalid lists back.
		for (String mailId : recipientsList) {

			if (AppUtilImpl.isMailIdValidFormat(mailId)) {
				validMailList.add(mailId);
			}
		}

		String messageSub = "Restart was Successful \n";

		// a string containing the message

		StringBuilder message = new StringBuilder(messageSub);

		message.append("\n").append("Service List: ").append("\n");

		for (String sName : serviceNamesList) {
			message.append(sName).append("\n");
		}

		// sending mail
		sendMail(validMailList, message.toString());

	}

}
