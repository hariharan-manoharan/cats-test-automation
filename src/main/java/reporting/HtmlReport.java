package main.java.reporting;


import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

import main.java.utils.Utility;

public class HtmlReport extends Utility {

	public static String reportFolderName = "Run_" + getCurrentFormattedTime();
	
	public static String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();

	public static String reportPath = relativePath+ "/Results/" + reportFolderName + "/TestSummary.html";
	


	// Create an object of HtmlReport

	private static ExtentReports instance = new ExtentReports(reportPath, true, NetworkMode.OFFLINE);

	// Create constructor private so that this class cannot be instantiated

	private HtmlReport() {

	}

	// Get the only object available

	public static ExtentReports getInstance() {
		return instance;
	}

}
