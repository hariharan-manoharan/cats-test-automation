package main.java.utils;

import java.io.IOException;

public class AppiumServerHandlerCmd {

	public String port;
	public String bootstrapPort;
	public Process p;

	public AppiumServerHandlerCmd(String port, String bootstrapPort) {
		this.port = port;
		this.bootstrapPort = bootstrapPort;
	}

	public void appiumServerStart() throws IOException, InterruptedException {

		String argss1 = "cd";
		String argss2 = "C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin";
		String argss3 = "node";
		String appiumJSPath = "appium.js";
		String args1 ="-a 127.0.0.1";
		String args2 ="-p "+port;
		String args3 ="-cp "+port;
		String args4 ="-bp "+bootstrapPort;
		
		String[] cmd = new String[]{argss1,argss2,argss3,appiumJSPath,args1,args2,args3,args4} ;
		ProcessBuilder pb = new ProcessBuilder(cmd);
		Process p = pb.start();
		p.waitFor();
		Thread.sleep(10000);
		if (p != null) {
			System.out.println("Appium server Is started now.");
		}
	}

	public void appiumServerStop() throws IOException {
		if (p != null) {
			p.destroy();
		}
		System.out.println("Appium server Is stopped now.");
	}

	/*
	 * public static void main(String[] agrs) throws ExecuteException,
	 * IOException, InterruptedException { AppiumServerHandlerCmd cmd = new
	 * AppiumServerHandlerCmd("4723", "4444"); cmd.appiumServerStart();
	 * 
	 * }
	 */

}
