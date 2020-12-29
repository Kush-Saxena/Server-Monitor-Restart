package com.cg.servermonitorrest.services.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.util.AppUtilImpl;

@Component
public class WebHelperService {

	private static Logger logger = LogManager.getLogger();

	public boolean updateServiceList(String serviceName) {

		if (serviceName == null || serviceName.isEmpty()) {
			logger.error("Empty String detected");
			return false;
		}

		try {
			updateConfigFile(GlobalConstants.serviceValuePropertyKey, serviceName, GlobalConstants.serviceFile,
					SharedVariables.serviceList);
			return true;
		} catch (ConfigurationException | IOException e) {
			logger.error("Unable to write to file: " + GlobalConstants.serviceFile);
			e.printStackTrace();
			return false;
		}

	}

	public boolean updateMailingList(String recipientId) {
		if (recipientId == null || recipientId.isEmpty()) {
			logger.error("Empty String detected");
			return false;
		}

		if (!AppUtilImpl.isMailIdValidFormat(recipientId)) {
			logger.error("Invalid emailId Detected");
			return false;
		}

		try {
			updateConfigFile(GlobalConstants.mailRecipientPropertyKey, recipientId, GlobalConstants.MailerFile,
					SharedVariables.mailRecipients);
			return true;
		} catch (ConfigurationException | IOException e) {
			logger.error("Unable to write to file: " + GlobalConstants.MailerFile);
			e.printStackTrace();
			return false;
		}

	}

	private void updateConfigFile(String propName, String propVal, String fileName, List<String> list)
			throws ConfigurationException, IOException {

		File f = AppUtilImpl.getFileFromName(fileName);
		FileInputStream configStream = new FileInputStream(f);
		Properties prop = new Properties();
		prop.load(configStream);
		configStream.close();
		if (!prop.containsValue(propVal)) {
			int listSize = list.size() + 1;
			prop.setProperty(propName + "." + listSize, propVal);
			FileOutputStream output = new FileOutputStream(f.getAbsoluteFile());
			prop.store(output, "FileUpdated");
			output.close();

			list.add(propVal);
		} else {
			logger.error("Property already present in properties file");
			throw new IOException();
		}
	}

}
