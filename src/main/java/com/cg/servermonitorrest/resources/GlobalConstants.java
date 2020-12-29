package com.cg.servermonitorrest.resources;

import java.io.File;

public interface GlobalConstants {

	public static int sleepInterval = 30;
	public static String platform = "Ubuntu";
	public static RunningModes runningMode = RunningModes.ServiceRunningMode;
	public static String serverType = "Tomcat";

	public static String basePath = new File(".").getAbsolutePath();
	public static String nonRunningProcessListFileName = "FailedProcesses";

	// RFC 5322
	public static String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	// mailing constants
	public static String mailFrom = "kk@abc.com";
	//public static String mailHost = "smtp.mailtrap.io";
	//public static String mailPort = "2525";
	//public static String mailUsername = "cb5e5f80a79111"; // "96ad25283e4d8f";
	//public static String mailPassword = "1f2f660bb03a81";// "ad65f935eb9519";

	public static String mailRecipientPropertyKey = "recipient";
	public static String noSevereMessage = "Auto restart started";
	public static String severeMessage = "Auto restart failed, Manual restart required";
	public static String successMessage = "Restart Successful";

	// allowed difference between app faliure and next start
	public static long allowedHourDifference = 3;
	
	//app rerun minutes
	public static int AppRerunMinutes = 5;
	
	//restart success check time
	public static long RestartCheckTime = 1;

	public static String MailerFile = "mailer.properties";
	public static String serviceFile = "monitor.properties";
	public static String serviceValuePropertyKey = "service";
	// === ProcessDump properties ===//
	/*
	 * public static int captionIndex = 46; public static int commandLineIndex = 47;
	 * public static int creationClassNameIndex = 48; public static int
	 * creationDateIndex = 49; public static int csCreationClassNameIndex = 50;
	 * public static int csNameIndex = 51; public static int descriptionIndex = 52;
	 * public static int executablePathIndex = 53; public static int
	 * executionStateIndex = 54; public static int handleIndex = 55; public static
	 * int handleCountIndex = 56; public static int installDateIndex = 57; public
	 * static int kernelModeTimeIndex = 58; public static int
	 * maximumWorkingSetSizeIndex = 59; public static int minimumWorkingSetSizeIndex
	 * = 60; public static int nameIndex = 61; public static int
	 * osCreationClassNameIndex = 62; public static int osNameIndex = 63; public
	 * static int otherOperationCountIndex = 64; public static int
	 * otherTransferCountIndex = 65; public static int pageFaultsIndex = 66; public
	 * static int pageFileUsageIndex = 67; public static int parentProcessIdIndex =
	 * 68; public static int peakPageFileUsageIndex = 69; public static int
	 * peakVirtualSizeIndex = 70; public static int peakWorkingSetSizeIndex = 71;
	 * public static int priorityIndex = 72; public static int privatePageCountIndex
	 * = 73; public static int processIdIndex = 74; public static int
	 * quotaNonPagedPoolUsageIndex = 75; public static int quotaPagedPoolUsageIndex
	 * = 76; public static int quotaPeakNonPagedPoolUsageIndex = 77; public static
	 * int quotaPeakPagedPoolUsageIndex = 78; public static int
	 * readOperationCountIndex = 79; public static int readTransferCountIndex = 80;
	 * public static int sessionIdIndex = 81; public static int statusIndex = 82;
	 * public static int terminationDateIndex = 83; public static int
	 * threadCountIndex = 84; public static int userModeTimeIndex = 85; public
	 * static int virtualSizeIndex = 86; public static int windowsVersionIndex = 87;
	 * public static int workingSetSizeIndex = 88; public static int
	 * writeOperationCountIndex = 89; public static int writeTransferCountIndex =
	 * 90;
	 */
	

}
