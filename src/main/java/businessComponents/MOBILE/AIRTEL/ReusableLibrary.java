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
		waitCommand(By.xpath(String.format(XPATH_TXT_CONTAINS, "Enter")));	
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
		
		waitCommand(ID_PICKLIST_SEARCHFIELD);
		
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
		
			
			takeScreenshot("Pick List Value - <b>"+pickListValue+"</b> is selected");
		
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
		
		
		
		test.log(LogStatus.PASS, reportName + "<b>"+data+"</b>",
				"<b>Screenshot: <b>" + test.addScreenCapture("./" + screenshotName + ".png"));
		}else{
			test.log(LogStatus.PASS, reportName + "<b>"+data+"</b>");	
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
		
		delaySeconds = delaySeconds +"000";
		
		try {
			Thread.sleep(Long.parseLong(delaySeconds));
		} catch (Exception e) {

		}

	}
	
	
	// Routine specific validations
	
/*	public void parentReceivedCount(){
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
	}*/
	
	public void deliveryConfirmation(){
		String validateDC = "SELECT * FROM CATSCON_POREC_STG WHERE ITEM_CODE='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging",testParameters.getCurrentTestCase()+"_DC");
		int recordId = deliveryConfirmationQuery(dataMap);
		validateInboundTransaction("Delivery Confirmation :","PROCESS_FLAG", "ERROR_MESSAGE", validateDC, dataMap.get("VALUE7"),recordId);
	}
	
	
	public void createPurchaseOrder(){
		String validatePO = "SELECT * FROM CATSCON_PO_STG WHERE PHA_PO_NUMBER='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging",testParameters.getCurrentTestCase()+"_PO");		
		int recordId = createPurchaseOrderQuery(dataMap);
		validateInboundTransaction("PO", "PROCESS_FLAG", "ERROR_MESSAGE", validatePO, getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER"),recordId);		
	}
	
	
	public void createMaterialReceiveReceipt(){
		String validateMRR = "SELECT * FROM CATSCON_MRR_STG WHERE RECEIPT_NUM='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging",testParameters.getCurrentTestCase()+"_MRR");
		int recordId = createMaterialReceiveReceiptQuery(dataMap);
		validateInboundTransaction("MRR", "PROCESS_FLAG", "ERROR_MESSAGE", validateMRR, getRuntimeTestdata(testParameters.getCurrentTestCase()+"#MRRNUMBER"),recordId);
		poTaxUpdateQuery(dataMap);
	}
	
	
	public int createPurchaseOrderQuery(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		int RECORD_ID = 0;
		CallableStatement stproc_stmt;  
		try {
			
			RECORD_ID = generateRandomNum(10000000);
			
			String purchaseOrder = generateTestData("PONUMBER", inputValueMap.get("VALUE2"));
			
						
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
					    +"'"+purchaseOrder+"',"
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
	
	public int createMaterialReceiveReceiptQuery(LinkedHashMap<String, String> inputValueMap){
		String query = null;
		int RECORD_ID = 0;
		CallableStatement stproc_stmt; 
		try {
			
			RECORD_ID = generateRandomNum(10000000);
			
			String mrrNumber = generateTestData("MRRNUMBER", inputValueMap.get("VALUE9"));
			
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
						    +"'"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"',"
						    + Integer.parseInt(inputValueMap.get("VALUE7"))+","
						    + Integer.parseInt(inputValueMap.get("VALUE8"))+","
						    +"'"+mrrNumber+"',"
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
	

	
	@SuppressWarnings("resource")
	 public int deliveryConfirmationQuery(LinkedHashMap<String, String> inputValueMap) {

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
						+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#MRRNUMBER")+"',"
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
						+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#MRRNUMBER")+"',"
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
	
	
	public void poTaxUpdateQuery(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		try {			
			query = "UPDATE CATSCUST_MRR "
					+"SET TAX_UPDATE = 'Y'"
					+"WHERE MRRID IN (SELECT MRRID FROM CATSCUST_MRR WHERE POCODE='"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"')";
			executeUpdateQuery(query, "Tax Update for PO - <b>"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"</b>");
			connection.commit();			
			
		} catch (SQLException e) {	
			test.log(LogStatus.FAIL, "Tax Update for PO - <b>"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"</b>");
			e.printStackTrace();			
		}
	}
}
