package main.java.businessComponents.WEBAPP;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class FunctionalComponents {
	
	private ExtentTest test;
	
	public FunctionalComponents(ExtentTest test){
		this.test = test;
	}

	public void methodOne() {
		System.out.println("methodOne");
		test.log(LogStatus.PASS, "methodOne");
	}

	public void methodTwo() {
		System.out.println("methodTwo");
		test.log(LogStatus.FAIL, "methodTwo");
	}

	public void methodThree() {
		System.out.println("methodThree");
		test.log(LogStatus.PASS, "methodThree");
	}

	public void methodFour() {
		System.out.println("methodFour");
		test.log(LogStatus.PASS, "methodFour");
	}

	public void methodFive() {
		System.out.println("methodFive");
		test.log(LogStatus.PASS, "methodFive");
	}

}
