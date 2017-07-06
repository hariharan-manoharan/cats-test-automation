package main.java.businessComponents.MOBILE.AIRTEL;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import main.java.executionSetup.TestParameters;
import main.java.reporting.HtmlReport;
import main.java.testDataAccess.DataTable;
import main.java.utils.GlobalRuntimeDataProperties;
import main.java.utils.Utility;

public class ReusableLibrary extends Utility implements RoutineObjectRepository {
	
	
	private static HashMap<String, String> globalRuntimeDatamap = new HashMap<String, String>();
	private boolean attachmentFlag = false;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().description(\"?\")")
	private AndroidElement myElement;

	@SuppressWarnings("rawtypes")
	public ReusableLibrary(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable, testParameters);
	}
		
	
	public void createNewConnection() throws TimeoutException, NoSuchElementException  {

		LoginActivity loginActivity = new LoginActivity(test, driver, dataTable,testParameters);
		loginActivity.addConnection();

	}

	public void login() throws TimeoutException, NoSuchElementException  {

		LoginActivity loginActivity = new LoginActivity(test, driver, dataTable,testParameters);
		loginActivity.login();

	}

	public void selectUserProfile(String profile) throws TimeoutException, NoSuchElementException{		
		
		waitCommand(By.name(profile));			
		Click(By.name(profile), "Click - "+profile+" Profile is selected");

	}
	
	public void clickRoutineFolder(String folderName) throws TimeoutException, NoSuchElementException  {
		Click(By.name(folderName), "Click - Routines Folder - " + folderName + " is selected");
	}
	
	public void clickRoutine(String routineName) throws TimeoutException, NoSuchElementException  {
		// ScrolltoText(routineName);
		Click(By.name(routineName), "Click - Routine - " + routineName + " is selected");
	}



	/**
	 * Function to enter text in WebElement
	 * 
	 * @param1 String fieldName
	 * @param2 String value	
	 * @return void
	 * @author Hari
	 * @since 06/27/2017
	 * 
	 */

	public void enterText(String field, String data) throws TimeoutException, NoSuchElementException {

		By by = By.xpath(String.format(XPATH_TXT, field));
		
		waitCommand(by);
		WebElement element = this.driver.findElement(by);
		this.driver.pressKeyCode(112); // DELETE Key event - https://developer.android.com/reference/android/view/KeyEvent.html#KEYCODE_FORWARD_DEL
		element.sendKeys(data);
		takeScreenshot(field, data);

	}

	/**
	 * Function to click "Next" button in CATS Mobility - Application Specific
	 * 
	 * @param no parameters
	 * @return void
	 * @author Hari
	 * @since 06/27/2017
	 * 
	 */

	public void clickNext() throws TimeoutException, NoSuchElementException{
		 	
		    waitCommand(By.xpath(String.format(XPATH_TXT_CONTAINS, ":")));		    
			this.driver.findElement(By.id("next")).click();				
		}
		
	
	
	/**
	 * Function to click "Previous" button in CATS Mobility - Application Specific
	 * 
	 * @param no parameters
	 * @return void
	 * @author Hari 
	 * @since 06/27/2017
	 * 
	 */

	public void clickPrevious() throws TimeoutException, NoSuchElementException{
		
			waitCommand(By.id("previous"));
			this.driver.findElement(By.id("previous")).click();
			takeScreenshot("Click Previous Button");
		
	}

	
	/**
	 * Function to click spyglass  - Need to Fix this one - Avoid getting the fieldIndex from user
	 * 
	 * @param1 String reportName	 
	 * @return void
	 * @author Hari 
	 * @since 06/27/2017
	 * 
	 */

	@SuppressWarnings("unchecked")
	public void clickSpyGlass(String pickListName) throws TimeoutException, NoSuchElementException {
		HardDelay(8000L);
		List<WebElement> element = driver.findElementsByAndroidUIAutomator(
				"new UiSelector().className(\"android.view.View\").index(0).clickable(true)");
		int size = element.size();
		if (size > 1) {
			element.get(size - 1).click();
		} else {
			element.get(0).click();
		}

		HardDelay(5000L);
		takeScreenshot("Clicked - " + pickListName + " spyglass");

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
		
		if(pickListValue.contains("#")){
			pickListValue = getRuntimeTestdata(pickListValue);
		}			
	
		List<WebElement> elements = this.driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout/android.widget.TextView[@index='0']");
		
		for(WebElement element: elements){			
			
			if(element.getText().equalsIgnoreCase(pickListValue)){
				element.click();	
				break;
			}
			}
		
			
			takeScreenshot("Pick List Value - "+pickListValue+" is selected");
		
	}
	
	
	
	/**
	 * Function to enter text in WebElement
	 * 
	 * @param1 String fieldName
	 * @param2 String value	
	 * @return void
	 * @author Hari
	 * @since 06/27/2017
	 * 
	 */

	public void enterTextFormattedData(String field, String data, String columnName) throws TimeoutException, NoSuchElementException {
		
		data = (data.contains("#")) ?  getRuntimeTestdata(data) : generateTestData(columnName, data);
		
		By by = By.xpath(String.format(XPATH_TXT, field));
		
		waitCommand(by);
		WebElement element = this.driver.findElement(by);
		this.driver.pressKeyCode(112); // DELETE Key event - https://developer.android.com/reference/android/view/KeyEvent.html#KEYCODE_FORWARD_DEL
		element.sendKeys(data);
		takeScreenshot(field, data);
	
	}
	
	
	public void enterTransferOrder(){
		
		String NoofTransfer = selectQuerySingleValue(TRANSFERCOUNT_PICK, "BAL-MUNDKA-MDEL");
		
	}
	public static long generateRandom(int length) {
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return Long.parseLong(new String(digits));
	}
	
	
	
	// Verification Components
	
	public void validateLoopField(String loopField) {		
		if (isElementPresent(By.xpath(String.format(XPATH_TXT, loopField)),"Loop field - "+loopField)) {
			report(driver,test, " Transaction is successfull", LogStatus.PASS);			
		} else {
			report(driver,test, " Transaction is not successfull", LogStatus.FAIL);			
		}
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

	public void takeScreenshot(String reportName, String data) {

		if(properties.getProperty("take.screenshot.on.pass").equalsIgnoreCase("True")){
		
		String screenshotName = getCurrentFormattedTime("dd_MMM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		test.log(LogStatus.PASS, reportName + data,
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenshotName + ".png"));
		}else{
			test.log(LogStatus.PASS, reportName + data);	
		}

	}
	
	public void clickConfirmPrompt(String msg , String data) {		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).equalsIgnoreCase(msg)) {
			report(driver,test, msg + " is displayed", LogStatus.PASS);	
			
			if(data.equalsIgnoreCase("Yes")){
			Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + msg);
			}
			else{
			Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + msg);	
			}
		} else {
			report(driver,test, msg + " is not displayed", LogStatus.FAIL);			
		}
	}
	
	
	public void clickYesConfirmPromptContains(String msgContains) {		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).contains(msgContains)) {
			report(driver,test, msgContains + " is displayed", LogStatus.PASS);		
			Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + msgContains);
		} else {
			report(driver,test, msgContains + " is not displayed", LogStatus.FAIL);			
		}
	}
	
	public void clickNoConfirmPromptContains(String msgContains) {		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).contains(msgContains)) {
			report(driver,test, msgContains + " is displayed", LogStatus.PASS);		
			Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + msgContains);
		} else {
			report(driver,test, msgContains + " is not displayed", LogStatus.FAIL);			
		}
	}
	
	
	public void clickOkPrompt(String msg) {

		if (msg.contains("@")){
			String[] key =msg.split("@");
			String errormessage1 =  key [0];
			String errormessage2 =  key[1];
			String errormessage3 =  key[2];

			String data = runtimeDataProperties.getProperty(errormessage2);

			String errormessage = errormessage1 +data+errormessage3;
			msg=errormessage;

		}

		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).equalsIgnoreCase(msg)) {
			report(driver,test, msg + " is displayed", LogStatus.PASS);		
			Click(ID_MESSAGE_OK, "Clicked 'Ok' for prompt - " + msg);
		} else {
			report(driver,test, msg + " is not displayed", LogStatus.FAIL);			
		}
	}
	
	

	public void clearField(){
		this.driver.pressKeyCode(112);
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
	
	public void getPutTestdata(String field , String name){
		

		waitCommand(By.xpath(String.format(XPATH_TXT, field)+"/following-sibling::android.view.View"));
		
		String Fieldvalue = driver.findElement(By.xpath(String.format(XPATH_TXT, field)+"/following-sibling::android.view.View")).getAttribute("name");	
		
		
		addRuntimeTestData(name, Fieldvalue);
		
	}
	
	
	public void verifyAutopopulatefieldvalues(String field, String value) {
		
		waitCommand(By.xpath(String.format(XPATH_TXT, field)+"/following-sibling::android.view.View"));
		String fieldValue = driver.findElement(By.xpath(String.format(XPATH_TXT, field)+"/following-sibling::android.view.View")).getAttribute("name");			
	
		if(value.contains("#")){
			
			String value1= runtimeDataProperties.getProperty(value);
			
			value=value1;
			
		}
	
		if (!fieldValue.equals("")){
		if (value.equalsIgnoreCase(fieldValue)) {
			test.log(LogStatus.PASS, "<b>" + field + "</b> matches the given Testdata <b>" + value + "</b>", "");
		} else {
			test.log(LogStatus.FAIL, "<font color=red><b>" + field + "</b></font>-not matches the given Testdata- <b> <font color=red>" + value + "</b></font>", "");
		}
	}
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

	public static void waitForSeconds(String delaySeconds) {
		
		delaySeconds = delaySeconds +"000L";
		
		try {
			Thread.sleep(Long.parseLong(delaySeconds));
		} catch (Exception e) {

		}

	}
	
	
	// Routine specific validations
	
	public void parentReceivedCount(){
		By PARENT_RECEIVED_COUNT = By.xpath(String.format(XPATH_TXT_CONTAINS, "/"));
		int receivedCount = 0;
		int totalCount = 0;	
		String parentReceiveCount[] = new String[2];
		
		waitCommand(PARENT_RECEIVED_COUNT);
		
		if(isFieldDisplayed(PARENT_RECEIVED_COUNT, "Parent Received Count")){	
			parentReceiveCount = GetAttributeValue(PARENT_RECEIVED_COUNT, "name", "Parent Received Count").split("/");					
			receivedCount = Integer.parseInt(parentReceiveCount[0]);
			totalCount = Integer.parseInt(parentReceiveCount[1]);	
		}
	}
	
	public void deliveryConfirmation(){
		FunctionalComponents functionalComponents = new FunctionalComponents(test, driver, dataTable,testParameters);
		functionalComponents.deliveryConfirmation();
	}
	
	
	public void createPurchaseOrder(){
		FunctionalComponents functionalComponents = new FunctionalComponents(test, driver, dataTable,testParameters);
		functionalComponents.createPurchaseOrder();
	}
	
	public void createMaterialReceiveReceipt(){
		FunctionalComponents functionalComponents = new FunctionalComponents(test, driver, dataTable,testParameters);
		functionalComponents.createPurchaseOrder();
	}

}
