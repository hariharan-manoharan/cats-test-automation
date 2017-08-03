package main.java.businessComponents.MOBILE.AIRTEL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import main.java.executionSetup.TestParameters;
import main.java.testDataAccess.DataTable;
import main.java.utils.Utility;
//import main.java.utils.JMeterFromExistingJMX;


public class SQLLibrary extends Utility {
	
	public Lock lock;
	public Connection connection;

	
	@SuppressWarnings("rawtypes")
	public SQLLibrary(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters, Lock lock, Connection connection) {
		super(test, driver, dataTable, testParameters, lock, connection);
		this.lock = lock;
		this.connection = connection;
	}
	
	@SuppressWarnings("rawtypes")
	public SQLLibrary(ExtentTest test, AndroidDriver driver, DataTable dataTable, TestParameters testParameters, Lock lock, Connection connection, Properties runtimeDataProperties) {
		super(test, driver, dataTable, testParameters, lock, connection, runtimeDataProperties);
		this.lock = lock;
		this.connection = connection;		
	}

	
	//SQL FUNCTIONS
	
	
	
	public void createNewPart(){
		String validateItem = "SELECT * FROM CATSCON_PART_STG WHERE ITEM='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging", testParameters.getCurrentTestCase());			
		int recordId = createNewPart(dataMap);
		validateInboundTransaction("Item", "PROCESS_FLAG", "ERROR_MESSAGE", validateItem, getRuntimeTestdata(testParameters.getCurrentTestCase()+"#ITEMCODE"),recordId);	
	}
	
	public void addManufacturer(){
		String validateMFG = "SELECT * FROM CATSCON_MFG_STG WHERE MANUFACTURER_NAME='%s' AND RECORD_ID=%d";
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging", testParameters.getCurrentTestCase());		
		int recordId = addMfgForItem(dataMap);
		validateInboundTransaction("MFG", "PROCESS_FLAG", "ERROR_MESSAGE", validateMFG, getRuntimeTestdata(testParameters.getCurrentTestCase()+"#MFG"),recordId);
	}
	

	
	public void createBillOfMaterial(){
		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging", testParameters.getCurrentTestCase());
		createBillOfMaterial(dataMap);
	}


	public void createBulkTransferRequest(){
		

		String validateBulkTransferRequest = "SELECT * FROM CATSCON_TRANSFERREQ_STG WHERE REFERENCENUMBER='%s' AND STAGEID=%d";	

		LinkedHashMap<String, String> dataMap = dataTable.getRowData("Bulk_Transfer_Request", testParameters.getCurrentTestCase());		
		int stageId = createBulkTransferRequest(dataMap);
		boolean successFlag = validateInboundTransaction("Bulk Transfer Request", "PROCESSED", "ERRORMESSAGE", validateBulkTransferRequest, dataMap.get("REFERENCENUMBER"),stageId);	
		
		if(successFlag){
			String RequestNumber = selectQuerySingleValue(String.format(validateBulkTransferRequest, dataMap.get("REFERENCENUMBER"), stageId), "GENERATEDREQNUM");		
			String query = "SELECT * FROM CATS_TRANSFER WHERE REFERENCENUMBER ="+"'"+RequestNumber+"'";
			String TRANSFERNO = selectQuerySingleValue(query, "TRANSFERNUMBER");
			addRuntimeTestData("REQUESTNUMBER", RequestNumber);
			addRuntimeTestData("TRANSFERNUMBER", TRANSFERNO);
		}
		
	}	
	
	
	public void deliveryConfirmation() {

		try {

			lock.lock();
			String validateDC = "SELECT * FROM CATSCON_POREC_STG WHERE ITEM_CODE='%s' AND RECORD_ID=%d";
			LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging",
					testParameters.getCurrentTestCase() + "_DC");
			int recordId = deliveryConfirmationQuery(dataMap);
			validateInboundTransaction("Delivery Confirmation :", "PROCESS_FLAG", "ERROR_MESSAGE", validateDC,
					getRuntimeTestdata(dataMap.get("VALUE7")), recordId);
		} finally {
			lock.unlock();
		}
	}


	public void createPurchaseOrder() {

		try {

			lock.lock();
			String validatePO = "SELECT * FROM CATSCON_PO_STG WHERE PHA_PO_NUMBER='%s' AND RECORD_ID=%d";
			LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging",
					testParameters.getCurrentTestCase() + "_PO");
			int recordId = createPurchaseOrderQuery(dataMap);
			validateInboundTransaction("PO", "PROCESS_FLAG", "ERROR_MESSAGE", validatePO,
					getRuntimeTestdata(testParameters.getCurrentTestCase() + "#PONUMBER"), recordId);

		} finally {
			lock.unlock();
		}
	}


	public void createMaterialReceiveReceipt() {

		try {

			lock.lock();
			String validateMRR = "SELECT * FROM CATSCON_MRR_STG WHERE RECEIPT_NUM='%s' AND RECORD_ID=%d";
			LinkedHashMap<String, String> dataMap = dataTable.getRowData("Data_Staging",
					testParameters.getCurrentTestCase() + "_MRR");
			int recordId = createMaterialReceiveReceiptQuery(dataMap);
			validateInboundTransaction("MRR", "PROCESS_FLAG", "ERROR_MESSAGE", validateMRR,
					getRuntimeTestdata(testParameters.getCurrentTestCase() + "#MRRNUMBER"), recordId);
			poTaxUpdateQuery(dataMap);

		} finally {
			lock.unlock();
		}
	}


	public int createPurchaseOrderQuery(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		int RECORD_ID = 0;
		CallableStatement stproc_stmt;  
		try {

			RECORD_ID = generateRandomNum(10000000);

			String purchaseOrder = generateTestData("PONUMBER", inputValueMap.get("VALUE2"));
			String itemcode = getRuntimeTestdata(inputValueMap.get("VALUE18"));


			query="INSERT "
					+"INTO CATS.CATSCON_PO_STG"
					+"("
					+"PHA_OPERATING_UNIT_ID,"
					+"PHA_PO_NUMBER,"
					+"PHA_REVISION_NUM,"
					+"PHA_VENDOR_NUMBER,"
					+"PHA_VENDOR_NAME,"
					+"PHA_VENDOR_SITE_CODE,"
					+"PHA_SHIP_TO_LOCATION_ID,"
					+"PHA_COMMENTS,"
					+"PHA_AUTHORIZATION_STATUS,"
					+"PHA_CANCEL_FLAG,"
					+"PHA_CLOSED_CODE,"
					+"PHA_CURRENCY_CODE,"
					+"PHA_CREATION_DATE,"
					+"PHA_APPROVED_DATE,"
					+"PHA_LAST_UPDATE_DATE,"
					+"PHA_CREATED_BY,"
					+"PLA_LINE_NUM,"
					+"PLA_ITEM_CODE,"
					+"PLA_ITEM_DESCRIPTION,"
					+"PLA_ITEM_CATEGORY,"
					+"PLA_UNIT_MEASURE_CODE,"
					+"PLA_UNIT_PRICE,"
					+"PLA_WARRANTY_START_DATE,"
					+"PLA_WARRANTY_PERIOD,"
					+"PLA_CANCEL_FLAG,"
					+"PLA_CLOSED_CODE,"
					+"PLA_CREATION_DATE,"
					+"PLA_LAST_UPDATE_DATE,"
					+"PLLA_SHIPMENT_NUM,"
					+"PLLA_SHIP_TO_ORGANIZATION_ID,"
					+"PLLA_SHIP_TO_LOCATION_ID,"
					+"PLLA_QUANTITY_ORDERED,"
					+"PLLA_NEED_BY_DATE,"
					+"PLLA_QUANTITY_RECEIVED,"
					+"PLLA_QUANTITY_CANCELLED,"
					+"PLLA_CLOSED_CODE,"
					+"PDA_PO_DISTRIBUTION_ID,"
					+"PDA_DISTRIBUTION_NUM,"
					+"PDA_DESTINATION_TYPE_CODE,"
					+"PDA_CHARGE_ACCOUNT,"
					+"PDA_EX_RATE_DATE,"
					+"RECORD_ID,"
					+"CREATION_DATE,"
					+"PROCESS_FLAG"
					+")"
					+"VALUES"
					+"("
					+"'"+inputValueMap.get("VALUE1")+"',"
					+"'"+purchaseOrder+"',"
					+"'"+inputValueMap.get("VALUE3")+"',"
					+"'"+inputValueMap.get("VALUE4")+"',"
					+"'"+inputValueMap.get("VALUE5")+"',"
					+"'"+inputValueMap.get("VALUE6")+"',"
					+"'"+inputValueMap.get("VALUE7")+"',"
					+"'"+inputValueMap.get("VALUE8")+"',"
					+"'"+inputValueMap.get("VALUE9")+"',"
					+"'"+inputValueMap.get("VALUE10")+"',"
					+"'"+inputValueMap.get("VALUE11")+"',"
					+"'"+inputValueMap.get("VALUE12")+"',"
					+inputValueMap.get("VALUE13")+","
					+inputValueMap.get("VALUE14")+","
					+inputValueMap.get("VALUE15")+","
					+"'"+inputValueMap.get("VALUE16")+"',"
					+"'"+inputValueMap.get("VALUE17")+"',"
					+"'"+itemcode+"',"
					+"'"+inputValueMap.get("VALUE19")+"',"
					+"'"+inputValueMap.get("VALUE20")+"',"
					+"'"+inputValueMap.get("VALUE21")+"',"
					+"'"+inputValueMap.get("VALUE22")+"',"
					+"'"+inputValueMap.get("VALUE23")+"',"
					+"'"+inputValueMap.get("VALUE24")+"',"
					+"'"+inputValueMap.get("VALUE25")+"',"
					+"'"+inputValueMap.get("VALUE26")+"',"
					+inputValueMap.get("VALUE27")+","
					+inputValueMap.get("VALUE28")+","
					+"'"+inputValueMap.get("VALUE29")+"',"
					+"'"+inputValueMap.get("VALUE30")+"',"
					+"'"+inputValueMap.get("VALUE31")+"',"
					+"'"+inputValueMap.get("VALUE32")+"',"
					+inputValueMap.get("VALUE33")+","
					+"'"+inputValueMap.get("VALUE34")+"',"
					+"'"+inputValueMap.get("VALUE35")+"',"
					+"'"+inputValueMap.get("VALUE36")+"',"
					+"'"+inputValueMap.get("VALUE37")+"',"
					+"'"+inputValueMap.get("VALUE38")+"',"
					+"'"+inputValueMap.get("VALUE39")+"',"
					+"'"+inputValueMap.get("VALUE40")+"',"
					+inputValueMap.get("VALUE41")+","
					+RECORD_ID+","
					+inputValueMap.get("VALUE43")+","
					+"'"+inputValueMap.get("VALUE44")+"')";

			//System.out.println(query);
			executeUpdateQuery(query, "PO - <b>"+purchaseOrder+"</b> for Item <b>"+itemcode+"</b> is inserted in to CATSCON_PO_STG table");
			connection.commit();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_PO}");	
			stproc_stmt.executeUpdate();		
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_POINTERFACE.SP_INITPOINTERFACE(?)}");
			stproc_stmt.setString(1, "");
			stproc_stmt.executeUpdate();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_PO_ERPACK}");	
			stproc_stmt.executeUpdate();

			stproc_stmt.close();

		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;
	}

	public int createMaterialReceiveReceiptQuery(LinkedHashMap<String, String> inputValueMap){
		String query = null;
		int RECORD_ID = 0;
		CallableStatement stproc_stmt; 
		try {

			RECORD_ID = generateRandomNum(10000000);

			String mrrNumber = generateTestData("MRRNUMBER", inputValueMap.get("VALUE9"));

			query = "INSERT "
					+"INTO CATSCON_MRR_STG"
					+"("
					+"OPERATING_UNIT,"
					+"TO_ORGANIZATION_ID,"
					+"VENDOR_NUMBER,"
					+"VENDOR_NAME,"
					+"VENDOR_SITE_CODE,"
					+"PO_NUMBER,"
					+"PO_LINE_NUM ,"
					+"PO_SHIPMENT_NUM,"
					+"RECEIPT_NUM,"
					+"SHIPMENT_NUM,"
					+"SHIPPED_DATE,"
					+"TRANSACTION_DATE,"
					+"TRANSACTION_ID,"
					+"SHIPMENT_HEADER_ID,"
					+"SHIPMENT_LINE_ID,"
					+"ITEM_CODE ,"
					+"QUANTITY,"
					+"LOCATION_ID,"
					+"PO_UNIT_PRICE,"
					+"H_ATTRIBUTE10,"
					+"H_ATTRIBUTE11,"
					+"H_ATTRIBUTE14,"
					+"H_ATTRIBUTE6,"
					+"H_ATTRIBUTE7,"
					+"H_ATTRIBUTE8,"
					+"H_ATTRIBUTE9 ,"
					+"L_ATTRIBUTE10,"
					+"L_ATTRIBUTE12,"
					+"L_ATTRIBUTE13,"
					+"DESTINATION_TYPE_CODE,"
					+"MRR_CREATED_BY,"
					+"INTERFACE_NAME,"
					+"RECORD_ID,"
					+"PROCESS_FLAG,"
					+"CREATION_DATE,"
					+"CREATED_BY"
					+")"
					+"VALUES"
					+"("
					+ Integer.parseInt(inputValueMap.get("VALUE1"))+","
					+ Integer.parseInt(inputValueMap.get("VALUE2"))+","
					+"'"+inputValueMap.get("VALUE3")+"',"
					+"'"+inputValueMap.get("VALUE4")+"',"
					+"'"+inputValueMap.get("VALUE5")+"',"
					+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"',"
					+ Integer.parseInt(inputValueMap.get("VALUE7"))+","
					+ Integer.parseInt(inputValueMap.get("VALUE8"))+","
					+"'"+mrrNumber+"',"
					+"'"+inputValueMap.get("VALUE10")+"',"
					+inputValueMap.get("VALUE11")+","
					+inputValueMap.get("VALUE12")+","
					+generateRandomNum(10000000)+","
					+generateRandomNum(10000000)+","
					+ Integer.parseInt(inputValueMap.get("VALUE15"))+","
					+"'"+getRuntimeTestdata(inputValueMap.get("VALUE16"))+"',"
					+ Integer.parseInt(inputValueMap.get("VALUE17"))+","
					+ Integer.parseInt(inputValueMap.get("VALUE18"))+","
					+ Integer.parseInt(inputValueMap.get("VALUE19"))+","
					+"'"+inputValueMap.get("VALUE20")+"',"
					+"'"+inputValueMap.get("VALUE21")+"',"
					+"'"+inputValueMap.get("VALUE22")+"',"
					+"'"+inputValueMap.get("VALUE23")+"',"
					+inputValueMap.get("VALUE24")+","
					+inputValueMap.get("VALUE25")+","
					+inputValueMap.get("VALUE26")+","
					+"'"+inputValueMap.get("VALUE27")+"',"
					+"'"+inputValueMap.get("VALUE28")+"',"
					+"'"+inputValueMap.get("VALUE29")+"',"
					+"'"+inputValueMap.get("VALUE30")+"',"
					+inputValueMap.get("VALUE31")+","
					+"'"+inputValueMap.get("VALUE32")+"',"
					+RECORD_ID+","
					+"'"+inputValueMap.get("VALUE34")+"',"
					+inputValueMap.get("VALUE35")+","
					+ Integer.parseInt(inputValueMap.get("VALUE36"))+")";

			//System.out.println(query);

			executeUpdateQuery(query, "MRR - <b>"+mrrNumber+"</b> is created for PO - <b>"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"</b>");
			connection.commit();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_MRR}");	
			stproc_stmt.executeUpdate();		
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_MRRINTERFACE.SP_INITMRRINTERFACE(?)}");
			stproc_stmt.setString(1, "");
			stproc_stmt.executeUpdate();
			stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_MRR_ERPACK}");	
			stproc_stmt.executeUpdate();

			stproc_stmt.close();		


		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return RECORD_ID;
	}



	@SuppressWarnings("resource")
	public int deliveryConfirmationQuery(LinkedHashMap<String, String> inputValueMap) {

		String query = null;
		String SERIALIZED;
		String TRANSACTIONID;
		String ASSETCODE;	
		int RECORD_ID = 0;
		ResultSet rs;
		Statement stmt;
		CallableStatement stproc_stmt;


		try {
			String	query1 =  "SELECT * FROM CATS_PART WHERE PARTCODE =" +"'"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"'";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query1);
			while (rs.next()) {
				SERIALIZED = rs.getString("SERIALIZED");

				if (SERIALIZED.equalsIgnoreCase("N")){

					String query2 = "SELECT MAX(PARTTRANSACTIONID) AS PARTTRANSACTIONID FROM CATS_PARTTRANSACTION WHERE ORIGINATOR ="+"'CATS_POTRANSACTION'"
							+"AND PARTCODE= "+"'"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"' AND LOTNUMBER="+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase() + "#MRRNUMBER")+"'";
					stmt = connection.createStatement();

					rs = stmt.executeQuery(query2);	
					while (rs.next()) {
						TRANSACTIONID = rs.getString("PARTTRANSACTIONID");	
						RECORD_ID = generateRandomNum(10000000);
						query = "INSERT INTO "
								+"CATS.CATSCON_POREC_STG"
								+"("
								+"CATS_RCPT_LINE_DLVR_TRX_ID,"
								+"PO_RCPT_LINE_DLVR_ID,"
								+"LOT_NUMBER,"
								+"RECORD_ID,"
								+"CREATION_DATE,"
								+"PROCESS_FLAG,"
								+"ITEM_CODE,"
								+"CREATED_BY"
								+")"
								+
								"VALUES"
								+ "("
								+"'"+TRANSACTIONID+"',"
								+"'"+inputValueMap.get("VALUE2")+"',"
								+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#MRRNUMBER")+"',"
								+"'"+RECORD_ID+"',"
								+inputValueMap.get("VALUE5")+","
								+"'"+inputValueMap.get("VALUE6")+"',"
								+"'"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"',"
								+selectQuerySingleValue("SELECT * FROM CATS_CONTACT_UDFDATA WHERE CONTACTID=1", "NUMBER3")
								+")";
						executeUpdateQuery(query, "Delivery Confirmation  - <b>"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"</b>");
						connection.commit();
						stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_POREC}");	
						stproc_stmt.executeUpdate();		
						stproc_stmt = connection.prepareCall ("{call CATSCON_P_POCONFINTERFACE.SP_INITPOCONFINTERFACE(?)}");
						stproc_stmt.setString(1, "");
						stproc_stmt.executeUpdate();
						stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_POREC_ERPACK}");	
						stproc_stmt.executeUpdate();

						stproc_stmt.close();		

					}

				}else{
					String query3 = "SELECT * FROM CATS_ASSETTRANSACTION WHERE ASSETTRANSACTIONID IN (select MAX(ASSETTRANSACTIONID) AS ASSETTRANSACTIONID  FROM CATS_ASSETTRANSACTION WHERE ORIGINATOR ="+"'CATS_POTRANSACTION'"
							+"AND PARTCODE= "+"'"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"' AND LOTNUMBER="+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase() + "#MRRNUMBER")+"')";
					stmt = connection.createStatement();
					rs = stmt.executeQuery(query3);	
					while (rs.next()) {
						TRANSACTIONID = rs.getString("ASSETTRANSACTIONID");	
						ASSETCODE = rs.getString("ASSETCODE");				
						addRuntimeTestData(testParameters.getCurrentKeywordColumnName(), ASSETCODE);				
						RECORD_ID = generateRandomNum(10000000);
						query = "INSERT INTO "
								+"CATS.CATSCON_POREC_STG"
								+"("
								+"CATS_RCPT_LINE_DLVR_TRX_ID,"
								+"PO_RCPT_LINE_DLVR_ID,"
								+"LOT_NUMBER,"
								+"RECORD_ID,"
								+"CREATION_DATE,"
								+"PROCESS_FLAG,"
								+"ITEM_CODE,"
								+"CREATED_BY"
								+")"
								+
								"VALUES"
								+ "("
								+"'"+TRANSACTIONID+"',"
								+"'"+inputValueMap.get("VALUE2")+"',"
								+"'"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#MRRNUMBER")+"',"
								+"'"+RECORD_ID+"',"
								+inputValueMap.get("VALUE5")+","
								+"'"+inputValueMap.get("VALUE6")+"',"
								+"'"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"',"
								+selectQuerySingleValue("SELECT * FROM CATS_CONTACT_UDFDATA WHERE CONTACTID=1", "NUMBER3")
								+")";
						connection.commit();
						executeUpdateQuery(query, "Delivery Confirmation ITEMCODE : - <b>"+getRuntimeTestdata(inputValueMap.get("VALUE7"))+"</b> with Assetcode : <b>" + ASSETCODE +"</b>");
						stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_STG_INT_POREC}");	
						stproc_stmt.executeUpdate();		
						stproc_stmt = connection.prepareCall ("{call CATSCON_P_POCONFINTERFACE.SP_INITPOCONFINTERFACE(?)}");
						stproc_stmt.setString(1, "");
						stproc_stmt.executeUpdate();
						stproc_stmt = connection.prepareCall ("{call CATSCON_P_ERPINBOUND.SP_POREC_ERPACK}");	
						stproc_stmt.executeUpdate();

						stproc_stmt.close();		
					}

				}

			}
		}catch (SQLException e) {	
			test.log(LogStatus.FAIL, "Delivery Confirmation   - "+getRuntimeTestdata(inputValueMap.get("VALUE7"))+" is not done successfully");
			e.printStackTrace();			
		}
		return RECORD_ID;
	}


	public void poTaxUpdateQuery(LinkedHashMap<String, String> inputValueMap){
		String query = null;		
		try {			
			query = "UPDATE CATSCUST_MRR "
					+"SET TAX_UPDATE = 'Y'"
					+"WHERE MRRID IN (SELECT MRRID FROM CATSCUST_MRR WHERE POCODE='"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"')";
			executeUpdateQuery(query, "Tax Update for PO - <b>"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"</b>");
			connection.commit();			

		} catch (SQLException e) {	
			test.log(LogStatus.FAIL, "Tax Update for PO - <b>"+getRuntimeTestdata(testParameters.getCurrentTestCase()+"#PONUMBER")+"</b>");
			e.printStackTrace();			
		}
	}

	public void deliveryinfocomplete( String Complete, String TCID){


		if(Complete.equalsIgnoreCase("Yes")){

			deliveryinfocomplete(TCID);
		}

	}

	public void deliveryinfocomplete(String TCID){

		String SHIPMENTID;
		String query;
		String query1;

		String Shipmentnumber= null;
		
		if(properties.getProperty("ExecutionMode").equalsIgnoreCase("DISTRIBUTED")) {
		Shipmentnumber =distributedRuntimeDataProperties.getProperty(TCID);
		}else {
		Shipmentnumber =parallelRuntimeDataProperties.getProperty(TCID);	
		}

		try{
			query = "select * FROM CATS_SHIPMENT where SHIPMENTNUMBER ="+"'"+Shipmentnumber+"'";

			SHIPMENTID = selectQuerySingleValue(query, "SHIPMENTID");

			query1 = "UPDATE  CATSCUST_V_SHIPMENT   SET  VEHICLENUMBER = 'TN47AW4752', COMPLETE = 'Y' WHERE SHIPMENTID ="+"'"+SHIPMENTID+"'";

			executeUpdateQuery(query1, "Shipment  # <b>"+Shipmentnumber+"</b> with Delivery info completed is updated in CATSCUST_V_SHIPMENT");	

			connection.commit();



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.log(LogStatus.FAIL, "SHIPMENT # <b>"+Shipmentnumber+"</b> is not delivery info completed");
		}


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
		CallableStatement stproc_stmt;  
		String REASONCODE = inputValueMap.get("REASON");

		try {
			
			stmt = connection.createStatement();
			String query = "SELECT * from CATSCUST_TRANSFERREASON WHERE REASONCODE ="+"'"+REASONCODE+"'";
			rs = stmt.executeQuery(query);
			//if Transfer Reason is present then below code will not run

			while (!rs.isBeforeFirst()) {

				createtransferreason(inputValueMap);
				
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
		String PARTCODE = ((inputValueMap.get("PARTCODE") == null) ? "NULL" : inputValueMap.get("PARTCODE") );
		String MFGPARTNUMBER = ((inputValueMap.get("MFGPARTNUMBER") == null) ? "NULL" : "'"+inputValueMap.get("MFGPARTNUMBER") +"'");
		String ASSETCODE = ((inputValueMap.get("ASSETCODE") == null) ? "NULL" : "'"+inputValueMap.get("ASSETCODE") +"'");
		String RMANUMBER = ((inputValueMap.get("RMANUMBER") == null) ? "NULL" : "'"+inputValueMap.get("RMANUMBER") +"'");
		String FAULT_CODE = ((inputValueMap.get("FAULT_CODE") == null) ? "NULL" : "'"+inputValueMap.get("FAULT_CODE") +"'");
		String NOTES = ((inputValueMap.get("NOTES") == null) ? "NULL" : "'"+inputValueMap.get("NOTES") +"'");
		
		String WO_NUMBER = ((inputValueMap.get("NOTES") == null) ? "NULL" : "'"+inputValueMap.get("WO_NUMBER") +"'");
		
		
		PARTCODE =  "'"+getRuntimeTestdata(PARTCODE)+"'";


		try {
			lock.lock();
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
						+WO_NUMBER+","
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
				stproc_stmt = connection.prepareCall ("{call CATSCON_P_TRANSFERIMPORT.SP_PROCESS_TRANSFERS}");	
				stproc_stmt.executeUpdate();	
				
			}else {
				lastStageId = Integer.parseInt(inputValueMap.get("STAGEID"));
				currentStageId = lastStageId;

				updatequery = "";

				executeUpdateQuery(updatequery, "Bulk Transfer request with REFERENCENUMBER - <b>"+inputValueMap.get("REFERENCENUMBER")+"</b> is <b>"+inputValueMap.get("TRANSACTION_TYPE")+"ED</b> in CATSCON_TRANSFERREQ_STG table (STAGEID - <b>"+currentStageId+"</b>)");

			}				


			connection.commit();			

		} catch (SQLException e) {			
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return currentStageId;
	} 
	
	
	public void createLocatorRuleset() {
		
	LinkedHashMap<String, String> inputValueMap = dataTable.getRowData("Data_Staging", testParameters.getCurrentTestCase()+"_RULESET");		
		
	String query = null;	

	try {
		
		int lastRuleSetId = getLastTransactionId("SELECT MAX(LOCATORRULESETID) AS LOCATORRULESETID FROM CATS_LOCATORRULESET","LOCATORRULESETID" );
		int currentRuleSetId = lastRuleSetId+1;	
		
		String locatorRuleSetName = inputValueMap.get("VALUE1")+getCurrentFormattedTime("ddMMhhmmssSSS");
	
		query = "INSERT "
				+"INTO CATS_LOCATORRULESET"
				  +"("
				   +" LOCATORRULESETID,"
				    +"NAME,"
				    +"DESCRIPTION,"
				    +"SEGMENT1_NAME,"
				    +"SEGMENT1_DISPLAY,"
				    +"SEGMENT2_NAME,"
				    +"SEGMENT2_DISPLAY,"
				    +"SEGMENT3_NAME,"
				    +"SEGMENT3_DISPLAY,"    
				    +"ADDCONTACTID,"
				    +"ADDDTTM,"
				    +"MODIFIEDCONTACTID,"
				    +"MODIFIEDDTTM"
				  +")"
				  +"VALUES"
				  +"("
				    +currentRuleSetId+","
				    +"'"+locatorRuleSetName+"',"
				    +"'"+inputValueMap.get("VALUE2")+"',"
				    +"'"+inputValueMap.get("VALUE3")+"',"
				    +"'"+inputValueMap.get("VALUE4")+"',"
				    +"'"+inputValueMap.get("VALUE5")+"',"
				    +"'"+inputValueMap.get("VALUE6")+"',"
				    +"'"+inputValueMap.get("VALUE7")+"',"
				    +"'"+inputValueMap.get("VALUE8")+"',"
				    +inputValueMap.get("VALUE9")+","
				    +inputValueMap.get("VALUE10")+","
				    +inputValueMap.get("VALUE11")+","
				    +inputValueMap.get("VALUE12")+")";		
			
		executeUpdateQuery(query, "Locator Ruleset with Name <b>"+locatorRuleSetName+ "</b> is inserted into table CATS_LOCATORRULESET (<b>LOCATORRULESETID - "+currentRuleSetId+"</b>).");		
		connection.commit();	
		
		addRuntimeTestData("LOCATORRULESETID", String.valueOf(currentRuleSetId));
		addRuntimeTestData("LOCATORRULESET_NAME", String.valueOf(locatorRuleSetName));	
		
		createLocatorRule(currentRuleSetId);
		
	} catch (SQLException e) {		
		e.printStackTrace();
	}

}
	
	
	public void createLocatorRule(int locatorRulsetID) {
		
	LinkedHashMap<String, String> inputValueMap	  = dataTable.getRowData("Data_Staging", testParameters.getCurrentTestCase()+"_RULE");	
		
	String query = null;	
	
	try {
		lock.lock();
		
		
		int lastRuleId = getLastTransactionId("SELECT MAX(LOCATORRULEID) AS LOCATORRULEID FROM CATS_LOCATORRULE","LOCATORRULEID" );
		int currentRuleId = lastRuleId+1;		
		
		
		String locatorRuleName = inputValueMap.get("VALUE1")+getCurrentFormattedTime("ddMMhhmmssSSS");
	
		query = "INSERT "
				+"INTO CATS_LOCATORRULE"
				+"("
				+"LOCATORRULEID,"
				+"LOCATORRULESETID,"
				+"NAME,"
				+"DESCRIPTION,"
				+"SEGMENT1_CODECOMPONENT,"
				+"SEGMENT1_LOWER,"
				+"SEGMENT1_UPPER,"
				+"SEGMENT2_CODECOMPONENT,"
				+"SEGMENT2_LOWER,"
				+"SEGMENT2_UPPER,"
				+"SEGMENT3_CODECOMPONENT,"
				+"SEGMENT3_LOWER,"
				+"SEGMENT3_UPPER,"
				+"ADDCONTACTID,"
				+"ADDDTTM,"
				+"MODIFIEDCONTACTID,"
				+"MODIFIEDDTTM,"
				+"REVISION"
				+")"
				+"VALUES"
				+"("
				+currentRuleId+","
				+locatorRulsetID+","
				+"'"+locatorRuleName+"',"
				+"'"+inputValueMap.get("VALUE2")+"',"
    			+"'"+inputValueMap.get("VALUE3")+"',"
    			+"'"+inputValueMap.get("VALUE4")+"',"
    			+"'"+inputValueMap.get("VALUE5")+"',"
    			+"'"+inputValueMap.get("VALUE6")+"',"
    			+"'"+inputValueMap.get("VALUE7")+"',"
    			+"'"+inputValueMap.get("VALUE8")+"',"
    			+"'"+inputValueMap.get("VALUE9")+"',"
    			+"'"+inputValueMap.get("VALUE10")+"',"
    			+"'"+inputValueMap.get("VALUE11")+"',"
    			+inputValueMap.get("VALUE12")+","
    			+inputValueMap.get("VALUE13")+","
    			+inputValueMap.get("VALUE14")+","
    			+inputValueMap.get("VALUE15")+","
    			+inputValueMap.get("VALUE16")+")";			
			
		executeUpdateQuery(query, "Locator Rule with Name <b>"+locatorRuleName+ "</b> is inserted into table CATS_LOCATORRULE (<b>LOCATORRULEID - "+currentRuleId+"</b>).");		
		connection.commit();	
		
		addRuntimeTestData("LOCATORRULEID", String.valueOf(currentRuleId));	
		addRuntimeTestData("LOCATORRULE_NAME", String.valueOf(locatorRuleName));	
			
		
		
	} catch (SQLException e) {		
		e.printStackTrace();
	}finally{
		lock.unlock();
	}

}
	
	public void updateLocatorRuleSet(String locationName, String locatorRuleSetID){
		
		String query = null;	
		
		try {
			lock.lock();
			
			if(locatorRuleSetID.contains("#")){
				locatorRuleSetID = getRuntimeTestdata(locatorRuleSetID);
			}
			
			query = "UPDATE CATS_LOCATION SET LOCATORRULESETID="+locatorRuleSetID+" WHERE NAME='"+locationName+"'";
			
			executeUpdateQuery(query, "Locator Ruleset with LOCATORRULESETID <b>"+locatorRuleSetID+ "</b> is linked to Location <b>NAME - "+locationName+"</b>.");		
			connection.commit();	
			
		}catch (SQLException e) {		
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		
	}


}