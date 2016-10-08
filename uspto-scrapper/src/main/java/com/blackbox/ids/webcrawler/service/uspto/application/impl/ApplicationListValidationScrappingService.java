package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationListValidationScrappingService;

@Service
public class ApplicationListValidationScrappingService implements
		IApplicationListValidationScrappingService {
	
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * 
	 * Scrapping data to perform search by customer number
	 * @param customer_nos 
	 * @param uiSelectedCustomerNos
	 */
	@Override
	public List<String> scrappingData(WebDriver webDriver){
		List<String> uiSelectedCustomerNos = new ArrayList<String>();
		List<String> customer_nos = customerRepository.findAllCustomersByNumber();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String xPathExpression = null;
		WebElement element = null;
		
		xPathExpression = "//*[@id='selectnewcase']/div[1]/form/div[1]";
		element = webDriver.findElement(By.xpath(xPathExpression));
		element.click();

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		xPathExpression = "//*[@id='ApplicationsbyCustomerNumberforCorrespondenceAddress']/option[1]";
		element = webDriver.findElement(By.xpath(xPathExpression));
		element.click();
		
		xPathExpression = "//*[@id='ApplicationsbyCustomerNumberforCorrespondenceAddress']/option[1]";
		element = webDriver.findElement(By.xpath(xPathExpression));
		element.click();
		
		xPathExpression = "//*[@id='ApplicationsbyCustomerNumberforCorrespondenceAddress']/option[1]";
		element = webDriver.findElement(By.xpath(xPathExpression));
		element.click();
		
	
		WebElement elementSelect;
		xPathExpression = "//*[@id='customernumber_id']";
		elementSelect = webDriver.findElement(By.xpath(xPathExpression));
		
		List<WebElement> elementOption = elementSelect.findElements(By.tagName("option"));
		for(WebElement ele:elementOption){
			String optionValue = ele.getAttribute("value");	
			if(customer_nos.contains(optionValue)){
				uiSelectedCustomerNos.add(optionValue);
				xPathExpression = "//*[@id='div_cust_list_not_all']/table/tbody/tr/td[3]/a";
				element = webDriver.findElement(By.xpath(xPathExpression));
				element.click();
			}	
		}
		
		return uiSelectedCustomerNos;
	}
}
