package main.java.reporting;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

import main.java.utils.Utility;

public class HtmlReportParallel extends Utility{
	
	private String reportFolderName = "Parallel_Run_" + getCurrentFormattedTime("dd_MMM_yyyy_hh_mm_ss");	
	private String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
	private String reportPath;
	
	public HtmlReportParallel(String reportName){
		this.reportPath = relativePath+ "/Results/" + reportFolderName + reportName +".html";
	}
	
	public ExtentReports initialize() {	
		
		return new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
		
	}

}
