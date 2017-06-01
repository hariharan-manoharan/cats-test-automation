package main.java.businessComponents.MOBILE.AIRTEL;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class Inquiry extends Utility implements RoutineObjectRepository   {
	
	//Xpath
	
		By BARCODE1_XPATH = By.xpath(String.format(XPATH_TXT, "Enter Manufacturer or Item Code or Mfg Part Code (*) :"));

		
		//Other Declarations
		
		private String folderName = "Inquiry";
		private HashMap<String, String> inquiryTestDataHashmap = new HashMap<String, String>();

		@SuppressWarnings("rawtypes")
		public Inquiry(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
			super(test, driver, dataTable);
			getTestData();
			selectRoutineFolder(folderName);
		}
		
		public void getTestData(){
		//	inquiryTestDataHashmap = dataTable.getRowData("Inquiry");
		}

		public void selectRoutine(String routineName) {
			//ScrolltoText(routineName);
			Click(By.name(routineName), "Click - Routine - " + routineName + " is selected");
		}
		
		public void selectRoutineFolder(String folderName) {		
			Click(By.name(folderName), "Click - Routines Folder - " + folderName + " is selected");
		}
		
		public void itemInquiry() throws TimeoutException, NoSuchElementException,  WebDriverException {
			
			selectRoutine("Item Inquiry");	
			if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equalsIgnoreCase("Item Inquiry")) {
				
				EnterText(BARCODE1_XPATH, "Enter Manufacturer or Item Code or Mfg Part Code(*):", "HNSNEWPART1");
				ClickNext();
				
			}
			}
			
		
}
