package main.java.businessComponents.MOBILE.AIRTEL;

import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;


public class CustomLibrary extends ReusableLibrary implements RoutineObjectRepository {
	
	public Lock lock;
	public Connection connection;

	
	@SuppressWarnings("rawtypes")
	public CustomLibrary(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters, Lock lock, Connection connection) {
		super(test, driver, dataTable, testParameters, lock, connection);
		this.lock = lock;
		this.connection = connection;
	}

	public CustomLibrary(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters, Lock lock, Connection connection, Properties runtimeDataProperties) {
		super(test, driver, dataTable, testParameters, lock, connection, runtimeDataProperties);
		this.lock = lock;
		this.connection = connection;
	}
	
	/************************************************************************************************
	 * Function :enterTransferOrder 
	 * Decsription:Function to used enter TransferOrder in Pack Routine
	 * Date :24-07-2017 
	 * Author :Saran
	 *************************************************************************************************/
	public void enterTransferOrder(String locationName, String columnName){

		String TRANSFERCOUNT = String.format(TRANSFERCOUNT_PACK, locationName,locationName,locationName);

		String NoofTransfer = selectQuerySingleValue(TRANSFERCOUNT, "TRANSFERCOUNT");

		int count = 	Integer.parseInt(NoofTransfer);

		String data = getRuntimeTestdata(columnName);

		if (count>1){
			enterText("Enter Transfer Order (*) :", data);
			clickNext();
			clickConfirmPrompt("Generate new shipment?", "Yes");
		}
		else
		{
			clickConfirmPrompt("Generate new shipment?", "Yes");
			verifyAutopopulatefieldvalues("Transfer Order", data);
		}
	}
	
	/************************************************************************************************
	 * Function :enterShipmentNumber 
	 * Decsription:Function to used enter ShipmentNumber in InternalReceive Routine
	 * Date :24-07-2017 
	 * Author :Saran
	 *************************************************************************************************/
	public void enterShipmentNumber(String locationName, String columnName){

		String SHIPMENTCOUNT = String.format(SHIPMENTCOUNT_IR, locationName,locationName,locationName);

		String Noofshipments = selectQuerySingleValue(SHIPMENTCOUNT, "SHIPMENTCOUNT");

		int count = 	Integer.parseInt(Noofshipments);

		String data = getRuntimeTestdata(columnName);

		if (count>1){
			enterText("Enter Shipment # (*) :", data);
			clickNext();
		}
		else
		{
			verifyAutopopulatefieldvalues("Shipment #", data);
		}
	}
}
