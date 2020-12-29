package com.cg.servermonitorrest.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AutomaticllyServerStart {

	public static void main(String[] args) throws IOException {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("C:\\Apache-tomcat-9.0.30\\apache-tomcat-9.0.30\\bin\\startup.bat");

			String command = "C:\\Apache-tomcat-9.0.30\\apache-tomcat-9.0.30\\bin\\startup.bat";// for linux use .sh
			Process child = Runtime.getRuntime().exec(command);
			Process p = Runtime
					.getRuntime().exec(
							new String[] { "cmd.exe", "/C", "start",
									"C:\\Apache-tomcat-9.0.30\\apache-tomcat-9.0.30\\bin\\startup.bat", "start" },
							null, null);

			Socket socket = new Socket("localhost", 8005);
			System.out.println("socket.isConnected()===> " + socket.isConnected());

			if (socket.isConnected()) {
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println("myShutDownCommand");// send shut down command pw.close();
				socket.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}