package com.blackbox.ids.abbyy.client;

public interface IOCRClient {

	/**
	 * Perform ocr on the input PDF passed.
	 *
	 * @param inputPdfPath the input pdf path
	 */
	void performOCR(String inputPdfPath);
}
