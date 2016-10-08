package com.blackbox.ids.services.correspondence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.webcrawler.CreateApplicationQueueRepository;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.ICorrespondenceStagingImportService;

import junit.framework.Assert;

public class CorrespondenceStagingImportServiceTest extends AbstractIntegrationTest {

	@Autowired
	private ICorrespondenceStagingImportService correspondenceStagingImportService ;
	
	@Autowired
	private CorrespondenceBaseRepository correspondenceRepository;
	
	@Autowired
	private CreateApplicationQueueRepository createApplicationQueueRepository;
	
	@Test
	public void testStagingImport(){
		String testFolder = FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("757");
		String stageFolder= FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("staging");
		boolean proceed = copyTestFolderToStaging(testFolder,stageFolder) ;
		if(proceed){
			String baseFolder = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("base") ;
			deleteAndCreateFolder(baseFolder) ;
			List<CorrespondenceBase> correspondenceBaseList = correspondenceRepository.findAll() ;
			int initialBaseCount = correspondenceBaseList.size() ;
			correspondenceStagingImportService.importData();
			correspondenceBaseList = correspondenceRepository.findAll() ;
			int finalBaseCount = correspondenceBaseList.size() ;
			int added = finalBaseCount - initialBaseCount ;
			File folder = new File(baseFolder);
			File[] listOfFiles = folder.listFiles();
			Assert.assertEquals(added, listOfFiles.length);
			deleteAndCreateFolder(baseFolder) ;
		}
	}
	
	private void deleteAndCreateFolder(String fileTobDeleted) {
		File file = new File(fileTobDeleted);
		if (!file.exists()) {
			file.mkdir();
		} else {
			deleteDir(file) ;
			file.mkdir();
		}
	}
	
	private  boolean deleteDir(File dir) {
	      if (dir.isDirectory()) {
	         String[] children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir
	            (new File(dir, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
	      }
	      return dir.delete();
	 }
	
	private boolean copyTestFolderToStaging(String testFolder , String stageFolder) {
		boolean success = true ;
		String destFolder = stageFolder+File.separator+"757" ;
		File srcFile = new File(testFolder) ;
		File destFile = new File(destFolder) ;
		try {
			copyFolder(srcFile, destFile) ;
		} catch (IOException e) {
			success=false;
			e.printStackTrace();
		}
		return success;
	}
	
	private  void copyFolder(File src, File dest)
	    	throws IOException{
	    	
	    	if(src.isDirectory()){
	    		
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		}
	    		
	    		//list all the directory contents
	    		String files[] = src.list();
	    		
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	    	   
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	    	                     
	    	        byte[] buffer = new byte[1024];
	    	    
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();	    	}
	    }

}
