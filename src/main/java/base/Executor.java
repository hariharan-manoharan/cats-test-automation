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

	private ExtentReports report;
	private ExtentTest test;
	private TestParameters testParameters;
	private ExecutionMode execMode;
	private DataTable dataTable;
	private AppiumServerHandler appiumServerHandler;
	private AppiumServerHandlerCmd appiumServerHandlerCmd;
	@SuppressWarnings("rawtypes")
	private AndroidDriver driver;
	private TestRailListener testRailListenter;
	String testRailEnabled = properties.getProperty("testRail.enabled");
	int projectId = Integer.parseInt(properties.getProperty("testRail.projectId"));

	public Executor(TestParameters testParameters, ExtentReports report, ExecutionMode execMode, DataTable dataTable, TestRailListener testRailListenter) {
		this.testParameters = testParameters;
		this.report = report;
		this.execMode = execMode;
		this.dataTable = dataTable;
		this.testRailListenter = testRailListenter;

	}
	
	public Executor(TestParameters testParameters, ExtentReports report, ExecutionMode execMode, DataTable dataTable) {
		this.testParameters = testParameters;
		this.report = report;
		this.execMode = execMode;
		this.dataTable = dataTable;

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
			return;
		} catch (ExecuteException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (ClassNotFoundException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (InstantiationException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (IllegalAccessException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (IllegalArgumentException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (InvocationTargetException e) {
			test.log(LogStatus.FAIL, e.getCause());
			return;
		} catch (NoSuchMethodException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (SecurityException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (IOException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (InterruptedException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (TimeoutException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (NoSuchElementException e) {
			test.log(LogStatus.FAIL, e);
			return;
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
			return;
		} finally {
			end();
		}
	}

	public void executeKeywords(LinkedHashMap<String, String> keywords)
			throws ExecuteException, IOException, InterruptedException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, SessionNotCreatedException, TimeoutException, NoSuchElementException {
		
		if(!testParameters.getCurrentTestCase().contains("STAGE_DATA")){
		driverSetUp();
		}

		Class<?> className = Class.forName("main.java.businessComponents." + execMode + "."
				+ properties.getProperty("Project") + ".FunctionalComponents");
		Constructor<?> constructor = className.getDeclaredConstructors()[0];
		Object classInstance = constructor.newInstance(test, driver, dataTable);

		for (Entry<String, String> map : keywords.entrySet()) {
			if (!map.getKey().equals("TC_ID")) {
				String currentKeyword = map.getValue().substring(0, 1).toLowerCase() + map.getValue().substring(1);
				test.log(LogStatus.INFO, "Current Keyword - " + currentKeyword, "");
				Method method = className.getMethod(currentKeyword);
				method.invoke(classInstance);
			}
		}

		end();
	}

	
	/**
	 * Function to Business Keywords
	 * 
	 * @param1 nil
	 * @return LinkedHashMap<String, String>
	 * @author Hari
	 * @since 01/05/2017
	 * 
	 */
	
	public LinkedHashMap<String, String> getKeywords() {

		LinkedHashMap<String, String> keywordMap = new LinkedHashMap<String, String>();
		keywordMap = dataTable.getRowData("BusinessFlow");
		return keywordMap;

	}

	@SuppressWarnings("rawtypes")
	public void driverSetUp() throws ExecuteException, IOException, InterruptedException, SessionNotCreatedException {

		appiumServerHandler = new AppiumServerHandler(Integer.parseInt(testParameters.getPort()),
				testParameters.getBootstrapPort());
		appiumServerHandler.appiumServerStart();

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
		capabilities.setCapability("newCommandTimeout", 300);		
		capabilities.setCapability("noReset", true);
		
		driver = new AndroidDriver(new URL(
				"http://" + properties.getProperty("RemoteAddress") + ":" + testParameters.getPort() + "/wd/hub"),
				capabilities);

		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

		test.log(LogStatus.INFO, "Android Driver and Appium server setup done Successfully", "");

	}


	public void end() {

		if (driver != null) {
			driver.quit();
		}

		if (appiumServerHandler != null) {
			appiumServerHandler.appiumServerStop();
		}

	}

}
