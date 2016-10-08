package com.blackbox.ids.abbyy.services.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abbyy.FCEngine.IDocument;
import com.abbyy.FCEngine.IDocumentDefinition;
import com.abbyy.FCEngine.IField;
import com.abbyy.FCEngine.IFields;
import com.abbyy.FCEngine.IFlexiCaptureProcessor;
import com.blackbox.ids.abbyy.api.response.Document;
import com.blackbox.ids.abbyy.services.IDataExtractionService;

/**
 * The Class DataExtraction892TemplateService is the implementation class for {@link IDataExtractionService} which does the OCR and returns the response object.
 */
@Service("dataExtraction892TemplateService")
public class DataExtraction892TemplateService implements IDataExtractionService {

	private static final Logger LOGGER = Logger.getLogger(DataExtraction892TemplateService.class);
	
	@Override
	public Document extractAndPrepareData(IFlexiCaptureProcessor flexiCaptureProcessor) {
		IDocument pdfDocument = flexiCaptureProcessor.RecognizeNextDocument();
		System.out.println("Num Pages:: " + pdfDocument.getPages().getCount());
		
		for (int j = 0; j < pdfDocument.getPages().getCount(); j++) {

			System.out.println("************ j is " + j);

			IDocumentDefinition definition = pdfDocument.getDocumentDefinition();
			System.out.println(pdfDocument.getPages().getCount());
			assert(definition != null);
			assert(pdfDocument.getPages().getCount() == 1);

			LOGGER.trace("====================================");
			LOGGER.trace("DocumentType: " + pdfDocument.getDocumentDefinition().getName());
			LOGGER.trace("====================================");
			
			IFields fields = pdfDocument.getSections().Item(0).getChildren();
			for (int i = 0; i < fields.getCount(); i++) {
				IField field = fields.getElement(i);
				LOGGER.trace(
						field.getName() + ": " + (field.getValue() != null ? field.getValue().getAsString() : "."));

			}
			LOGGER.trace("====================================");
		}

		return null;
	}
}
