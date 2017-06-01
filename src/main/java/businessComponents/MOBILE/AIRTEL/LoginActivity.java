package main.java.businessComponents.MOBILE.AIRTEL;


import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class LoginActivity extends Utility implements RoutineObjectRepository {

	@SuppressWarnings("rawtypes")
	public LoginActivity(ExtentTest test, AndroidDriver driver,DataTable  dataTable) {
		super(test,driver,dataTable);
	}

	public void addConnection() {
		Click(NAME_ADD_CONNECTION, "Click - AddConnection");
		Click(ID_ADD_CONNECTIONS, "Click - AddConnection Symbol");
		EnterText(NAME_TXT_CONNECTION_NAME, "Enter - Connection Name", environmentVariables.get("EnvironmentName"));
		EnterText(ID_TXT_CONNECTION_HOST, "Enter - Host", environmentVariables.get("MobilityHost"));
		EnterText(ID_TXT_CONNECTION_PORT, "Enter - Port", environmentVariables.get("MobilityPort"));
		if(environmentVariables.get("MobilitySSL").equalsIgnoreCase("Yes")){
		Click(ID_TOGGLE_BTN_SSL, "Click - Enable SSL");
		}
		Click(ID_ICON_SAVE, "Click - Save Connection");
		Click(ID_IMG_BACK_BTN, "Click - Back button");
	}

	public void login() {
		//EnterText(ID_TXT_USERNAME, "Enter - Username", "catsadm");
		//EnterText(ID_TXT_PASSWORD, "Enter - Password", "catscats11");
		//HideKeyboard();
		Click(ID_BTN_CONNECT, "Click - Connect button");
	}
}
