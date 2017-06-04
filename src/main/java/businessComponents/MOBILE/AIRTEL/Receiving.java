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

	String confirmMsg1 = "Receive into a container?";
	String confirmMsg2 = "Close the container?";
	
	//Transaction Complete Message
	
	String transactionCompleteMsg = "There are no more items to receive for this MRR.";

	// Error Messages

	// Routine Names

	String MMR_RECEIVE = "MRR Receive";


	// Other Declarations

	private String folderName = "Receiving";
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
	
	
	public void validateTransaction(String routineName ,String loopField) {		
		if (isObjectPresent(By.xpath(String.format(XPATH_TXT, loopField)),"Loop field - "+loopField)) {
			report(routineName+" Transaction is successfull", LogStatus.PASS);			
		} else {
			report(routineName+" Transaction is not successfull", LogStatus.FAIL);			
		}
	}
	
	public void mrrReceive() throws TimeoutException, NoSuchElementException, WebDriverException {

		String location = receivingTestDataHashmap.get("LOCATION_NAME");
		String mrrNumber = receivingTestDataHashmap.get("MRR_NUMBER");
		String subInventory = receivingTestDataHashmap.get("SUB_INVENTORY");
		String receiveInContainer = receivingTestDataHashmap.get("RECEIVE_IN_CONTAINER");		
		String containerCode = receivingTestDataHashmap.get("CONTAINER_CODE");		
		String closeContainer = receivingTestDataHashmap.get("CLOSE_CONTAINER");
		String noOfLineItem = receivingTestDataHashmap.get("NO_OF_LINE_ITEM");
		String notes = receivingTestDataHashmap.get("NOTES");
		
		
		
		for(int i=1; i<= Integer.parseInt(noOfLineItem); i++){
			
		String receiveAllQty = receivingTestDataHashmap.get("RECEIVE_ALL_QUANTITY_"+i);
		String barcodeType = receivingTestDataHashmap.get("BARCODE_TYPE_"+i);
		String barcode = receivingTestDataHashmap.get("BARCODE_"+i);
		String serialNumber = receivingTestDataHashmap.get("SERIAL_NUMBER_"+i);
		String qty = receivingTestDataHashmap.get("QUANTITY_"+i);
		String packageId = receivingTestDataHashmap.get("PACKAGE_ID_"+i);
		String hardwareVersion = receivingTestDataHashmap.get("HARDWARE_VERSION_"+i);
		String condition = receivingTestDataHashmap.get("CONDITION_"+i);
			
			
		
		selectRoutine(MMR_RECEIVE);
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals(MMR_RECEIVE)) {

			EnterText(LOCATION_XPATH, "Enter Location Name (*) :", location);
			ClickNext();
			EnterText(MRR_NUMBER_XPATH, "Enter MRR Number (*) :", mrrNumber);
			ClickNext();
			EnterText(SUB_INVENTORY_XPATH, "Enter Sub-Inventory :", subInventory);
			ClickNext();

			if (validateMessage(confirmMsg1)) {
				if (receiveInContainer.equalsIgnoreCase("Yes")) {
					Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + confirmMsg1);
					EnterText(CONTAINERC0DE_XPATH, "Enter Container Code (*) :", containerCode);
					ClickNext();
				} else {
					Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + confirmMsg1);					
				}
			}
									
			switch(barcodeType){
			
			case "SERIALIZED_ITEMCODE":	
				EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
				ClickNext();
			break;
			
			case "NON_SERIALIZED_ITEMCODE":				
				EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
				ClickNext();				
				if(!receiveAllQty.equalsIgnoreCase("Yes")){
				EnterText(QTY_XPATH, "Enter Quantity (*) :", qty);
				ClickNext();
				}else{
				waitCommand(QTY_XPATH);
				ClickNext();
				}
				EnterText(PACKAGEID_XPATH, "Enter Package ID (*) :", packageId);
				ClickNext();								
				EnterText(CONDITION_XPATH, "Enter Condition (*) :", condition);
				ClickNext();				

				if(receiveInContainer.equalsIgnoreCase("Yes") && receiveAllQty.equalsIgnoreCase("Yes")){
					if(validateMessage(confirmMsg2)){
						if(closeContainer.equalsIgnoreCase("Yes")){
						Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + confirmMsg2);						
						}else{
						Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + confirmMsg2);	
						}
						ClickNext();
					}
				}else{
					ClickNext();
				}
				
				
			break;
			
			case "MFG_PART_NUMBER":				
				EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
				ClickNext();
			break;
			
			default:
			// do nothing	
			break;
			}
	
			EnterText(NOTES_XPATH, "Enter Notes :", notes);
			ClickNext();
			
		}
			
			//Verify whether Transaction is completed successfully
			
			if (receiveAllQty.equalsIgnoreCase("Yes")) {
				if (validateMessage(transactionCompleteMsg)) {
					Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
					validateTransaction("MMR_RECEIVE", "Enter MRR Number (*) :");
				}
			}else{
				 
			}
			
			
			
		}
	}
	
}
