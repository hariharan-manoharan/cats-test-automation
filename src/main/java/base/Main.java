/**
 * 
 */
package main.java.base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import main.java.executionSetup.ExecutionMode;
import main.java.executionSetup.TestParameters;
import main.java.reporting.HtmlReport;
import main.java.testDataAccess.DataTable;
import main.java.testDataAccess.DataTableAbstractFactory;
import main.java.testDataAccess.DataTableFactoryProducer;
import main.java.utils.CopyLatestResult;
import main.java.utils.GlobalProperties;
import main.java.utils.TestRailListener;
import main.java.utils.Utility;


/**
 * <h1>This class contains the main method and controls the flow of the framework.</h1>
 * 
 * @author HARISH
 * @version 1.0
 * @since 05/10/2016
 */
public class Main{
	
	private static final int Current_DB = 0;
	private static ExtentReports report;
	private static String absolutePath;
	private static Properties properties;
	private static DataTableAbstractFactory runManagerFactory;
	private static DataTable runManager;
	private static DataTableAbstractFactory dataTableFactory;
	private static DataTable dataTable;
	private static ExecutionMode execMode;
	private static TestRailListener testRailListenter;
	private static ArrayList<TestParameters> testInstancesToRun;	
	private static ExtentTest test;
	private static int nThreads;
	
	

	/**
	 * This is the main method.
	 *  
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		initializeTestReport();			
		prepare();	
		initializeTestRailReporting();
		collectRunInfo();		
		setup();
		execute();
		tearDown();			
	}
	
	/**
	 * prepare method is used to get framework and execution related properties.
	 * 
	 * @param nil
	 * 
	 */

	private static void prepare() {
		
		setAbsolutePath();
		collectGlobalProperties();
		
	}	
	
	/**
	 * collectRunInfo method gathers the test instances to run from Run manager.xls.
	 * 
	 * @param nil
	 * 
	 */

	private static void collectRunInfo() {
		
		String runManagerType = properties.getProperty("RunManagerType");
		String runManagerName = properties.getProperty("RunManagerName");
		
		runManagerFactory = DataTableFactoryProducer.getDataTableFactory();
		runManager = runManagerFactory.getTestDataTableAccess(runManagerType, "./"+runManagerName);		
		testInstancesToRun = runManager.getRunManagerInfo();
		
		Utility utility = new Utility();
		utility.setEnvironmentVariables(runManager.getRowData("EnvironmentDetails", properties.getProperty("Environment")));
			
	}

	/**
	 * setup method is used to initialize the Test data access,Reporting etc...
	 * 
	 * @param nil
	 * 
	 */
	
	private static void setup() {
		
		setUpExecutionMode();
		initializeDataTable();
			
	}
		
	
	/**
	 * execute method is used to handle the execution of the test instances. 
	 * 
	 * @param nil
	 * 
	 */

	private static void execute() {

		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);
		Runnable testRunner = null;
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++) {
			if(properties.getProperty("testRail.enabled").equalsIgnoreCase("True")){
			testRunner = new Executor(testInstancesToRun.get(currentTestInstance), report, execMode, dataTable, testRailListenter);
			}else{
			testRunner = new Executor(testInstancesToRun.get(currentTestInstance), report, execMode, dataTable);	
			}
			
			parallelExecutor.execute(testRunner);
		}

		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * tearDown method is used to wrap up the execution. 
	 * 
	 * @param nil
	 * 
	 */

	private static void tearDown() {
		
		report.flush();		
		
		try {
			Desktop.getDesktop().open(new File(HtmlReport.reportPath));
			CopyLatestResult copyLatestResult = new CopyLatestResult();
			copyLatestResult.copyLatestResultFolder();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * setAbsolutePath method is used to set the absolute path of the framework
	 * which is in turn used for saving the report files, accessing test data
	 * files, Run manager etc...    
	 * 
	 * @param nil
	 * 
	 */

	private static void setAbsolutePath() {
		
		absolutePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		
	}
		
	/**
	 * collectGlobalProperties method is used to gather Global properties from user  
	 * stored in a property file called GlobalProperties.properties.  
	 * 
	 * @param nil
	 * 
	 */
	
	private static void collectGlobalProperties() {

		GlobalProperties globalProperties = GlobalProperties.getInstance();
		properties = globalProperties.loadPropertyFile();
		
		Utility utility = new Utility();
		utility.setProperties(properties);

	}
	
	/**	 
	 * 
	 * 
	 * @param nil
	 * 
	 */
	
	private static void setUpExecutionMode() {
		
		execMode = ExecutionMode.valueOf(properties.getProperty("ExecutionMode"));
		nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		
	}
	
	/**
	 * initializeDataTable method is used to create a DataTable object based on the input
	 * of TestDataTableType property. 
	 * 
	 * @param nil
	 * 
	 */

	private static void initializeDataTable() {
		
		String dataTablePath = null;
		String dataTableType = properties.getProperty("TestDataTableType");
		String dataTableName = properties.getProperty("TestDataTableName");
		
		if(dataTableType.equalsIgnoreCase("MSExcel")){
			dataTablePath = "./src/main/resources/TestData/MSExcel/"+dataTableName;
		}else if(dataTableType.equalsIgnoreCase("MSAccess")){
			dataTablePath = "./src/main/resources/TestData/MSAccess/"+dataTableName;
		}
		
		dataTableFactory = DataTableFactoryProducer.getDataTableFactory();		
		dataTable = dataTableFactory.getTestDataTableAccess(dataTableType, dataTablePath);			
	
	}
	
	/**	 
	 * 
	 * 
	 * @param nil
	 * 
	 */
	
	private static void initializeTestReport() {
		
		report = HtmlReport.getInstance();
		report.loadConfig((new File("./src/main/resources/PropertyFiles/extent-report-config.xml")));
		
	}
	
	
	private static void initializeTestRailReporting(){			
		
		if (properties.getProperty("testRail.enabled").equalsIgnoreCase("True")) {		
		int projectId = Integer.parseInt(properties.getProperty("testRail.projectId"));
		testRailListenter = new TestRailListener(projectId);
		testRailListenter.initialize();
		if (properties.getProperty("testRail.addNewRun").equalsIgnoreCase("True")) {	
		testRailListenter.addTestRun();
		}
		}
	}
	
	
		
}
