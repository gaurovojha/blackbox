package com.blackbox.ids.abbyy.services.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abbyy.FCEngine.IClassificationResult;
import com.abbyy.FCEngine.IFlexiCaptureProcessor;
import com.abbyy.FCEngine.IStringsCollection;
import com.abbyy.FCEngine.PageTypeEnum;
import com.blackbox.ids.abbyy.server.DefaultAbbyyResource;
import com.blackbox.ids.abbyy.services.IClassificationService;

@Service
public class ClassificationServiceImpl implements IClassificationService {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(DefaultAbbyyResource.class);
	
	@Override
	public String getTemplateType(String inputPDFPath, IFlexiCaptureProcessor flexiCaptureProcessor) {
		
		String templateProjectPath = "";//TODO path of .cfl file
		flexiCaptureProcessor.AddClassificationTreeFile(templateProjectPath);

		flexiCaptureProcessor.AddImageFile(inputPDFPath);

		IClassificationResult result = flexiCaptureProcessor.ClassifyNextPage();
		String projectType = null;

		if (result != null && result.getPageType() == PageTypeEnum.PT_MeetsDocumentDefinition) {
			IStringsCollection names = result.GetClassNames();
			projectType = names.Item(0);
			LOGGER.info("Classifier Result : " + projectType);
		}
		return projectType;
	}

}
