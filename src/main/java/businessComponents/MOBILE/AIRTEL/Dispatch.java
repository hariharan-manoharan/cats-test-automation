package main.java.businessComponents.MOBILE.AIRTEL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

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

public class Dispatch extends Utility implements RoutineObjectRepository{

	//Xpath

	//PICK
	By LOCATION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Location (*) :"));
	By TRANSFERORDER_XPATH =By.xpath(String.format(XPATH_TXT, "Enter Transfer Order (*) :"));
	By LINENOORCONTAINER_XPATH =By.xpath(String.format(XPATH_TXT, "Enter Line # or Container (*) :"));
	By LOTNUMBER_XPATH =By.xpath(String.format(XPATH_TXT, "Enter Lot # (*) :"));
	By NOTES_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Notes :"));
	By QTY_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Quantity (*) :"));
	By ENTERPACKAGEID_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Package ID :"));
	By BARCODE2_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Asset Code (UIN)/Serial #/Package Tag :"));

	String BARCODE_XPATH=String.format(XPATH_TXT,"Barcode");
	String ITEMCODE_XPATH=String.format(XPATH_TXT,"Item Code");
	String FROMSTATUS_XPATH=String.format(XPATH_TXT,"From Status");
	String ITEMDESCRIPTION_XPATH=String.format(XPATH_TXT,"Item Description");
	String PICKCOUNT_XPATH=String.format(XPATH_TXT,"Pick Count");
	String MFGPARTNO_XPATH=String.format(XPATH_TXT,"Mfg. Part #");
	String LOCATOR_XPATH=String.format(XPATH_TXT,"Locator Code");
	String ASSETCODE_XPATH=String.format(XPATH_TXT,"Asset Code (UIN)");
	String SERIALNO_XPATH=String.format(XPATH_TXT,"Serial Number");
	String PACKAGEID_XPATH=String.format(XPATH_TXT,"Package ID");
	
	//PACK
	By SHIPMENTNO_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Shipment (*) :"));
	By PACK_BARCODE_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Barcode (*) :"));
	
	String PACKAGETAG_XPATH=String.format(XPATH_TXT,"Package Tag");
	String PACKMFGPARTNO_XPATH=String.format(XPATH_TXT,"Mfg Part Number");
	
	
	//SHIP
	By FROM_LOCATION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter From Location (*) :"));
	By SHIPMENTMETHOD_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Shipment Method (*) :"));
	By TRACKINGNUMBER_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Tracking Number :"));
	
	
	
	
	String TOLOCATION_XPATH=String.format(XPATH_TXT,"To Location");
	String TOLOCATION_ADDRESS_XPATH=String.format(XPATH_TXT,"To Location Address");
	String SHIPMENTNUMBER_XPATH=String.format(XPATH_TXT,"Shipment Number");
	
	
	
	// Other Declarations

	private String folderName = "Dispatch";
	private HashMap<String, String> dispatchTestDataHashmap = new HashMap<String, String>();

	@SuppressWarnings("rawtypes")
	public Dispatch(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable,testParameters);
		getTestData();
		selectRoutineFolder(folderName);
	}

	public void getTestData(){
		dispatchTestDataHashmap = dataTable.getRowData("Dispatch");
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
		if (isElementPresent(By.xpath(String.format(XPATH_TXT, loopField)),"Loop field - "+loopField)) {
			report(routineName+" Transaction is successfull", LogStatus.PASS);			
		} else {
			report(routineName+" Transaction is not successfull", LogStatus.FAIL);			
		}
	}


	public void pick()throws TimeoutException, NoSuchElementException, WebDriverException, SQLException{
		
		String Itemtype= dispatchTestDataHashmap.get("ITEMTYPE");
		String Itemcode= dispatchTestDataHashmap.get("ITEMCODE");
		String Location = dispatchTestDataHashmap.get("FROM_LOCATION");
		String LineNo = dispatchTestDataHashmap.get("LINE#");
		String Container = dispatchTestDataHashmap.get("CONTAINER");
		String Barcode= dispatchTestDataHashmap.get("BARCODE");
		String FromStatus= dispatchTestDataHashmap.get("FROMSTATUS");
		String ItemDescription = dispatchTestDataHashmap.get("ITEMDESCRIPTION");
		String LOTNo = dispatchTestDataHashmap.get("LOTNO");
		String MFGPartno = dispatchTestDataHashmap.get("MFGPARTNUMBER");
		String Locator= dispatchTestDataHashmap.get("LOCATOR");
		String Packageid =dispatchTestDataHashmap.get("PACKAGEID");
		String Quantity = dispatchTestDataHashmap.get("QUANTITY");
		String Assetcode = properties.getProperty("ASSETCODE");
		String SerialNO = properties.getProperty("SERIALNUMBER");
		String RequestNo=properties.getProperty("REQUESTNUMBER");
		String pickcount;
		String TRANSFERNO = null;
		String query = null;
		
		query = "SELECT * FROM CATS_TRANSFER WHERE REFERENCENUMBER ="+"'"+RequestNo+"'";
		TRANSFERNO = selectQuerySingleValue(query, "TRANSFERNUMBER");
		properties.setProperty("TRANSFERNUMBER",TRANSFERNO);
		if(Itemtype.equalsIgnoreCase("NONSERIALIZED")){
			pickcount = "0/"+Quantity;
		}else
		{
			pickcount ="0/1";
		}
		
		
		selectRoutine("Pick");
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals("Pick")) {
			// Entering header level details
			EnterText(LOCATION_XPATH, "Enter Location(*) :", Location);									  
			ClickNext();
			
			EnterText(TRANSFERORDER_XPATH, "Enter Transfer Order(*):", TRANSFERNO);
			ClickNext();
			

			if (Itemtype.equalsIgnoreCase("CONTAINER")){
				EnterText(LINENOORCONTAINER_XPATH, "Enter Line # or Container(*):", Container);
				ClickNext();

				waitCommand(By.xpath(".//android.view.View[@index='7']/android.view.View[@index='2']/android.view.View[@index='0']/android.view.View[@index='0']"));

				driver.findElement(By.xpath(".//android.view.View[@index='7']/android.view.View[@index='2']/android.view.View[@index='0']/android.view.View[@index='0']")).click();

				String Itemcode1 = GetPickListValue(1);


				if (Itemcode.equalsIgnoreCase(Itemcode1)){
					test.log(LogStatus.PASS, "<b>" + Itemcode1 + "</b> matches the given Testdata <b>"+Itemcode+"</b>", "");	
				}
				else{
					test.log(LogStatus.FAIL, "<font color=red><b>" + Itemcode1 + "</b></font>-not matches the given Testdata- <b> <font color=red>"+Itemcode1+"</b></font>", "");
				}

				ClickNext();


			}
			else{


				EnterText(LINENOORCONTAINER_XPATH, "Enter Line # or Container(*):", LineNo);
				ClickNext();
				
			

				VerfiyAutopopulatefieldvalues(BARCODE_XPATH,"Barcode",Barcode);
				VerfiyAutopopulatefieldvalues(ITEMCODE_XPATH,"Item Code",Itemcode);
				VerfiyAutopopulatefieldvalues(FROMSTATUS_XPATH,"From Status",FromStatus);
				VerfiyAutopopulatefieldvalues(ITEMDESCRIPTION_XPATH,"Item Description",ItemDescription);	
				VerfiyAutopopulatefieldvalues(PICKCOUNT_XPATH,"Pick Count",pickcount);


				if(Itemtype.equalsIgnoreCase("NONSERIALIZED")){
					EnterText(LOTNUMBER_XPATH, "Enter Lot #:", LOTNo);
					ClickNext();

					VerfiyAutopopulatefieldvalues(MFGPARTNO_XPATH,"Mfg. Part #",MFGPartno);

					VerfiyAutopopulatefieldvalues(LOCATOR_XPATH,"Locator Code",Locator);

					EnterText(QTY_XPATH, "Enter Quantity (*) :", Quantity);
					ClickNext();

					EnterText(ENTERPACKAGEID_XPATH, "Enter Package ID :", Packageid);
					ClickNext();
				}

				if(Itemtype.equalsIgnoreCase("SERIALIZED")){

					EnterText(BARCODE2_XPATH, "Enter Asset Code (UIN)/Serial #/Package Tag :", Assetcode);
					ClickNext();

					waitCommand(By.xpath(".//android.view.View[@content-desc='Serial Number']"));
					
					
					VerfiyAutopopulatefieldvalues(ASSETCODE_XPATH,"Asset Code (UIN)",Assetcode);

					VerfiyAutopopulatefieldvalues(SERIALNO_XPATH,"Serial Number",SerialNO);

					VerfiyAutopopulatefieldvalues(PACKAGEID_XPATH,"Package ID",Packageid);
					
					waitCommand(By.xpath(MFGPARTNO_XPATH));

					VerfiyAutopopulatefieldvalues(MFGPARTNO_XPATH,"Mfg. Part #",MFGPartno);
				}

			}
			
			//All items have been picked for line 1
			//Transfer T000000084 has been fully picked.
			
			EnterText(NOTES_XPATH, "Enter Notes :", "Automation:Pick Routine");
			ClickNext();
		}
			
	}
	

	private void VerfiyAutopopulatefieldvalues(String labelxpath ,String objectName , String values ){
		

		String value1 = driver.findElement(By.xpath(labelxpath+"/following-sibling::android.view.View")).getAttribute("name");	
		String value2 = values;
		
		if (value1.equalsIgnoreCase(value2)){
			test.log(LogStatus.PASS, "<b>" + objectName + "</b> matches the given Testdata <b>"+value2+"</b>", "");	
		}
		else{
			test.log(LogStatus.FAIL, "<font color=red><b>" + objectName + "</b></font>-not matches the given Testdata- <b> <font color=red>"+value2+"</b></font>", "");
		}
		
	}
	
	public void pack()throws TimeoutException, NoSuchElementException, WebDriverException{
		
		String TransferNumber=properties.getProperty("TRANSFERNUMBER");
		String Location = dispatchTestDataHashmap.get("FROM_LOCATION");
		String NewShipment = dispatchTestDataHashmap.get("NEW_SHIPMENT");
		String confirmmsg="Generate new shipment?";
		String Alertmsg ="No Shipment exists for the selected Transfer Order. Enter new Shipment Number to proceed";
		String Barcode= dispatchTestDataHashmap.get("BARCODE");
		String Itemtype= dispatchTestDataHashmap.get("ITEMTYPE");
		String Itemcode= dispatchTestDataHashmap.get("ITEMCODE");
		String ItemDescription = dispatchTestDataHashmap.get("ITEMDESCRIPTION");
		String MFGPartno = dispatchTestDataHashmap.get("MFGPARTNUMBER");
		String Quantity = dispatchTestDataHashmap.get("QUANTITY"); 
		String Packageid =dispatchTestDataHashmap.get("PACKAGEID");
		String ShipmentNo =dispatchTestDataHashmap.get("SHIPMENT_NUMBER");
		String Assetcode = properties.getProperty("ASSETCODE");
		String SerialNO = properties.getProperty("SERIALNUMBER");
		
		selectRoutine("Pack");
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals("Pack")) {
		EnterText(LOCATION_XPATH, "Enter Location (*) :", Location);
		ClickNext();
		
		EnterText(TRANSFERORDER_XPATH, "Enter Transfer Order (*) :", TransferNumber);
		ClickNext();
		
		if(validateMessage("Generate new shipment?")){
			if(NewShipment.equalsIgnoreCase("Yes")){
				Click(ID_MESSAGE_CONFIRM_YES, "Clicked 'Yes' for prompt - "+confirmmsg);

				waitCommand(SHIPMENTNO_XPATH);

				String Shipmentno=GetText(SHIPMENTNO_XPATH, "Enter Shipment (*) :");

				properties.setProperty("SHIPMENTNO", Shipmentno);

			}else{
				Click(ID_MESSAGE_CONFIRM_NO, "Clicked 'No' for prompt - "+confirmmsg);	

				validateMessage(Alertmsg);

				Click(ID_MESSAGE_OK, "Clicked 'OK' for prompt - "+Alertmsg);

				EnterText(SHIPMENTNO_XPATH, "Enter Shipment (*) :", ShipmentNo);

			}
			ClickNext();
		}
		

		
		if(Itemtype.equalsIgnoreCase("NONSERIALIZED")){
		
			EnterText(PACK_BARCODE_XPATH, "Enter Barcode (*) :", Barcode);
			ClickNext();
			
			waitCommand(By.xpath(ITEMDESCRIPTION_XPATH));
		
			VerfiyAutopopulatefieldvalues(ITEMCODE_XPATH,"Item Code",Itemcode);
		
			VerfiyAutopopulatefieldvalues(ITEMDESCRIPTION_XPATH,"Item Description",ItemDescription);
			
			VerfiyAutopopulatefieldvalues(PACKMFGPARTNO_XPATH,"Mfg. Part #",MFGPartno);
		
			EnterText(QTY_XPATH, "Enter Quantity (*) :", Quantity);
			ClickNext();
		
		
		}
		
		if(Itemtype.equalsIgnoreCase("SERIALIZED")){
			
			EnterText(PACK_BARCODE_XPATH, "Enter Barcode (*) :", Assetcode);
			ClickNext();
			
			waitCommand(By.xpath(ASSETCODE_XPATH));
			
			VerfiyAutopopulatefieldvalues(ITEMCODE_XPATH,"Item Code",Itemcode);
		
			VerfiyAutopopulatefieldvalues(ITEMDESCRIPTION_XPATH,"Item Description",ItemDescription);
			
			VerfiyAutopopulatefieldvalues(ASSETCODE_XPATH,"Asset Code (UIN)",Assetcode);

			VerfiyAutopopulatefieldvalues(SERIALNO_XPATH,"Serial Number",SerialNO);

			VerfiyAutopopulatefieldvalues(PACKAGETAG_XPATH,"Package ID",Packageid);
			
			waitCommand(By.xpath(PACKMFGPARTNO_XPATH));

			VerfiyAutopopulatefieldvalues(PACKMFGPARTNO_XPATH,"Mfg. Part #",MFGPartno);
		
		
			
		}

		EnterText(NOTES_XPATH, "Enter Notes :", "Automation:Pack Routine");
		ClickNext();	
		
	}	
		
	}	
		
		
	public void ship()throws TimeoutException, NoSuchElementException, WebDriverException{
		
		String TransferNumber=properties.getProperty("TRANSFERNUMBER");
		String FromLocation = dispatchTestDataHashmap.get("FROM_LOCATION");
		String ToLocation = dispatchTestDataHashmap.get("TO_LOCATION");
		String ToLocationAdderss = dispatchTestDataHashmap.get("TO_LOCATION_ADDRESS");
		String Shipmentmethod = dispatchTestDataHashmap.get("SHIPMENTMETHOD");
		String Itemtype= dispatchTestDataHashmap.get("ITEMTYPE");
		
		
		String TrackingNumber=dispatchTestDataHashmap.get("TRACKING_NUMBER");
		String NewShipment = dispatchTestDataHashmap.get("NEW_SHIPMENT");
		String Itemcode= dispatchTestDataHashmap.get("ITEMCODE");
		String Shipmentnumber = null ;
		String Assetcode = properties.getProperty("ASSETCODE");
		
		
		selectRoutine("Ship");
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals("Ship")) {
			
		EnterText(FROM_LOCATION_XPATH, "FROM_LOCATION_XPATH", FromLocation);
		ClickNext();
		
		EnterText(TRANSFERORDER_XPATH, "Enter Transfer Order (*) :", TransferNumber);
		ClickNext();

		VerfiyAutopopulatefieldvalues(TOLOCATION_XPATH,"To Location",ToLocation);
		
		VerfiyAutopopulatefieldvalues(TOLOCATION_ADDRESS_XPATH,"To Location Address",ToLocationAdderss);
		
		EnterText(SHIPMENTMETHOD_XPATH, "Enter Shipment Method (*) :", Shipmentmethod);
		ClickNext();
		
		EnterText(TRACKINGNUMBER_XPATH, "Enter Tracking Number :", TrackingNumber);
		ClickNext();
		
		if(NewShipment.equalsIgnoreCase("Yes")){
		Shipmentnumber = properties.getProperty("SHIPMENTNO");
		}else{
		Shipmentnumber =dispatchTestDataHashmap.get("SHIPMENT_NUMBER");
		}
		VerfiyAutopopulatefieldvalues(SHIPMENTNUMBER_XPATH,"Shipment Number",Shipmentnumber);
		
		waitCommand(By.xpath(".//android.view.View[@index='15']/android.view.View[@index='2']/android.view.View[@index='0']/android.view.View[@index='0']"));

		driver.findElement(By.xpath(".//android.view.View[@index='15']/android.view.View[@index='2']/android.view.View[@index='0']/android.view.View[@index='0']")).click();
		
		

		if(Itemtype.equalsIgnoreCase("NONSERIALIZED")){
		String Itemcode1 = GetPickListValue(1);
		if (Itemcode.equalsIgnoreCase(Itemcode1)){
			test.log(LogStatus.PASS, "<b>" + Itemcode1 + "</b> matches the given Testdata <b>"+Itemcode+"</b>", "");	
		}
		else{
			test.log(LogStatus.FAIL, "<font color=red><b>" + Itemcode1 + "</b></font>-not matches the given Testdata- <b> <font color=red>"+Itemcode1+"</b></font>", "");
		}
		}
		
		if(Itemtype.equalsIgnoreCase("SERIALIZED")){
		
		String Assetcode1 = GetPickListValue(1);
		if (Assetcode1.equalsIgnoreCase(Assetcode)){
			test.log(LogStatus.PASS, "<b>" + Assetcode1 + "</b> matches the given Testdata <b>"+Assetcode+"</b>", "");	
		}
		else{
			test.log(LogStatus.FAIL, "<font color=red><b>" + Assetcode1 + "</b></font>-not matches the given Testdata- <b> <font color=red>"+Assetcode+"</b></font>", "");
		}
		}
		
		if(Itemtype.equalsIgnoreCase("CONTAINER")){
			
			String Assetcode1 = GetPickListValue(1);
			if (Assetcode1.equalsIgnoreCase(Assetcode)){
				test.log(LogStatus.PASS, "<b>" + Assetcode1 + "</b> matches the given Testdata <b>"+Assetcode+"</b>", "");	
			}
			else{
				test.log(LogStatus.FAIL, "<font color=red><b>" + Assetcode1 + "</b></font>-not matches the given Testdata- <b> <font color=red>"+Assetcode+"</b></font>", "");
			}
		}
		ClickNext();
		ClickNext();
		
		EnterText(NOTES_XPATH, "Enter Notes :", "Automation:Ship Routine");
		ClickNext();	
		
		}
	
	}
		
}
	
