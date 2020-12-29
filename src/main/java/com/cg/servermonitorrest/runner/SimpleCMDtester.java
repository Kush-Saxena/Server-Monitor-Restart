package com.cg.servermonitorrest.runner;

import com.cg.servermonitorrest.util.AppUtilImpl;

public class SimpleCMDtester {

	public static void main(String[] args) {

		
		//System.out.println("s = " + ss+ "xyz");
		String serviceName = "tomcat";

		String commandToExec = "sudo systemctl status " + serviceName + " .service";

		// result in form of string for the executed command
		String commandResult = AppUtilImpl.shellCommandExecute(commandToExec);
		System.out.println("result==");
		System.out.println(commandResult);

		String[] val = commandResult.split("\n");
		for (String s : val) {
			System.out.println(" val : " + s);

			if (s.contains("Active")) {
				String processId = s.substring(s.indexOf(":") + 2, s.length());
				System.out.println("process id = " + processId);

			}
		}
	}

}
