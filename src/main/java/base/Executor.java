package main.java.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.ExecutionMode;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.AppiumServerHandler;
import main.java.utils.AppiumServerHandlerCmd;
import main.java.utils.TestRailListener;
import main.java.utils.Utility;

public class Executor extends Utility implements Runnable {

	private static ExtentReports report; 
	private static ExtentTest test;
	private TestParameters testParameters;
	private ExecutionMode execMode;
	private DataTable dataTable;
	private static AppiumServerHandler appiumServerHandler;
	private AppiumServerHandlerCmd appiumServerHandlerCmd;
	private int totalTestInstanceToRun ;
	@SuppressWarnings("rawtypes")
	private static AndroidDriver driver;
	private TestRailListener testRailListenter;
	String testRailEnabled = properties.getProperty("testRail.enabled");
	int projectId = Integer.parseInt(properties.getProperty("testRail.projectId"));
	
	private static int testCaseExecuted = 0;
	public static int driverInstanceCount = 0;
	public static int appiumServerInstanceCount = 0;
	private int totalKeywords = 0;
	private int keywordCounter = 0;
	
	
		
	LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
	

	public Executor(TestParameters testParameters, ExtentReports report, ExecutionMode execMode, DataTable dataTable, TestRailListener testRailListenter, int totalTestInstanceToRun) {
		this.testParameters = testParameters;
		Executor.report = report;
		this.execMode = execMode;
		this.dataTable = dataTable;
		this.testRailListenter = testRailListenter;
		this.totalTestInstanceToRun = totalTestInstanceToRun;

	}
	
	public Executor(TestParameters testParameters, ExtentReports report, ExecutionMode execMode, DataTable dataTable, int totalTestInstanceToRun) {
		this.testParameters = testParameters;
		Executor.report = report;
		this.execMode = execMode;
		this.dataTable = dataTable;
		this.totalTestInstanceToRun = totalTestInstanceToRun;

	}

	@Override
	public void run() {
		try {
			if (testParameters.getExecuteCurrentTestCase().equalsIgnoreCase("Yes")) {
				test = report.startTest(testParameters.getCurrentTestCase() + " : " + testParameters.getDescription());
				dataTable.setCurrentRow(testParameters.getCurrentTestCase());
				test.log(LogStatus.INFO, testParameters.getCurrentTestCase() + " execution started", "");				

				if (testParameters.getConnectDB().equalsIgnoreCase("Yes")) {
					Getconnections();
				}

				executeKeywords(getKeywords());

				if (testParameters.getConnectDB().equalsIgnoreCase("Yes")) {
					Closeconnections();
				}

				test.log(LogStatus.INFO, testParameters.getCurrentTestCase() + " execution completed", "");
				report.endTest(test);
				report.flush();				
					
				if(test.getRunStatus() == LogStatus.PASS && testRailEnabled.equalsIgnoreCase("True")){
				testRailListenter.addTestResult(Integer.parseInt(testParameters.getCurrentTestCase()), 1);
				}else if (testRailEnabled.equalsIgnoreCase("True")) {
				testRailListenter.addTestResult(Integer.parseInt(testParameters.getCurrentTestCase()), 5);	
				}
			}
		} catch (SessionNotCreatedException e) {
			test.log(LogStatus.FAIL, "Android Driver and Appium server setup not done Successfully", "");
			test.log(LogStatus.FAIL, e);	
			report.flush();	
			return;
		} catch (ExecuteException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			test.log(LogStatus.FAIL, e);	
			if(driver!=null){
			report(driver, test, "Exception occured", LogStatus.FAIL);
			exceptionHandler();
			}
			report.flush();				
			return;
		} catch (IOException | InterruptedException | TimeoutException | NoSuchElementException e) {
			test.log(LogStatus.FAIL, e);
			if(driver!=null){
				report(driver, test, "Exception occured", LogStatus.FAIL);
				exceptionHandler();
				}
			report.flush();				
			return;
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
			if(driver!=null){
				report(driver, test, "Exception occured", LogStatus.FAIL);
				exceptionHandler();
				}
			report.flush();				
			return;
		} finally {			
			end();
		}
	}

	public void executeKeywords(LinkedHashMap<String, String> keywords)
			throws ExecuteException, IOException, InterruptedException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, SessionNotCreatedException, TimeoutException, NoSuchElementException {
		
		
		if(!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {
		testCaseExecuted++;
		if(newServerSetupForEachTestcase.equalsIgnoreCase("False") && driverInstanceCount==0){
		driverInstanceCount++;		
		driverSetUp();
		}else if(newServerSetupForEachTestcase.equalsIgnoreCase("True") && driverInstanceCount==0) {		
		driverSetUp();	
		}
		}
		
		if(testParameters.getBusinessFlowClass().equalsIgnoreCase("ReusableLibrary")){
		getFields();
		getData();		
		}		
		
		Method method;		

		Class<?> className = Class.forName("main.java.businessComponents." + execMode + "."
				+ properties.getProperty("Project") + "."+testParameters.getBusinessFlowClass());
		Constructor<?> constructor = className.getDeclaredConstructors()[0];
		Object classInstance = constructor.newInstance(test, driver, dataTable, testParameters);
		
		totalKeywords = keywords.size();

		for (Entry<String, String> map : keywords.entrySet()) {
			if (!map.getKey().equals("TC_ID")) {
				keywordCounter++;
				String currentKeyword = map.getValue().substring(0, 1).toLowerCase() + map.getValue().substring(1);
				test.log(LogStatus.INFO, "Current Keyword - " + currentKeyword, "");
				
				if(newServerSetupForEachTestcase.equalsIgnoreCase("False") && (testCaseExecuted>1) && (currentKeyword.equals("createNewConnection")
						|| currentKeyword.equals("login")||currentKeyword.equals("selectUserProfile"))) {
					test.log(LogStatus.INFO, "Keyword - " + currentKeyword + " is skipped", "");
					continue;
				}
				
				
				switch(currentKeyword){
				
				case "enterText":	
				case "verifyAutopopulatefieldvalues":
				case "clickConfirmPrompt":
				case "getPutTestdata":	
					method = className.getDeclaredMethod(currentKeyword, String.class, String.class);
					method.invoke(classInstance, fieldMap.get("KEYWORD_"+keywordCounter), dataMap.get("KEYWORD_"+keywordCounter));	
					break;
				case "enterTextFormattedData":	
					method = className.getDeclaredMethod(currentKeyword, String.class, String.class, String.class);
					method.invoke(classInstance, fieldMap.get("KEYWORD_"+keywordCounter), dataMap.get("KEYWORD_"+keywordCounter), map.getKey());	
					break;					
				case "clickRoutineFolder":
				case "clickRoutine":
				case "selectPickListValue":
				case "validateLoopField":
				case "clickYesConfirmPromptContains":
				case "clickNoConfirmPromptContains":
				case "selectUserProfile":
				case "clickOkPrompt":
				case "clickSpyGlass":
				case "waitForSeconds":
				case "clickNextMultiple":
					method = className.getDeclaredMethod(currentKeyword, String.class);
					method.invoke(classInstance, dataMap.get("KEYWORD_"+keywordCounter));
					break;	

				default:
					method = className.getDeclaredMethod(currentKeyword);
					method.invoke(classInstance);
					break;
					
					
				}
			
				
			}
		}
		
		keywordCounter = 0;

		end();
	}

	
	/**
	 * Function to get Business Keywords
	 * 
	 * @param1 nil
	 * @return LinkedHashMap<String, String>
	 * @author Hari
	 * @since 01/05/2017
	 * 
	 */
	
	public LinkedHashMap<String, String> getKeywords() {

		LinkedHashMap<String, String> keywordMap = new LinkedHashMap<String, String>();
		keywordMap = dataTable.getRowData("BusinessFlow",testParameters.getCurrentTestCase());
		return keywordMap;

	}
	
	
	/**
	 * Function to get Fields
	 * 
	 * @param1 nil
	 * @return LinkedHashMap<String, String>
	 * @author Hari
	 * @since 01/05/2017
	 * 
	 */
	
	public LinkedHashMap<String, String> getFields() {
	
		fieldMap = dataTable.getRowData("BusinessFlow",testParameters.getCurrentTestCase()+"_FIELD");
		fieldMap.remove("TC_ID");
		return fieldMap;

	}
	
	
	/**
	 * Function to get Data
	 * 
	 * @param1 nil
	 * @return LinkedHashMap<String, String>
	 * @author Hari
	 * @since 01/05/2017
	 * 
	 */
	
	public LinkedHashMap<String, String> getData() {
		
		dataMap = dataTable.getRowData("BusinessFlow",testParameters.getCurrentTestCase()+"_DATA");
		dataMap.remove("TC_ID");
		return dataMap;

	}

	@SuppressWarnings("rawtypes")
	public void driverSetUp() throws ExecuteException, IOException, InterruptedException, SessionNotCreatedException {
		

		if(!testParameters.getCurrentTestCase().contains("STAGE_DATA") && newServerSetupForEachTestcase.equalsIgnoreCase("False") && appiumServerInstanceCount==0){
		appiumServerInstanceCount++;
		appiumServerHandler = new AppiumServerHandler(Integer.parseInt(testParameters.getPort()),
				testParameters.getBootstrapPort());
		appiumServerHandler.appiumServerStart();
		}else if(!testParameters.getCurrentTestCase().contains("STAGE_DATA") && newServerSetupForEachTestcase.equalsIgnoreCase("True") && appiumServerInstanceCount==0) {
			appiumServerHandler = new AppiumServerHandler(Integer.parseInt(testParameters.getPort()),
					testParameters.getBootstrapPort());
			appiumServerHandler.appiumServerStart();	
		}

		String absolutePath = new File(System.getProperty("user.dir")).getAbsolutePath();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", testParameters.getDeviceName());
		capabilities.setCapability("udid", testParameters.getUdid());
		capabilities.setCapability(CapabilityType.BROWSER_NAME, testParameters.getBROWSER_NAME());
		capabilities.setCapability(CapabilityType.VERSION, testParameters.getVERSION());
		capabilities.setCapability("app", absolutePath +
		 "\\src\\main\\resources\\Libs\\" + testParameters.getApp());
		capabilities.setCapability("platformName", testParameters.getPlatformName());
		capabilities.setCapability("appPackage", testParameters.getAppPackage());
		capabilities.setCapability("appActivity", testParameters.getAppActivity());
		capabilities.setCapability("unicodeKeyboard", "true");
		capabilities.setCapability("resetKeyboard", "true");
		capabilities.setCapability("newCommandTimeout", properties.getProperty("New.Command.TimeOut"));		
		capabilities.setCapability("noReset", true);
		
		driver = new AndroidDriver(new URL(
				"http://" + properties.getProperty("RemoteAddress") + ":" + testParameters.getPort() + "/wd/hub"),
				capabilities);

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		test.log(LogStatus.INFO, "Android Driver and Appium server setup done Successfully", "");

	}


	public void end() {		
		
		if(newServerSetupForEachTestcase.equalsIgnoreCase("False") && totalTestInstanceToRun==0){
			
		if (driver != null) {
			driver.quit();
		}

		if (appiumServerHandler != null) {
			appiumServerHandler.appiumServerStop();
		}
		}else if (newServerSetupForEachTestcase.equalsIgnoreCase("True")) {
			if (driver != null) {
				driver.quit();
			}

			if (appiumServerHandler != null) {
				appiumServerHandler.appiumServerStop();
			}	
		}

	}
	
	
	public void exceptionHandler() {
		if(!testParameters.getCurrentTestCase().contains("STAGE_DATA") && (totalKeywords-keywordCounter)>=2) {
			
			if(isElementPresent(ID_MESSAGE, "Prompt Message")) {
				GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title"));
				Click(ID_MESSAGE_OK, "Clicked 'Ok' for prompt");
			}
			
			clickRoutineBackButton();
			clickRoutineBackButton();
		}
	}

}
