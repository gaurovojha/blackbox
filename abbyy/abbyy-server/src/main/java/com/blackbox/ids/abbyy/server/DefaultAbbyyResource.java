package com.blackbox.ids.abbyy.server;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.abbyy.FCEngine.Engine;
import com.abbyy.FCEngine.IEngine;
import com.abbyy.FCEngine.IFlexiCaptureProcessor;
import com.blackbox.ids.abbyy.api.resource.AbbyyResource;
import com.blackbox.ids.abbyy.api.response.Document;
import com.blackbox.ids.abbyy.api.util.OCRUtil;
import com.blackbox.ids.abbyy.exception.AbbyEngineConnectionException;
import com.blackbox.ids.abbyy.services.IClassificationService;
import com.blackbox.ids.abbyy.services.IDataExtractionService;
import com.blackbox.ids.abbyy.util.AbbyEngineConnectionUtil;

/**
 * The Class DefaultAbbyyResource.
 */
public class DefaultAbbyyResource implements AbbyyResource {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(DefaultAbbyyResource.class);

	/** The classification to project map. */
	@Resource(name = "classificationToProjectMapping")
	private Map<String, IDataExtractionService> classificationToProjectMap;

	/** The environment properties. */
	@Resource(name = "environmentProperties")
	private Properties environmentProperties;
	
	@Resource(name = "templateToDocumentDefinitionMapping")
	private Map<String,String> templateToDocumentDefinitionMapping;
	
	/** The data extraction service. */
	@Autowired
	IDataExtractionService dataExtractionService;
	
	/** The classification service. */
	@Autowired
	private IClassificationService classificationService;

	
	@Override
	public Document extract(String inputPDFPath) {
		
		// Test Block just for testing the web service from client. To  be deleted afterwards.
//		if(true) {
//			System.out.println("*** Input PDF Path is "+inputPDFPath);
//			System.out.println(environmentProperties.getProperty("abbyy.root.dir"));
//			Document document = new Document();
//			document.setClassificationType("ABHAY");
//			document.setDocumentConfidenceLevel(BigInteger.valueOf(12345));
////			throw new NullPointerException("sas");
//			return document;
//		}
//		
		IEngine flexiCaptureEngine;
		IFlexiCaptureProcessor flexiCaptureProcessor;
		Document document = null;

		try {
			flexiCaptureEngine = AbbyEngineConnectionUtil.getEngine();
			flexiCaptureProcessor = flexiCaptureEngine.CreateFlexiCaptureProcessor();

			String projectType = classificationService.getTemplateType(inputPDFPath, flexiCaptureProcessor);

			LOGGER.info(MessageFormat.format("Template classified for PDF {0} is {1}", inputPDFPath,projectType));
			
			if (projectType != null) {
				document = processClassifiedProject(inputPDFPath, flexiCaptureProcessor, document, projectType);
			} else {
				LOGGER.info(MessageFormat.format("No project could be classified for PDF {0}", inputPDFPath));
			}
		} catch (Exception e) {
			String errorMessage = "Some exception occured while connecting to Abby Engine. Exception is "
					+ e.getMessage();
			LOGGER.error(errorMessage);
			throw new AbbyEngineConnectionException(errorMessage);
		} finally {
			Engine.Unload();
		}
		return document;
	}

	/**
	 * Process classified project.
	 *
	 * @param inputPDFPath the input pdf path
	 * @param flexiCaptureProcessor the flexi capture processor
	 * @param document the document
	 * @param projectType the project type
	 * @return the document
	 */
	private Document processClassifiedProject(String inputPDFPath, IFlexiCaptureProcessor flexiCaptureProcessor,
			Document document, String projectType) {
		String templateProjectRelativePath = templateToDocumentDefinitionMapping.get(projectType);
		
		if(templateProjectRelativePath != null) {
			
		flexiCaptureProcessor.AddDocumentDefinitionFile(getCompletePath(templateProjectRelativePath));

		IDataExtractionService dataExtractionService = projectType != null ? classificationToProjectMap.get(projectType) : null;
		document = dataExtractionService != null ? dataExtractionService.extractAndPrepareData(flexiCaptureProcessor) : null;
		
		LOGGER.info(MessageFormat.format("Extracted Document for PDF {0} is {1}", inputPDFPath,document));
		
		} else {
			LOGGER.info(MessageFormat.format("No project found for classifed value : [{0}] for PDF [{1}]", projectType,inputPDFPath));
		}
		return document;
	}

	/**
	 * Gets the complete path.
	 *
	 * @param string the string
	 * @return the complete path
	 */
	private String getCompletePath(String templateProjectRelativePath) {
		return OCRUtil.concat(environmentProperties.getProperty("abbyy.root.dir"),templateProjectRelativePath);
	}

}
