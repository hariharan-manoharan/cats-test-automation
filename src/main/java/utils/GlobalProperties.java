package main.java.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 
 * Java Design pattern followed - Singleton Pattern
 * 
 */
public class GlobalProperties{

	//Create an object of GlobalProperties
	
	private static GlobalProperties instance = new GlobalProperties();

    //Create constructor private so that this class cannot be instantiated
	
	private GlobalProperties(){
		
	}
	
	//Get the only object available	
	
	public static GlobalProperties getInstance(){
		return instance;
	}
	
	//Loads GlobalProperties.properties files and return Properties object
	
	public Properties loadPropertyFile() {
		Properties properties = new Properties();
		InputStream input = null;

		try {
			
			input = new FileInputStream("src/main/resources/PropertyFiles/GlobalProperties.properties");
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

}
