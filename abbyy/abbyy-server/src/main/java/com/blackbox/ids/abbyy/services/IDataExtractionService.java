package com.blackbox.ids.abbyy.services;

import com.abbyy.FCEngine.IFlexiCaptureProcessor;
import com.blackbox.ids.abbyy.api.response.Document;

/**
 * The Interface IDataExtractionService is the common interface for all templates 
 * for extracting data using Abbyy and preparing the response object. 
 */
public interface IDataExtractionService {

	
	/**
	 * Extract and prepare data.
	 *
	 * @param flexiCaptureProcessor the flexi capture engine
	 * @return the document
	 */
	Document extractAndPrepareData(IFlexiCaptureProcessor flexiCaptureProcessor);
}
