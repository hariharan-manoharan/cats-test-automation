package main.java.businessComponents.MOBILE.AIRTEL;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;
//import main.java.utils.JMeterFromExistingJMX;


public class FunctionalComponents extends Utility {

	@SuppressWarnings("rawtypes")
	public FunctionalComponents(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable, testParameters);
	}

	public void createNewConnection() {

		LoginActivity loginActivity = new LoginActivity(test, driver, dataTable,testParameters);
		loginActivity.addConnection();

	}

	public void login() {

		LoginActivity loginActivity = new LoginActivity(test, driver, dataTable,testParameters);
		loginActivity.login();

	}

	public void selectProfile() {
		ProfilesActivity profilesActivity = new ProfilesActivity(test, driver, dataTable,testParameters);
		profilesActivity.selectProfile();
	}
	
		
	
	//SQL FUNCTIONS
	
	public void createNewPart(){
		String validateItem = "SELECT * FROM CATSCON_PART_STG WHERE ITEM='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");		
		int recordId = createNewPart(dataMap);
		validateInboundTransaction("Item", "PROCESS_FLAG", "ERROR_MESSAGE", validateItem, dataMap.get("VALUE2"),recordId);	
	}
	
	public void addManufacturer(){
		String validateMFG = "SELECT * FROM CATSCON_MFG_STG WHERE MANUFACTURER_NAME='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");		
		int recordId = addMfgForItem(dataMap);
		validateInboundTransaction("MFG", "PROCESS_FLAG", "ERROR_MESSAGE", validateMFG, dataMap.get("VALUE1"),recordId);
	}
	
	public void createPurchaseOrder(){
		String validatePO = "SELECT * FROM CATSCON_PO_STG WHERE PHA_PO_NUMBER='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");		
		int recordId = createPurchaseOrder(dataMap);
		validateInboundTransaction("PO", "PROCESS_FLAG", "ERROR_MESSAGE", validatePO, dataMap.get("VALUE2"),recordId);		
	}
	
	public void createBillOfMaterial(){
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");
		createBillOfMaterial(dataMap);
	}
	
	public void createMaterialReceiveReceipt(){
		String validateMRR = "SELECT * FROM CATSCON_MRR_STG WHERE RECEIPT_NUM='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");
		int recordId = createMaterialReceiveReceipt(dataMap);
		validateInboundTransaction("MRR", "PROCESS_FLAG", "ERROR_MESSAGE", validateMRR, dataMap.get("VALUE9"),recordId);
		poTaxUpdate(dataMap);
	}
	
	public void deliveryConfirmation(){
		String validateDC = "SELECT * FROM CATSCON_POREC_STG WHERE ITEM_CODE='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");
		int recordId = deliveryConfirmation(dataMap);
		validateInboundTransaction("Delivery Confirmation :","PROCESS_FLAG", "ERROR_MESSAGE", validateDC, dataMap.get("VALUE7"),recordId);
	}


	public void createBulkTransferRequest(){
		String validateBulkTransferRequest = "SELECT * FROM CATSCON_TRANSFERREQ_STG WHERE REFERENCENUMBER='%s' AND STAGEID=%d";	

		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Bulk_Transfer_Request");
		Transfer transfer = new Transfer(test, driver, dataTable,testParameters);
		int stageId = transfer.createBulkTransferRequest(dataMap);
		boolean successFlag = validateInboundTransaction("Bulk Transfer Request", "PROCESSED", "ERRORMESSAGE", validateBulkTransferRequest, dataMap.get("REFERENCENUMBER"),stageId);	
		
		if(successFlag){
			String RequestNumber = selectQuerySingleValue(String.format(validateBulkTransferRequest, dataMap.get("REFERENCENUMBER"), stageId), "GENERATEDREQNUM");		
			String query = "SELECT * FROM CATS_TRANSFER WHERE REFERENCENUMBER ="+"'"+RequestNumber+"'";
			String TRANSFERNO = selectQuerySingleValue(query, "TRANSFERNUMBER");
			addRuntimeTestData("REQUESTNUMBER", RequestNumber);
			addRuntimeTestData("TRANSFERNUMBER", TRANSFERNO);
		}
		
	}

	//Pick Routines
	
	public void pick() throws WebDriverException, SQLException {
		Dispatch dispatch = new Dispatch(test, driver, dataTable,testParameters);
		dispatch.pick();
    }
	
	//Pick Routines
	
	public void pack() throws TimeoutException , NoSuchElementException{
		Dispatch dispatch = new Dispatch(test, driver, dataTable,testParameters);
		dispatch.pack();
	}
	
	//Ship Routines
	public void ship() throws TimeoutException , NoSuchElementException{
		Dispatch dispatch = new Dispatch(test, driver, dataTable,testParameters);
		dispatch.ship(); 

	}
	
	public void itemInquiry() throws TimeoutException, NoSuchElementException{
		
		Inquiry Inquiry = new Inquiry(test, driver , dataTable,testParameters);
		Inquiry.itemInquiry();
		
	}
	public void assetInquiry() throws TimeoutException, NoSuchElementException{
		
		Inquiry Inquiry = new Inquiry(test, driver , dataTable,testParameters);
		Inquiry.assetInquiry();
		
	}
	
	//Performance Testing

/*	public void performanceTest() throws Exception{
		
		JMeterFromExistingJMX JMeterFromExistingJMX = new JMeterFromExistingJMX();
		JMeterFromExistingJMX.run();
		
	}*/

}