package main.java.reporting;

import java.io.File;
import java.util.ArrayList;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

import main.java.utils.Utility;

public class HtmlReport extends Utility{
	
	public static String reportFolderName;	
	public static ArrayList<String> staticReportPath = new ArrayList<String>();
	public String reportPath;
	public static String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
	
	public HtmlReport(String reportName, String reportFolderName){
		HtmlReport.reportFolderName = reportFolderName;
		HtmlReport.staticReportPath.add(relativePath+ "/Results/" + reportFolderName +"/"+ reportName +".html");
		this.reportPath = relativePath+ "/Results/" + reportFolderName +"/"+ reportName +".html";
	}
	
	public ExtentReports initialize() {	
		
		return new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
		
	}

}
