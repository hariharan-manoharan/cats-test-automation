package main.java.businessComponents.MOBILE.AIRTEL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class Receiving extends Utility implements RoutineObjectRepository {
	
	
	// MRR
	
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
	By PARENT_RECEIVED_COUNT = By.xpath(String.format(XPATH_TXT_CONTAINS, "/"));
	
	//MRR SITE RECEIVE
	
		final By MRR_SITE_RECEIVE_SITEID_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Site ID :"));
		final By MRR_SITE_RECEIVE_PACKAGEID_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Package ID :"));
	
	// Internal Receipt
	By TO_LOCATION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Location :"));
	By SHIPMENTNO_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Shipment # (*) :"));
	By RECEIVING_LOCATION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Receiving Location (*) :"));
	By BARCODE_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Barcode (*) :"));
	By TOSTATUS_XPATH = By.xpath(String.format(XPATH_TXT, "Enter To Status :"));
	
	
	String FROMLOCATION_XPATH=String.format(XPATH_TXT,"From Location");
	String ITEMCODE_XPATH=String.format(XPATH_TXT,"Item Code");
	String ITEMDESCRIPTION_XPATH=String.format(XPATH_TXT,"Item Description");
	String PACKAGETAG_XPATH=String.format(XPATH_TXT,"Package Tag");
	String ASSETCODE_XPATH=String.format(XPATH_TXT,"Asset Code (UIN)");
	String SERIALNUMBER_XPATH=String.format(XPATH_TXT,"Serial Number");
	String MFGPARTNO_XPATH=String.format(XPATH_TXT,"Mfg Part Number");
	
	@SuppressWarnings("serial")
	//List of Fields to be displayed
	
	ArrayList<String> mrrReceiveAutoPopulatingFields = new ArrayList<String>(){{
		add("PO Code");
		add("INV Org Code");
		add("Item Code");
		add("Item Description");
		add("Manufacturer");
		add("Mfg. Part #");
		add("Line Number");
		add("Item Type");
		add("Serialized");
		add("Parent Received Count");	
	}};	
	
	// Confirm Messages

	String confirmMsg1 = "Receive into a container?";
	String confirmMsg2 = "Close the container?";
	
	//Transaction Complete Message
	
	String transactionCompleteMsg = "There are no more items to receive for this MRR.";

	// Error Messages

	// Routine Names

	String MMR_RECEIVE = "MRR Receive";
	String MMR_SITE_RECEIVE = "MRR Site Receive";

	// Other Declarations

	private String folderName = "Receiving";
	private HashMap<String, String> receivingTestDataHashmap = new HashMap<String, String>();

	@SuppressWarnings("rawtypes")
	public Receiving(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable,testParameters);
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
			report(driver,test, msg + " is displayed", LogStatus.PASS);	
			return true;
		} else {
			report(driver,test, msg + " is not displayed", LogStatus.FAIL);	
			return false;
		}
	}
	
	
	public void validateTransaction(String routineName ,String loopField) {		
		if (isElementPresent(By.xpath(String.format(XPATH_TXT, loopField)),"Loop field - "+loopField)) {
			report(driver,test, routineName+" Transaction is successfull", LogStatus.PASS);			
		} else {
			report(driver,test, routineName+" Transaction is not successfull", LogStatus.FAIL);			
		}
	}
	
	public void mrrReceive() throws TimeoutException, NoSuchElementException, WebDriverException {
		
		// Get all header level data
		String location = receivingTestDataHashmap.get("LOCATION_NAME");
		String mrrNumber = receivingTestDataHashmap.get("MRR_NUMBER");
		String subInventory = receivingTestDataHashmap.get("SUB_INVENTORY");
		String receiveInContainer = receivingTestDataHashmap.get("RECEIVE_IN_CONTAINER");		
		String containerCode = receivingTestDataHashmap.get("CONTAINER_CODE");		
		String closeContainer = receivingTestDataHashmap.get("CLOSE_CONTAINER");
		String noOfLineItem = receivingTestDataHashmap.get("NO_OF_LINE_ITEM");
		String notes = receivingTestDataHashmap.get("NOTES");
		int mfgPartNumberIndex = 0;
		
		
		// Looping based on the number of line item to be received
		for(int i=1; i<= Integer.parseInt(noOfLineItem); i++){
			
		String receiveAllQty = receivingTestDataHashmap.get("RECEIVE_ALL_QUANTITY_"+i);
		String barcodeType = receivingTestDataHashmap.get("BARCODE_TYPE_"+i);
		String isAssembly = receivingTestDataHashmap.get("IS_ASSEMBLY_"+i);
		String barcode = receivingTestDataHashmap.get("BARCODE_"+i);
		String serialNumber = receivingTestDataHashmap.get("SERIAL_NUMBER_"+i);
		String qty = receivingTestDataHashmap.get("QUANTITY_"+i);
		String packageId = receivingTestDataHashmap.get("PACKAGE_ID_"+i);
		String hardwareVersion = receivingTestDataHashmap.get("HARDWARE_VERSION_"+i);
		String condition = receivingTestDataHashmap.get("CONDITION_"+i);
		
			
			
		
		selectRoutine(MMR_RECEIVE);
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals(MMR_RECEIVE)) {
			// Entering header level details
			EnterText(LOCATION_XPATH, "Enter Location Name (*) :", location);
			ClickNext();
			EnterText(MRR_NUMBER_XPATH, "Enter MRR Number (*) :", mrrNumber);
			ClickNext();
			EnterText(SUB_INVENTORY_XPATH, "Enter Sub-Inventory :", subInventory);
			ClickNext();
			// Checking if user needs to receive items in a Container or not
			if (validateMessage(confirmMsg1)) {
				if (receiveInContainer.equalsIgnoreCase("Yes")) {
					mfgPartNumberIndex = 28;
					Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - " + confirmMsg1);
					EnterText(CONTAINERC0DE_XPATH, "Enter Container Code (*) :", containerCode+generateRandomNum(10000));
					ClickNext();
				} else {
					mfgPartNumberIndex = 25;
					Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - " + confirmMsg1);					
				}
			}
			
			// Check the barcode type and perform actions accordingly
			switch(barcodeType){
			
			case "SERIALIZED_ITEMCODE":	
				int receivedCount = 0;
				int totalCount = 0;	
				String parentReceiveCount[] = new String[2];
				
				EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
				ClickNext();
				if(isElementPresent(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :")){
					ClickSpyGlass("Enter Mfg. Part Number :",mfgPartNumberIndex);
					String mfgPartNumber = GetPickListValue(1);
					addRuntimeTestData("RECEIVING", "MFG_PART_NUMBER", mfgPartNumber);
					EnterText(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :", mfgPartNumber);
					ClickNext();					
				}
				
				waitCommand(PARENT_RECEIVED_COUNT);
				// Checks Parent Received Count 
				if(isElementPresent(PARENT_RECEIVED_COUNT, "Parent Received Count")){					
					parentReceiveCount = GetAttributeValue(PARENT_RECEIVED_COUNT, "name", "Parent Received Count").split("/");					
					receivedCount = Integer.parseInt(parentReceiveCount[0]);
					totalCount = Integer.parseInt(parentReceiveCount[1]);				
				
				// Checks if users wants to receive all the quantity 
					// If Yes - Loops till it receives remaining count
					// If No - Receives one asset
				if(receiveAllQty.equalsIgnoreCase("Yes")){	
				
				for(int j=1; j<=(totalCount-receivedCount);j++){
				
				// Checks received count on each iteration to ensure when to perform certain actions like 
					//Close Container Prompt, Attachment field and Transaction complete message	
				parentReceiveCount = GetAttributeValue(PARENT_RECEIVED_COUNT, "name", "Parent Received Count").split("/");					
				receivedCount = Integer.parseInt(parentReceiveCount[0]);	
				
				// Checks if the item code type is Assembly 
				//If Yes - Skips Serial #
				//If No - Enters Serial #
				if(!isAssembly.equalsIgnoreCase("Yes")){					
				EnterText(MFG_SERIALNUM_XPATH, "Enter Mfg. Serial Number (*) :", (serialNumber = (serialNumber.contains("#")) ?  getRuntimeTestdata("RECEIVING",serialNumber) : generateTestData("RECEIVING", "SERIAL_NUMBER_"+j, serialNumber)));
				ClickNext();
				}
				EnterText(PACKAGEID_XPATH, "Enter Package ID (*) :", (packageId = (packageId.contains("#")) ?  getRuntimeTestdata("RECEIVING",packageId) : generateTestData("RECEIVING", "PACKAGE_ID_"+j, packageId)));
				ClickNext();
				EnterText(HARDWARE_VERSION_XPATH, "Enter Hardware Version :", hardwareVersion);
				ClickNext();
				EnterText(CONDITION_XPATH, "Enter Condition (*) :", condition);
				ClickNext();				
				
				// Checks if user needs to click 'Yes' for the Close container Prompt
				if(receivedCount == (totalCount-1)){
					if(receiveInContainer.equalsIgnoreCase("Yes")){
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
				}
				
				// Checks list of fields to be auto-populated is correct
				validateAutoPopulatedFields(mrrReceiveAutoPopulatingFields);
				EnterText(NOTES_XPATH, "Enter Notes :", notes);
				ClickNext();
				
				// Checks when to click 'Ok' for the transaction complete message
				if(receivedCount == (totalCount-1)){					
					if (validateMessage(transactionCompleteMsg)) {
						Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
						validateTransaction(MMR_RECEIVE, "Enter MRR Number (*) :");
						deliveryConfirmation();
					}
				}else{
					validateTransaction(MMR_RECEIVE, "Enter Mfg. Serial Number (*) :");	
					deliveryConfirmation();
				}
				}				
				
				}else{					
					
					if(!isAssembly.equalsIgnoreCase("Yes")){
						waitCommand(MFG_SERIALNUM_XPATH);
						EnterText(MFG_SERIALNUM_XPATH, "Enter Mfg. Serial Number (*) :", (serialNumber = (serialNumber.contains("#")) ?  getRuntimeTestdata("RECEIVING",serialNumber) : generateTestData("RECEIVING", "SERIAL_NUMBER_1", serialNumber)));
						ClickNext();
					}
					EnterText(PACKAGEID_XPATH, "Enter Package ID (*) :", (packageId = (packageId.contains("#")) ?  getRuntimeTestdata("RECEIVING",packageId) : generateTestData("RECEIVING", "PACKAGE_ID_1", packageId)));
					ClickNext();
					EnterText(HARDWARE_VERSION_XPATH, "Enter Hardware Version :", hardwareVersion);
					ClickNext();
					EnterText(CONDITION_XPATH, "Enter Condition (*) :", condition);
					ClickNext();
					

					if(receivedCount == (totalCount-1)){
						if(receiveInContainer.equalsIgnoreCase("Yes")){
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
					}
					
					
					validateAutoPopulatedFields(mrrReceiveAutoPopulatingFields);
					EnterText(NOTES_XPATH, "Enter Notes :", notes);
					ClickNext();
					
					if(receivedCount == (totalCount-1)){
						if (validateMessage(transactionCompleteMsg)) {
							Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
							validateTransaction(MMR_RECEIVE, "Enter MRR Number (*) :");
							deliveryConfirmation();
						}
					}else{
						validateTransaction(MMR_RECEIVE, "Enter Mfg. Serial Number (*) :");	
						deliveryConfirmation();
					}
				}
				}
				
			break;
			
			case "NON_SERIALIZED_ITEMCODE":				
				EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
				ClickNext();
				if(isElementPresent(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :")){
					ClickSpyGlass("Enter Mfg. Part Number :",25);
					String mfgPartNumber = GetPickListValue(1);
					addRuntimeTestData("RECEIVING", "MFG_PART_NUMBER", mfgPartNumber);
					EnterText(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :", mfgPartNumber);
					ClickNext();			
				}
				if(!receiveAllQty.equalsIgnoreCase("Yes")){
				EnterText(QTY_XPATH, "Enter Quantity (*) :", qty);
				ClickNext();
				}else{
				waitCommand(QTY_XPATH);
				ClickNext();
				}
				EnterText(PACKAGEID_XPATH, "Enter Package ID (*) :", (packageId = (packageId.contains("#")) ?  getRuntimeTestdata("RECEIVING",packageId) : generateTestData("RECEIVING", "PACKAGE_ID", packageId)));
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
				
				validateAutoPopulatedFields(mrrReceiveAutoPopulatingFields);
				
				EnterText(NOTES_XPATH, "Enter Notes :", notes);
				ClickNext();
				
				if (receiveAllQty.equalsIgnoreCase("Yes")) {
					if (validateMessage(transactionCompleteMsg)) {
						Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
						validateTransaction(MMR_RECEIVE, "Enter MRR Number (*) :");
						deliveryConfirmation();
					}
				}else{
					validateTransaction(MMR_RECEIVE, "Enter Quantity (*) :");
					deliveryConfirmation();
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
	}
					
		}	
	
	
	}
	
	
	
	public void mrrSiteReceive() throws TimeoutException, NoSuchElementException, WebDriverException {

		String location = receivingTestDataHashmap.get("LOCATION_NAME");
		String mrrNumber = receivingTestDataHashmap.get("MRR_NUMBER");
		String subInventory = receivingTestDataHashmap.get("SUB_INVENTORY");
		String noOfLineItem = receivingTestDataHashmap.get("NO_OF_LINE_ITEM");
		String notes = receivingTestDataHashmap.get("NOTES");

		selectRoutine(MMR_SITE_RECEIVE);
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals(MMR_SITE_RECEIVE)) {

			// Entering header level details
			EnterText(LOCATION_XPATH, "Enter Location Name (*) :", location);
			ClickNext();
						
			if (isElementPresent(MRR_SITE_RECEIVE_SITEID_XPATH, "Enter Site ID :")) {
				ClickSpyGlass("Enter Site ID :", 4);
				String siteId = GetPickListValue(1);
				addRuntimeTestData("RECEIVING", "SITE_ID", siteId);
				EnterText(MRR_SITE_RECEIVE_SITEID_XPATH, "Enter Site ID :", siteId);
				ClickNext();
			}

			EnterText(MRR_NUMBER_XPATH, "Enter MRR Number (*) :", mrrNumber);
			ClickNext();
			EnterText(SUB_INVENTORY_XPATH, "Enter Sub-Inventory :", subInventory);
			ClickNext();

			// Looping based on the number of line item to be received
			for (int i = 1; i <= Integer.parseInt(noOfLineItem); i++) {
				
				int receivedCount = 0;
				int totalCount = 0;
				String parentReceiveCount[] = new String[2];

				String receiveAllQty = receivingTestDataHashmap.get("RECEIVE_ALL_QUANTITY_" + i);
				String barcodeType = receivingTestDataHashmap.get("BARCODE_TYPE_" + i);
				String isAssembly = receivingTestDataHashmap.get("IS_ASSEMBLY_" + i);
				String barcode = receivingTestDataHashmap.get("BARCODE_" + i);
				String serialNumber = receivingTestDataHashmap.get("SERIAL_NUMBER_" + i);
				String qty = receivingTestDataHashmap.get("QUANTITY_" + i);
				String packageId = receivingTestDataHashmap.get("PACKAGE_ID_" + i);
				String hardwareVersion = receivingTestDataHashmap.get("HARDWARE_VERSION_" + i);
				String condition = receivingTestDataHashmap.get("CONDITION_" + i);

				// Check the barcode type and perform actions accordingly
				switch (barcodeType) {

				case "SERIALIZED_ITEMCODE":
					
					EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
					ClickNext();
					if (isElementPresent(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :")) {
						ClickSpyGlass("Enter Mfg. Part Number :", 28);
						String mfgPartNumber = GetPickListValue(1);
						addRuntimeTestData("RECEIVING", "MFG_PART_NUMBER", mfgPartNumber);
						EnterText(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :", mfgPartNumber);
						ClickNext();
					}

					waitCommand(PARENT_RECEIVED_COUNT);
					// Checks Parent Received Count
					if (isElementPresent(PARENT_RECEIVED_COUNT, "Parent Received Count")) {
						parentReceiveCount = GetAttributeValue(PARENT_RECEIVED_COUNT, "name", "Parent Received Count")
								.split("/");
						receivedCount = Integer.parseInt(parentReceiveCount[0]);
						totalCount = Integer.parseInt(parentReceiveCount[1]);

						// Checks if users wants to receive all the quantity
						// If Yes - Loops till it receives remaining count
						// If No - Receives one asset
						if (receiveAllQty.equalsIgnoreCase("Yes")) {

							for (int j = 1; j <= (totalCount - receivedCount); j++) {

								// Checks received count on each iteration to
								// ensure when to perform certain actions like
								// Close Container Prompt, Attachment field and
								// Transaction complete message
								parentReceiveCount = GetAttributeValue(PARENT_RECEIVED_COUNT, "name",
										"Parent Received Count").split("/");
								receivedCount = Integer.parseInt(parentReceiveCount[0]);

								// Checks if the item code type is Assembly
								// If Yes - Skips Serial #
								// If No - Enters Serial #
								if (!isAssembly.equalsIgnoreCase("Yes")) {
									EnterText(MFG_SERIALNUM_XPATH, "Enter Mfg. Serial Number (*) :",
											(serialNumber = (serialNumber.contains("#"))
													? getRuntimeTestdata("RECEIVING", serialNumber)
													: generateTestData("RECEIVING", "SERIAL_NUMBER_" + j,
															serialNumber)));
									ClickNext();
								}
								EnterText(MRR_SITE_RECEIVE_PACKAGEID_XPATH, "Enter Package ID (*) :",
										(packageId = (packageId.contains("#"))
												? getRuntimeTestdata("RECEIVING", packageId)
												: generateTestData("RECEIVING", "PACKAGE_ID_" + j, packageId)));
								ClickNext();
								EnterText(HARDWARE_VERSION_XPATH, "Enter Hardware Version :", hardwareVersion);
								ClickNext();
								EnterText(CONDITION_XPATH, "Enter Condition (*) :", condition);
								ClickNext();
								
								// Click next from attachment field - Displayed only when the MRR is about to fully received
								if (receivedCount == (totalCount - 1)) {
									ClickNext();	
								}

								// Checks list of fields to be auto-populated is
								// correct
								validateAutoPopulatedFields(mrrReceiveAutoPopulatingFields);
								EnterText(NOTES_XPATH, "Enter Notes :", notes);
								ClickNext();

								// Checks when to click 'Ok' for the transaction
								// complete message
								if (receivedCount == (totalCount - 1)) {
									if (validateMessage(transactionCompleteMsg)) {
										Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
										validateTransaction(MMR_RECEIVE, "Enter MRR Number (*) :");
										deliveryConfirmation();
									}
								} else {
									validateTransaction(MMR_RECEIVE, "Enter Mfg. Serial Number (*) :");
									deliveryConfirmation();
								}
							}

						} else {

							if (!isAssembly.equalsIgnoreCase("Yes")) {
								waitCommand(MFG_SERIALNUM_XPATH);
								EnterText(MFG_SERIALNUM_XPATH, "Enter Mfg. Serial Number (*) :",
										(serialNumber = (serialNumber.contains("#"))
												? getRuntimeTestdata("RECEIVING", serialNumber)
												: generateTestData("RECEIVING", "SERIAL_NUMBER_1", serialNumber)));
								ClickNext();
							}
							EnterText(MRR_SITE_RECEIVE_PACKAGEID_XPATH, "Enter Package ID (*) :",
									(packageId = (packageId.contains("#"))
											? getRuntimeTestdata("RECEIVING", packageId)
											: generateTestData("RECEIVING", "PACKAGE_ID_1", packageId)));
							ClickNext();
							EnterText(HARDWARE_VERSION_XPATH, "Enter Hardware Version :", hardwareVersion);
							ClickNext();
							EnterText(CONDITION_XPATH, "Enter Condition (*) :", condition);
							ClickNext();
							
							// Click next from attachment field - Displayed only when the MRR is about to fully received
							if (receivedCount == (totalCount - 1)) {
								ClickNext();	
							}

							validateAutoPopulatedFields(mrrReceiveAutoPopulatingFields);
							EnterText(NOTES_XPATH, "Enter Notes :", notes);
							ClickNext();

							if (receivedCount == (totalCount - 1)) {
								if (validateMessage(transactionCompleteMsg)) {
									Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
									validateTransaction(MMR_RECEIVE, "Enter MRR Number (*) :");
									deliveryConfirmation();
								}
							} else {
								validateTransaction(MMR_RECEIVE, "Enter Mfg. Serial Number (*) :");
								deliveryConfirmation();
							}
						}
					}
					
					break;
					
				case "NON_SERIALIZED_ITEMCODE":
					EnterText(ITEMCODE_MFGPARTNUMBER_XPATH, "Enter Item Code or Mfg. Part # (*) :", barcode);
					ClickNext();
					if (isElementPresent(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :")) {
						ClickSpyGlass("Enter Mfg. Part Number :", 28);
						String mfgPartNumber = GetPickListValue(1);
						addRuntimeTestData("RECEIVING", "MFG_PART_NUMBER", mfgPartNumber);
						EnterText(MFGPARTNUMBER_XPATH, "Enter Mfg. Part # :", mfgPartNumber);
						ClickNext();
					}
					
					
					waitCommand(PARENT_RECEIVED_COUNT);
					// Checks Parent Received Count
					if (isElementPresent(PARENT_RECEIVED_COUNT, "Parent Received Count")) {
						parentReceiveCount = GetAttributeValue(PARENT_RECEIVED_COUNT, "name", "Parent Received Count")
								.split("/");
						receivedCount = Integer.parseInt(parentReceiveCount[0]);
						totalCount = Integer.parseInt(parentReceiveCount[1]);
					}
					
					if ((!receiveAllQty.equalsIgnoreCase("Yes")) && (totalCount-receivedCount) == Integer.parseInt(qty)) {
						EnterText(QTY_XPATH, "Enter Quantity (*) :", qty);
						ClickNext();
						}else{
						waitCommand(QTY_XPATH);
						ClickNext();
						}	
					EnterText(MRR_SITE_RECEIVE_PACKAGEID_XPATH, "Enter Package ID (*) :",
							(packageId = (packageId.contains("#"))
									? getRuntimeTestdata("RECEIVING", packageId)
									: generateTestData("RECEIVING", "PACKAGE_ID_1", packageId)));	
					
					ClickNext();
					EnterText(CONDITION_XPATH, "Enter Condition (*) :", condition);
					ClickNext();
					
					// Click next from attachment field - Displayed only when the MRR is about to fully received
						
					if ((!receiveAllQty.equalsIgnoreCase("Yes")) && (totalCount-receivedCount) == Integer.parseInt(qty)) {
						ClickNext();
					}
					
					validateAutoPopulatedFields(mrrReceiveAutoPopulatingFields);
					
					EnterText(NOTES_XPATH, "Enter Notes :", notes);
					ClickNext();
					
					if (receiveAllQty.equalsIgnoreCase("Yes")) {
						if (validateMessage(transactionCompleteMsg)) {
							Click(ID_MESSAGE_OK, "Clicked 'Ok' for Transaction complete message");
							validateTransaction(MMR_RECEIVE, "Enter MRR Number (*) :");
							deliveryConfirmation();
						}
					}else{
						validateTransaction(MMR_RECEIVE, "Enter Quantity (*) :");
						deliveryConfirmation();
					}
					
					
				break;

				}

			}

		}else{
			test.log(LogStatus.FAIL, "Routine name - "+MMR_SITE_RECEIVE+" is not selected");
		}
	}
		
	private void deliveryConfirmation(){
		String validateDC = "SELECT * FROM CATSCON_POREC_STG WHERE ITEM_CODE='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging");
		int recordId = deliveryConfirmation(dataMap);
		validateInboundTransaction("Delivery Confirmation :","PROCESS_FLAG", "ERROR_MESSAGE", validateDC, dataMap.get("VALUE7"),recordId);
	}

	private void validateAutoPopulatedFields(ArrayList<String> autoPopulatingFields) {
		
		int numberOfFields = autoPopulatingFields.size();
		
		test.log(LogStatus.INFO, "<b>Start: Validating Auto-Populated fields...</b>");
		
		for(int i=0; i<numberOfFields; i++){
			
			isAutoPopulateFieldPresent(String.format(XPATH_TXT,autoPopulatingFields.get(i)), autoPopulatingFields.get(i));
			
		}
		
		test.log(LogStatus.INFO, "<b>End: Validating Auto-Populated fields.</b>");
	}
	
	
	public void internalreceipt(){
		
		
		String Itemtype = receivingTestDataHashmap.get("BARCODE_TYPE_1");
		String ToLocation = receivingTestDataHashmap.get("IR_TO_LOCATION");
		String NewShipment = receivingTestDataHashmap.get("NEW_SHIPMENT");
		String FromLocation = receivingTestDataHashmap.get("IR_FROM_LOCATION");
		String ReceivingLocation = receivingTestDataHashmap.get("IR_RECEIVING_LOCATION");		
		String Barcode = receivingTestDataHashmap.get("IR_BARCODE");		
		String Itemcode =receivingTestDataHashmap.get("IR_ITEMCODE");
		String ItemDescription = receivingTestDataHashmap.get("IR_ITEMDESCRIPTION");
		String Quantity = receivingTestDataHashmap.get("IR_QUANTITY");		
		String Condition = receivingTestDataHashmap.get("IR_CONDITION");		
		String ToStatus = receivingTestDataHashmap.get("IR_TOSTATUS");
		String Assetcode = properties.getProperty("ASSETCODE");
		String SerialNO = properties.getProperty("SERIALNUMBER");		
		String MFGpartno = receivingTestDataHashmap.get("IR_MFGPARTNO");
		String packagetag =properties.getProperty("PACKAGETAG");
		String Shipmentno = null;
		
		
		selectRoutine("Internal Warehouse Receive");
			
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals("Internal Warehouse Receive")) {
			
			EnterText(TO_LOCATION_XPATH, "Enter Location :", ToLocation);
			ClickNext();
			if(NewShipment.equalsIgnoreCase("Yes")){
			Shipmentno = receivingTestDataHashmap.get("IR_SHIPMENT_NO");
			EnterText(SHIPMENTNO_XPATH, "Enter Shipment # (*) :", Shipmentno);
			ClickNext();
			}
			else{
				
			Shipmentno =properties.getProperty("SHIPMENTNO");
					
			EnterText(SHIPMENTNO_XPATH, "Enter Shipment # (*) :", Shipmentno);
			ClickNext();	
			}
			
			
			VerfiyAutopopulatefieldvalues(FROMLOCATION_XPATH,"From Location",FromLocation);
			
			EnterText(RECEIVING_LOCATION_XPATH, "Enter Receiving Location (*) :", ReceivingLocation);
			ClickNext();
			
			EnterText(BARCODE_XPATH, "Enter Barcode (*) :", Barcode);
			ClickNext();
			
			
			
			VerfiyAutopopulatefieldvalues(ITEMCODE_XPATH,"Item Code",Itemcode);
			//VerfiyAutopopulatefieldvalues(ITEMDESCRIPTION_XPATH,"Item Description",ItemDescription);	
			

			
			if(Itemtype.equalsIgnoreCase("NON_SERIALIZED")){
				
				VerfiyAutopopulatefieldvalues(MFGPARTNO_XPATH,"Mfg Part Number",MFGpartno);	
				
				EnterText(QTY_XPATH, "Enter Quantity (*) :", Quantity);
				ClickNext();
				
			}
			if(Itemtype.equalsIgnoreCase("SERIALIZED")){
				
				VerfiyAutopopulatefieldvalues(PACKAGETAG_XPATH,"Package Tag",packagetag);	
				VerfiyAutopopulatefieldvalues(ASSETCODE_XPATH,"Asset Code (UIN)",Assetcode);	
				VerfiyAutopopulatefieldvalues(SERIALNUMBER_XPATH,"Serial Number",SerialNO);	
				VerfiyAutopopulatefieldvalues(MFGPARTNO_XPATH,"Mfg Part Number",MFGpartno);	
			}
			
			EnterText(CONDITION_XPATH, "Enter Condition (*) :", Condition);
			ClickNext();
			
			EnterText(TOSTATUS_XPATH, "Enter To Status :", ToStatus);
			ClickNext();			
			ClickNext();
			
			EnterText(NOTES_XPATH, "Enter Notes :", "Automation:Internal Receipt Routine");
			ClickNext();
			
			String Alertmsg1 = "Shipment "+ Shipmentno+" has been fully received.";
			verifyMessage(Alertmsg1);
			Click(ID_MESSAGE_OK, "Clicked 'OK' for prompt - "+Alertmsg1);
			verifyLoopingField("");
			
		}
		
		
			
		
	}
	
	
}
