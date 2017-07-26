/**
 * 
 */
package main.java.base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.ExecutionType;
import main.java.executionSetup.TestParameters;
import main.java.reporting.HtmlReport;
import main.java.testDataAccess.DataTable;
import main.java.testDataAccess.DataTableAbstractFactory;
import main.java.testDataAccess.DataTableFactoryProducer;
import main.java.utils.AppiumServerHandler;
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

	private static ExtentReports report;
	private static String absolutePath;
	private static Properties properties;
	private static Properties runtimeDataProperties;
	private static Properties testRailProperties;
	private static Properties desiredCapabilitiesProperties;
	private static DataTableAbstractFactory runManagerFactory;
	private static DataTable runManager;
	private static DataTableAbstractFactory dataTableFactory;
	private static DataTable dataTable;
	private static ExecutionType executionType;
	private static TestRailListener testRailListenter;
	private static ArrayList<TestParameters> testInstancesToRun;	
	private static ArrayList<Integer> setCategoryList = new ArrayList<>();
	private static ArrayList<TestParameters> groupedTestInstances = null;
	private static ArrayList<ArrayList<TestParameters>> groupedtestInstancesToRun;	
	private static int nThreads;
	private static FrameworkProperties globalRuntimeDataProperties;
	private static FrameworkProperties globalProperties;
	private static FrameworkProperties frameworkTestRailProperties;
	private static FrameworkProperties globalDesiredCapabilitiesProperties;
	private static Utility utility;
	private static ExtentTest runManagerTest;
	private static Lock lock;
	private static ArrayList<AndroidDriver> androidDriverList = new ArrayList<>();
	private static ArrayList<AppiumServerHandler> appiumServerInstanceList = new ArrayList<>();
	private static ArrayList<Integer> driverSequence = new ArrayList<>();
	
	private static final String globalPropertyFilePath = "./src/main/resources/PropertyFiles/GlobalProperties.properties";
	private static final String globalRuntimeDataPropertyFilePath = "./src/main/resources/PropertyFiles/GlobalRuntimeDataProperties.properties";
	private static final String testRailPropertyFilePath = "./src/main/resources/PropertyFiles/TestRail.properties";
	private static final String desiredCapabilityPropertyFilePath = "./src/main/resources/DesiredCapabilities/DesiredCapabilities.properties";
	
	

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
		collectDesiredCapabilitiesProperties();
		
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
		
		for(int i=0;i<testInstancesToRun.size();i++) {
			setCategoryList.add(Integer.parseInt(testInstancesToRun.get(i).getSetCategory())) ;
		}
		
		setCategoryList = new ArrayList<Integer>(new LinkedHashSet<Integer>(setCategoryList));
		
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
		
		if(!testInstancesToRun.isEmpty()) {
		String executionMode = properties.getProperty("ExecutionMode");	
		
		if(executionMode.equalsIgnoreCase("DISTRIBUTED")) {
			distributedExecution();
		}else if(executionMode.equalsIgnoreCase("PARALLEL")){
			parallelExecution();
		}
		}		
	    
	}
	
	
	public static void distributedExecution() {
		
		groupedtestInstancesToRun = new ArrayList<ArrayList<TestParameters>>();
		String appSetup = properties.getProperty("appSetup");
		
		Runnable testRunner = null;
		lock = new ReentrantLock();
		
		for (int t = 0; t < nThreads; t++) {

			appiumServerSetup(t + 1);
			androidDriverSetUp(t + 1);
			if(appSetup.equalsIgnoreCase("True")) {
			setupAppForTesting(t);
			}

		}

		for (int i = 0; i < setCategoryList.size(); i++) {

			groupedTestInstances = new ArrayList<TestParameters>();
			for (int j = 0; j < testInstancesToRun.size(); j++) {
				if (Integer.parseInt(testInstancesToRun.get(j).getSetCategory()) == setCategoryList.get(i)) {
					groupedTestInstances.add(testInstancesToRun.get(j));
				}

			}
			groupedtestInstancesToRun.add(groupedTestInstances);
		}

		for (int k = 0; k < groupedtestInstancesToRun.size(); k++) {

			ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);

			int groupedTestInstanceSize = groupedtestInstancesToRun.get(k).size();

			driverSequence(groupedTestInstanceSize);

			for (int currentTestInstance = 0; currentTestInstance < groupedTestInstanceSize; currentTestInstance++) {
				if (testRailProperties.getProperty("testRail.enabled").equalsIgnoreCase("True")) {
					testRunner = new Executor(groupedtestInstancesToRun.get(k).get(currentTestInstance), report,
							executionType, dataTable, testRailListenter, lock,
							androidDriverList.get(driverSequence.get(currentTestInstance)));
				} else {
					testRunner = new Executor(groupedtestInstancesToRun.get(k).get(currentTestInstance), report,
							executionType, dataTable, lock,
							androidDriverList.get(driverSequence.get(currentTestInstance)));
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

	}
	
	


	public static void parallelExecution() {
		
		int numberOfNodes = Integer.parseInt(properties.getProperty("NumberOfNodes"));
		ExecutorService[] parallelExecutor = new ExecutorService[numberOfNodes] ;	
		
		Runnable testRunner = null;
		lock = new ReentrantLock();
		
		for (int t = 0; t < numberOfNodes; t++) {

			appiumServerSetup(t + 1);
			androidDriverSetUp(t + 1);

		}
		
		for(int run = 0; run < numberOfNodes; run ++ ) {
			
			parallelExecutor[run] = Executors.newFixedThreadPool(1);
			
			for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++) {
				
				if (testRailProperties.getProperty("testRail.enabled").equalsIgnoreCase("True")) {
					testRunner = new Executor(testInstancesToRun.get(currentTestInstance), report,
							executionType, dataTable, testRailListenter, lock,
							androidDriverList.get(run));
				} else {
					testRunner = new Executor(testInstancesToRun.get(currentTestInstance), report,
							executionType, dataTable, lock,
							androidDriverList.get(run));
				}

				parallelExecutor[run].execute(testRunner);				
				

			}			
			
		}
		
		for(int run = 0; run < numberOfNodes; run ++ ) {		
		parallelExecutor[run].shutdown();
		while (!parallelExecutor[run].isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
		
		
		
	}

	
	
	public static void driverSequence(int sequenceRepeatCount) {
		
		for(int j=0;j<sequenceRepeatCount;j++) {
		
		for(int i=0;i<nThreads;i++) {
			driverSequence.add(i);
		}
		
		}
		
	}
	
	
	public static void appiumServerSetup(int selectDevice) {		
		AppiumServerHandler appiumServerHandler = new AppiumServerHandler(Integer.parseInt(desiredCapabilitiesProperties.getProperty("device"+selectDevice+".appium.port").trim())
														, desiredCapabilitiesProperties.getProperty("device"+selectDevice+".appium.bootstrap.port").trim());
		appiumServerHandler.appiumServerStart();	
		appiumServerInstanceList.add(appiumServerHandler);		
	}
	

	@SuppressWarnings("rawtypes")
	public static void androidDriverSetUp(int selectDevice) {
		
		try {
			
		AndroidDriver driver;	

		String absolutePath = new File(System.getProperty("user.dir")).getAbsolutePath();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", desiredCapabilitiesProperties.getProperty("device"+selectDevice+".deviceName"));
		capabilities.setCapability("udid", desiredCapabilitiesProperties.getProperty("device"+selectDevice+".udid"));
		capabilities.setCapability(CapabilityType.BROWSER_NAME, desiredCapabilitiesProperties.getProperty("device1"+selectDevice+".browserName"));
		capabilities.setCapability(CapabilityType.VERSION, desiredCapabilitiesProperties.getProperty("device"+selectDevice+".version"));
		capabilities.setCapability("app", absolutePath + "\\src\\main\\resources\\Libs\\" + desiredCapabilitiesProperties.getProperty("device"+selectDevice+".app"));
		capabilities.setCapability("platformName", desiredCapabilitiesProperties.getProperty("device"+selectDevice+".platformName"));
		capabilities.setCapability("appPackage", desiredCapabilitiesProperties.getProperty("device"+selectDevice+".appPackage"));
		capabilities.setCapability("appActivity", desiredCapabilitiesProperties.getProperty("device"+selectDevice+".appActivity"));
		capabilities.setCapability("unicodeKeyboard", properties.getProperty("unicodeKeyboard"));
		capabilities.setCapability("resetKeyboard", properties.getProperty("resetKeyboard"));
		capabilities.setCapability("newCommandTimeout", properties.getProperty("newCommandTimeout"));
		capabilities.setCapability("noReset", properties.getProperty("noReset"));

		
		driver = new AndroidDriver(new URL(	"http://" + properties.getProperty("RemoteAddress") + ":" + desiredCapabilitiesProperties.getProperty("device"+selectDevice+".appium.port") + "/wd/hub"),capabilities);
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);	
		
		androidDriverList.add(driver);
		
		} catch (MalformedURLException e) {		
			e.printStackTrace();
		}
		
	}
	
	
	
	

	public static void shutDownAppiumAndAndroidDriver() {

		if (!androidDriverList.isEmpty() && !appiumServerInstanceList.isEmpty())

			for (int i = 0; i < nThreads; i++) {

				if (androidDriverList.get(i) != null) {
					androidDriverList.get(i).quit();
				}

				if (appiumServerInstanceList.get(i) != null) {
					appiumServerInstanceList.get(i).appiumServerStop();
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
		shutDownAppiumAndAndroidDriver();
		
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
		
	}
	
	private static void collectTestRailProperties() {
	
	frameworkTestRailProperties = FrameworkProperties.getInstance();			
	testRailProperties = frameworkTestRailProperties.loadPropertyFile(testRailPropertyFilePath);			
	utility.setTestRailProperties(testRailProperties);
	
	}
	
	
	private static void collectDesiredCapabilitiesProperties() {
		
		globalDesiredCapabilitiesProperties = FrameworkProperties.getInstance();			
		desiredCapabilitiesProperties = globalDesiredCapabilitiesProperties.loadPropertyFile(desiredCapabilityPropertyFilePath);	
			
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
		
		executionType = ExecutionType.valueOf(properties.getProperty("ExecutionType"));
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
	
	
	
	private static void setupAppForTesting(int driverIndex) {
		
		androidDriverList.get(driverIndex).findElement(By.id("btn_connect")).click();
		androidDriverList.get(driverIndex).findElement(By.name(properties.getProperty("userProfile"))).click();
		
	}
	
	
	
		
}
