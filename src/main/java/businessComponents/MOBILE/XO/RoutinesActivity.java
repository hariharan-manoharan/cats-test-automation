package main.java.businessComponents.MOBILE.XO;



import java.util.Random;

import org.openqa.selenium.By;
import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class RoutinesActivity extends Utility  implements RoutineObjectRepository {
	

	@SuppressWarnings("rawtypes")
	public RoutinesActivity(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
		super(test,driver,dataTable);
	}	
	
	/************************************************************************************************
	 * Function   :selectRoutine
	 * Decsription:Function to select respective Routines
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/			
	public void selectRoutine(String routineName){		
		ScrolltoText(routineName);		
		Click(By.name(routineName), "Click - Routine - "+routineName+" is selected");
	}
	

	/************************************************************************************************
	 * Function   :stockASSET
	 * Decsription:Function to STOCK asset to a Location(WH,NDO)
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	
	public void stockASSET(){
		
		
		String toLocation= dataTable.getData("Stock", "To_Location_Asset");
		String toStatus= dataTable.getData("Stock", "To_Status_Asset");
		String toBin= dataTable.getData("Stock", "To_Bin_Asset");
		String assetcode= "ASSET";
		String serialno= "SER";
		String condition= dataTable.getData("Stock", "Condition_Asset");
		String notes= dataTable.getData("Stock", "Notes_Asset");
		String itemno=GetSerializedPart();
		selectRoutine("Stock Equipment");	
		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");		
		if(text.equals("Stock Equipment")){
			
			EnterTextWebView(XPATH_TXT_WEBVIEW,"To Location : ",toLocation);
			ClickNext();		
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter To Status(*):",toStatus);
			ClickNext();			
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter To Bin(*):",toBin);
			ClickNext();
			
			
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Item/Mfg.Part #(*):",itemno);
			ClickNext();
			Random rand = new Random();
			int randomNum = rand.nextInt(10);
		    int randomNum1 = rand.nextInt(100);
		    
		    properties.setProperty("ASSETCODE",randomNum+assetcode+randomNum1);
		    
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Assetcode(*):",randomNum+assetcode+randomNum1);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Serial#(*):",serialno+randomNum1);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Condition(*):",condition);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Notes (*)	",notes);
			ClickNext();
			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Routine Back Button");
		}
				
	}	
	/************************************************************************************************
	 * Function   :Pick
	 * Decsription:Function to Pick Asset or Part as a part of transfer request.
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void pick(){
		selectRoutine("Pick");	
	
		String tranferNo= dataTable.getData("Pick", "Transfer_#");
		String lineitem= dataTable.getData("Pick", "Line/Item_#");
		String Assetcode= properties.getProperty("ASSETCODE");
		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");		
		if(text.equals("Pick")){
			
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Transfer #",tranferNo);
			ClickNext();		
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Line/Item # ",lineitem);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Asset Code/Serial # ",Assetcode);
			ClickNext();
			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Routine Back Button");
		}
				
	}

	/************************************************************************************************
	 * Function   :Pack
	 * Decsription:Function to Pack Asset or Part as a part of transfer request.
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void pack() {		
		selectRoutine("Pack");
		
		String tranferNo= dataTable.getData("Pack", "Transfer_#");
		String container= dataTable.getData("Pack", "Generate_Container");
		String Assetcode= properties.getProperty("ASSETCODE");
		
		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");		
		if(text.equals("Pack")){
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Transfer #",tranferNo);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Generate Container? (*)",container);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Barcode (*)",Assetcode);
			ClickNext();
			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Rouitne Back Button");
		}
	}
	/************************************************************************************************
	 * Function   :Ship
	 * Decsription:Function to Ship Asset or Part as a part of transfer request.
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	

	public void ship() {		
		selectRoutine("Ship");	
		String tranferNo= dataTable.getData("Ship", "Transfer_#");
		String shipmentNo= dataTable.getData("Ship", "Shipment_#");
		String shipmentMethod= dataTable.getData("Ship", "Shipment_Method");
		String trackingNo= dataTable.getData("Ship", "Tracking_#");
		String notes= dataTable.getData("Ship", "Notes");
		
		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");		
		if(text.equals("Ship")){
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Transfer #",tranferNo);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Shipment Number (*)",shipmentNo);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Shipment Method ",shipmentMethod);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Tracking Number",trackingNo);
			ClickNext();			
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Notes",notes);
			ClickNext();
			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Routine Back Button");
		}
	}
	/************************************************************************************************
	 * Function   :Internal Receive
	 * Decsription:Function to Receive Asset or Part as a part of transfer request.
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void internalReceive() {		
		selectRoutine("Internal Receive");	
		
		String tranferNo= dataTable.getData("Internal_Receive", "Transfer_#");
		String Assetcode= properties.getProperty("ASSETCODE");
		String bin= dataTable.getData("Internal_Receive", "Bin");
		String notes= dataTable.getData("Internal_Receive", "Notes");
		String text = GetText(ID_ACTION_BAR_SUBTITLE, "Routine name");	
		
		if(text.equals("Internal Receive")){
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Requisition #",tranferNo);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Barcode(*)",Assetcode);
			ClickNext();
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Bin(*)",bin);
			ClickNext();		
			EnterTextWebView(XPATH_TXT_WEBVIEW,"Enter Notes",notes);
			ClickNext();
			Click(CONTENT_DESC_ROUITNE_BACK_BTN, "Click Routine Back Button");
		}
	}
}
