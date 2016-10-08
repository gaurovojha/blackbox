package com.blackbox.ids.exception;

/**
 * This exception is thrown when any generic exception occurs while web crawling.
 * 
 * @author abhay2566
 *
 */
public class WebCrawlerGenericException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -2882136152622129499L;

	public WebCrawlerGenericException(String message) {
		super(message);
	}

	public WebCrawlerGenericException(Throwable cause) {
		super(cause);
	}

	public WebCrawlerGenericException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebCrawlerGenericException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}