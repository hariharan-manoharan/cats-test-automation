package main.java.businessComponents.MOBILE.XO;


import org.openqa.selenium.By;
import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class ProfilesActivity extends Utility implements RoutineObjectRepository {	

	@SuppressWarnings("rawtypes")
	public ProfilesActivity(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
		super(test,driver,dataTable);
	}	
	/************************************************************************************************
	 * Function   :selectAdminProfile
	 * Decsription:Function to Select admin profile.
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void selectAdminProfile() {		
		
		waitCommand(TAB_ADMIN);
		ScrolltoText("ADMIN");		
		Click(TAB_ADMIN, "Profile-Admin is Clicked");

	}
	/************************************************************************************************
	 * Function   :verifyInfo
	 * Decsription:Function to verify version of the CATS App.
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void verifyInfo() {	
		selectAdminProfile(); 
		waitCommand(IMG_HOME);
		Click(IMG_HOME, "Profile-Home Page is clicked");
		Click(TAB_INFO, "Profile-Info is clicked");
		
		GetText(TXT_CLIENT_VERSION,"ClientVersion");
		GetText(TXT_SERVER_VERSION,"ServerVersion");
		GetText(TXT_CATS_VERSION,"CATSVersion");
		
		
		Click(BTN_INFO_DONE,"Info - Done Button is clicked");
		
		
		
	}
	
}
