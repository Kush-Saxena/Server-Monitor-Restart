package com.cg.servermonitorrest.services.initialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.SharedVariables;
import com.cg.servermonitorrest.util.AppUtilImpl;
import com.cg.servermonitorrest.util.IAppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InitializeServiceImpl implements IInitializeService {

	IAppUtil appUtil = new AppUtilImpl();

	private ObjectMapper jsonMapper = new ObjectMapper();

	private static Logger logger = LogManager.getLogger();

	@Override
	public void initFailedServicesList() {

		logger.info("Started Building failed services list.");
		List<ServiceBean> serviceList = new ArrayList<>();

		// read old file data
		String fileData = appUtil.readFiles(GlobalConstants.nonRunningProcessListFileName);

		if (!(fileData == null || fileData.isEmpty())) {

			try {
				serviceList = jsonMapper.readValue(fileData, new TypeReference<List<ServiceBean>>() {
				});

				// this method creates a list with latest values only.
				HashMap<String, ServiceBean> serviceMap = new HashMap<>();
				for (ServiceBean bean : serviceList) {
					String serviceName = bean.getServiceName();
					if (serviceMap.containsKey(serviceName)) {
						ServiceBean fetchedBean = serviceMap.get(serviceName);
						if (bean.getTimeCreated().after(fetchedBean.getTimeCreated())) {
							serviceMap.put(serviceName, bean);
						} else {
							serviceMap.put(serviceName, fetchedBean);
						}
					} else {
						serviceMap.put(serviceName, bean);
					}
				}

				serviceList = new ArrayList<>(serviceMap.values());

			} catch (JsonProcessingException e) {
				logger.warn("Unable to read init file, initializing to new object");
				// e.printStackTrace();
			}
		}
		SharedVariables.failedServicesList = serviceList;

		logger.info("Completed Building failed services list.");

	}

	@Override
	public void buildMailingList() {
		logger.info("Stated Building Mailing List.");

		// building a initial list of mail recipients to send mail.
		// loading all the properties with specific prefix
		List<String> mailRecipients = new ArrayList<String>();
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(GlobalConstants.MailerFile);
			Iterator<String> mailKeys = config.getKeys(GlobalConstants.mailRecipientPropertyKey);
			while (mailKeys.hasNext()) {
				mailRecipients.add(config.getString(mailKeys.next()));
			}
		} catch (ConfigurationException e) {
			logger.error("Unable to read file: " + GlobalConstants.MailerFile);
			e.printStackTrace();
		}
		SharedVariables.mailRecipients = mailRecipients;

		logger.info("Completed Building Mailing List.");
	}

	@Override
	public void initServiceList() {
		logger.info("Start Building Services List.");

		// building a initial list of services to scan.
		// loading all the properties with specific prefix
		List<String> serviceList = new ArrayList<String>();
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(GlobalConstants.serviceFile);
			Iterator<String> serviceKeys = config.getKeys(GlobalConstants.serviceValuePropertyKey);
			while (serviceKeys.hasNext()) {
				serviceList.add(config.getString(serviceKeys.next()));
			}
		} catch (ConfigurationException e) {
			logger.error("Unable to read file: " + GlobalConstants.serviceFile);
			e.printStackTrace();
		}

		SharedVariables.serviceList = serviceList;

		logger.info("Completed Building Services List.");

	}

}
