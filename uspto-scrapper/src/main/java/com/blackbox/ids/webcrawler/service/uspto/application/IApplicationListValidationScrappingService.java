package com.blackbox.ids.webcrawler.service.uspto.application;

import java.util.List;

import org.openqa.selenium.WebDriver;

public interface IApplicationListValidationScrappingService {

	/**
	 * 
	 * To scrap data and get required information
	 * @return uiSelectedApplicationNumbers list of application numbers visible on uspto page
	 */
	 List<String> scrappingData(WebDriver webDriver);
}
