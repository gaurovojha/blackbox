package com.blackbox.ids.services.ocr.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.ocr.IOCRClient;

/**
 * The Class OCRClientImpl is an implementation class for {@link IOCRClient}
 * which fetches the OCR'd data
 * 
 */
@Service
public class OCRClientImpl implements IOCRClient {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(OCRClientImpl.class);

}
