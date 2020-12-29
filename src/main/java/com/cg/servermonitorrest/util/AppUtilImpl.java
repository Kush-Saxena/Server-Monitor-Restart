package com.cg.servermonitorrest.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cg.servermonitorrest.resources.GlobalConstants;
import com.cg.servermonitorrest.resources.ProcessDumpBean;
import com.cg.servermonitorrest.resources.ServiceBean;
import com.cg.servermonitorrest.resources.ServiceTypeEnum;

@Component
public class AppUtilImpl implements IAppUtil {

	int baseSkipConstant = 55;
	int initProcessLoc = 85;
	String basePath = GlobalConstants.basePath.substring(0, GlobalConstants.basePath.length() - 1) + "reports/";;

	private static ProcessBuilder processBuilder = new ProcessBuilder();

	private static Logger logger = LogManager.getLogger();

	public static File getFileFromName(String fileName) {

		String absolutePath = GlobalConstants.basePath.substring(0, GlobalConstants.basePath.length() - 1)
				+ "src/main/resources/" + fileName;

		return new File(absolutePath);

	}

	@Override
	public String readFiles(String fileName) {

		StringBuilder fileContent = new StringBuilder();

		String absolutePath = basePath + fileName;

		try {
			FileReader fr = new FileReader(new File(absolutePath));
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				fileContent.append(br.readLine()).append("\n");
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			logger.warn("Specified File not found in path: " + fileName);
			// System.out.println("Specified File not found in path");
		}
		return fileContent.toString();
	}

	@Override
	public boolean writeFiles(String fileName, String content) {
		logger.debug("Writing to File: " + fileName);
		try {

			// Create new file
			String absolutePath = basePath + fileName;

			FileWriter fw = new FileWriter(absolutePath);
			BufferedWriter bw = new BufferedWriter(fw);

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Specified File Path is not Found");
			return false;
		}
		return true;
	}

	public static String shellCommandExecute(String command) {

		if (GlobalConstants.platform.equalsIgnoreCase("Windows")) {

			processBuilder.command("cmd.exe", "/c", command);
		} else if (GlobalConstants.platform.equalsIgnoreCase("Ubuntu")) {
			processBuilder.command("/bin/bash", "-c", command);

		}
		StringBuilder sbuilder = new StringBuilder();

		try {

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {

				line = line.trim();
				if (!line.isEmpty()) {
					sbuilder.append(line).append("\n");
				}
			}

			process.waitFor();
			reader.close();

		} catch (Exception err) {
			err.printStackTrace();
		}
		return sbuilder.toString();
	}

	@Override
	public List<ProcessDumpBean> buildProcessBean(String processDump) {

		List<ProcessDumpBean> processBeanList = new ArrayList<>();

		if (processDump.trim().isEmpty()) {
			return processBeanList;
		}

		List<String> processProp = splitAndTrim(processDump);
		int lookupIndex = initProcessLoc;

		for (String s : processProp) {
			if (processProp.size() > lookupIndex) {
				ProcessDumpBean pBean = new ProcessDumpBean();
				String processId = processProp.get(lookupIndex);
				List<String> processDetails = findProcessInformation(processId);
				if (processDetails.size() > 0) {
					String processStatus = processDetails.get(28);
					String processName = processDetails.get(31);

					pBean.setProcessProcessId(processId);
					pBean.setProcessStatus(processStatus);
					pBean.setProcessName(processName);

					processBeanList.add(pBean);
				}
				lookupIndex += baseSkipConstant;
			}
		}

		return processBeanList;
	}

	public static List<String> splitAndTrim(String str) {
		String[] sp = str.split(" ");
		List<String> trimmedProp = new ArrayList<>();

		for (String s : sp) {
			if (!s.trim().isEmpty()) {

				trimmedProp.add(s);
			}
		}

		return trimmedProp;

	}

	private static List<String> findProcessInformation(String processId) {
		String processDetailsCommand = "tasklist /v /fi \"pid eq " + processId + "\"";
		String pDetails = shellCommandExecute(processDetailsCommand);
		List<String> sp = AppUtilImpl.splitAndTrim(pDetails);

		return sp;
	}

	public static ServiceBean processBeanToServiceBean(ProcessDumpBean pBean) {

		ServiceBean sBean = new ServiceBean();
		sBean.setServiceID(pBean.getProcessProcessId());
		sBean.setServiceName(pBean.getProcessName());

		String status = pBean.getProcessStatus();
		if (status.equalsIgnoreCase("running")) {
			sBean.setServiceType(ServiceTypeEnum.RUNNING);
		} else if (status.equalsIgnoreCase("stopped")) {
			sBean.setServiceType(ServiceTypeEnum.STOPPED);
		} else if (status.equalsIgnoreCase("unknown")) {
			sBean.setServiceType(ServiceTypeEnum.UNKNOWN);
		}

		return sBean;

	}

	public static boolean isMailIdValidFormat(String id) {

		Pattern validEmail = Pattern.compile(GlobalConstants.regex);
		return validEmail.matcher(id).matches();
	}

	private static Properties readProperties(String fileName) {

		Properties prop = new Properties();

		try {
			FileReader fr = new FileReader(fileName);
			prop.load(fr);
		} catch (FileNotFoundException e) {
			logger.error("Properties File Not found: " + fileName);
		} catch (IOException e) {
			logger.error("Unable to parse the property file : " + fileName);
		}

		return prop;
	}

	@Override
	public Map<String, String> findPropertiesWithPrefix(String fileName, String prefix) {
		Properties p = readProperties(fileName);
		HashMap<String, String> resultVals = new HashMap<>();

		Set<Entry<Object, Object>> entrySet = p.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = entry.getKey().toString();
			if (key.startsWith(prefix)) {
				resultVals.put(key, entry.getValue().toString());
			}
		}

		return resultVals;
	}
}
