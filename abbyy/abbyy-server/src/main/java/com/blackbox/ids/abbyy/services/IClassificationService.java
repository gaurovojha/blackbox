package com.blackbox.ids.abbyy.services;

import com.abbyy.FCEngine.IFlexiCaptureProcessor;

public interface IClassificationService {

	/**
	 * This API classifies and fetches the template type.
	 *
	 * @param inputPDFPath the input pdf path
	 * @param flexiCaptureProcessor the flexi capture processor
	 * @return the template type
	 */
	String getTemplateType(String inputPDFPath, IFlexiCaptureProcessor flexiCaptureProcessor);
}
