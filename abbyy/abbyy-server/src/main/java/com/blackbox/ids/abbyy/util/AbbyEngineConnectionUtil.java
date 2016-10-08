package com.blackbox.ids.abbyy.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abbyy.FCEngine.Engine;
import com.abbyy.FCEngine.IEngine;
import com.blackbox.ids.abbyy.exception.AbbyEngineInstantiationException;

/**
 * The Class AbbyEngineConnectionUtil is a utility class for connecting and fetching the Abby Engine.
 */
public class AbbyEngineConnectionUtil {

	/** The Constant LOGGER. */
	private static final Log LOGGER = LogFactory.getLog(AbbyEngineConnectionUtil.class);

	/**
	 * Instantiates a new abby engine connection util.
	 */
	private AbbyEngineConnectionUtil() {
		String errorMessage = "Illegal Attempt to initialise class has been made.";
		LOGGER.error(errorMessage);
		throw new AbbyEngineInstantiationException(errorMessage);
	}
	
	
	/**
	 * The API for fetching the Abby Flexi Capture engine.
	 *
	 * @return the engine
	 * @throws Exception 
	 */
	public static IEngine getEngine() throws Exception {
		String dllPath = "";
		String serialNumber = "";
		
		LOGGER.info("About to initialise Flexi Capture Engine");
		
		IEngine flexiCaptureEngine = Engine.Load( dllPath, serialNumber, "" );
		
		LOGGER.info("Abby Flexi Capture Engine started succesfully");
		return flexiCaptureEngine;
	}
	
}
