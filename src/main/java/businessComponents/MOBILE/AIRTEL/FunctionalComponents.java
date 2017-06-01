package main.java.businessComponents.MOBILE.AIRTEL;

import java.util.LinkedHashMap;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.relevantcodes.extentreports.ExtentTest;
import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;
import main.java.utils.SqlQueries;

public class FunctionalComponents extends Utility {

	@SuppressWarnings("rawtypes")
	public FunctionalComponents(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
		super(test, driver, dataTable);
	}

	public void createNewConnection() {

		LoginActivity loginActivity = new LoginActivity(test, driver, dataTable);
		loginActivity.addConnection();

	}

	public void login() {

		LoginActivity loginActivity = new LoginActivity(test, driver, dataTable);
		loginActivity.login();

	}

	public void selectUserProfile() {
		ProfilesActivity profilesActivity = new ProfilesActivity(test, driver, dataTable);
		profilesActivity.selectProfile();
	}
	
	
	public void openContainer() throws TimeoutException, NoSuchElementException {
		Container Container = new Container(test, driver, dataTable);
		Container.openContainer();
	}
	
	public void closeContainer() throws TimeoutException, NoSuchElementException {
		Container Container = new Container(test, driver, dataTable);
		Container.closeContainer();
	}
	
	public void addToContainer() throws TimeoutException, NoSuchElementException {
		Container Container = new Container(test, driver, dataTable);
		Container.addToContainer();
	}
	

	
	//SQL FUNCTIONS
	
	public void createNewPart(){
		String validateItem = "SELECT * FROM CATSCON_PART_STG WHERE ITEM='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");		
		int recordId = createNewPart(dataMap);
		validateInboundTransaction("Item", validateItem, dataMap.get("VALUE2"),recordId);	
	}
	
	public void addManufacturer(){
		String validateMFG = "SELECT * FROM CATSCON_MFG_STG WHERE MANUFACTURER_NAME='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");		
		int recordId = addMfgForItem(dataMap);
		validateInboundTransaction("MFG", validateMFG, dataMap.get("VALUE1"),recordId);
	}
	
	public void createPurchaseOrder(){
		String validatePO = "SELECT * FROM CATSCON_PO_STG WHERE PHA_PO_NUMBER='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");		
		int recordId = createPurchaseOrder(dataMap);
		validateInboundTransaction("PO", validatePO, dataMap.get("VALUE2"),recordId);		
	}
	
	public void createBillOfMaterial(){
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");
		createBillOfMaterial(dataMap);
	}
	
	public void createMaterialReceiveReceipt(){
		String validateMRR = "SELECT * FROM CATSCON_MRR_STG WHERE RECEIPT_NUM='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");
		int recordId = createMaterialReceiveReceipt(dataMap);
		validateInboundTransaction("MRR", validateMRR, dataMap.get("VALUE9"),recordId);
		poTaxUpdate(dataMap);
	}


	public void itemInquiry() throws TimeoutException, NoSuchElementException{
		
		Inquiry Inquiry = new Inquiry(test, driver , dataTable);
		Inquiry.itemInquiry();
		
	}

}