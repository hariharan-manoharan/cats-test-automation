/**
 * 
 */
package main.java.base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import main.java.executionSetup.ExecutionMode;
import main.java.executionSetup.TestParameters;
import main.java.reporting.HtmlReport;
import main.java.testDataAccess.DataTable;
import main.java.testDataAccess.DataTableAbstractFactory;
import main.java.testDataAccess.DataTableFactoryProducer;
import main.java.utils.CopyLatestResult;
import main.java.utils.FrameworkProperties;
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
	private static Properties runtimeDataProperties;
	private static Properties testRailProperties;
	private static DataTableAbstractFactory runManagerFactory;
	private static DataTable runManager;
	private static DataTableAbstractFactory dataTableFactory;
	private static DataTable dataTable;
	private static ExecutionMode execMode;
	private static TestRailListener testRailListenter;
	private static ArrayList<TestParameters> testInstancesToRun;	
	private static ExtentTest test;
	private static int nThreads;
	private static FrameworkProperties globalRuntimeDataProperties;
	private static FrameworkProperties globalProperties;
	private static FrameworkProperties frameworkTestRailProperties;
	private static Utility utility;
	private static ExtentTest runManagerTest;
	
	private static final String globalPropertyFilePath = "./src/main/resources/PropertyFiles/GlobalProperties.properties";
	private static final String globalRuntimeDataPropertyFilePath = "./src/main/resources/PropertyFiles/GlobalRuntimeDataProperties.properties";
	private static final String testRailPropertyFilePath = "./src/main/resources/PropertyFiles/TestRail.properties";
	
	

	/**
	 * This is the main method.
	 *  
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		prepare();	
		initializeTestReport();		
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
		initializeGlobalRuntimeDataProperties();
		collectTestRailProperties();
		
	}	
	
	/**
	 * collectRunInfo method gathers the test instances to run from Run manager.xls.
	 * 
	 * @param nil
	 * 
	 */

	@SuppressWarnings("unchecked")
	private static void collectRunInfo() {
		
		String runManagerType = properties.getProperty("RunManagerType");
		String runManagerName = properties.getProperty("RunManagerName");
		
		runManagerFactory = DataTableFactoryProducer.getDataTableFactory();
		runManager = runManagerFactory.getTestDataTableAccess(runManagerType, "./"+runManagerName);		
		testInstancesToRun = runManager.getRunManagerInfo();
		
		if(testInstancesToRun.isEmpty()) {
			runManagerTest = report.startTest("Run Manager status");
			runManagerTest.log(LogStatus.FATAL, "No test cases are selected in Run Manager for execution.");
		}
		
		Collections.sort(testInstancesToRun);
		
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
		int totalTestInstanceToRun = testInstancesToRun.size();		
		
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++) {
			totalTestInstanceToRun--;
			if(testRailProperties.getProperty("testRail.enabled").equalsIgnoreCase("True")){
			testRunner = new Executor(testInstancesToRun.get(currentTestInstance), report, execMode, dataTable, testRailListenter, totalTestInstanceToRun);
			}else{
			testRunner = new Executor(testInstancesToRun.get(currentTestInstance), report, execMode, dataTable, totalTestInstanceToRun);	
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
		globalRuntimeDataProperties.writeGlobalRuntimeDataProperties(globalRuntimeDataPropertyFilePath, utility.getRuntimeDataProperties());	
		frameworkTestRailProperties.writeGlobalRuntimeDataProperties(testRailPropertyFilePath, utility.getTestRailProperties());
		
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

		globalProperties = FrameworkProperties.getInstance();
		properties = globalProperties.loadPropertyFile(globalPropertyFilePath);
		
		utility = new Utility();
		utility.setProperties(properties);
		utility.setNewServerSetupForEachTestcase();

	}
	
	private static void collectTestRailProperties() {
	
	frameworkTestRailProperties = FrameworkProperties.getInstance();			
	testRailProperties = frameworkTestRailProperties.loadPropertyFile(testRailPropertyFilePath);			
	utility.setTestRailProperties(testRailProperties);
	
	}
	
	/**
	 * collectGlobalProperties method is used to gather Global properties from user  
	 * stored in a property file called GlobalProperties.properties.  
	 * 
	 * @param nil
	 * 
	 */
	
	private static void initializeGlobalRuntimeDataProperties() {

		globalRuntimeDataProperties = FrameworkProperties.getInstance();
		runtimeDataProperties = globalRuntimeDataProperties.loadPropertyFile(globalRuntimeDataPropertyFilePath);
		
		utility.setRuntimeDataProperties(runtimeDataProperties);

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
		report.addSystemInfo("Project", properties.getProperty("Project"));
		report.addSystemInfo("Environment", properties.getProperty("Environment"));
		report.addSystemInfo("Project ID", testRailProperties.getProperty("testRail.projectId"));
		report.addSystemInfo("Suite ID", testRailProperties.getProperty("testRail.suiteId"));
		report.addSystemInfo("Test Run name", testRailProperties.getProperty("testRail.testRunName"));		
		
	}
	
	
	private static void initializeTestRailReporting(){			
		
		if (testRailProperties.getProperty("testRail.enabled").equalsIgnoreCase("True")) {			
		int projectId = Integer.parseInt(testRailProperties.getProperty("testRail.projectId"));
		testRailListenter = new TestRailListener(projectId);
		testRailListenter.initialize();
		if (testRailProperties.getProperty("testRail.addNewRun").equalsIgnoreCase("True")) {	
		testRailListenter.addTestRun();
		}
		}
	}
	
	
		
}
