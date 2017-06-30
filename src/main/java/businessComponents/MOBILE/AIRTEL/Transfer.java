package main.java.businessComponents.MOBILE.AIRTEL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;

public class Transfer extends Utility implements RoutineObjectRepository {
	
	@SuppressWarnings("rawtypes")
	public Transfer(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters) {
		super(test, driver, dataTable,testParameters);
	}

	
	public void createtransferreason(LinkedHashMap<String, String> inputValueMap) {

		String query = null;
		int TRANSFERREASONID=0;
		TRANSFERREASONID = generateRandomNum(100);
		String REASONCODE =  inputValueMap.get("REASON");
		String DESCRIPTION = inputValueMap.get("REASON_DESCRIPTION");
		String TRANSFERTYPE =  inputValueMap.get("REASON_TRANSFERTYPE");
		String APPROVAL_REQUIRED =  inputValueMap.get("REASON_APPROVAL_REQUIRED");
		String DEACTIVATE =  inputValueMap.get("REASON_DEACTIVATE");
		String SPARE =  inputValueMap.get("REASON_SPARE");

		try {
			query ="INSERT INTO "
					+"CATSCUST_TRANSFERREASON"+"("

				+"TRANSFERREASONID,"
				+"REASONCODE,"
				+"DESCRIPTION,"
				+"TRANSFERTYPE,"
				+"APPROVAL_REQUIRED,"
				+"DEACTIVATE,"
				+"SPARE,"
				+"ADDCONTACTID,"
				+"ADDDTTM,"
				+"MODIFIEDCONTACTID,"
				+"MODIFIEDDTTM)"
				+"VALUES"
				+"("
				+TRANSFERREASONID+","
				+"'"+REASONCODE+"',"
				+"'"+DESCRIPTION+"',"
				+"'"+TRANSFERTYPE+"',"
				+"'"+APPROVAL_REQUIRED+"',"
				+"'"+DEACTIVATE+"',"
				+"'"+SPARE+"',"
				+"'"+1+"',"
				+"SYSDATE,"
				+"'"+1+"',"
				+"SYSDATE"+")";

			executeUpdateQuery(query, "TRANSFER REASON: <b>"+REASONCODE+"</b>  is added  into CATSCUST_TRANSFERREASON table");

			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.log(LogStatus.FAIL, "TRANSFER REASON: <b>"+REASONCODE+"</b>  is not added  into CATSCUST_TRANSFERREASON table");
		}	

	}
	public int createBulkTransferRequest(LinkedHashMap<String, String> inputValueMap){

		ResultSet rs;
		Statement stmt;

		String REASONCODE = inputValueMap.get("REASON");

		try {
			stmt = connection.createStatement();
			String query = "SELECT * from CATSCUST_TRANSFERREASON WHERE REASONCODE ="+"'"+REASONCODE+"'";
			rs = stmt.executeQuery(query);
			//if Transfer Reason is present then below code will not run
			if(rs!=null){
			while (rs.next()) {

				createtransferreason(inputValueMap);
				
			}
			}
		}
		catch (SQLException e) {
			test.log(LogStatus.FAIL, e);
		}


		String insertquery = null;	
		String updatequery = null;
		int lastStageId = 0;
		int currentStageId = 0;

		String FROMSUBINVENTORY = ((inputValueMap.get("FROMSUBINVENTORY") == null) ? "NULL" : "'"+inputValueMap.get("FROMSUBINVENTORY") +"'");
		String TOSTATUS = ((inputValueMap.get("TOSTATUS") == null) ? "NULL" : "'"+inputValueMap.get("TOSTATUS") +"'");
		String TOSUBINVENTORY = ((inputValueMap.get("TOSUBINVENTORY") == null) ? "NULL" : "'"+inputValueMap.get("TOSUBINVENTORY") +"'");
		String DESTINATIONLOCATION = ((inputValueMap.get("DESTINATIONLOCATION") == null) ? "NULL" : "'"+inputValueMap.get("DESTINATIONLOCATION") +"'");
		String SERVICEORDER = ((inputValueMap.get("SERVICEORDER") == null) ? "NULL" : "'"+inputValueMap.get("SERVICEORDER") +"'");
		String RECEIVERNAME = ((inputValueMap.get("RECEIVERNAME") == null) ? "NULL" : "'"+inputValueMap.get("RECEIVERNAME") +"'");
		String RECEIVERPHONE = ((inputValueMap.get("RECEIVERPHONE") == null) ? "NULL" : "'"+inputValueMap.get("RECEIVERPHONE") +"'");
		String BEHALFOF = ((inputValueMap.get("BEHALFOF") == null) ? "NULL" : "'"+inputValueMap.get("BEHALFOF") +"'");
		String NEEDBYDATE = ((inputValueMap.get("NEEDBYDATE") == null) ? "NULL" : "'"+inputValueMap.get("NEEDBYDATE") +"'");
		String PARTCODE = ((inputValueMap.get("PARTCODE") == null) ? "NULL" : "'"+inputValueMap.get("PARTCODE") +"'");
		String MFGPARTNUMBER = ((inputValueMap.get("MFGPARTNUMBER") == null) ? "NULL" : "'"+inputValueMap.get("MFGPARTNUMBER") +"'");
		String ASSETCODE = ((inputValueMap.get("ASSETCODE") == null) ? "NULL" : "'"+inputValueMap.get("ASSETCODE") +"'");
		String RMANUMBER = ((inputValueMap.get("RMANUMBER") == null) ? "NULL" : "'"+inputValueMap.get("RMANUMBER") +"'");
		String FAULT_CODE = ((inputValueMap.get("FAULT_CODE") == null) ? "NULL" : "'"+inputValueMap.get("FAULT_CODE") +"'");
		String NOTES = ((inputValueMap.get("NOTES") == null) ? "NULL" : "'"+inputValueMap.get("NOTES") +"'");



		try {

			if(inputValueMap.get("TRANSACTION_TYPE").equalsIgnoreCase("INSERT")){			
				lastStageId = getLastTransactionId("SELECT MAX(STAGEID) AS STAGEID FROM CATSCON_TRANSFERREQ_STG","STAGEID" );
				currentStageId = lastStageId+1;

				insertquery = "INSERT "
						+"INTO CATSCON_TRANSFERREQ_STG"
						+"("
						+"STAGEID,"
						+"PROCESSED,"
						+"GENERATEDREQNUM,"
						+"REFERENCENUMBER,"
						+"FROMLOCATION,"
						+"FROMSTATUS,"
						+"FROMSUBINVENTORY,"
						+"TOLOCATION,"
						+"TOSTATUS,"
						+"TOSUBINVENTORY,"
						+"DESTINATIONLOCATION,"
						+"REASON,"
						+"WO_NUMBER,"
						+"SERVICEORDER,"
						+"RECEIVERNAME,"
						+"RECEIVERPHONE,"
						+"BEHALFOF,"
						+"NEEDBYDATE,"
						+"PARTCODE,"
						+"MFGPARTNUMBER,"
						+"ASSETCODE,"
						+"RMANUMBER,"
						+"FAULT_CODE,"						    
						+"ADDDTTM,"
						+"ADDCONTACTCODE,"
						+"QTYNEEDED,"
						+"NOTES"
						+")"
						+"VALUES"
						+"("
						+currentStageId+","
						+"'"+inputValueMap.get("PROCESSED")+"',"
						+"'"+inputValueMap.get("GENERATEDREQNUM")+"',"
						+"'"+inputValueMap.get("REFERENCENUMBER")+"',"
						+"'"+inputValueMap.get("FROMLOCATION")+"',"
						+"'"+inputValueMap.get("FROMSTATUS")+"',"
						+FROMSUBINVENTORY+","
						+"'"+inputValueMap.get("TOLOCATION")+"',"
						+TOSTATUS+","
						+TOSUBINVENTORY+","
						+DESTINATIONLOCATION+","
						+"'"+inputValueMap.get("REASON")+"',"
						+"'"+inputValueMap.get("WO_NUMBER")+"',"
						+SERVICEORDER+","
						+RECEIVERNAME+","
						+RECEIVERPHONE+","
						+BEHALFOF+","
						+NEEDBYDATE+","
						+PARTCODE+","
						+MFGPARTNUMBER+","
						+ASSETCODE+","
						+RMANUMBER+","
						+FAULT_CODE+","						 
						+inputValueMap.get("ADDDTTM")+","
						+"'"+inputValueMap.get("ADDCONTACTCODE")+"',"
						+inputValueMap.get("QTYNEEDED")+","
						+NOTES+")";

				executeUpdateQuery(insertquery, "Bulk Transfer request with REFERENCENUMBER - <b>"+inputValueMap.get("REFERENCENUMBER")+"</b> is <b>"+inputValueMap.get("TRANSACTION_TYPE")+"ED</b> in CATSCON_TRANSFERREQ_STG table (STAGEID - <b>"+currentStageId+"</b>)");

			}else {
				lastStageId = Integer.parseInt(inputValueMap.get("STAGEID"));
				currentStageId = lastStageId;

				updatequery = "";

				executeUpdateQuery(updatequery, "Bulk Transfer request with REFERENCENUMBER - <b>"+inputValueMap.get("REFERENCENUMBER")+"</b> is <b>"+inputValueMap.get("TRANSACTION_TYPE")+"ED</b> in CATSCON_TRANSFERREQ_STG table (STAGEID - <b>"+currentStageId+"</b>)");

			}				


			connection.commit();			

		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return currentStageId;
	} 
	
	
	

}
