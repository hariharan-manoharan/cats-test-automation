package main.java.testDataAccess;

import main.java.executionSetup.TestParameters;

public abstract class DataTableAbstractFactory {
	
	public abstract DataTable getTestDataTableAccess(String testDataTableType, String testDataTablePath, TestParameters testParameters);

	public abstract DataTable getTestDataTableAccess(String testDataTableType, String testDataTablePath) ;

}
