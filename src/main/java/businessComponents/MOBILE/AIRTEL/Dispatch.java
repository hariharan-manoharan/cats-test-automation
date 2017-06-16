package main.java.businessComponents.MOBILE.AIRTEL;

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

	By LOCATION_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Location (*) :"));
	By TRANSFERORDER_XPATH =By.xpath(String.format(XPATH_TXT, "Enter Transfer Order (*) :"));
	By LINENOORCONTAINER_XPATH =By.xpath(String.format(XPATH_TXT, "Enter Line # or Container (*) :"));
	By LOTNUMBER_XPATH =By.xpath(String.format(XPATH_TXT, "Enter Lot # (*) :"));
	By NOTES_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Notes :"));
	By QTY_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Quantity (*) :"));
	By PACKAGEID_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Package ID :"));

	
	// Other Declarations

	private String folderName = "Dispatch";
	private HashMap<String, String> dispatchTestDataHashmap = new HashMap<String, String>();

	@SuppressWarnings("rawtypes")
	public Dispatch(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable,testParameters);
		//getTestData();
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


	public void pick()throws TimeoutException, NoSuchElementException, WebDriverException{

		String BARCODE_TYPE= "Container1";
		String Itemcode="AUTOSARNS001";
		selectRoutine("Pick");
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals("Pick")) {
			// Entering header level details
			EnterText(LOCATION_XPATH, "Enter Location(*) :", "BAL-MUNDKA-MDEL");									  
			ClickNext();
			EnterText(TRANSFERORDER_XPATH, "Enter Transfer Order(*):", "T000000050");
			ClickNext();

			if (BARCODE_TYPE.equalsIgnoreCase("Container")){
				EnterText(LINENOORCONTAINER_XPATH, "Enter Line # or Container(*):", "PKGAUTONS34");
				ClickNext();
				
				waitCommand(By.xpath(".//android.view.View[@index='7']/android.view.View[@index='2']/android.view.View[@index='0']/android.view.View[@index='0']"));
				
				driver.findElement(By.xpath(".//android.view.View[@index='7']/android.view.View[@index='2']/android.view.View[@index='0']/android.view.View[@index='0']")).click();
				
				//android.view.View[@index='7]/android.view.View[@index='2']/android.view.View[@index='0']")
				
				String Itemcode1 = GetPickListValue(1);
				
				
				if (Itemcode.equalsIgnoreCase(Itemcode1)){
					test.log(LogStatus.PASS, "<b>" + Itemcode1 + "</b> matches the given Testdata <b>"+Itemcode+"</b>", "");	
				}
				else{
					test.log(LogStatus.FAIL, "<font color=red><b>" + Itemcode1 + "</b></font>-not matches the given Testdata- <b> <font color=red>"+Itemcode1+"</b></font>", "");
				}
				
				ClickNext();
				
				EnterText(NOTES_XPATH, "Enter Notes :", "Automation:Pick Routine");
				ClickNext();
				
			}
			else{
				EnterText(LINENOORCONTAINER_XPATH, "Enter Line # or Container(*):", "1");
				ClickNext();
				
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Barcode"),"Barcode","AUTOSARNS001");
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Item Code"),"Item Code","AUTOSARNS001");
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Item Code"),"From Status","ON HAND");
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Item Description"),"Item Description","POI CONNECTIVITY FOR BHARTI AIRTEL LIMITED .");	
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Pick Count"),"Pick Count","0/5");
				
				EnterText(LOTNUMBER_XPATH, "Enter Lot #:", "MRRSARNS001_4");
				ClickNext();
				
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Mfg. Part #"),"Mfg. Part #","1AUTOSARMFGPARTNUMNS001");
				VerfiyAutopopulatefieldvalues(String.format(XPATH_TXT,"Locator Code"),"Locator Code","");
				
				EnterText(QTY_XPATH, "Enter Quantity (*) :", "5");
				ClickNext();
				
				EnterText(PACKAGEID_XPATH, "Enter Package ID :", "");
				ClickNext();
				
				EnterText(NOTES_XPATH, "Enter Notes :", "Automation:Pick Routine");
				ClickNext();
				

			}

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
}