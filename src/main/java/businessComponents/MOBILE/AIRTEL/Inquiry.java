package main.java.businessComponents.MOBILE.AIRTEL;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class Inquiry extends Utility implements RoutineObjectRepository   {
	
	   //Item Inquiry
	
		By ITEMINQ_BARCODE1_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Manufacturer or Item Code or Mfg Part Code (*) :"));	
		By SUBMITTOCLEAR_XPATH = By.xpath(String.format(XPATH_TXT, "Submit to Clear Part"));
		
		
		String MANUFACTURER_XPATH=String.format(XPATH_TXT,"Manufacturer");
		String ITEMCODE_XPATH=String.format(XPATH_TXT,"Item Code");
		String ITEMDESCRIPTION_XPATH=String.format(XPATH_TXT,"Item Description");
		String MFGPARTNO_XPATH=String.format(XPATH_TXT,"Mfg. Part Number");
		String SERIALIZED_XPATH=String.format(XPATH_TXT,"Serialized");
		String ITEMTYPE_XPATH=String.format(XPATH_TXT,"Item Type");
		String ACTIVE_XPATH=String.format(XPATH_TXT,"Active");
		String UOM_XPATH=String.format(XPATH_TXT,"Unit of Measure");

		//Asset Inquiry
		By ASSETINQ_BARCODE1_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Mfg Part Code or Item Code or SNO or UIN (*) :"));
		By SNO_UIN_XPATH = By.xpath(String.format(XPATH_TXT, "Enter SNO or UIN :"));
		
		
		
		String SERIALNO_XPATH=String.format(XPATH_TXT,"Serial Number");
		String UIN_XPATH=String.format(XPATH_TXT,"UIN");
	    String PACKAGETAG_XPATH=String.format(XPATH_TXT,"Package Tag");
		String LOTNO_XPATH=String.format(XPATH_TXT,"Lot #");
		String FANUMBER_XPATH=String.format(XPATH_TXT,"FA Number");
		String HARDWARE_REV_XPATH=String.format(XPATH_TXT,"Hardware Revision");
		String WARRANTY_XPATH=String.format(XPATH_TXT,"Warranty?");
		String ASSEMBLY_XPATH=String.format(XPATH_TXT,"Assembly?");
		String LOCATION_XPATH=String.format(XPATH_TXT,"Location"); 
		String LOCATORCODE_XPATH=String.format(XPATH_TXT,"Locator Code");
		String STATUS_XPATH=String.format(XPATH_TXT,"Status");
		String INVENTORYORG_XPATH=String.format(XPATH_TXT,"Inventory Org");
		String CONTAINER_XPATH=String.format(XPATH_TXT,"Container");
		String SUBINVENTORY_XPATH=String.format(XPATH_TXT,"Sub Inventory");
		String NODE_XPATH=String.format(XPATH_TXT,"Node");
		String LASTMODIFIED_ON_XPATH=String.format(XPATH_TXT,"Last Modified On");
		String LASTMODIFIED_BY_XPATH=String.format(XPATH_TXT,"Last Modified By");
		String TRANSFERORDER_XPATH=String.format(XPATH_TXT,"Transfer Order");
		String SHIPMENTNO_XPATH=String.format(XPATH_TXT,"Shipment #");

		
	
		
		//Other Declarations
		
		private String folderName = "Inquiry";
		private HashMap<String, String> inquiryTestDataHashmap = new HashMap<String, String>();

		@SuppressWarnings("rawtypes")
		public Inquiry(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
			super(test, driver, dataTable,testParameters);
			getTestData();
			selectRoutineFolder(folderName);
		}
		
		public void getTestData(){
			inquiryTestDataHashmap = dataTable.getRowData("Inquiry");
		}

		public void selectRoutine(String routineName) {
			//ScrolltoText(routineName);
			Click(By.name(routineName), "Click - Routine - " + routineName + " is selected");
		}
		
		public void selectRoutineFolder(String folderName) {		
			Click(By.name(folderName), "Click - Routines Folder - " + folderName + " is selected");
		}
		
		public void itemInquiry() throws TimeoutException, NoSuchElementException,  WebDriverException {
			
			String Barcode1 = inquiryTestDataHashmap.get("BARCODE1");
			String Manufacturer = inquiryTestDataHashmap.get("MANUFACTURER");
			String Itemcode= inquiryTestDataHashmap.get("ITEMCODE");
			String ItemDescription = inquiryTestDataHashmap.get("ITEMDESCRIPTION");
			String MFGPartno = inquiryTestDataHashmap.get("MFGPARTNUMBER");
			String Serialized = inquiryTestDataHashmap.get("SERIALIZED");
			String Itemtype= inquiryTestDataHashmap.get("ITEMTYPE");
			String Active = inquiryTestDataHashmap.get("ACTIVE");
			String UOM= inquiryTestDataHashmap.get("UNITOFMEASURE");
			
			selectRoutine("Item Inquiry");	
			if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equalsIgnoreCase("Item Inquiry")) {
				
				EnterText(ITEMINQ_BARCODE1_XPATH, "Enter Manufacturer or Item Code or Mfg Part Code(*):", Barcode1);
				ClickNext();
				
				VerfiyAutopopulatefieldvalues(MANUFACTURER_XPATH,"Manufacturer", Manufacturer);
				VerfiyAutopopulatefieldvalues(ITEMCODE_XPATH,"Item Code",Itemcode);
				VerfiyAutopopulatefieldvalues(ITEMDESCRIPTION_XPATH,"Item Description",ItemDescription);	
				VerfiyAutopopulatefieldvalues(MFGPARTNO_XPATH,"Mfg. Part Number",MFGPartno); 
				VerfiyAutopopulatefieldvalues(SERIALIZED_XPATH,"Serialized",Serialized);
				VerfiyAutopopulatefieldvalues(ITEMTYPE_XPATH,"Item Type",Itemtype);
				VerfiyAutopopulatefieldvalues(ACTIVE_XPATH,"Active",Active);
				VerfiyAutopopulatefieldvalues(UOM_XPATH,"Unit of Measure",UOM);
				
				
				Click(SUBMITTOCLEAR_XPATH,"Submit to Clear Part");
			}
			}
			
		public void assetInquiry() throws TimeoutException, NoSuchElementException,  WebDriverException {
			
			
			String Barcodetype = inquiryTestDataHashmap.get("BARCODE_TYPE");
			String Barcode1 = inquiryTestDataHashmap.get("BARCODE1");
			String Manufacturer = inquiryTestDataHashmap.get("MANUFACTURER");
			String Itemcode= inquiryTestDataHashmap.get("ITEMCODE");
			String ItemDescription = inquiryTestDataHashmap.get("ITEMDESCRIPTION");
			String MFGPartno = inquiryTestDataHashmap.get("MFGPARTNUMBER");
			String Serialized = inquiryTestDataHashmap.get("SERIALIZED");
			String Itemtype= inquiryTestDataHashmap.get("ITEMTYPE");
			String Active = inquiryTestDataHashmap.get("ACTIVE");
			String UOM= inquiryTestDataHashmap.get("UNITOFMEASURE");
			
			selectRoutine("Asset Inquiry");	
			if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equalsIgnoreCase("Asset Inquiry")) {
				
				EnterText(ASSETINQ_BARCODE1_XPATH, "Enter Mfg Part Code or Item Code or SNO or UIN (*) :", Barcode1);
				ClickNext();
				
				VerfiyAutopopulatefieldvalues(MANUFACTURER_XPATH,"Manufacturer", Manufacturer);
				VerfiyAutopopulatefieldvalues(ITEMCODE_XPATH,"Item Code",Itemcode);
				VerfiyAutopopulatefieldvalues(ITEMDESCRIPTION_XPATH,"Item Description",ItemDescription);	
				VerfiyAutopopulatefieldvalues(MFGPARTNO_XPATH,"Mfg. Part Number",MFGPartno);
				
				if(Barcodetype.equalsIgnoreCase("NONSERIALIZED_ITEMCODE")){
					
					EnterText(SNO_UIN_XPATH, "Enter SNO or UIN :", Barcode1);
					ClickNext(); 
						
					
				}
				String Serialno ="";
				String Assetcode ="";
				String Packagetag ="";
				String Lotno ="";
				
				String FAnumber ="";
				String HardwareRevision ="";
				String Warranty ="";
				String Assembly ="";
				String Location ="";
				String Locatorcode ="";
				String Status ="";
				String InventoryOrg ="";
				String Container ="";
				String SubInventory ="";
				String Node ="";
				String LastMODON ="";
				String LastMODBY ="";
				String TransferNo ="";
				String ShipmentNo ="";
				
				VerfiyAutopopulatefieldvalues(SERIALNO_XPATH,"Serial Number",Serialno);
				VerfiyAutopopulatefieldvalues(UIN_XPATH,"UIN",Assetcode);
				VerfiyAutopopulatefieldvalues(PACKAGETAG_XPATH,"Package Tag",Packagetag);
				VerfiyAutopopulatefieldvalues(LOTNO_XPATH,"Lot #",Lotno);
				VerfiyAutopopulatefieldvalues(FANUMBER_XPATH,"FA Number",FAnumber);
				VerfiyAutopopulatefieldvalues(HARDWARE_REV_XPATH,"Hardware Revision",HardwareRevision);
				VerfiyAutopopulatefieldvalues(ITEMTYPE_XPATH,"Item Type",Itemtype);
				VerfiyAutopopulatefieldvalues(WARRANTY_XPATH,"Warranty?",Warranty);
				VerfiyAutopopulatefieldvalues(ASSEMBLY_XPATH,"Assembly?",Assembly);
				VerfiyAutopopulatefieldvalues(LOCATION_XPATH,"Location",Location);
				VerfiyAutopopulatefieldvalues(LOCATORCODE_XPATH,"Locator Code",Locatorcode);
				VerfiyAutopopulatefieldvalues(STATUS_XPATH,"Status",Status);
				VerfiyAutopopulatefieldvalues(INVENTORYORG_XPATH,"Inventory Org",InventoryOrg);
				VerfiyAutopopulatefieldvalues(CONTAINER_XPATH,"Container",Container);
				VerfiyAutopopulatefieldvalues(SUBINVENTORY_XPATH,"Sub Inventory",SubInventory);
				VerfiyAutopopulatefieldvalues(NODE_XPATH,"Node",Node);
				VerfiyAutopopulatefieldvalues(LASTMODIFIED_ON_XPATH,"Last Modified On",LastMODON);
				VerfiyAutopopulatefieldvalues(LASTMODIFIED_BY_XPATH,"Last Modified By",LastMODBY);
				VerfiyAutopopulatefieldvalues(TRANSFERORDER_XPATH,"Transfer Order",TransferNo);
				VerfiyAutopopulatefieldvalues(SHIPMENTNO_XPATH,"Shipment #",ShipmentNo);
				
				
				Click(SUBMITTOCLEAR_XPATH,"Submit to Clear Part");
			}
			}
}
