package main.java.businessComponents.MOBILE.XO;

import org.openqa.selenium.By;

public interface RoutineObjectRepository {


	/*	String TXT_VIEW="%s";
	String BUTTON="%s";
	 */

	//Add Connections
	By TAB_CONNECTIONS= By.name("Add Connection");
	By ICON_ADD=By.id("connections_add");
	By TXT_NAME=By.id("connection_edit_name");
	By TXT_HOST=By.id("connection_edit_host");
	By TXT_PORT=By.id("connection_edit_port");
	By TOGGLE_BTN_SSL =By.id("connection_edit_ssl");
	By ICON_SAVE=By.id("connections_save");
	By IMG_BACK_BTN =By.id("action_bar_title");
	

	//Login Page
	By TXT_USERNAME=By.id("username");
	By TXT_PASSWORD= By.id("password");
	By BTN_CONNECT= By.id("btn_connect");

	//Profile Page
	By TAB_ADMIN= By.name("ADMIN");
	By IMG_HOME=By.id("home");
	
	//InfoPage
	By TXT_CLIENT_VERSION=By.id("info_client_version");
	By TXT_SERVER_VERSION=By.id("info_server_version");
	By TXT_CATS_VERSION=By.id("info_cats_version");
	By BTN_INFO_DONE=By.id("info_done_button");
	
	//Routines Page
	By TAB_INFO= By.name("Info");
	By ID_ACTION_BAR_SUBTITLE = By.id("action_bar_subtitle");
	By ID_MESSAGE_CONFIRM = By.id("message");
	By ID_MESSAGE_CONFIRM_YES = By.id("button1");
	By ID_MESSAGE_CONFIRM_NO = By.id("button2");
	
	By XPATH_LABEL_WEBVIEW = By.xpath("//div[@class='ng-scope']/div[@class='routine-prompt ng-binding']");
	By XPATH_TXT_WEBVIEW = By.xpath("//div[@class='ng-scope']/div[@class='routine-input ng-binding']");
	By XPATH_SEARCH_WEBVIEW = By.xpath("//div[@class='ng-scope']/div[@class='btn-group pull-right ng-scope']");
	
	//Routines Back Button
	By CONTENT_DESC_ROUITNE_BACK_BTN = By.name("Back");
}

