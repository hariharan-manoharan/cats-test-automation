package main.java.executionSetup;

public class TestParameters implements Comparable {

	private String executionMode;
	private String currentTestCase;
	private String testRailTestcaseID;
	private String description;
	private String setCategory;
	private String connectDB;
	private String executeCurrentTestCase;
	private String port;
	private String bootstrapPort;
	private String deviceName;
	private String udid;
	private String BROWSER_NAME;
	private String VERSION;
	private String app;
	private String platformName;
	private String appPackage;
	private String appActivity;
	private String currentKeywordColumnName;


	public String getExecutionMode() {
		return executionMode;
	}

	public void setExecutionMode(String executionMode) {
		this.executionMode = executionMode;
	}

	public String getCurrentTestCase() {
		return currentTestCase;
	}

	public void setCurrentTestCase(String currentTestCase) {
		this.currentTestCase = currentTestCase;
	}
	
	public String getTestRailTestcaseID() {
		return testRailTestcaseID;
	}

	public void setTestRailTestcaseID(String testRailTestcaseID) {
		this.testRailTestcaseID = testRailTestcaseID;
	}

	public String getExecuteCurrentTestCase() {
		return executeCurrentTestCase;
	}

	public void setExecuteCurrentTestCase(String executeCurrentTestCase) {
		this.executeCurrentTestCase = executeCurrentTestCase;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getConnectDB() {
		return connectDB;
	}

	public void setConnectDB(String connectDB) {
		this.connectDB = connectDB;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getBootstrapPort() {
		return bootstrapPort;
	}

	public void setBootstrapPort(String bootstrapPort) {
		this.bootstrapPort = bootstrapPort;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getBROWSER_NAME() {
		return BROWSER_NAME;
	}

	public void setBROWSER_NAME(String bROWSER_NAME) {
		BROWSER_NAME = bROWSER_NAME;
	}

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppActivity() {
		return appActivity;
	}

	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}
	
	
	public String getCurrentKeywordColumnName() {
		return currentKeywordColumnName;
	}

	public void setCurrentKeywordColumnName(String currentKeywordColumnName) {
		this.currentKeywordColumnName = currentKeywordColumnName;
	}
	
	
	public String getSetCategory() {
		return setCategory;
	}

	public void setSetCategory(String setCategory) {
		this.setCategory = setCategory;
	}
	




	@Override
	public int compareTo(Object testParameters) {
		int compareSetCategory=Integer.parseInt(((TestParameters)testParameters).getSetCategory());       
        return Integer.parseInt(this.setCategory)-compareSetCategory;
	}


}
