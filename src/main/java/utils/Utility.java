package main.java.utils;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import main.java.businessComponents.MOBILE.AIRTEL.RoutineObjectRepository;
import main.java.executionSetup.TestParameters;
import main.java.reporting.HtmlReport;
import main.java.testDataAccess.DataTable;

public class Utility implements RoutineObjectRepository{

	@SuppressWarnings("rawtypes")
	protected static AndroidDriver driver;
	protected static ExtentTest test;
	protected DataTable dataTable;
	protected TestParameters testParameters;
	public static Properties properties;
	public static Properties runtimeDataProperties;
	public static Connection connection;
	public static LinkedHashMap<String, String> environmentVariables;
	private static HashMap<String, String> globalRuntimeRecevingDatamap = new HashMap<String, String>();
	private static HashMap<String, String> globalRuntimeContainerDatamap = new HashMap<String, String>();
	public static String newServerSetupForEachTestcase;
	int verifyCounter = 0;
	
	

	@SuppressWarnings("rawtypes")
	public Utility(ExtentTest test, AndroidDriver driver, DataTable dataTable,TestParameters testParameters) {
		Utility.test = test;
		Utility.driver = driver;
		this.dataTable = dataTable;
		this.testParameters = testParameters;
	}

	public Utility() {

	}

	@SuppressWarnings("static-access")
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	
	@SuppressWarnings("static-access")
	public void setRuntimeDataProperties(Properties runtimeDataProperties) {
		this.runtimeDataProperties = runtimeDataProperties;
	}
	
	
	@SuppressWarnings("static-access")
	public void setNewServerSetupForEachTestcase() {
		this.newServerSetupForEachTestcase = properties.getProperty("new.server.setup.for.each.testcase");
	}
	
	
	@SuppressWarnings("static-access")
	public String getNewServerSetupForEachTestcase() {
		return this.newServerSetupForEachTestcase ;
	}
	
	
	@SuppressWarnings("static-access")
	public Properties getRuntimeDataProperties() {
		return this.runtimeDataProperties;
	}

	@SuppressWarnings("static-access")
	public void setEnvironmentVariables(LinkedHashMap<String, String> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	/**
	 * Function to log test report with screenshot and Status PASS
	 * 
	 * @param1 String reportName
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public void takeScreenshot(String reportName) {

		if(properties.getProperty("take.screenshot.on.pass").equalsIgnoreCase("True")){	
		test.log(LogStatus.PASS, reportName,
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenShot() + ".png"));
		}else{
			test.log(LogStatus.PASS, reportName);	
		}

	}
	
	
	/**
	 * Function to log test report with screenshot and Status INFO
	 * 
	 * @param1 String reportName
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	@SuppressWarnings("rawtypes")
	public void takeScreenshotStatus(AndroidDriver driver,ExtentTest test, String reportName, LogStatus status) {		
		
		
		if(properties.getProperty("take.screenshot.on.pass").equalsIgnoreCase("True") && status.equals(LogStatus.PASS)){
			
			test.log(status, reportName, "<b>Screenshot: <b>" + test.addScreenCapture("./" + screenShot(driver) + ".png"));
			
		}else if (properties.getProperty("take.screenshot.on.pass").equalsIgnoreCase("False") && status.equals(LogStatus.PASS)){
			
			test.log(status, reportName);
			
		}else{			
			
			test.log(status, reportName, "<b>Screenshot: <b>" + test.addScreenCapture("./" + screenShot(driver) + ".png"));
		}
	}
	
	
	public String screenShot(AndroidDriver driver) {

		String screenshotName = null;

		screenshotName = getCurrentFormattedTime("dd_MMM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screenshotName;

	}
	
	public String screenShot() {

		String screenshotName = null;

		screenshotName = getCurrentFormattedTime("dd_MMM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screenshotName;

	}
	
	
	/**
	 * Function to log test report with screenshot - WEBVIEW
	 * 
	 * @param1 String reportName
	 * @param2 Set<String> contextHandles
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public void takeScreenshotWebView(Set<String> contextHandles, String reportName) {

		switchContext(contextHandles, "NATIVE");

		test.log(LogStatus.PASS, reportName,
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenShot() + ".png"));

	}

	/**
	 * Function to log test report without screenshot
	 * 
	 * @param1 String reportName
	 * @param2 LogStatus status
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public void report(AndroidDriver driver, ExtentTest test, String reportName, LogStatus status) {
		takeScreenshotStatus(driver,test, reportName, status);		

	}

	/**
	 * Function to format the current time instance
	 * 
	 * @return String (formatted date)
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public static String getCurrentFormattedTime(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}

	
	/**
	 * FulentWait Function - Waits until the object is available with timeout of 100 seconds polling every 5 seconds
	 * 
	 * @param1 By by	
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */
	
	
	public void waitCommand(final By by) throws TimeoutException, NoSuchElementException {

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.withTimeout(20, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);

		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver arg0) {
				boolean displayed = arg0.findElement(by).isEnabled();
				if (displayed) {
					return true;
				}
				return false;
			}
		};
		wait.until(function);
		
	}
	
	/**
	 *  	  
	 * @param1 By by
	 * @param2 String objectName	
	 * @return boolean 
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public boolean isElementPresent(final By by, final String objectName){
		
		try{
		driver.findElement(by).isDisplayed();		
		test.log(LogStatus.PASS, "Element - " + objectName + " is present", "");
		return true;		
		}catch(NoSuchElementException  e){
			test.log(LogStatus.INFO, "Element - " + objectName + " is not present", "");
			return false;
		}
		

	}

	
	/**
	 *  	  
	 * @param1 By by
	 * @param2 String objectName	
	 * @return boolean 
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public boolean isAutoPopulateFieldPresent(String labelXpath, final String objectName) {
		
		boolean flag = false;
		String value = null;
		
		try{
		
		flag = driver.findElement(By.xpath(labelXpath)).isDisplayed();
		
		if(flag){
			value = driver.findElement(By.xpath(labelXpath+"/following-sibling::android.view.View")).getAttribute("name");	
		}
		
		test.log(LogStatus.PASS, "<b>" + objectName + "</b> - <b>"+value+"</b>", "");
		return true;				
		
		}catch(NoSuchElementException  e){
			test.log(LogStatus.FAIL, "<b>" + objectName + "</b> is not present", "");
			return false;
		}
		

	}
	
	/**
	 * This function is similar to waitCommand which waits until the object is available with timeout of 100 seconds polling every 5 seconds
	 * and returns the value of until statement
	 *  	  
	 * @param1 By by
	 * @param2 String fieldName	
	 * @return boolean 
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public boolean isFieldDisplayed(final By by, final String fieldName) {
		
		boolean present = false;
		
		try{
		
		HardDelay(2000L);
		present = driver.findElement(by).isDisplayed();
		
		if(present){
			test.log(LogStatus.PASS, "Field - " + fieldName + " is present", "");
			return true;					
		}else{
			test.log(LogStatus.INFO, "Field - " + fieldName + " is not present", "");
			return false;
		}	
		}catch(Exception e){
			test.log(LogStatus.INFO, "Field - " + fieldName + " is not present", "");
			return false;
		}
		

	}
	/**
	 * FulentWait Predicate - Waits until the object is available with timeout of 100 seconds polling every 5 seconds
	 * 
	 * @param1 By by	
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */
	
	public void fluentPredicateWait(final By by) {
		new FluentWait<WebDriver>(driver).withTimeout(100, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
				.until(new Predicate<WebDriver>() {
					public boolean apply(WebDriver driver) {
						try {
							Boolean booFlag = driver.findElement(by).isDisplayed();
							if (!booFlag) {								
								return true;
							} else								
							return false;
						} catch (Exception e) {
							return true;
						}
					}
				});
	}
	
	
	/**
	 * Function implements thread.sleep function 
	 * 
	 * @param1 long delayTime	
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public static void HardDelay(long delayTime) {
		try {
			Thread.sleep(delayTime);
		} catch (Exception e) {

		}

	}
	


	public static void autoPopulateWaitTime(long delayTime) {
		try {
			Thread.sleep(delayTime);
		} catch (Exception e) {

		}

	}
	
	
	/**
	 * Function to enter text in WebElement
	 * 
	 * @param1 By by	
	 * @param2 String reportName
	 * @param3 String text
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void EnterText(By by, String reportName, String text)  throws TimeoutException, NoSuchElementException {

			waitCommand(by);
			WebElement element = driver.findElement(by);
			driver.pressKeyCode(112); // DELETE Key event - // https://developer.android.com/reference/android/view/KeyEvent.html#KEYCODE_FORWARD_DEL
			element.sendKeys(text);
			takeScreenshot(reportName);
		
	}
	
	
	/**
	 * Function to click WebElement
	 * 
	 * @param1 By by	
	 * @param2 String reportName	 
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void Click(By by, String reportName) throws TimeoutException, NoSuchElementException{
		
			waitCommand(by);
			driver.findElement(by).click();
			HardDelay(5000L);
			takeScreenshot(reportName);
		
	}
	
	
	/**
	 * Function to click spyglass  - Need to Fix this one - Avoid getting the fieldIndex from user
	 * 
	 * @param1 String reportName	 
	 * @return void
	 * @author Hari 
	 * @since 05/29/2017
	 * 
	 */

	public void ClickSpyGlass(String pickListName, int fieldIndex) {
		try {		
			
			//int fieldIndex = Integer.parseInt(driver.findElement(By.xpath(".//android.view.View[@content-desc='"+pickListName+"']")).getAttribute("index"));				
			fieldIndex = fieldIndex+2;			
					
			driver.findElement(By.xpath(".//android.view.View[@index='"+String.valueOf(fieldIndex)+"']/android.view.View[@index='0']/android.view.View[@index='0']")).click();
			HardDelay(5000L);
			takeScreenshot("Clicked - "+pickListName +" Spy glass");
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
	}
	
	
	/**
	 * Function to click WebElement
	 * 
	 * @param1 WebElement element	
	 * @param2 String reportName	 
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void Click(WebElement element, String reportName) {
		try {
			element.click();
			HardDelay(2000L);
			takeScreenshot(reportName);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
	}
	
	/**
	 * Function to get text from WebElement
	 * 
	 * @param1 By by	
	 * @param2 String fieldName	 
	 * @return String text
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public String GetText(By by, String fieldName){
		String text = null;

		try {
			waitCommand(by);
			WebElement element = driver.findElement(by);
			text = element.getText();
			test.log(LogStatus.INFO, fieldName + ":  Returned - Text:<b>" + text+"</b>");
			return text.trim();
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
			test.log(LogStatus.INFO, fieldName + ": Not Returned - Text:<b>" + text+"</b>");
			return "NULL";
		}
		

	}
	
	
	/**
	 * Function to get attribute value from WebElement
	 * 
	 * @param1 By by	
	 * @param2 String attribute	
	 * @param3 String fieldName	 
	 * @return String text
	 * @author Hari 
	 * @since 06/08/2017
	 * 
	 */
	
	public String GetAttributeValue(By by,String attribute, String fieldName) throws WebDriverException {
		String text = null;

		try {
			waitCommand(by);
			WebElement element = driver.findElement(by);
			text = element.getAttribute(attribute);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
			test.log(LogStatus.INFO,"Value of attribute '"+attribute+"' of field '"+ fieldName + "': Not Returned - Value:" + text);
		}
		test.log(LogStatus.INFO, "Value of attribute '"+attribute+"' of field '"+ fieldName + "': Returned - Value:" + text);
		return text.trim();

	}
	
	/**
	 * Function to get text from WebElement using UiSelector -> descriptionContains  -----> Not Tested, So don't use this method as of now
	 * 
	 * @param1 By by	
	 * @param2 String fieldName
	 * @param3 String contains	 
	 * @return String text
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public String GetTextByContains(By by, String fieldName, String contains) throws WebDriverException {
		String text = null;

		try {
			MobileElement element = (MobileElement) driver
					.findElementByAndroidUIAutomator("new UiSelector().descriptionContains(\"" + contains + "\")");
			text = element.getText();
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
			test.log(LogStatus.INFO, fieldName + ": Not Returned - " + text);
		}
		test.log(LogStatus.INFO, fieldName + ":  Returned - " + text);
		return text.trim();

	}
	
	
	/**
	 * Function to get text from WebElement - WEBVIEW
	 * 
	 * @param1 By by	
	 * @param2 String fieldname	 
	 * @return String text
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	@SuppressWarnings("unchecked")
	public String GetTextWebView(By by, String fieldName) {
		String text = null;

		try {
			Set<String> contextHandles = driver.getContextHandles();
			switchContext(contextHandles, "fulcrum");
			waitCommand(by);
			text = driver.findElement(by).getText();
			switchContext(contextHandles, "NATIVE");
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
		test.log(LogStatus.INFO, fieldName + ":  Returned - " + text);
		return text.trim();

	}
	
	
	/**
	 * Function to enter text in WebElement - WEBVIEW
	 * 
	 * @param1 By by	
	 * @param2 String reportName
	 * @param3 String text
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	@SuppressWarnings("unchecked")
	public void EnterTextWebView(By by, String reportName, String text)
			throws TimeoutException, NoSuchElementException {

		Set<String> contextHandles = driver.getContextHandles();
		switchContext(contextHandles, "fulcrum");
		waitCommand(by);
		WebElement element = driver.findElement(by);
		focusEnterText(element, text);
		takeScreenshotWebView(contextHandles, reportName);

	}
	
	
	/**
	 * Function to focus WebElement and perform actions to enter text (Works in combination with EnterTextWebView function) - WEBVIEW
	 * 
	 * @param1 WebElement element	
	 * @param2 String textToEnter
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void focusEnterText(WebElement element, String textToEnter) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		actions.sendKeys(textToEnter);
		actions.build().perform();
	}
	
	/**
	 * Function to get list of WebElements
	 * 
	 * @param1 By by 
	 * @return List<WebElement> webElements
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	@SuppressWarnings("unchecked")
	public List<WebElement> GetWebElements(By by) {
		List<WebElement> webElements = null;
		try {
			waitCommand(by);
			webElements = driver.findElements(by);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
		return webElements;
	}

	public void HideKeyboard() {
		driver.hideKeyboard();

	}
	
	
	/**
	 * Function to click "Next" button in CATS Mobility - Application Specific
	 * 
	 * @param no parameters
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void ClickNext() {
		try {
			waitCommand(By.id("next"));
			driver.findElement(By.id("next")).click();
			HardDelay(2000L);
			takeScreenshot("Click Next Button");
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
	}
	
	/**
	 * Function to click "Previous" button in CATS Mobility - Application Specific
	 * 
	 * @param no parameters
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void ClickPrevious() {
		try {
			waitCommand(By.id("previous"));
			driver.findElement(By.id("previous")).click();
			takeScreenshot("Click Previous Button");
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
	}

	
	/**
	 * Function to compare two texts 
	 * 
	 * @param1 String expected	
	 * @param2 By by
	 * @return boolean
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */
	
	public boolean CompareText(String expected, By by) {

		String actual = driver.findElement(by).getText().trim();

		if (expected.equals(actual)) {
			test.log(LogStatus.PASS, "Compare Text() - Expected - " + expected + ", Actual - " + actual);
			return true;
		} else {
			test.log(LogStatus.FAIL, "Compare Text() - Expected - " + expected + ", Actual - " + actual);
			return false;
		}

	}
	
	/**
	 * Function to Enter text using Command prompt  -  Not tested, So test this method using  
	 * 
	 * @param1 By by	
	 * @param2 String reportName
	 * @param3 String text
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	public void EnterTextCmd(By by, String reportName, String text) {
		try {
			waitCommand(by);
			WebElement element = driver.findElement(by);
			element.click();
			element.clear();
			Runtime.getRuntime().exec("adb -s emulator-5554 shell input text " + text);
			takeScreenshot(reportName);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);

		}
	}
	
	
	/**
	 * Function to switch between contexts  
	 * 
	 * @param1 Set<String> contextHandles	
	 * @param2 String contextName
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	@SuppressWarnings("unchecked")
	public void switchContext(Set<String> contextHandles, String contextName) {
		for (String s : contextHandles) {
			System.out.println("Context - " + s);
			if (s.contains(contextName)) {
				driver.context(s);
				break;
			}
		}
	}

	
	
	/**
	 * Function to Verify if transaction is completed - Application Specific  -   Not tested, So test this method using  
	 * 
	 * @param1 By by	
	 * @param2 String loopingField
	 * @return boolean
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */
	
	public boolean verifyTransactionCreation(By by, String loopingField) {

		if (GetTextWebView(by, "Looping field").equalsIgnoreCase(loopingField)) {
			test.log(LogStatus.PASS, "Transaction created successfully");
			return true;
		}
		test.log(LogStatus.FAIL, "Transaction created not successfully");
		return false;

	}

	/**
	 * Function to get single column data from Database
	 * 
	 * @param1 String query
	 * @param2 String dataRequired
	 * @return String data
	 * @author Hari
	 * @since 12/23/2016
	 * 
	 */

	public String selectQuerySingleValue(String query, String dataRequired) {
		String data = null;
		Statement stmt;
		ResultSet rs;

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				rs.getObject(1);
				data = rs.getString(dataRequired);
				if (!data.equals(null)) {
					break;
				}
				return data;

			}
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, dataRequired + " - <b>" + data +"</b>");
			test.log(LogStatus.FAIL, e);
		}
		test.log(LogStatus.INFO, dataRequired + " - <b>" + data +"</b>");
		return data.trim();
		

	}

	/**
	 * Function to get multiple column data from Database
	 * 
	 * @param1 String query
	 * @param2 String dataRequired
	 * @return LinkedHashMap<String, String> data
	 * @author Hari
	 * @since 12/24/2016
	 * 
	 */

	public LinkedHashMap<String, String> selectQueryMultipleValues(String query, String dataRequired) {

		Statement stmt;
		ResultSet rs;
		int lineCount = 0;
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {

				lineCount++;
				String[] key = dataRequired.split("#");

				for (int i = 0; i < key.length; i++) {
					String value = rs.getString(key[i]);
					data.put(key[i] + "_" + lineCount, value);
				}

			}
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, e);
		}

		return data;

	}
	
	public int generateRandomNum(int bound){		
	
	Random rand = new Random(); 
	return rand.nextInt(bound); 
	
	}
	
	
	/**
	 * Function to get Pick List value
	 * 	 
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */

	@SuppressWarnings("unchecked")
	public String GetPickListValue(int pickListRow) {		
		
		String text = null;
		
		try {
			
			List<WebElement> elements = driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout");
			
			for(WebElement element: elements){
				List<WebElement> eles = element.findElements(By.className("android.widget.TextView"));
				text = eles.get(pickListRow-1).getText();			
				break;
			}
			
			takeScreenshot("Pick List Values");	
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
		clickDeviceBackButton();
		return text;
	}
	
	/**
	 * Function to get Pick List values
	 * 	 
	 * @return void
	 * @author Hari 
	 * @since 12/27/2016
	 * 
	 */


	@SuppressWarnings("unchecked")
	public List<String> GetPickListValues() {		
		
		List<String> values = new ArrayList<String>();
		
		try {
			
			List<WebElement> elements = driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout");
			
			for(WebElement element: elements){
				List<WebElement> eles = element.findElements(By.className("android.widget.TextView"));
				values.add(eles.get(0).getText());					
			}
			
			takeScreenshot("Pick List Values");	
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
		clickDeviceBackButton();
		return values;
	}
	
		
	public void swipeElementExample(WebElement element) {
		  String orientation = driver.getOrientation().value();

		  // get the X coordinate of the upper left corner of the element, then add the element's width to get the rightmost X value of the element
		  int leftX = element.getLocation().getX();
		  int rightX = leftX + element.getSize().getWidth();

		  // get the Y coordinate of the upper left corner of the element, then subtract the height to get the lowest Y value of the element
		  int upperY = element.getLocation().getY();
		  int lowerY = upperY - element.getSize().getHeight();
		  int middleY = (upperY - lowerY) / 2;

		  if (orientation.equals("portrait")) {
		    // Swipe from just inside the left-middle to just inside the right-middle of the element over 500ms
		      driver.swipe(leftX + 5, middleY, rightX - 5, middleY, 500);
		  }
		  else if (orientation.equals("landscape")) {
		    // Swipe from just inside the right-middle to just inside the left-middle of the element over 500ms
		    driver.swipe(rightX - 5, middleY, leftX + 5, middleY, 500);
		  }
		}
	
	
	public void clickDeviceBackButton(){
		driver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
	
	public void horizontalScroll()
    {
		Dimension size=driver.manage().window().getSize();
        int x_start=(int)(size.width*0.60);
        int x_end=(int)(size.width*0.30);
        int y=130;
        System.out.println("");
        driver.swipe(x_start,y,x_end,y,4000);
    }
	
	public void executeQuery(String query, String report) {
		Statement stmt;		
		try {
			stmt = connection.createStatement();
			stmt.executeQuery(query);
			test.log(LogStatus.PASS, report + " executed successfully");
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, report + " not executed successfully");
			test.log(LogStatus.FAIL, e);
		}

	}
	
	
	public void executeUpdateQuery(String query, String report) {
		Statement stmt;		
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			test.log(LogStatus.PASS, report + " executed successfully");
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, report + " not executed successfully");
			test.log(LogStatus.FAIL, e);
		}

	}
	
public boolean checkRecordAvailable(String query) {
		
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			result.next();
			if (result.getInt(1) == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		
	}
	
	public boolean ifAssetAvailable(String assetCode){
		
		String query = "SELECT COUNT(*) FROM CATS_ASSET WHERE ASSETCODE='"+assetCode+"'";
		
		return checkRecordAvailable(query);
	}
	
	public boolean ifPartAvailable(String partCode){
		
		String query = "SELECT COUNT(*) FROM CATS_PART WHERE PARTCODE='"+partCode+"'";
		
		return checkRecordAvailable(query);
	}

	
	public void stock_serializedItem(String i_assetcode, String i_tolocation,
			                         String i_tolocationstatus, String i_tolocatorcode,
			                         String i_partcode, String i_addcontactcode,
			                         int i_tobusinessunitid, String lotNumber) {
		
		String query = 
				 "declare "
                + "aValues t_NameValue_tab := t_NameValue_tab(); " 
                + "begin " 
				+ "cats_p_nvp.set_value(aValues,'LOTNUMBER', '"+lotNumber+"'); "
				+ "cats_p_asset_xapi.sp_stock "
				+ "( "
						+ "i_assetcode         => '"+i_assetcode+"', "
						+ "i_tolocation        => '"+i_tolocation+"', "
						+ "i_tolocationstatus  => '"+i_tolocationstatus+"', "
						+ "i_tolocatorcode     => NULL, "
						+ "i_partcode          => '"+i_partcode+"', "
						+ "i_addcontactcode    => 'CATSADM', "
						+ "i_tobusinessunitid  => "+i_tobusinessunitid+", "
						+ "io_Values           => avalues "
				+ "); "
				+ "end;";
		
		executeQuery(query, "Stock - Serialized item - Assetcode: "+i_assetcode);

	}
	

	
	public void stock_nonSerializedItem(String i_partcode, int i_qty,
										String i_tolocation, String i_tolocationstatus,
										String i_tolocatorcode,int i_tobusinessunitid,
										String i_lotnumber, String i_addcontactcode
										){
		
		String query = 
				"DECLARE "
              + "aValues t_NameValue_tab := t_NameValue_tab(); "
              + "BEGIN "
              + "cats_p_item_xapi.sp_stock "
              + "( "
              		+ "i_partcode => '"+i_partcode+"', "
              		+ "i_qty => "+i_qty+", "
              		+ "i_tolocation => '"+i_tolocation+"', "
              		+ "i_tolocationstatus => '"+i_tolocationstatus+"', "
              		+ "i_tolocatorcode => NULL, "
              		+ "i_tobusinessunitid => "+i_tobusinessunitid+", "
              		+ "i_lotnumber => '"+i_lotnumber+"', "
              		+ "i_addcontactcode => 'CATSADM', "
              		+ "io_Values => avalues "
              + "); "
              + "END; ";
		
		executeQuery(query, "Stock - Non Serialized item - Partcode: "+i_partcode);
	}

	/************************************************************************************************
	 * Function :ScrolltoText() Decsription:Function to Scroll to give text.
	 * Date :14-12-2016 Author :Saran
	 *************************************************************************************************/
	/*public void ScrolltoText(String Text) {
		try {
			driver.scrollToExact(Text);
			takeScreenshot("Scrolled to : " + Text);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}

	}
*/
	/************************************************************************************************
	 * Function :Getconnections() Decsription:Function to connect Database Date
	 * :14-12-2016 Author :Saran
	 *************************************************************************************************/
	public void Getconnections() throws Exception {

		try {
			String driver = "oracle.jdbc.driver.OracleDriver";

			String url = "jdbc:oracle:thin:@" + environmentVariables.get("DataBaseURL");
			String username = "CATS";
			String password = "CATS";

			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "DB Connection not established");

		}

	}

	/************************************************************************************************
	 * Function :Closeconnections() Decsription:Function to connect Database
	 * Date :14-12-2016 Author :Saran
	 *************************************************************************************************/
	public void Closeconnections() throws Exception {
		try {
			connection.close();
			if (connection.isClosed()){
				//System.out.println("Connection closed.");
			}

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

	}

	/************************************************************************************************
	 * Function :Design in progress Decsription:Function to connect Database
	 * Date :14-12-2016 Author :Saran
	 *************************************************************************************************/
	public void SqlQuery(String Text, String Text1) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(Text);
			while (rs.next()) {

				String[] Key = Text1.split("#");
				int key1 = Key.length;
				for (int i = 0; i < key1; i++) {
					String txt = rs.getString(Key[i]);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(Key[i], txt);
					System.out.println(Key[i] + txt);
				}

			}
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, e);
		}
	}

	/************************************************************************************************
	 * Function :GetNonSerializedPart Decsription:Function to get Nonserialized
	 * part from DB Date :16-12-2016 Author :Saran
	 *************************************************************************************************/
	public String GetNonSerializedPart() {
		String partcode = null;
		Statement stmt;
		ResultSet rs;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"select * from cats_part p left join cats_partdetail pd on p.partid = pd.partid where p.Active = 'Y' and p.Trackable = 'Y'and p.ORDERABLE = 'Y'and p.ORDERABLE = 'Y'and p.PURCHASABLE = 'Y'and p.INSTALLABLE = 'Y'and p.SERIALIZED = 'N'and p.KIT = 'N'and p.ASSEMBLY = 'N'and pd.partid is null");
			while (rs.next()) {
				Object firstrow = rs.getObject(1);
				partcode = rs.getString("PARTCODE");
				System.out.println(partcode);
				if (!partcode.equals(null)) {
					break;
				}
				return partcode;

			}
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, e);
		}

		return partcode;

	}

	/************************************************************************************************
	 * Function :GetSerializedPart Decsription:Function to get serialized part
	 * from DB Date :16-12-2016 Author :Saran
	 *************************************************************************************************/

	public String GetSerializedPart() {
		String partcode = null;
		Statement stmt;
		ResultSet rs;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"select * from cats_part p left join cats_partdetail pd on p.partid = pd.partid where p.Active = 'Y' and p.Trackable = 'Y'and p.ORDERABLE = 'Y'and p.ORDERABLE = 'Y'and p.PURCHASABLE = 'Y'and p.INSTALLABLE = 'Y'and p.SERIALIZED = 'Y'and p.KIT = 'N'and p.ASSEMBLY = 'N'and pd.partid is null");
			while (rs.next()) {
				Object firstrow = rs.getObject(1);
				partcode = rs.getString("PARTCODE");
				System.out.println(partcode);
				if (!partcode.equals(null)) {
					break;
				}
				return partcode;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return partcode;

	}
	
	
public int createNewPart(LinkedHashMap<String, String> inputValueMap){
		
	String query = null;	
	int RECORD_ID = 0;
	CallableStatement stproc_stmt;  
	try {
		
		RECORD_ID = generateRandomNum(10000000);
		query = "INSERT "
				+"INTO CATS.CATSCON_PART_STG"
				  +"("
				    +"ORGANIZATION_ID,"
				    +"ITEM,"
				    +"DESCRIPTION,"
				    +"ITEM_STATUS_ACTIVE,"
				    +"COSTING_ENABLED_FLAG,"
				    +"SERIALIZED_FLAG,"
				    +"KIT_FLAG,"
				    +"ASSEMBLY_FLAG,"
				    +"LOT_CONTROL,"
				    +"EXPENSE_ACCOUNT,"
				    +"PURCHASABLE_FLAG,"
				    +"PRIMARY_UNIT_OF_MEASURE,"
				    +"BTVL_ITEM_CATEGORY,"
				    +"BTVL_ITEM_TYPE,"
				    +"ASSET_CATEGORY,"
				    +"USER_ITEM_TYPE,"
				    +"RECORD_ID,"
				    +"CREATION_DATE,"
				    +"PROCESS_FLAG"
				  +")"
				  +"VALUES"
				  +"("
				    + Integer.parseInt(inputValueMap.get("VALUE1"))+","
				    +"'"+inputValueMap.get("VALUE2")+"',"
				    +"'"+inputValueMap.get("VALUE3")+"',"
				    +"'"+inputValueMap.get("VALUE4")+"',"
				    +"'"+inputValueMap.get("VALUE5")+"',"
				    +"'"+inputValueMap.get("VALUE6")+"',"
				    +"'"+inputValueMap.get("VALUE7")+"',"
				    +"'"+inputValueMap.get("VALUE8")+"',"
				    +"'"+inputValueMap.get("VALUE9")+"',"
				    +"'"+inputValueMap.get("VALUE10")+"',"
				    +"'"+inputValueMap.get("VALUE11")+"',"
				    +"'"+inputValueMap.get("VALUE12")+"',"
				    +"'"+inputValueMap.get("VALUE13")+"',"
				    +"'"+inputValueMap.get("VALUE14")+"',"
				    +"'"+inputValueMap.get("VALUE15")+"',"
				    +"'"+inputValueMap.get("VALUE16")+"',"
				    +RECORD_ID+","
				    +inputValueMap.get("VALUE18")+","
				    +"'"+inputValueMap.get("VALUE19")+"')";				
			
		executeUpdateQuery(query, "Part Code <b>"+inputValueMap.get("VALUE2")+"</b> is inserted into CATSCON_PART_STG table");		
		connection.commit();
		stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_PART}");	
		stproc_stmt.executeUpdate();		
		stproc_stmt = connection.prepareCall ("{call CATSCON_P_PARTINTERFACE.SP_INITPARTINTERFACE(?)}");
		stproc_stmt.setString(1, "");
		stproc_stmt.executeUpdate();
		stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_PART_ERPACK}");	
		stproc_stmt.executeUpdate();
		
		stproc_stmt.close();
		
	} catch (SQLException e) {		
		e.printStackTrace();
	}
	return RECORD_ID;	

}


	public int addMfgForItem(LinkedHashMap<String, String> inputValueMap) {
		String query = null;		
		int RECORD_ID = 0;
		CallableStatement stproc_stmt;  
		try {
			
			RECORD_ID = generateRandomNum(10000000);
			
			query = "INSERT "
					 +"INTO CATS.CATSCON_MFG_STG"
					   +"("
					     +"MANUFACTURER_NAME,"
					     +"MFG_PART_NUM,"
					     +"ITEM_SEGMENT1,"
					     +"DESCRIPTION,"
					     +"UNIQUE_ID,"
					     +"RECORD_ID,"
					     +"CREATION_DATE,"
					     +"PROCESS_FLAG"
					   +")"
					   +"VALUES"
					   +"("
					     +"'"+inputValueMap.get("VALUE1")+"',"
					     +"'"+inputValueMap.get("VALUE2")+"',"
					     +"'"+inputValueMap.get("VALUE3")+"',"
					     +"'"+inputValueMap.get("VALUE4")+"',"
					     +"'"+inputValueMap.get("VALUE5")+"',"
					     +RECORD_ID+","
					     +inputValueMap.get("VALUE7")+","
					     +"'"+inputValueMap.get("VALUE8")+"')";	
			
			//System.out.println(query);
			
			executeUpdateQuery(query, "MFG <b>"+inputValueMap.get("VALUE1")+"</b> with MFG Part # <b>"+inputValueMap.get("VALUE2")+"</b> is added for Item code <b>"+inputValueMap.get("VALUE3")+"</b> into CATSCON_MFG_STG table");
			connection.commit();	
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_MFG}");	
			stproc_stmt.executeUpdate();		
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_PARTINTERFACE.SP_INITPARTINTERFACE(?)}");
			stproc_stmt.setString(1, "");
			stproc_stmt.executeUpdate();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_MFG_ERPACK}");	
			stproc_stmt.executeUpdate();
			
			stproc_stmt.close();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;	
		
	}

	
	public int createPurchaseOrder(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		int RECORD_ID = 0;
		CallableStatement stproc_stmt;  
		try {
			
			RECORD_ID = generateRandomNum(10000000);
			
			query="INSERT "
					+"INTO CATS.CATSCON_PO_STG"
					  +"("
					    +"PHA_OPERATING_UNIT_ID,"
					    +"PHA_PO_NUMBER,"
					    +"PHA_REVISION_NUM,"
					    +"PHA_VENDOR_NUMBER,"
					    +"PHA_VENDOR_NAME,"
					    +"PHA_VENDOR_SITE_CODE,"
					    +"PHA_SHIP_TO_LOCATION_ID,"
					    +"PHA_COMMENTS,"
					    +"PHA_AUTHORIZATION_STATUS,"
					    +"PHA_CANCEL_FLAG,"
					    +"PHA_CLOSED_CODE,"
					    +"PHA_CURRENCY_CODE,"
					    +"PHA_CREATION_DATE,"
					    +"PHA_APPROVED_DATE,"
					    +"PHA_LAST_UPDATE_DATE,"
					    +"PHA_CREATED_BY,"
					    +"PLA_LINE_NUM,"
					    +"PLA_ITEM_CODE,"
					    +"PLA_ITEM_DESCRIPTION,"
					    +"PLA_ITEM_CATEGORY,"
					    +"PLA_UNIT_MEASURE_CODE,"
					    +"PLA_UNIT_PRICE,"
					    +"PLA_WARRANTY_START_DATE,"
					    +"PLA_WARRANTY_PERIOD,"
					    +"PLA_CANCEL_FLAG,"
					    +"PLA_CLOSED_CODE,"
					    +"PLA_CREATION_DATE,"
					    +"PLA_LAST_UPDATE_DATE,"
					    +"PLLA_SHIPMENT_NUM,"
					    +"PLLA_SHIP_TO_ORGANIZATION_ID,"
					    +"PLLA_SHIP_TO_LOCATION_ID,"
					    +"PLLA_QUANTITY_ORDERED,"
					    +"PLLA_NEED_BY_DATE,"
					    +"PLLA_QUANTITY_RECEIVED,"
					    +"PLLA_QUANTITY_CANCELLED,"
					    +"PLLA_CLOSED_CODE,"
					    +"PDA_PO_DISTRIBUTION_ID,"
					    +"PDA_DISTRIBUTION_NUM,"
					    +"PDA_DESTINATION_TYPE_CODE,"
					    +"PDA_CHARGE_ACCOUNT,"
					    +"PDA_EX_RATE_DATE,"
					    +"RECORD_ID,"
					    +"CREATION_DATE,"
					    +"PROCESS_FLAG"
					  +")"
					  +"VALUES"
					  +"("
					    +"'"+inputValueMap.get("VALUE1")+"',"
					    +"'"+inputValueMap.get("VALUE2")+"',"
					    +"'"+inputValueMap.get("VALUE3")+"',"
					    +"'"+inputValueMap.get("VALUE4")+"',"
					    +"'"+inputValueMap.get("VALUE5")+"',"
					    +"'"+inputValueMap.get("VALUE6")+"',"
					    +"'"+inputValueMap.get("VALUE7")+"',"
					    +"'"+inputValueMap.get("VALUE8")+"',"
					    +"'"+inputValueMap.get("VALUE9")+"',"
					    +"'"+inputValueMap.get("VALUE10")+"',"
					    +"'"+inputValueMap.get("VALUE11")+"',"
					    +"'"+inputValueMap.get("VALUE12")+"',"
					    +inputValueMap.get("VALUE13")+","
					    +inputValueMap.get("VALUE14")+","
					    +inputValueMap.get("VALUE15")+","
					    +"'"+inputValueMap.get("VALUE16")+"',"
					    +"'"+inputValueMap.get("VALUE17")+"',"
					    +"'"+inputValueMap.get("VALUE18")+"',"
					    +"'"+inputValueMap.get("VALUE19")+"',"
					    +"'"+inputValueMap.get("VALUE20")+"',"
					    +"'"+inputValueMap.get("VALUE21")+"',"
					    +"'"+inputValueMap.get("VALUE22")+"',"
					    +"'"+inputValueMap.get("VALUE23")+"',"
					    +"'"+inputValueMap.get("VALUE24")+"',"
					    +"'"+inputValueMap.get("VALUE25")+"',"
					    +"'"+inputValueMap.get("VALUE26")+"',"
					    +inputValueMap.get("VALUE27")+","
					    +inputValueMap.get("VALUE28")+","
					    +"'"+inputValueMap.get("VALUE29")+"',"
					    +"'"+inputValueMap.get("VALUE30")+"',"
					    +"'"+inputValueMap.get("VALUE31")+"',"
					    +"'"+inputValueMap.get("VALUE32")+"',"
					    +inputValueMap.get("VALUE33")+","
					    +"'"+inputValueMap.get("VALUE34")+"',"
					    +"'"+inputValueMap.get("VALUE35")+"',"
					    +"'"+inputValueMap.get("VALUE36")+"',"
					    +"'"+inputValueMap.get("VALUE37")+"',"
					    +"'"+inputValueMap.get("VALUE38")+"',"
					    +"'"+inputValueMap.get("VALUE39")+"',"
					    +"'"+inputValueMap.get("VALUE40")+"',"
					    +inputValueMap.get("VALUE41")+","
					    +RECORD_ID+","
					    +inputValueMap.get("VALUE43")+","
					    +"'"+inputValueMap.get("VALUE44")+"')";
					 
			//System.out.println(query);
			executeUpdateQuery(query, "PO - <b>"+inputValueMap.get("VALUE2")+"</b> for Item <b>"+inputValueMap.get("VALUE18")+"</b> is inserted in to CATSCON_PO_STG table");
			connection.commit();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_PO}");	
			stproc_stmt.executeUpdate();		
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_POINTERFACE.SP_INITPOINTERFACE(?)}");
			stproc_stmt.setString(1, "");
			stproc_stmt.executeUpdate();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_PO_ERPACK}");	
			stproc_stmt.executeUpdate();
			
			stproc_stmt.close();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;
	}
	
	public void createBillOfMaterial(LinkedHashMap<String, String> inputValueMap){
		String query = null;	
		CallableStatement stproc_stmt; 
		try {
			
			query = "INSERT "
						+"INTO CATS.CATSCON_BOM_STG"
						  +"("
						    +"ORGANIZATION_CODE,"
						    +"ORGANIZATION_ID,"
						    +"BILL_ITEM_NAME,"
						    +"ITEM_SEQUENCE_NUMBER,"
						    +"COMPONENT_ITEM_NAME,"
						    +"QUANTITY_PER_ASSEMBLY,"
						    +"START_EFFECTIVE_DATE,"
						    +"UNIQUE_ID,"
						    +"RECORD_ID,"
						    +"CREATION_DATE,"
						    +"PROCESS_FLAG,"
						    + "YIELD"
						  +")"
						  +"VALUES"
						  +"("
						  	+"'"+inputValueMap.get("VALUE1")+"',"
						  	+ Integer.parseInt(inputValueMap.get("VALUE2"))+","
						    +"'"+inputValueMap.get("VALUE3")+"',"
						    + Integer.parseInt(inputValueMap.get("VALUE4"))+","
						    +"'"+inputValueMap.get("VALUE5")+"',"
						    + Integer.parseInt(inputValueMap.get("VALUE6"))+","
						    +inputValueMap.get("VALUE7")+","
						    +"'"+inputValueMap.get("VALUE8")+"',"
						    +generateRandomNum(10000000)+","
						    +inputValueMap.get("VALUE10")+","
						    +"'"+inputValueMap.get("VALUE11")+"',"
						    +"'"+inputValueMap.get("VALUE12")+"')";
						 
			//System.out.println(query);
			executeUpdateQuery(query, "BOM is created for Item code - "+inputValueMap.get("VALUE3")+" where, Item Sequence # - <b>"+inputValueMap.get("VALUE4")+
																										"</b>, Component Item Code - <b>"+inputValueMap.get("VALUE5")+
																										"</b>, Qty Per Assembly - <b>"+ inputValueMap.get("VALUE6")+"</b>");
			connection.commit();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_BOM}");	
			stproc_stmt.executeUpdate();		
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_PARTINTERFACE.SP_INITPARTINTERFACE(?)}");
			stproc_stmt.setString(1, "");
			stproc_stmt.executeUpdate();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_BOM_ERPACK}");	
			stproc_stmt.executeUpdate();
			
			stproc_stmt.close();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public int createMaterialReceiveReceipt(LinkedHashMap<String, String> inputValueMap){
		String query = null;
		int RECORD_ID = 0;
		CallableStatement stproc_stmt; 
		try {
			
			RECORD_ID = generateRandomNum(10000000);
			
			query = "INSERT "
						+"INTO CATSCON_MRR_STG"
						  +"("
						    +"OPERATING_UNIT,"
						    +"TO_ORGANIZATION_ID,"
						    +"VENDOR_NUMBER,"
						    +"VENDOR_NAME,"
						    +"VENDOR_SITE_CODE,"
						    +"PO_NUMBER,"
						    +"PO_LINE_NUM ,"
						    +"PO_SHIPMENT_NUM,"
						    +"RECEIPT_NUM,"
						    +"SHIPMENT_NUM,"
						    +"SHIPPED_DATE,"
						    +"TRANSACTION_DATE,"
						    +"TRANSACTION_ID,"
						    +"SHIPMENT_HEADER_ID,"
						    +"SHIPMENT_LINE_ID,"
						    +"ITEM_CODE ,"
						    +"QUANTITY,"
						    +"LOCATION_ID,"
						    +"PO_UNIT_PRICE,"
						    +"H_ATTRIBUTE10,"
						    +"H_ATTRIBUTE11,"
						    +"H_ATTRIBUTE14,"
						    +"H_ATTRIBUTE6,"
						    +"H_ATTRIBUTE7,"
						    +"H_ATTRIBUTE8,"
						    +"H_ATTRIBUTE9 ,"
						    +"L_ATTRIBUTE10,"
						    +"L_ATTRIBUTE12,"
						    +"L_ATTRIBUTE13,"
						    +"DESTINATION_TYPE_CODE,"
						    +"MRR_CREATED_BY,"
						    +"INTERFACE_NAME,"
						    +"RECORD_ID,"
						    +"PROCESS_FLAG,"
						    +"CREATION_DATE,"
						    +"CREATED_BY"
						  +")"
						  +"VALUES"
						  +"("
						  	+ Integer.parseInt(inputValueMap.get("VALUE1"))+","
						  	+ Integer.parseInt(inputValueMap.get("VALUE2"))+","
						    +"'"+inputValueMap.get("VALUE3")+"',"
						    +"'"+inputValueMap.get("VALUE4")+"',"
						    +"'"+inputValueMap.get("VALUE5")+"',"
						    +"'"+inputValueMap.get("VALUE6")+"',"
						    + Integer.parseInt(inputValueMap.get("VALUE7"))+","
						    + Integer.parseInt(inputValueMap.get("VALUE8"))+","
						    +"'"+inputValueMap.get("VALUE9")+"',"
						    +"'"+inputValueMap.get("VALUE10")+"',"
						    +inputValueMap.get("VALUE11")+","
						    +inputValueMap.get("VALUE12")+","
						    +generateRandomNum(10000000)+","
						    +generateRandomNum(10000000)+","
						    + Integer.parseInt(inputValueMap.get("VALUE15"))+","
						    +"'"+inputValueMap.get("VALUE16")+"',"
						    + Integer.parseInt(inputValueMap.get("VALUE17"))+","
						    + Integer.parseInt(inputValueMap.get("VALUE18"))+","
						    + Integer.parseInt(inputValueMap.get("VALUE19"))+","
						    +"'"+inputValueMap.get("VALUE20")+"',"
						    +"'"+inputValueMap.get("VALUE21")+"',"
						    +"'"+inputValueMap.get("VALUE22")+"',"
						    +"'"+inputValueMap.get("VALUE23")+"',"
						    +inputValueMap.get("VALUE24")+","
						    +inputValueMap.get("VALUE25")+","
						    +inputValueMap.get("VALUE26")+","
						    +"'"+inputValueMap.get("VALUE27")+"',"
						    +"'"+inputValueMap.get("VALUE28")+"',"
						    +"'"+inputValueMap.get("VALUE29")+"',"
						    +"'"+inputValueMap.get("VALUE30")+"',"
						    +inputValueMap.get("VALUE31")+","
						    +"'"+inputValueMap.get("VALUE32")+"',"
						    +RECORD_ID+","
						    +"'"+inputValueMap.get("VALUE34")+"',"
						    +inputValueMap.get("VALUE35")+","
						    + Integer.parseInt(inputValueMap.get("VALUE36"))+")";
			
			//System.out.println(query);
			
			executeUpdateQuery(query, "MRR - <b>"+inputValueMap.get("VALUE9")+"</b> is created for PO - <b>"+inputValueMap.get("VALUE6")+"</b>");
			connection.commit();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_MRR}");	
			stproc_stmt.executeUpdate();		
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_MRRINTERFACE.SP_INITMRRINTERFACE(?)}");
			stproc_stmt.setString(1, "");
			stproc_stmt.executeUpdate();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_MRR_ERPACK}");	
			stproc_stmt.executeUpdate();
			
			stproc_stmt.close();		
			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;
	}
	
	public void poTaxUpdate(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		try {			
			query = "UPDATE CATSCUST_MRR "
					+"SET TAX_UPDATE = 'Y'"
					+"WHERE MRRID IN (SELECT MRRID FROM CATSCUST_MRR WHERE POCODE='"+inputValueMap.get("VALUE6")+"')";
			executeUpdateQuery(query, "Tax Update for PO - <b>"+inputValueMap.get("VALUE6")+"</b>");
			connection.commit();			
			
		} catch (SQLException e) {	
			test.log(LogStatus.FAIL, "Tax Update for PO - <b>"+inputValueMap.get("VALUE6")+"</b>");
			e.printStackTrace();			
		}
	}
	
	
	@SuppressWarnings("resource")
	 public int deliveryConfirmation(LinkedHashMap<String, String> inputValueMap) {

		String query = null;
		String SERIALIZED;
		String TRANSACTIONID;
		String ASSETCODE;
		String SERIALNUMBER;
		int RECORD_ID = 0;
		ResultSet rs;
		Statement stmt;
		CallableStatement stproc_stmt;

		
		try {
			String	query1 =  "SELECT * FROM CATS_PART WHERE PARTCODE =" +"'"+inputValueMap.get("VALUE7")+"'";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
			SERIALIZED = rs.getString("SERIALIZED");
			
		if (SERIALIZED.equalsIgnoreCase("N")){
			
			String query2 = "SELECT MAX(PARTTRANSACTIONID) AS PARTTRANSACTIONID FROM CATS_PARTTRANSACTION WHERE ORIGINATOR ="+"'CATS_POTRANSACTION'"
			                +"AND PARTCODE= "+"'"+inputValueMap.get("VALUE7")+"'";
			stmt = connection.createStatement();
			
			rs = stmt.executeQuery(query2);	
			while (rs.next()) {
				TRANSACTIONID = rs.getString("PARTTRANSACTIONID");	
				RECORD_ID = generateRandomNum(10000000);
				query = "INSERT INTO "
						+"CATS.CATSCON_POREC_STG"
						+"("
						+"CATS_RCPT_LINE_DLVR_TRX_ID,"
						+"PO_RCPT_LINE_DLVR_ID,"
						+"LOT_NUMBER,"
						+"RECORD_ID,"
						+"CREATION_DATE,"
						+"PROCESS_FLAG,"
						+"ITEM_CODE,"
						+"CREATED_BY"
						+")"
						+
						"VALUES"
						+ "("
						+"'"+TRANSACTIONID+"',"
						+"'"+inputValueMap.get("VALUE2")+"',"
						+"'"+inputValueMap.get("VALUE3")+"',"
						+"'"+RECORD_ID+"',"
						+inputValueMap.get("VALUE5")+","
						+"'"+inputValueMap.get("VALUE6")+"',"
						+"'"+inputValueMap.get("VALUE7")+"',"
						+selectQuerySingleValue("SELECT * FROM CATS_CONTACT_UDFDATA WHERE CONTACTID=1", "NUMBER3")
						+")";
				executeUpdateQuery(query, "Delivery Confirmation  - <b>"+inputValueMap.get("VALUE7")+"</b>");
				connection.commit();
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_POREC}");	
				stproc_stmt.executeUpdate();		
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_POCONFINTERFACE.SP_INITPOCONFINTERFACE(?)}");
				stproc_stmt.setString(1, "");
				stproc_stmt.executeUpdate();
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_POREC_ERPACK}");	
				stproc_stmt.executeUpdate();
				
				stproc_stmt.close();		
				
			}
			
		}else{
			String query3 = "SELECT * FROM CATS_ASSETTRANSACTION WHERE ASSETTRANSACTIONID IN (select MAX(ASSETTRANSACTIONID) AS ASSETTRANSACTIONID  FROM CATS_ASSETTRANSACTION WHERE ORIGINATOR ="+"'CATS_POTRANSACTION'"
					+"AND PARTCODE= "+"'"+inputValueMap.get("VALUE7")+"')";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query3);	
			while (rs.next()) {
				TRANSACTIONID = rs.getString("ASSETTRANSACTIONID");	
				ASSETCODE = rs.getString("ASSETCODE");
				SERIALNUMBER = rs.getString("SERIALNUMBER");
				addRuntimeTestData("ASSETCODE", ASSETCODE);
				RECORD_ID = generateRandomNum(10000000);
				query = "INSERT INTO "
						+"CATS.CATSCON_POREC_STG"
						+"("
						+"CATS_RCPT_LINE_DLVR_TRX_ID,"
						+"PO_RCPT_LINE_DLVR_ID,"
						+"LOT_NUMBER,"
						+"RECORD_ID,"
						+"CREATION_DATE,"
						+"PROCESS_FLAG,"
						+"ITEM_CODE,"
						+"CREATED_BY"
						+")"
						+
						"VALUES"
						+ "("
						+"'"+TRANSACTIONID+"',"
						+"'"+inputValueMap.get("VALUE2")+"',"
						+"'"+inputValueMap.get("VALUE3")+"',"
						+"'"+RECORD_ID+"',"
						+inputValueMap.get("VALUE5")+","
						+"'"+inputValueMap.get("VALUE6")+"',"
						+"'"+inputValueMap.get("VALUE7")+"',"
						+selectQuerySingleValue("SELECT * FROM CATS_CONTACT_UDFDATA WHERE CONTACTID=1", "NUMBER3")
						+")";
				connection.commit();
				executeUpdateQuery(query, "Delivery Confirmation ITEMCODE : - <b>"+inputValueMap.get("VALUE7")+"</b> with Assetcode : <b>" + ASSETCODE +"</b>");
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_POREC}");	
				stproc_stmt.executeUpdate();		
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_POCONFINTERFACE.SP_INITPOCONFINTERFACE(?)}");
				stproc_stmt.setString(1, "");
				stproc_stmt.executeUpdate();
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_POREC_ERPACK}");	
				stproc_stmt.executeUpdate();
				
				stproc_stmt.close();		
			}

		}
				
		}
		}catch (SQLException e) {	
			test.log(LogStatus.FAIL, "Delivery Confirmation   - "+inputValueMap.get("VALUE7")+" is not done successfully");
			e.printStackTrace();			
		}
		return RECORD_ID;
	}
	
	public int getLastTransactionId(String query, String columnName){
		
		ResultSet rs;
		Statement stmt;
		int lastTransactionId = 0;
		try{
		stmt = connection.createStatement();
		rs = stmt.executeQuery(String.format(query));
		if(rs!=null){
		while (rs.next()) {
			if(rs.getString(columnName)!=null){
			lastTransactionId = Integer.parseInt(rs.getString(columnName));
			break;
		}
		}
		}
		}catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return lastTransactionId;
		
	}
	
	//Transaction Validations
	
	public boolean validateInboundTransaction(String inboundType, String processFlag, String errorFlag, String query, String inputValue1, int recordId) {
		ResultSet rs;
		Statement stmt;
		String PROCESS_FLAG;
		String ERROR_MESSAGE;
		boolean succesFlag = false;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(String.format(query, inputValue1,recordId ));
			while (rs.next()) {
				PROCESS_FLAG = rs.getString(processFlag);
				
				verifyCounter++;
				if (PROCESS_FLAG.equals("P")) {
					test.log(LogStatus.PASS,inboundType +" - <b>" + inputValue1 + "</b> is processed successfully (RECORD_ID - <b>" + recordId + "</b>)");
					verifyCounter=0;
					succesFlag = true;
					break;
				} else if (PROCESS_FLAG.equals("N") || PROCESS_FLAG.equals("D")){
					if (verifyCounter < 20) {
						HardDelay(3000);
						test.log(LogStatus.INFO,verifyCounter + ": Re-checking "+processFlag+" after 3 secs wait....");
						succesFlag = validateInboundTransaction(inboundType, processFlag, errorFlag, query, inputValue1, recordId);
					} else {
						ERROR_MESSAGE = rs.getString(errorFlag);
						test.log(LogStatus.FAIL,inboundType +" - <b>" + inputValue1 + "</b> is not processed successfully (RECORD_ID - <b>" + recordId + "</b>)");
						test.log(LogStatus.FAIL,processFlag +" - <b>"+PROCESS_FLAG + "</b> | "  + errorFlag+" - <b>"+ERROR_MESSAGE+"</b>");
						verifyCounter=0;						
					}
				}else{
					ERROR_MESSAGE = rs.getString(errorFlag);
					test.log(LogStatus.FAIL,inboundType +" - <b>" + inputValue1 + "</b> is not processed successfully (RECORD_ID - <b>" + recordId + "</b>)");
					test.log(LogStatus.FAIL,processFlag +" - <b>"+PROCESS_FLAG + "</b> | " + errorFlag+" - <b>"+ERROR_MESSAGE+"</b>");				
				}
			}
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, e);
		}
		return succesFlag;
	}
	
	
	public String generateTestData(String routineFolderName, String columnName, String columnValue) {

		String value = null;

		try {

			value = columnValue + getCurrentFormattedTime("ddMMhhmmss");

			switch (routineFolderName) {

			case "CONTAINER":
				globalRuntimeContainerDatamap.put(testParameters.getCurrentTestCase() + "#" + columnName, value);
				break;
			
			case "RECEIVING":
				globalRuntimeRecevingDatamap.put(testParameters.getCurrentTestCase() + "#" + columnName, value);
				break;
				
			}

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

		return value;

	}
	
	
	
	public void addRuntimeTestData(String routineFolderName, String columnName, String columnValue) {

		
		try {
			

			switch (routineFolderName) {

			case "CONTAINER":
				globalRuntimeContainerDatamap.put(testParameters.getCurrentTestCase() + "#" + columnName, columnValue);
				break;
			
			case "RECEIVING":
				globalRuntimeRecevingDatamap.put(testParameters.getCurrentTestCase() + "#" + columnName, columnValue);
				break;
				
			}

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

		

	}
	
	
	public String getRuntimeTestdata(String routineFolderName, String tescase_ColumnName) {

		String value = null;

		try {

			switch (routineFolderName) {

			case "CONTAINER":
				value = globalRuntimeContainerDatamap.get(tescase_ColumnName);
				break;

			case "RECEIVING":
				value = globalRuntimeRecevingDatamap.get(tescase_ColumnName);
				break;

			}

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

		return value;

	}
	
	
	
	
	
	
	public void VerfiyAutopopulatefieldvalues(String labelxpath ,String objectName , String values ){
		

		String value1 = driver.findElement(By.xpath(labelxpath+"/following-sibling::android.view.View")).getAttribute("name");	
		String value2 = values;
		
		if (value1.equalsIgnoreCase(value2)){
			test.log(LogStatus.PASS, "<b>" + objectName + "</b> matches the given Testdata <b>"+value2+"</b>", "");	
		}
		else{
			test.log(LogStatus.FAIL, "<font color=red><b>" + objectName + "</b></font>-not matches the given Testdata- <b> <font color=red>"+value2+"</b></font>", "");
		}
		
	}
	
	public void verifyMessage(String msg) {
		
		By ID_MESSAGE= By.id("message");
		By ID_ALERT_TITLE= By.id("alertTitle");
		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).equalsIgnoreCase(msg)) {
			report(driver,test,msg + " is displayed", LogStatus.PASS);	

		} else {
			report(driver,test,msg + " is not displayed", LogStatus.FAIL);	
	
		}
	}

	public boolean validateMessage(String msg) {	
		By ID_MESSAGE= By.id("message");
		By ID_ALERT_TITLE= By.id("alertTitle");
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).equalsIgnoreCase(msg)) {
			report(driver,test,msg + " is displayed", LogStatus.PASS);	
			return true;
		} else {
			report(driver,test,msg + " is not displayed", LogStatus.FAIL);	
			return false;
		}
	}
	
	// Verification Components
	
	public void validateLoopField(String loopField) {	
		String XPATH_TXT = ".//android.view.View[@content-desc='%s']";
		if (isElementPresent(By.xpath(String.format(XPATH_TXT, loopField)),"Loop field - "+loopField)) {
			report(driver,test, " Transaction is successfull", LogStatus.PASS);			
		} else {
			report(driver,test, " Transaction is not successfull", LogStatus.FAIL);			
		}
	}
	
	public void addRuntimeTestData(String columnName, String columnValue) {

		try {

			//globalRuntimeDatamap.put(testParameters.getCurrentTestCase() + "#" + columnName, columnValue);
			runtimeDataProperties.put(testParameters.getCurrentTestCase() + "#" + columnName, columnValue);

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

	}
	
	
	public String getRuntimeTestdata(String tescase_ColumnName) {

		String data = null;

		try {

			//data = globalRuntimeDatamap.get(tescase_ColumnName);
			data = runtimeDataProperties.getProperty(tescase_ColumnName);

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

		return data;

	}
	
	
	public String generateTestData(String columnName, String columnValue) {

		String data = null;

		try {
			
			data = columnValue + getCurrentFormattedTime("ddMMhhmmss");

			//globalRuntimeDatamap.put(testParameters.getCurrentTestCase() + "#" + columnName, data);
			runtimeDataProperties.put(testParameters.getCurrentTestCase() + "#" + columnName, data);

		} catch (Exception e) {
			test.log(LogStatus.FAIL, e);
		}

		return data;

	}
	
	/**
	 * Function to get Pick List value
	 * 	 
	 * @return void
	 * @author Hari 
	 * @since 06/27/2017
	 * 
	 */

	@SuppressWarnings("unchecked")
	public void selectPickListValue(String pickListValue) throws TimeoutException, NoSuchElementException{

				
		List<WebElement> elements = driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout/android.widget.TextView[@index='0']");
		
		for(WebElement element: elements){			
			
			if(element.getText().equalsIgnoreCase(pickListValue)){
				element.click();	
				break;
			}
			}
		
			
			takeScreenshot("Pick List Value - "+pickListValue+" is selected");
		
	}
	
	
	
	public void clickRoutineBackButton() throws TimeoutException, NoSuchElementException {

		waitCommand(CONTENT_DESC_ROUITNE_BACK_BTN);
		driver.findElement(CONTENT_DESC_ROUITNE_BACK_BTN).click();
		takeScreenshot("Click Routine back Button");

	}
	
}