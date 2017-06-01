package main.java.testDataAccess;

import main.java.executionSetup.TestParameters;

public class DataTableFactory extends DataTableAbstractFactory {

	@Override
	public DataTable getTestDataTableAccess(String testDataTableType, String testDataTablePath, TestParameters testParameters) {

		if (testDataTableType == null) {
			return null;
		}

		if (testDataTableType.equalsIgnoreCase("MSExcel")) {
			return new MSExcel(testDataTablePath, testParameters);

		} else if (testDataTableType.equalsIgnoreCase("MSAccess")) {
			return new MSAccess(testDataTablePath, testParameters);

		}

		return null;
	}
	
	
	@Override
	public DataTable getTestDataTableAccess(String testDataTableType, String testDataTablePath) {

		if (testDataTableType == null) {
			return null;
		}

		if (testDataTableType.equalsIgnoreCase("MSExcel")) {
			return new MSExcel(testDataTablePath);

		} else if (testDataTableType.equalsIgnoreCase("MSAccess")) {
			return new MSAccess(testDataTablePath);

		}

		return null;
	}

}
