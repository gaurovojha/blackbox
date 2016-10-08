package com.blackbox.ids.scheduler.correspondence;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.correspondence.IBulkUploadService;

@Service
public class ProcessBulkUploadedFilesJob {

	private Logger log = Logger.getLogger(ProcessBulkUploadedFilesJob.class);
	
	@Autowired
	private IBulkUploadService bulkUploadService ;
	
	public final void processZippFiles(){
		
		try{
			log.info("ProcessBulkUploadedFilesJob started \n");
			bulkUploadService.processJobFiles() ;
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Exception in processing zipFiles -"+ex);
		}
	}
}
