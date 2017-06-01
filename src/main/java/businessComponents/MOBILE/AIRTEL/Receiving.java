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

public class Receiving extends Utility implements RoutineObjectRepository {

	// Xpath

	// Confirm Messages

	String confirmMsg = "";

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
	

	
	public void mrrReceive() throws TimeoutException, NoSuchElementException,  WebDriverException {
		
		selectRoutine(MMR_RECEIVE);		
		if (GetText(ID_ACTION_BAR_SUBTITLE, "Routine name").equals(MMR_RECEIVE)) {
			
		}
	}
}
