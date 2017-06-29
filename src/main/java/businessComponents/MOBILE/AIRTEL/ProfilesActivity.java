package main.java.businessComponents.MOBILE.AIRTEL;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class ProfilesActivity extends Utility implements RoutineObjectRepository  {	

	@SuppressWarnings("rawtypes")
	public ProfilesActivity(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test,driver,dataTable,testParameters);
	}

	public void selectProfile() throws TimeoutException, NoSuchElementException{		
		
		waitCommand(By.name("ADMIN"));			
		Click(NAME_LISTTEXT_ADMIN, "Click - ADMIN Profile is selected");

	}
	
	
}
