package main.java.utils;

import java.io.File;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumServerHandler extends Utility implements AppiumServer {

	public AppiumDriverLocalService service;
	public int port;
	public String bootstrapPort;
	
	public AppiumServerHandler(int port, String bootstrapPort){
		this.port = port;
		this.bootstrapPort = bootstrapPort;	
	}
	
	
	@Override
	public void appiumServerStart() {

		service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File(properties.getProperty("DriverExecutable")))
				.withAppiumJS(new File(properties.getProperty("AppiumJS")))
				.withIPAddress(properties.getProperty("IPAddress"))
				.usingPort(port)
				.withLogFile(new File("./AppiumServer.log"))
				.withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER,bootstrapPort));

		service.start();
		HardDelay(5000L);

		if (service.isRunning()) {
			System.out.println("Appium Server started successfully!!!");
		}

	}

	@Override
	public void appiumServerStop() {

		service.stop();
		
		if (!service.isRunning()) {
			System.out.println("Appium Server stopped successfully!!!");
		}
	}

}
