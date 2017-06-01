//package main.java.businessComponents.MOBILE.AIRTEL;
//
//import java.util.LinkedHashMap;
//import java.util.NoSuchElementException;
//import java.util.concurrent.TimeoutException;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriverException;
//
//import com.relevantcodes.extentreports.ExtentTest;
//
//import io.appium.java_client.android.AndroidDriver;
//import main.java.testDataAccess.DataTable;
//import main.java.utils.Utility;
//
//public class TransferRequest extends Utility implements RoutineObjectRepository {
//	
//	String requestNumber = dataTable.getData("TransferRequest", "REQNUMBER");
//	String transferNumber  =  selectQuerySingleValue(String.format(TR_TRANSFER_NO_QUERY,requestNumber), "TRANSFERNUMBER");
//	String lineItemCount  =  selectQuerySingleValue(String.format(TR_LINEITEM_COUNT,requestNumber), "COUNT");
//	LinkedHashMap<String, String> lineItemDetails = selectQueryMultipleValues(String.format(TR_LINEITEM_DETAILS,requestNumber), "LINENUMBER#PARTID#ASSETID");
//			
//	@SuppressWarnings("rawtypes")
//	public TransferRequest(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
//		super(test, driver, dataTable);
//	}
//
//	public void selectRoutine(String routineName) {
//		ScrolltoText(routineName);
//		Click(By.name(routineName), "Click - Routine - " + routineName + " is selected");
//	}
//
//
//	public void pick() throws TimeoutException, NoSuchElementException, WebDriverException {
//
//		String fromLocation = selectQuerySingleValue(String.format(TR_FROM_LOC_QUERY, requestNumber), "NAME");
//		String lineItem = null;
//		boolean serializedItem = false;
//
//		selectRoutine("Pick");
//		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");
//		
//		if (text.equals("Pick")) {
//			
//			EnterText(XPATH_TXT, "Enter Location Name", fromLocation);
//			ClickNext();
//			EnterText(XPATH_TXT, "Enter Transfer Request (*)", transferNumber);
//			ClickNext();
//
//			for (int line = 1; line <= Integer.parseInt(lineItemCount); line++) {
//
//				if (lineItemDetails.get("ASSETID_" + line) != null) {
//					lineItem = selectQuerySingleValue(String.format(TR_ASSETCODE_QUERY, requestNumber), "ASSETCODE");
//					serializedItem = true;
//				} else {
//					lineItem = selectQuerySingleValue(String.format(TR_PARTCODE_QUERY, String.valueOf(line), requestNumber), "PARTCODE");
//					serializedItem = false;
//				}
//				
//				EnterText(XPATH_TXT, "Enter Line/Item # (*) - "+line, lineItem);
//				ClickNext();
//
//				if (serializedItem) {					
//					EnterText(XPATH_TXT, "Enter Notes", "Pick - Test Notes");
//					ClickNext();
//				} else {
//					ClickNext();
//					ClickNext();
//					ClickNext();
//					EnterText(XPATH_TXT, "Enter Notes", "Pick - Test Notes");
//					ClickNext();
//				}
//
//			}
//
//			if (isObjectPresent(ID_MESSAGE, "Transcation completed message")) {
//				GetText(ID_MESSAGE, "Transcation completed message");
//				Click(ID_MESSAGE_OK, "Click OK");
//			}
//			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Rouitne Back Button");
//
//		}
//
//	}
//
//	public void pack() throws TimeoutException, NoSuchElementException, WebDriverException {
//
//		String fromLocation = selectQuerySingleValue(String.format(TR_FROM_LOC_QUERY, requestNumber), "NAME");
//		String lineItem = null;
//		boolean serializedItem = false;
//
//		selectRoutine("Pack");
//		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");
//		if (text.equals("Pack")) {
//			
//			EnterText(XPATH_TXT, "Enter Location Name", fromLocation);
//			ClickNext();
//			EnterText(XPATH_TXT, "Enter Transfer Request (*)", transferNumber);
//			ClickNext();
//			if (GetText(ID_MESSAGE, "Confirm Message").equalsIgnoreCase("Generate new shipment?")) {
//				Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for 'Generate new shipment?' message");
//
//			}
//			ClickNext();
//
//			for (int line = 1; line <= Integer.parseInt(lineItemCount); line++) {
//
//				if (lineItemDetails.get("ASSETID_" + line) != null) {
//					lineItem = selectQuerySingleValue(String.format(TR_ASSETCODE_QUERY, requestNumber), "ASSETCODE");
//					serializedItem = true;
//				} else {
//					lineItem = selectQuerySingleValue(
//							String.format(TR_PARTCODE_QUERY, String.valueOf(line), requestNumber), "PARTCODE");
//					serializedItem = false;
//				}
//
//				EnterText(XPATH_TXT, "Enter Line/Item # (*) - "+line, lineItem);
//				ClickNext();
//
//				if (serializedItem) {
//					EnterText(XPATH_TXT, "Enter Notes", "Pack - Test Notes");
//					ClickNext();
//				} else {
//					ClickNext();
//					ClickNext();
//					ClickNext();
//					EnterText(XPATH_TXT, "Enter Notes", "Pack - Test Notes");
//					ClickNext();
//				}
//
//			}
//
//			if (isObjectPresent(ID_MESSAGE, "Transcation completed message")) {
//				GetText(ID_MESSAGE, "Transcation completed message");
//				Click(ID_MESSAGE_OK, "Click OK");
//			}
//			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Rouitne Back Button");
//		}
//	}
//
//	public void ship() throws TimeoutException, NoSuchElementException,  WebDriverException {
//		
//		String fromLocation = selectQuerySingleValue(String.format(TR_FROM_LOC_QUERY,requestNumber),"NAME");
//		String ShipmentNumber = selectQuerySingleValue(String.format(TR_SHIPMENT_NO_QUERY,requestNumber),"SHIPMENTNUMBER");
//		int shipmentCount  =  Integer.parseInt(selectQuerySingleValue(String.format(TR_SHIPMENT_COUNT,requestNumber), "COUNT"));
//		
//		selectRoutine("Ship");
//		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");
//		if (text.equals("Ship")) {
//			EnterText(XPATH_TXT, "Enter Location Name", fromLocation);
//			ClickNext();
//			EnterText(XPATH_TXT, "Enter Transfer Request (*)", transferNumber);
//			ClickNext();
//			EnterText(XPATH_TXT, "Enter Shipment Method (*)", "AIRWAYS");
//			ClickNext();
//			EnterText(XPATH_TXT, "Enter Tracking Number", "TEST");
//			ClickNext();	
//			
//			if(shipmentCount!=1){
//				EnterText(XPATH_TXT, "Enter Shipment Number (*)", ShipmentNumber);			
//				ClickNext();
//			}
//			
//			ClickNext();
//			EnterText(XPATH_TXT, "Enter Notes", "Ship - Test Notes");
//			ClickNext();
//			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Rouitne Back Button");
//		}
//	}*/
//	
//
//}
