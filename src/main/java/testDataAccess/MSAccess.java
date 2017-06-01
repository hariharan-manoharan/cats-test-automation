package main.java.testDataAccess;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.executionSetup.TestParameters;


public class MSAccess implements DataTable{

	
	public MSAccess(String dataTablePath, TestParameters testParameters){
		
	}
	
	public MSAccess(String dataTablePath){
		
	}
	
	@Override
	public String getData(String tableName, String columnName) {
		
		return null;
	}

	@Override
	public LinkedHashMap<String, String> getRowData(String tableName) {
		
		return null;
	}

	@Override
	public String writeData(String arg1, String arg2) {
		
		return null;
	}

	@Override
	public ArrayList<TestParameters> getRunManagerInfo() {
		
		return null;
	}

	@Override
	public void setCurrentRow(String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedHashMap<String, String> getRowData(String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}




}
