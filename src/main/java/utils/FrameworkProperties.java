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
public class FrameworkProperties{

	//Create an object of GlobalProperties
	
	private static FrameworkProperties instance = new FrameworkProperties();

    //Create constructor private so that this class cannot be instantiated
	
	private FrameworkProperties(){
		
	}
	
	//Get the only object available	
	
	public static FrameworkProperties getInstance(){
		return instance;
	}
	
	//Loads GlobalProperties.properties files and return Properties object
	
	public Properties loadPropertyFile(String propertyFilePath) {
		Properties properties = new Properties();
		InputStream input = null;

		try {
			
			input = new FileInputStream(propertyFilePath);
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
	
	
	public void writeGlobalRuntimeDataProperties(String propertyFilePath, Properties globalRuntimeDataProperties){
		OutputStream output = null;
		
		try {

			output = new FileOutputStream(propertyFilePath);	

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
