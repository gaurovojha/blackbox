package com.blackbox.ids.services.crawler;

import java.io.File;
import java.io.FileNotFoundException;

public interface IPrivatePairCommonService {

	/**
	 * Process and validate xml records.
	 *
	 * @param lastDownloadedFile the last downloaded file
	 * @param userId TODO
	 * @param fileName 
	 * @throws FileNotFoundException the file not found exception
	 */
	boolean processAndValidateXMLRecords(final File lastDownloadedFile,Long userId, String fileName) throws FileNotFoundException;
}
