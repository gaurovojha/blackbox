package com.blackbox.ids.services.correspondence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IPairAuditService {
	
	 /**
	 * Do Audit of file.
	 * @throws FileNotFoundException 
	 */
	 void processManualAuditJobFiles() throws FileNotFoundException;
	 
	 Map<String, String> validateInputXML(final File file) throws FileNotFoundException;

	 void uploadFileToSpecificLocation(final String uploadDirectory,final MultipartFile multipartFile) throws IOException;

}
