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
	//private AppiumServerHandlerCmd appiumServerHandlerCmd;
	private int totalTestInstanceToRun ;
	@SuppressWarnings("rawtypes")
	private static AndroidDriver driver;
	private TestRailListener testRailListenter;
	String testRailEnabled = testRailProperties.getProperty("testRail.enabled");
	int projectId = Integer.parseInt(testRailProperties.getProperty("testRail.projectId"));
	
	private static int testCaseExecuted = 0;
	public static int driverInstanceCount = 0;
	public static int appiumServerInstanceCount = 0;
	private int totalKeywords = 0;
	private int keywordCounter = 0;
	private static boolean networkFlag= true;
	
	
	LinkedHashMap<String, String> keywordMap = new LinkedHashMap<String, String>();
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
				
				if(!networkFlag) {
					test.log(LogStatus.WARNING,"<font color=red><b>Netwok not available. Suspending this testcase execution</b></font>", "");	
					return;
				}

				if (testParameters.getConnectDB().equalsIgnoreCase("Yes")) {
					Getconnections();
				}

				getKeywords();
				
				if(!keywordMap.isEmpty()) {
				executeKeywords(keywordMap);
				}

				if (testParameters.getConnectDB().equalsIgnoreCase("Yes")) {
					Closeconnections();
				}

				test.log(LogStatus.INFO, testParameters.getCurrentTestCase() + " execution completed", "");
				report.endTest(test);
				report.flush();		
				
				testRailReport();
			}
		} catch (SessionNotCreatedException e) {
			test.log(LogStatus.FAIL, "Android Driver and Appium server setup not done Successfully", "");
			test.log(LogStatus.FAIL, e);	
			report.flush();
			testRailReport();
			return;
		} catch (ExecuteException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			test.log(LogStatus.FAIL, e);	
			if(driver!=null){
			report(driver, test, "Exception occured", LogStatus.FAIL);
			exceptionHandler();
			}
			report.flush();	
			testRailReport();
			return;
		} catch (IOException | InterruptedException | TimeoutException | NoSuchElementException e) {
			test.log(LogStatus.FAIL, e);
			if(driver!=null){
				report(driver, test, "Exception occured", LogStatus.FAIL);
				exceptionHandler();
				}
			report.flush();	
			testRailReport();
			return;
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
			if(driver!=null){
				report(driver, test, "Exception occured", LogStatus.FAIL);
				exceptionHandler();
				}
			report.flush();	
			testRailReport();
			return;
		} finally {			
			end();
		}
	}

	public void executeKeywords(LinkedHashMap<String, String> keywords)
			throws ExecuteException, IOException, InterruptedException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			SecurityException, SessionNotCreatedException, TimeoutException, NoSuchElementException {

		if (!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {
			testCaseExecuted++;
			if (newServerSetupForEachTestcase.equalsIgnoreCase("False") && driverInstanceCount == 0) {
				driverInstanceCount++;
				driverSetUp();
			} else if (newServerSetupForEachTestcase.equalsIgnoreCase("True") && driverInstanceCount == 0) {
				driverSetUp();
			}
		}

		if (!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {
			getFields();
			getData();
		}
		

		totalKeywords = keywords.size();	
		
		Class<?> dynamicClass = null;
		Constructor<?> constructor = null;
		Object classInstance = null;
		Method method = null;
		String className = null;


		for (Entry<String, String> map : keywords.entrySet()) {
			
			boolean isMethodFound = false;
			
			if (!map.getKey().equals("TC_ID") && !(map.getValue().length()<=1)) {
				
				//System.out.println(map.getKey() + " - " +map.getValue() + " - length() - "+ map.getValue().length());
				
				String currentKeyword = map.getValue().substring(0, 1).toLowerCase() + map.getValue().substring(1);
				test.log(LogStatus.INFO,
						"<font size=2 face = Bedrock color=blue><b>" + currentKeyword.toUpperCase() + "</font></b>",
						"");
				

				if (newServerSetupForEachTestcase.equalsIgnoreCase("False") && (testCaseExecuted > 1)
						&& (currentKeyword.equals("createNewConnection") || currentKeyword.equals("login")
								|| currentKeyword.equals("selectUserProfile"))) {
					test.log(LogStatus.INFO, "Keyword - " + currentKeyword + " is skipped", "");
					continue;
				}	

			File packageDirectory = new File(
					"./src/main/java/businessComponents/" + execMode + "/" + properties.getProperty("Project"));

			File[] packageFiles = packageDirectory.listFiles();			

			for (int i = 0; i < packageFiles.length; i++) {		
				
				if(isMethodFound) {
					break;
				}				
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();

				if (fileName.endsWith(".class")) {
					 className = fileName.substring(0, fileName.length() - ".class".length());
				}else if (fileName.endsWith(".java")){
					 className = fileName.substring(0, fileName.length() - ".java".length());
				}

				

					String currentKey = map.getKey();
					testParameters.setCurrentKeywordColumnName(map.getKey());
					

					dynamicClass = Class.forName("main.java.businessComponents." + execMode + "." + properties.getProperty("Project") + "." + className);
					if(dynamicClass.isInterface()) {
						continue;
					}
					constructor = dynamicClass.getDeclaredConstructors()[0];
					classInstance = constructor.newInstance(test, driver, dataTable, testParameters);


					switch (currentKeyword) {

					case "enterText":
					case "verifyAutopopulatefieldvalues":
					case "clickConfirmPrompt":
					case "getPutTestdata":
					case "enterTransferOrder":
					case "enterShipmentNumber":
					case "deliveryinfocomplete":
					case "multipleClickNext":
					case "validatePicklistValue":
						try {
							method = dynamicClass.getDeclaredMethod(currentKeyword, String.class, String.class);
							isMethodFound = true;
						} catch (NoSuchMethodException e) {
							isMethodFound = false;
							break;
						}
						if (isMethodFound) {
							method.invoke(classInstance, fieldMap.get(currentKey), dataMap.get(currentKey));
						}
						break;
					case "enterTextFormattedData":
						try {
							method = dynamicClass.getDeclaredMethod(currentKeyword, String.class, String.class,
									String.class);
							isMethodFound = true;
						} catch (NoSuchMethodException e) {
							isMethodFound = false;
							break;
						}
						if (isMethodFound) {
							method.invoke(classInstance, fieldMap.get(currentKey), dataMap.get(currentKey), currentKey);							
						}
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
					case "verifyRoutine":
					case "clickNextWaitTillFieldContains":
					case "swipingHorizontal":
						try {
							method = dynamicClass.getDeclaredMethod(currentKeyword, String.class);
							isMethodFound = true;
						} catch (NoSuchMethodException e) {
							isMethodFound = false;
							break;
						}
						if (isMethodFound) {
							method.invoke(classInstance, dataMap.get(currentKey));
						}
						break;

					default:
						try {
							method = dynamicClass.getDeclaredMethod(currentKeyword);
							isMethodFound = true;
						} catch (NoSuchMethodException e) {
							isMethodFound = false;
							break;
						}
						if (isMethodFound) {
							method.invoke(classInstance);							
						}
						break;

					}

				}
			}
		}

		keywordCounter = 0;

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
		
		keywordMap = dataTable.getRowData("BusinessFlow", testParameters.getCurrentTestCase());
		
		if(keywordMap.isEmpty()) {
			test.log(LogStatus.FATAL, "No Business flow available for current test case in worksheet - <b>BusinessFlow</b>");
		}
		
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

		fieldMap = dataTable.getRowData("BusinessFlow", testParameters.getCurrentTestCase() + "_FIELD");
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

		dataMap = dataTable.getRowData("BusinessFlow", testParameters.getCurrentTestCase() + "_DATA");
		dataMap.remove("TC_ID");
		return dataMap;

	}

	@SuppressWarnings("rawtypes")
	public void driverSetUp() throws ExecuteException, IOException, InterruptedException, SessionNotCreatedException {

		if (!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {
			if (newServerSetupForEachTestcase.equalsIgnoreCase("False") && appiumServerInstanceCount == 0) {

				appiumServerInstanceCount++;
				appiumServerHandler = new AppiumServerHandler(Integer.parseInt(testParameters.getPort()),
						testParameters.getBootstrapPort());
				appiumServerHandler.appiumServerStart();

			} else if (newServerSetupForEachTestcase.equalsIgnoreCase("True") && appiumServerInstanceCount == 0) {

				appiumServerHandler = new AppiumServerHandler(Integer.parseInt(testParameters.getPort()),
						testParameters.getBootstrapPort());
				appiumServerHandler.appiumServerStart();

			}
		}

		String absolutePath = new File(System.getProperty("user.dir")).getAbsolutePath();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", testParameters.getDeviceName());
		capabilities.setCapability("udid", testParameters.getUdid());
		capabilities.setCapability(CapabilityType.BROWSER_NAME, testParameters.getBROWSER_NAME());
		capabilities.setCapability(CapabilityType.VERSION, testParameters.getVERSION());
		capabilities.setCapability("app", absolutePath + "\\src\\main\\resources\\Libs\\" + testParameters.getApp());
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

		driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);

		test.log(LogStatus.INFO, "Android Driver and Appium server setup done Successfully", "");

	}

	public void end() {

		if (newServerSetupForEachTestcase.equalsIgnoreCase("False") && totalTestInstanceToRun == 0) {

			if (driver != null) {
				driver.quit();
			}

			if (appiumServerHandler != null) {
				appiumServerHandler.appiumServerStop();
			}
		} else if (newServerSetupForEachTestcase.equalsIgnoreCase("True")) {
			if (driver != null) {
				driver.quit();
			}

			if (appiumServerHandler != null) {
				appiumServerHandler.appiumServerStop();
			}
		}

	}

	public void exceptionHandler() {
		try {
		if (!testParameters.getCurrentTestCase().contains("STAGE_DATA") && (totalKeywords - keywordCounter) >= 2) {

			test.log(LogStatus.INFO, "<b>Executing exception handler</b>");

			if (isElementPresent(ID_MESSAGE, "Prompt Message")) {
				String msg = GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title"));

				if (msg.equals("Would you like to switch to Batch mode?")) {
					networkFlag = false;
					test.log(LogStatus.WARNING,
							"<font color=red><b>Network not available....Please check network connectivity</b></font>");
					test.log(LogStatus.WARNING,
							"<font color=red><b>Execution of upcoming test cases will be suspended</b></font>");
					test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
					return;
				} else if (GetText(ID_ALERT_TITLE, "Alert Title").equals("Mobility")) {
					Click(ID_MESSAGE_OK, "Clicked 'Ok' for prompt");
					clickRoutineBackButton();
					clickRoutineBackButton();
					test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
				} else if (GetText(ID_ALERT_TITLE, "Alert Title").equals("Confirm")) {
					Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt");
					clickRoutineBackButton();
					clickRoutineBackButton();
					test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
				}

			} else {
				clickRoutineBackButton();
				clickRoutineBackButton();
				test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
			}
		}
	}
	catch(Exception e) {
		test.log(LogStatus.INFO, "<b>Exception occured while executing - Exception handler</b>");
		test.log(LogStatus.INFO, e);	
	}
	}
	
	
	private void testRailReport() {
		
		try {
		
		if(!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {					
			if(test.getRunStatus() == LogStatus.PASS && testRailEnabled.equalsIgnoreCase("True")){
			testRailListenter.addTestResult(Integer.parseInt(testParameters.getTestRailTestcaseID()), 1);
			}else if (testRailEnabled.equalsIgnoreCase("True")) {
			testRailListenter.addTestResult(Integer.parseInt(testParameters.getTestRailTestcaseID()), 5);	
			}
			}
		}catch(RuntimeException e) {
			test.log(LogStatus.WARNING, "Exception occured while updating test result in test rail.");
			test.log(LogStatus.WARNING, e);
		}
	}

}
