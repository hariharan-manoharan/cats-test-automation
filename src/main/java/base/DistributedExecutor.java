package main.java.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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
import main.java.executionSetup.ExecutionType;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.AppiumServerHandler;
import main.java.utils.AppiumServerHandlerCmd;
import main.java.utils.TestRailListener;
import main.java.utils.Utility;

public class DistributedExecutor extends Utility implements Runnable {

	private ExtentReports report; 
	private ExtentTest test;
	private TestParameters testParameters;
	private ExecutionType executionType;
	private DataTable dataTable;
	private ArrayList<AndroidDriver> driverList;
	private AndroidDriver driver;
	private TestRailListener testRailListenter;
	String testRailEnabled = testRailProperties.getProperty("testRail.enabled");
	int projectId = Integer.parseInt(testRailProperties.getProperty("testRail.projectId"));
	private Lock lock;
	private Connection connection;


	public DistributedExecutor(TestParameters testParameters, ExtentReports report, ExecutionType executionType, DataTable dataTable, TestRailListener testRailListenter, Lock lock,  ArrayList<AndroidDriver> androidDriverList) {
		this.testParameters = testParameters;
		this.report = report;
		this.executionType = executionType;
		this.dataTable = dataTable;
		this.testRailListenter = testRailListenter;
		this.lock = lock;
		this.driverList = androidDriverList;

	}
	
	public DistributedExecutor(TestParameters testParameters, ExtentReports report, ExecutionType executionType, DataTable dataTable,Lock lock,  ArrayList<AndroidDriver> androidDriverList) {
		this.testParameters = testParameters;
		this.report = report;
		this.executionType = executionType;
		this.dataTable = dataTable;	
		this.lock = lock;
		this.driverList = androidDriverList;

	}

	@Override
	public void run() {
		
		LinkedHashMap<String, String> keywordMap = new LinkedHashMap<String, String>();
		try {	
						
			if (testParameters.getExecuteCurrentTestCase().equalsIgnoreCase("Yes")) {				
				
				test = report.startTest(testParameters.getCurrentTestCase() + " : " + testParameters.getDescription());
				dataTable.setCurrentRow(testParameters.getCurrentTestCase());
				test.log(LogStatus.INFO, testParameters.getCurrentTestCase() + " execution started", "");
				test.log(LogStatus.INFO, "Current Thread - "+Thread.currentThread().getName().toString(), "");
				
				
				String[] threadNumber = Thread.currentThread().getName().toString().split("-");
				
				int driverindex = Integer.parseInt(threadNumber[3])-1;				
				
				driver = driverList.get(driverindex);
				
				test.log(LogStatus.INFO, "AndroidDriver Session ID - "+driver.getSessionId().toString(), "");

				if (testParameters.getConnectDB().equalsIgnoreCase("Yes")) {
					connection = Getconnections();
				}

				keywordMap = getKeywords();
				
				if(!keywordMap.isEmpty()) {
				executeKeywords(keywordMap);
				}

				if (testParameters.getConnectDB().equalsIgnoreCase("Yes")) {
					Closeconnections(connection);
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
		} 
	}

	public void executeKeywords(LinkedHashMap<String, String> keywords)
			throws ExecuteException, IOException, InterruptedException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			SecurityException, SessionNotCreatedException, TimeoutException, NoSuchElementException {
		
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();


		if (!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {
			fieldMap = getFields();
			dataMap = getData();
		}

		
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
				
			String requiredDirectory = findDir(new File(System.getProperty("user.dir")), properties.getProperty("Project"));			
			
			File packageDirectory = new File(requiredDirectory);

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
					

					dynamicClass = Class.forName("main.java.businessComponents." + executionType + "." + properties.getProperty("Project") + "." + className);
					if(dynamicClass.isInterface()) {
						continue;
					}
					constructor = dynamicClass.getDeclaredConstructors()[0];
					classInstance = constructor.newInstance(test, driver, dataTable, testParameters, lock, connection);


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
					case "updateLocatorRuleSet":
					case "verifyPrompt":
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
					case "clickButtonWithText":
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
					case "createLocatorRule":
						try {
							method = dynamicClass.getDeclaredMethod(currentKeyword, Integer.TYPE);
							isMethodFound = true;
						} catch (NoSuchMethodException e) {
							isMethodFound = false;
							break;
						}
						if (isMethodFound) {
							method.invoke(classInstance, Integer.parseInt(dataMap.get(currentKey)));
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
					
					report.flush();

				}
			}
		}


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
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();


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
		LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();

		dataMap = dataTable.getRowData("BusinessFlow", testParameters.getCurrentTestCase() + "_DATA");
		dataMap.remove("TC_ID");		
		
		return dataMap;

	}

	

	public void exceptionHandler() {
		try {
		if (!testParameters.getCurrentTestCase().contains("STAGE_DATA")) {

			this.test.log(LogStatus.INFO, "<b>Executing exception handler</b>");

			if (isElementPresent(this.driver, this.test,ID_MESSAGE, "Prompt Message")) {
				String msg = GetText(this.driver, this.test,ID_MESSAGE, GetText(this.driver,this.test,ID_ALERT_TITLE, "Alert Title"));

				if (msg.equals("Would you like to switch to Batch mode?")) {					
					this.test.log(LogStatus.WARNING,
							"<font color=red><b>Network not available....Please check network connectivity</b></font>");
/*					this.test.log(LogStatus.WARNING,
							"<font color=red><b>Execution of upcoming this.test cases will be suspended</b></font>");*/
					this.test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
					return;
				} else if (GetText(this.driver,this.test,ID_ALERT_TITLE, "Alert Title").equals("Mobility")) {
					Click(this.driver, this.test,ID_MESSAGE_OK, "Clicked 'Ok' for prompt");
					clickRoutineBackButton(this.driver, this.test);
					clickRoutineBackButton(this.driver, this.test);
					this.test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
				} else if (GetText(this.driver,this.test,ID_ALERT_TITLE, "Alert Title").equals("Confirm")) {
					Click(this.driver, this.test,ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt");
					clickRoutineBackButton(this.driver,this.test);
					clickRoutineBackButton(this.driver, this.test);
					this.test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
				}

			} else {
				clickRoutineBackButton(this.driver, this.test);
				clickRoutineBackButton(this.driver, this.test);
				this.test.log(LogStatus.INFO, "<b>Exception handler completed</b>");
			}
		}
	}
	catch(Exception e) {
		this.test.log(LogStatus.INFO, "<b>Exception occured while executing - Exception handler</b>");
		this.test.log(LogStatus.INFO, e);	
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
