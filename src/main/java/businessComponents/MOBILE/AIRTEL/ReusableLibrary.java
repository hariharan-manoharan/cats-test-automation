package main.java.businessComponents.MOBILE.AIRTEL;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
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
import main.java.utils.Utility;

public class ReusableLibrary extends Utility implements RoutineObjectRepository {


	@AndroidFindBy(uiAutomator = "new UiSelector().description(\"?\")")
	private AndroidElement myElement;

	@SuppressWarnings("rawtypes")
	public ReusableLibrary(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable, testParameters);
	}


	public void createNewConnection() throws TimeoutException, NoSuchElementException  {
		
		Click(NAME_ADD_CONNECTION, "Click - AddConnection");
		Click(ID_ADD_CONNECTIONS, "Click - AddConnection Symbol");
		EnterText(NAME_TXT_CONNECTION_NAME, "Enter - Connection Name", environmentVariables.get("EnvironmentName"));
		EnterText(ID_TXT_CONNECTION_HOST, "Enter - Host", environmentVariables.get("MobilityHost"));
		EnterText(ID_TXT_CONNECTION_PORT, "Enter - Port", environmentVariables.get("MobilityPort"));
		if(environmentVariables.get("MobilitySSL").equalsIgnoreCase("Yes")){
		Click(ID_TOGGLE_BTN_SSL, "Click - Enable SSL");
		}
		Click(ID_ICON_SAVE, "Click - Save Connection");
		Click(ID_IMG_BACK_BTN, "Click - Back button");

	}

	public void login() throws TimeoutException, NoSuchElementException {

		// EnterText(ID_TXT_USERNAME, "Enter - Username", "catsadm");
		// EnterText(ID_TXT_PASSWORD, "Enter - Password", "catscats11");
		// HideKeyboard();
		Click(ID_BTN_CONNECT, "Click - Connect button");
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
		WebElement element = driver.findElement(by);
		driver.pressKeyCode(112); // DELETE Key event - https://developer.android.com/reference/android/view/KeyEvent.html#KEYCODE_FORWARD_DEL
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
		driver.findElement(By.id("next")).click();				
	}
	
	public void clickNextWaitTillFieldContains(String fieldLabel) throws TimeoutException, NoSuchElementException{
	 	
		waitCommand(By.xpath(String.format(XPATH_TXT_CONTAINS, fieldLabel)));		    
		driver.findElement(By.id("next")).click();				
	}


	public void clickNextMultiple(String times) throws TimeoutException, NoSuchElementException{

		for(int i=1;i<=Integer.parseInt(times);i++){ 	
			waitCommand(By.xpath(String.format(XPATH_TXT_CONTAINS, ":")));		    
			driver.findElement(By.id("next")).click();	
			HardDelay(3000L);
		}
	}

	public void multipleClickNext(String field,String times) throws TimeoutException, NoSuchElementException{

		for(int i=1;i<=Integer.parseInt(times);i++){ 	
			waitCommand(By.xpath(String.format(XPATH_TXT, field)));		    
			driver.findElement(By.id("next")).click();	
			HardDelay(3000L);
		}
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
		driver.findElement(By.id("previous")).click();
		takeScreenshot("Click Previous Button");

	}


	public void clickRoutineBackButton() throws TimeoutException, NoSuchElementException{

		waitCommand(CONTENT_DESC_ROUITNE_BACK_BTN);
		driver.findElement(CONTENT_DESC_ROUITNE_BACK_BTN).click();
		takeScreenshot("Click Routine back Button");

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
		
		waitCommand(By.xpath(String.format(XPATH_TXT, pickListName)));		
		
		List<WebElement> element = driver.findElementsByAndroidUIAutomator(
				"new UiSelector().className(\"android.view.View\").index(0).clickable(true)");
		
		
		int size = element.size();
		if (size > 1) {
			//((JavascriptExecutor) driver).executeScript("arguments[0].click();", element.get(size - 1));
			//element.get(size - 1).click();
			
			int x = element.get(size - 1).getLocation().getX();
			int y = element.get(size - 1).getLocation().getY();
			
			driver.tap(1, x, y, 2);
			
			
			takeScreenshot("Clicked - " + pickListName + " spyglass");
		} else {
		    //((JavascriptExecutor) driver).executeScript("arguments[0].click();", element.get(0));
			//element.get(0).click();
			
			int x = element.get(0).getLocation().getX();
			int y = element.get(0).getLocation().getY();
			
			driver.tap(1, x, y, 2);
			
			takeScreenshot("Clicked - " + pickListName + " spyglass");
		}
		HardDelay(1000L);	
		

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

		waitCommand(ID_PICKLIST_SEARCHFIELD);

		if(pickListValue.contains("#")){
			pickListValue = getRuntimeTestdata(pickListValue);
		}			

		List<WebElement> elements = driver.findElementsByXPath(".//android.widget.ListView[@resource-id='android:id/list']/android.widget.LinearLayout/android.widget.TextView[@index='0']");
		int size = elements.size();
		for(WebElement element: elements){			
			size--;
			if(element.getText().equalsIgnoreCase(pickListValue)){
				element.click();	
				takeScreenshot("Pick List Value - <b>"+pickListValue+"</b> is selected");
				break;
			}else if(size==0){
				takeScreenshot("Pick List Value - <b>"+pickListValue+"</b> is not selected");
			}
		}




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
		WebElement element = driver.findElement(by);
		driver.pressKeyCode(112); // DELETE Key event - https://developer.android.com/reference/android/view/KeyEvent.html#KEYCODE_FORWARD_DEL
		element.sendKeys(data);
		takeScreenshot(field, data);

	}


	public void enterTransferOrder(String locationName, String columnName){

		String TRANSFERCOUNT = String.format(TRANSFERCOUNT_PACK, locationName,locationName,locationName);

		String NoofTransfer = selectQuerySingleValue(TRANSFERCOUNT, "TRANSFERCOUNT");

		int count = 	Integer.parseInt(NoofTransfer);

		String data = getRuntimeTestdata(columnName);

		if (count>1){
			enterText("Enter Transfer Order (*) :", data);
			clickNext();
			clickConfirmPrompt("Generate new shipment?", "Yes");
		}
		else
		{
			clickConfirmPrompt("Generate new shipment?", "Yes");
			verifyAutopopulatefieldvalues("Transfer Order", data);
		}
	}
	public void enterShipmentNumber(String locationName, String columnName){

		String SHIPMENTCOUNT = String.format(SHIPMENTCOUNT_IR, locationName,locationName,locationName);

		String Noofshipments = selectQuerySingleValue(SHIPMENTCOUNT, "SHIPMENTCOUNT");

		int count = 	Integer.parseInt(Noofshipments);

		String data = getRuntimeTestdata(columnName);

		if (count>1){
			enterText("Enter Shipment # (*) :", data);
			clickNext();
		}
		else
		{
			verifyAutopopulatefieldvalues("Shipment #", data);
		}
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

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile,
						new File("./Results/" + HtmlReport.reportFolderName + "/" + screenshotName + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}



			test.log(LogStatus.PASS, reportName + "<b>"+data+"</b>",
					"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenshotName + ".png"));
		}else{
			test.log(LogStatus.PASS, reportName + "<b>"+data+"</b>");	
		}

	}

	public void clickConfirmPrompt(String msg , String data) throws TimeoutException, NoSuchElementException{		
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


	public void clickYesConfirmPromptContains(String msgContains) throws TimeoutException, NoSuchElementException{		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).contains(msgContains)) {
			report(driver,test, msgContains + " is displayed", LogStatus.PASS);		
			Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + msgContains);
		} else {
			report(driver,test, msgContains + " is not displayed", LogStatus.FAIL);			
		}
	}

	public void clickNoConfirmPromptContains(String msgContains) throws TimeoutException, NoSuchElementException{		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).contains(msgContains)) {
			report(driver,test, msgContains + " is displayed", LogStatus.PASS);		
			Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + msgContains);
		} else {
			report(driver,test, msgContains + " is not displayed", LogStatus.FAIL);			
		}
	}


	public void clickOkPrompt(String msg) throws TimeoutException, NoSuchElementException{

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
		driver.pressKeyCode(112);
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


	public void verifyAutopopulatefieldvalues(String field, String data)  throws TimeoutException, NoSuchElementException {

		waitCommand(By.xpath(String.format(XPATH_TXT, field)+"/following-sibling::android.view.View"));
		waitForSeconds("3");
		String fieldValue = driver.findElement(By.xpath(String.format(XPATH_TXT, field)+"/following-sibling::android.view.View")).getAttribute("name");		
		
		if(data!=null){
			if(data.contains("#")){

				data = runtimeDataProperties.getProperty(data);

			}
		}

		if (!fieldValue.equals("")){
			if (data.equalsIgnoreCase(fieldValue)) {
				test.log(LogStatus.PASS, field + "</br>Expected - <b>" + data + "</b></br>"
													   + "Actual - <b>" + fieldValue +"</b>", "");
			} else {
				test.log(LogStatus.FAIL, field + "</br>Expected - <b>" + data + "</b></br>"
													   + "Actual - <font color=red><b>" + fieldValue +"</font></b>", "");
				takeScreenshot(field + " is not populated as expected");
			}
		}
	}

	/**
	 * Function implements thread.sleep function 8
	 * 
	 * @param1 long delayTime	
	 * @return void
	 * @author Hari
	 * @since 12/27/2016
	 * 
	 */

	public static void waitForSeconds(String delaySeconds) {

		delaySeconds = delaySeconds +"000";

		try {
			Thread.sleep(Long.parseLong(delaySeconds));
		} catch (Exception e) {

		}

	}




	public void verifyRoutine(String routinename){

		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals(routinename)) {

			test.log(LogStatus.PASS, routinename + " - Routine is displayed");
		}
		else{
			test.log(LogStatus.FAIL, routinename + " - Routine is not displayed");

		}

	}



}
