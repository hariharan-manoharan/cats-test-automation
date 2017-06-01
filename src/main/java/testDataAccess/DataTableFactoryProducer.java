package main.java.testDataAccess;

public class DataTableFactoryProducer {
	
	public static DataTableAbstractFactory getDataTableFactory(){
		return new DataTableFactory();
	}

}
