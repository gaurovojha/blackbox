package com.blackbox.ids.ui.common;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.blackbox.ids.core.ApplicationException;

/**
 * The Class GlobalExceptionHandler handles all the business related exceptions. This handles all controller level
 * exceptions and in future it may handle the Concrete level Exceptions like. IOException etc.
 * @author Nagarro
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);

	/**
	 * This handler deals with ApplicationException.
	 * @param appExec
	 *            the ApplicationException
	 */
	@ExceptionHandler(ApplicationException.class)
	public void handleApplicationException(ApplicationException appExec) {
		LOGGER.error(appExec.getMessage(), appExec);
		throw new ApplicationException(appExec);
	}

	/**
	 * This is default exception handler and may handle all the exceptions.
	 * @param exception
	 *            the Exception
	 */
	@ExceptionHandler(Exception.class)
	public void handleException(Exception exception) {
		LOGGER.error(exception.getMessage(), exception);
		throw new ApplicationException(exception);
	}
}
