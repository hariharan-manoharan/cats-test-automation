package main.java.businessComponents.MOBILE.AIRTEL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class Receiving extends Utility implements RoutineObjectRepository {
	
	
	// Xpath
	
	By LOCATION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter To Location (*) :"));
	By MRR_NUMBER_XPATH = By.xpath(String.format(XPATH_TXT, "Enter MRR Number (*) :"));
	By SUB_INVENTORY_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Sub-Inventory :"));
	By CONTAINERC0DE_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Container Code (*) :"));
	By ITEMCODE_MFGPARTNUMBER_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Item Code or Mfg. Part # (*) :"));
	By MFGPARTNUMBER_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Mfg. Part # :"));	
	By MFG_SERIALNUM_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Mfg. Serial Number (*) :"));
	By PACKAGEID_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Package ID (*) :"));	
	By HARDWARE_VERSION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Hardware Version :"));
	By QTY_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Quantity (*) :"));
	By CONDITION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Condition (*) :"));
	By NOTES_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Notes :"));
	
	@SuppressWarnings("serial")
	//List of Fields to be displayed
	
	ArrayList<String> listOfFields = new ArrayList<String>(){{
		add("To Location");
		add("MRR Number");
		add("PO Code");
		add("INV Org Code");
		add("Sub-Inventory");
		add("Item Code or Mfg. Part #");
		add("Item Code");
		add("Item Description");
		add("Manufacturer");
		add("Mfg. Part #");
		add("Line Number");
		add("Item Type");
		add("Serialized");
		add("Parent Received Count");
		add("Mfg. Serial Number");
		add("Child Manufacturer");
		add("Child Item Description");
	    add("Quantity");
		add("Package ID");
		add("Hardware Version");
		add("Status");	
		add("Condition");
		add("Attach Photos");
		add("Enter Notes :");
	}};	
	
	// Confirm Messages

	String confirmMsg = "Receive into a container?";

	// Error Messages

	// Routine Names

	String MMR_RECEIVE = "MRR Receive";


	// Other Declarations

	private String folderName = "Container";
	private HashMap<String, String> receivingTestDataHashmap = new HashMap<String, String>();

	@SuppressWarnings("rawtypes")
	public Receiving(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
		super(test, driver, dataTable);
		getTestData();
		selectRoutineFolder(folderName);
	}
	
	public void getTestData(){
		receivingTestDataHashmap = dataTable.getRowData("Receiving");
	}

	public void selectRoutine(String routineName) {
		//ScrolltoText(routineName);
		Click(By.name(routineName), "Click - Routine - " + routineName + " is selected");
	}
	
	public void selectRoutineFolder(String folderName) {		
		Click(By.name(folderName), "Click - Routines Folder - " + folderName + " is selected");
	}
	

	public boolean validateMessage(String msg) {		
		if (GetText(ID_MESSAGE, GetText(ID_ALERT_TITLE, "Alert Title")).equalsIgnoreCase(msg)) {
			report(msg + " is displayed", LogStatus.PASS);	
			return true;
		} else {
			report(msg + " is not displayed", LogStatus.FAIL);	
			return false;
		}
	}
	
	public void mrrReceive() throws TimeoutException, NoSuchElementException, WebDriverException {

		String location = receivingTestDataHashmap.get("LOCATION_NAME");
		String mrrNumber = receivingTestDataHashmap.get("MRR_NUMBER");
		String subInventory = receivingTestDataHashmap.get("MRR_NUMBER");
		String receiveInContainer = receivingTestDataHashmap.get("RECEIVE_IN_CONTAINER");
		String barcodeType = receivingTestDataHashmap.get("BARCODE_TYPE");
		String barcode = receivingTestDataHashmap.get("BARCODE");
		String qty = receivingTestDataHashmap.get("QUANTITY");
		String packageId = receivingTestDataHashmap.get("PACKAGE_ID");
		String condition = receivingTestDataHashmap.get("CONDITION");
						

		selectRoutine(MMR_RECEIVE);
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals(MMR_RECEIVE)) {

			EnterText(LOCATION_XPATH, "Enter Location Name (*) :", location);
			ClickNext();
			EnterText(MRR_NUMBER_XPATH, "Enter MRR Number (*) :", mrrNumber);
			ClickNext();
			EnterText(SUB_INVENTORY_XPATH, "Enter Sub-Inventory :", subInventory);
			ClickNext();

			if (validateMessage(confirmMsg)) {
				if (receiveInContainer.equalsIgnoreCase("Yes")) {
					Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + confirmMsg);
				} else {
					Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + confirmMsg);
				}
			}
			
			EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
			ClickNext();
			
			switch(barcodeType){
			
			case "SERIALIZED_ITEMCODE":	
				
			break;
			
			case "NON_SERIALIZED_ITEMCODE":				
				
			break;
			
			case "MFG_PART_NUMBER":				
				
			break;
			
			default:
			// do nothing	
			break;
			}
	
			
			//Verify whether Transaction is completed successfully

			
			
		}
	}
	
}
