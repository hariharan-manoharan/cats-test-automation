package main.java.utils;

import java.io.File;
import java.io.IOException;
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
import main.java.reporting.HtmlReport;
import main.java.testDataAccess.DataTable;

public class Utility {

	@SuppressWarnings("rawtypes")
	protected AndroidDriver driver;
	protected ExtentTest test;
	protected DataTable dataTable;
	public static Properties properties;
	public static Connection connection;
	public static LinkedHashMap<String, String> environmentVariables;
	int verifyCounter = 0;
	

	@SuppressWarnings("rawtypes")
	public Utility(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
		this.test = test;
		this.driver = driver;
		this.dataTable = dataTable;
	}

	public Utility() {

	}

	@SuppressWarnings("static-access")
	public void setProperties(Properties properties) {
		this.properties = properties;
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

		String screenshotName = getCurrentFormattedTime();

		File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.log(LogStatus.PASS, reportName,
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenshotName + ".png"));

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

	public void takeScreenshotStatus(String reportName, LogStatus status) {

		String screenshotName = getCurrentFormattedTime();

		File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.log(status, reportName,
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenshotName + ".png"));

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

		String screenshotName = getCurrentFormattedTime();

		File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.log(LogStatus.PASS, reportName,
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenshotName + ".png"));

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

	public void report(String reportName, LogStatus status) {
		takeScreenshotStatus(reportName, status);		

	}

	/**
	 * Function to format the current time instance
	 * 
	 * @return String (formatted date)
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public static String getCurrentFormattedTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");
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

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(this.driver);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.withTimeout(100, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);

		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver arg0) {
				WebElement element = arg0.findElement(by);
				if (element != null) {
					return true;
				}
				return false;
			}
		};
		wait.until(function);

	}
	
	/**
	 * This function is similar to waitCommand which waits until the object is available with timeout of 100 seconds polling every 5 seconds
	 * and returns the value of until statement
	 *  	  
	 * @param1 By by
	 * @param2 String objectName	
	 * @return boolean 
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public boolean isObjectPresent(final By by, final String objectName) {
		
		try{
		
		this.driver.findElement(by).isDisplayed();		
		test.log(LogStatus.PASS, "Object - " + objectName + " is present", "");
		return true;				
		
		}catch(NoSuchElementException  e){
			test.log(LogStatus.INFO, "Object - " + objectName + " is not present", "");
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
		
		present = this.driver.findElement(by).isDisplayed();
		
		if(present){
			test.log(LogStatus.PASS, "Field - " + fieldName + " is present", "");
			return true;					
		}else{
			test.log(LogStatus.INFO, "Field - " + fieldName + " is not present", "");
			return false;
		}	
		}catch(Exception e){
			test.log(LogStatus.FAIL, "Field - " + fieldName + " is not present", "");
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

	public void EnterText(By by, String reportName, String text) {
		try {
			waitCommand(by);
			WebElement element = this.driver.findElement(by);
			this.driver.pressKeyCode(112); // DELETE Key event - // https://developer.android.com/reference/android/view/KeyEvent.html#KEYCODE_FORWARD_DEL
			element.sendKeys(text);
			takeScreenshot(reportName);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);

		}
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

	public void Click(By by, String reportName) {
		try {
			waitCommand(by);
			this.driver.findElement(by).click();
			HardDelay(5000L);
			takeScreenshot(reportName);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
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
					
			this.driver.findElement(By.xpath(".//android.view.View[@index='"+String.valueOf(fieldIndex)+"']/android.view.View[@index='0']/android.view.View[@index='0']")).click();
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

	public String GetText(By by, String fieldName) throws WebDriverException {
		String text = null;

		try {
			waitCommand(by);
			WebElement element = this.driver.findElement(by);
			text = element.getText();
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
			test.log(LogStatus.INFO, fieldName + ": Not Returned - " + text);
		}
		test.log(LogStatus.INFO, fieldName + ":  Returned - " + text);
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
		Actions actions = new Actions(this.driver);
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
			webElements = this.driver.findElements(by);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}
		return webElements;
	}

	public void HideKeyboard() {
		this.driver.hideKeyboard();

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
			this.driver.findElement(By.id("next")).click();
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
			this.driver.findElement(By.id("previous")).click();
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

		String actual = this.driver.findElement(by).getText().trim();

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
			WebElement element = this.driver.findElement(by);
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
			e.printStackTrace();
		}
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
			
			List<WebElement> elements = this.driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout");
			
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
			
			List<WebElement> elements = this.driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout");
			
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
	public void ScrolltoText(String Text) {
		try {
			this.driver.scrollToExact(Text);
			takeScreenshot("Scrolled to : " + Text);
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
		}

	}

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
			
		executeUpdateQuery(query, "Part Code "+inputValueMap.get("VALUE2")+" is inserted into CATSCON_PART_STG table");
		connection.commit();			
		
	} catch (SQLException e) {		
		e.printStackTrace();
	}
	return RECORD_ID;	

}


	public int addMfgForItem(LinkedHashMap<String, String> inputValueMap) {
		String query = null;		
		int RECORD_ID = 0;
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
			
			executeUpdateQuery(query, "MFG "+inputValueMap.get("VALUE1")+" with MFG Part # "+inputValueMap.get("VALUE2")+" is added for Item code "+inputValueMap.get("VALUE3")+" into CATSCON_MFG_STG table");
			connection.commit();			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;	
		
	}

	
	public int createPurchaseOrder(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		int RECORD_ID = 0;
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
			executeUpdateQuery(query, "PO - "+inputValueMap.get("VALUE2")+" for Item "+inputValueMap.get("VALUE18")+" is inserted in to CATSCON_PO_STG table");
			connection.commit();			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;
	}
	
	public void createBillOfMaterial(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
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
						 
			System.out.println(query);
			executeUpdateQuery(query, "BOM is created for Item code - "+inputValueMap.get("VALUE3")+" where, Item Sequence # - "+inputValueMap.get("VALUE4")+
																										", Component Item Code - "+inputValueMap.get("VALUE5")+
																										", Qty Per Assembly - "+ inputValueMap.get("VALUE6") );
			connection.commit();			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public int createMaterialReceiveReceipt(LinkedHashMap<String, String> inputValueMap){
		String query = null;
		int RECORD_ID = 0;
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
			
			executeUpdateQuery(query, "MRR - "+inputValueMap.get("VALUE9")+" is created for PO - "+inputValueMap.get("VALUE6"));
			connection.commit();			
			
			
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
			executeUpdateQuery(query, "Tax Update for PO - "+inputValueMap.get("VALUE6")+" is done successfully");
			connection.commit();			
			
		} catch (SQLException e) {	
			test.log(LogStatus.FAIL, "Tax Update for PO - "+inputValueMap.get("VALUE6")+" is not done successfully");
			e.printStackTrace();			
		}
	}
	
	
	
	
	//Transaction Validations
	
	public void validateInboundTransaction(String inboundType, String query, String inputValue1, int recordId) {
		ResultSet rs;
		Statement stmt;
		String PROCESS_FLAG;
		String ERROR_MESSAGE;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(String.format(query, inputValue1,recordId ));
			while (rs.next()) {
				PROCESS_FLAG = rs.getString("PROCESS_FLAG");
				
				verifyCounter++;
				if (PROCESS_FLAG.equals("P")) {
					test.log(LogStatus.PASS,inboundType +" - " + inputValue1 + " is processed successfully (RECORD_ID - " + recordId + ")");
					verifyCounter=0;
					break;
				} else {
					if (verifyCounter < 3) {
						HardDelay(30000);
						test.log(LogStatus.INFO,verifyCounter + ": Re-checking PROCESS_FLAG after 30 secs wait....");
						validateInboundTransaction(inboundType, query, inputValue1, recordId);
					} else {
						ERROR_MESSAGE = rs.getString("ERROR_MESSAGE");
						test.log(LogStatus.FAIL,inboundType +" - " + inputValue1 + " is not processed successfully (RECORD_ID - " + recordId + ")");
						test.log(LogStatus.INFO,"PROCESS_FLAG - "+PROCESS_FLAG+" ERROR_MESSAGE - "+ERROR_MESSAGE);
						verifyCounter=0;
					}

				}
			}
		} catch (SQLException e) {
			test.log(LogStatus.FAIL, e);
		}
	}
	

}