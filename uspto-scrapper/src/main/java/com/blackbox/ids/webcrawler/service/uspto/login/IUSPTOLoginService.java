package com.blackbox.ids.webcrawler.service.uspto.login;

import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.model.mstr.Customer;

public interface IUSPTOLoginService {

	void login(WebDriver webDriver , String epfFilePath, String password);

	boolean login(WebDriver webDriver, String customerNumber);

	boolean login(WebDriver webDriver, Customer customer);
}
