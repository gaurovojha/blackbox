package com.blackbox.ids.abbyy.client.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.blackbox.ids.abbyy.api.resource.AbbyyResource;
import com.blackbox.ids.abbyy.api.response.Document;
import com.blackbox.ids.abbyy.client.IOCRClient;

/**
 * The Class OCRClientImpl is an implementation class for {@link IOCRClient} which fetches the OCR'd data 
 * and routes the processing of data to further services. 
 */
@Service
public class OCRClientImpl implements IOCRClient {
	
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(OCRClientImpl.class);
	
	/** The abbyy resource. */
	@Autowired
	private AbbyyResource abbyyResource;
	
	
	/* Added just for testing purpose . To be removed after final development */
	public static void main(String[] args) {
		
		Map<String,String> test = new HashMap<String,String>();
		test.put("dsd", null);
		
		System.out.println(test.get(null));
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/abbyy-client.xml");
		AbbyyResource resource = context.getBean(AbbyyResource.class);

		try {
		Document document = resource.extract("nnn");
		LOGGER.info("Data Extracted from document is " + document);
		} catch(InternalServerErrorException ex) {
			String errorMessage = MessageFormat.format("Exception occured while fetching response from OCR server. Exception is {0}", ex.getMessage());
			LOGGER.error(errorMessage);
		}
	}
	
	@Override
	public void performOCR(String inputPdfPath) {
		try {
			Document document = abbyyResource.extract("nnn");
			System.out.println("Output...");
			
			//TODO
			// Route to specific or generic service for processing extracted data for template.
			} catch(InternalServerErrorException ex) {
				String errorMessage = MessageFormat.format("Exception occured while fetching response from OCR server. Exception is {0}", ex.getMessage());
				LOGGER.error(errorMessage);
			}
	}

}
