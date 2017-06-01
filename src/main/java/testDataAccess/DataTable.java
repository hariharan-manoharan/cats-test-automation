package main.java.testDataAccess;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import main.java.executionSetup.TestParameters;



public interface DataTable {	

	ArrayList<TestParameters> getRunManagerInfo();
	
	String getData(String workSheetName, String columnName);
	
	String writeData(String workSheetName, String columnName);
	
	LinkedHashMap<String, String> getRowData(String workSheetName);
	
	LinkedHashMap<String, String> getRowData(String workSheetName, String testcaseName);
	
	void setCurrentRow(String arg1);

}
