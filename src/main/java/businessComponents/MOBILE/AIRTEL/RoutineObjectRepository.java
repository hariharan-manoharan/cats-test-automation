package main.java.businessComponents.MOBILE.AIRTEL;

import org.openqa.selenium.By;

public interface RoutineObjectRepository {
	

	// ADD CONNECTION
	
	By NAME_ADD_CONNECTION= By.name("Add Connection");
	By ID_ADD_CONNECTIONS=By.id("connections_add");
	By NAME_TXT_CONNECTION_NAME=By.id("connection_edit_name");
	By ID_TXT_CONNECTION_HOST=By.id("connection_edit_host");
	By ID_TXT_CONNECTION_PORT=By.id("connection_edit_port");
	By ID_TOGGLE_BTN_SSL =By.id("connection_edit_ssl");
	By ID_ICON_SAVE=By.id("connections_save");
	By ID_IMG_BACK_BTN =By.id("action_bar_title");
	
	//LOGIN
	
	By ID_TXT_USERNAME = By.id("username");
	By ID_TXT_PASSWORD = By.id("password");
	By ID_BTN_CONNECT = By.id("btn_connect");
	
	// PROFILES
	
	By NAME_LISTTEXT_ADMIN = By.name("ADMIN");
	
	// ID
	
	By ID_ACTION_BAR_SUBTITLE = By.id("action_bar_subtitle");
	By ID_MESSAGE= By.id("message");
	By ID_ALERT_TITLE= By.id("alertTitle");
	By ID_MESSAGE_CONFIRM_YES = By.id("button1");
	By ID_MESSAGE_CONFIRM_NO = By.id("button2");
	By ID_MESSAGE_OK = By.id("button1");
	By ID_PICKLLIST = By.id("list");
	//android.webkit.WebView[@content-desc='Fulcrum Mobility']/android.widget.EditText[1]
	
	// NAME or CONTENT_DESC
	
	By CONTENT_DESC_ROUITNE_BACK_BTN = By.name("Back");
	
	
	//
	
	By XPATH_LABEL_WEBVIEW = By.xpath("//div[@class='ng-scope']/div[@class='routine-prompt ng-binding']");
	By XPATH_TXT_WEBVIEW = By.xpath("//div[@class='ng-scope']/div[@class='routine-input ng-binding']");
	By XPATH_SEARCH_WEBVIEW = By.xpath("//div[@class='ng-scope']/div[@class='btn-group pull-right ng-scope']");
	
	By XPATH_LABEL = By.xpath("");
	//By XPATH_TXT = By.xpath("//android.webkit.WebView[@content-desc='Fulcrum Mobility']/android.widget.EditText[1]");
	By XPATH_TXT_GET = By.xpath("//android.webkit.WebView[@content-desc='Fulcrum Mobility']/android.widget.EditText[1][contains(@content-desc,'-')]");
	By XPATH_SEARCH = By.xpath("");
	
	
	String XPATH_TXT = ".//android.view.View[@content-desc='%s']";
	String XPATH_TXT_NAME = ".//android.view.View[@name='%s']";
	
	By XPATH_SPYGLASS = By.xpath(".//android.view.View[@content-desc='?']");
	
	
	// TRANSFER REQUEST RELATED QUERIES
	
	String TR_FROM_LOC_QUERY = "SELECT NAME FROM CATS_LOCATION WHERE LOCATIONID IN (SELECT FROMLOCATIONID FROM CATSCUST_REQUISITION WHERE REQNUMBER='%s')";
	String TR_ASSETCODE_QUERY = "SELECT ASSETCODE FROM CATS_ASSET WHERE ASSETID IN (SELECT ASSETID FROM CATSCUST_REQUISITIONLINE WHERE REQUISITIONID IN (SELECT REQUISITIONID FROM CATSCUST_REQUISITION WHERE REQNUMBER='%s'))";
	String TR_PARTCODE_QUERY = "SELECT PARTCODE FROM CATS_PART WHERE PARTID IN (SELECT PARTID FROM CATSCUST_REQUISITIONLINE WHERE LINENUMBER=%s AND REQUISITIONID IN (SELECT REQUISITIONID FROM CATSCUST_REQUISITION WHERE REQNUMBER='%s'))";
	String TR_SHIPMENT_NO_QUERY = "SELECT SHIPMENTNUMBER FROM CATS_SHIPMENT WHERE TRANSFERID IN (SELECT TRANSFERID FROM CATS_TRANSFER WHERE REFERENCENUMBER='%s') ORDER BY SHIPMENTNUMBER DESC";
	String TR_SHIPMENT_COUNT = "SELECT COUNT(*) AS COUNT FROM CATS_SHIPMENT WHERE TRANSFERID IN (SELECT TRANSFERID FROM CATS_TRANSFER WHERE REFERENCENUMBER='%s') ORDER BY SHIPMENTNUMBER DESC";
	String TR_TRANSFER_NO_QUERY = "SELECT TRANSFERNUMBER FROM CATS_TRANSFER WHERE REFERENCENUMBER IN (SELECT REQNUMBER FROM CATSCUST_REQUISITION WHERE REQNUMBER='%s')";
	String TR_LINEITEM_COUNT = "SELECT COUNT(*) AS COUNT FROM CATSCUST_REQUISITIONLINE WHERE REQUISITIONID IN (SELECT REQUISITIONID FROM CATSCUST_REQUISITION WHERE REQNUMBER='%s')";
	String TR_LINEITEM_DETAILS = "SELECT LINENUMBER, PARTID, ASSETID FROM CATSCUST_REQUISITIONLINE WHERE REQUISITIONID IN (SELECT REQUISITIONID FROM CATSCUST_REQUISITION WHERE REQNUMBER='%s')";

	/*String TR_FROM_LOC_QUERY = "SELECT NAME FROM CATS_LOCATION WHERE LOCATIONID IN (SELECT FROMLOCATIONID FROM CATS_TRANSFER WHERE TRANSFERNUMBER='%s')";
	String TR_ASSETCODE_QUERY = "SELECT ASSETCODE FROM CATS_ASSET WHERE ASSETID IN (SELECT ASSETID FROM CATS_TRANSFERLINEDETAILS WHERE TRANSFERLINEID IN (SELECT TRANSFERLINEID FROM CATS_TRANSFERLINE WHERE TRANSFERID IN (SELECT TRANSFERID FROM CATS_TRANSFER WHERE  TRANSFERNUMBER='%s')))";
	String TR_SHIPMENT_NO_QUERY = "SELECT SHIPMENTNUMBER FROM CATS_SHIPMENT WHERE TRANSFERID IN (SELECT TRANSFERID FROM CATS_TRANSFER WHERE TRANSFERNUMBER='%s') ORDER BY SHIPMENTNUMBER DESC";
	*/


}