package main.java.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/*
 * 
 * Java Design pattern followed - Singleton Pattern
 * 
 */
public class GlobalRuntimeDataProperties{

	//Create an object of GlobalProperties
	
	private static GlobalRuntimeDataProperties instance = new GlobalRuntimeDataProperties();

    //Create constructor private so that this class cannot be instantiated
	
	private GlobalRuntimeDataProperties(){
		
	}
	
	//Get the only object available	
	
	public static GlobalRuntimeDataProperties getInstance(){
		return instance;
	}
	
	//Loads GlobalProperties.properties files and return Properties object
	
	public Properties loadPropertyFile() {
		Properties properties = new Properties();
		InputStream input = null;

		try {
			
			input = new FileInputStream("./src/main/resources/PropertyFiles/GlobalRuntimeDataProperties.properties");
			properties.load(input);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return properties;

	}
	
	
	public void writeGlobalRuntimeDataProperties(Properties globalRuntimeDataProperties){
		OutputStream output = null;
		
		try {

			output = new FileOutputStream("./src/main/resources/PropertyFiles/GlobalRuntimeDataProperties.properties");	

			// save properties to project root folder
			globalRuntimeDataProperties.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

	}

}
}
