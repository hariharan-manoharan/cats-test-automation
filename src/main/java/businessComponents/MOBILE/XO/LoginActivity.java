package main.java.businessComponents.MOBILE.XO;

import org.openqa.selenium.By;


import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class LoginActivity extends Utility implements RoutineObjectRepository {

	@SuppressWarnings("rawtypes")
	public LoginActivity(ExtentTest test, AndroidDriver driver, DataTable dataTable) {
		super(test,driver,dataTable);
	}	

	/************************************************************************************************
	 * Function   :addConnection
	 * Decsription:Function to  add new Connections 
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void addConnection() {
		Click(TAB_CONNECTIONS, "Click - AddConnection");
		Click(ICON_ADD, "Click - AddConnection Symbol");
		EnterText(TXT_NAME, "Enter - Connection Name", environmentVariables.get("EnvironmentName"));
		EnterText(TXT_HOST, "Enter - Host", environmentVariables.get("MobilityHost"));
		EnterText(TXT_PORT, "Enter - Port", environmentVariables.get("MobilityPort"));

		if(environmentVariables.get("MobilitySSL").equalsIgnoreCase("Yes"))
			{
			Click(TOGGLE_BTN_SSL, "Click - Enable SSL");
			}
		Click(ICON_SAVE, "Click - Save Connection");
		Click(IMG_BACK_BTN , "Click - Back button");
	}
	/************************************************************************************************
	 * Function   :login
	 * Decsription:Function to login CATS APP
	 * Date		  :21-12-2016	
	 * Author	  :Saran	
	 *************************************************************************************************/	
	public void login() {
		
		String username = dataTable.getData("Login", "Username");
		String password = dataTable.getData("Login", "Password");
		
		EnterText(TXT_USERNAME, "Enter - Username", username);
		EnterText(TXT_PASSWORD, "Enter - Password", password);
		HideKeyboard();
		Click(BTN_CONNECT, "Click - Connect button");
	}
}
