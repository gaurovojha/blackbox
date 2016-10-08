package com.blackbox.ids.services.correspondence;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.dao.correspondence.IJobDAO;
import com.blackbox.ids.core.dao.correspondence.impl.CorrespondenceStageDAOImpl;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.repository.email.MessageDataRepository;
import com.blackbox.ids.it.AbstractIntegrationTest;

import junit.framework.Assert;

public class BulkUploadServiceTest extends AbstractIntegrationTest {

	@Autowired
	private IBulkUploadService bulkUploadService ;
	
	/** Correspondence Staging Dao **/
	@Autowired
	private CorrespondenceStageDAOImpl correspondenceStageDao;
	
	@Autowired
	private MessageDataRepository msgDataRepository ;
	
	@SuppressWarnings("deprecation")
	@Test
	public void testProcessJobFiles(){
		String stageFolder =  FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("staging");
		deleteAndCreateFolder(stageFolder) ;
		List<MessageData> messageDataList = msgDataRepository.findAll() ;
		int initialMessageDataCount = messageDataList.size() ;
		List<CorrespondenceStaging> stagingList = correspondenceStageDao.findAll();
		//call service method
		bulkUploadService.processJobFiles();
		
		messageDataList = msgDataRepository.findAll() ;
		int finalMessageDataCount = messageDataList.size() ;
		int rowsAddedToMessageData = finalMessageDataCount - initialMessageDataCount ;
		Assert.assertEquals(53, rowsAddedToMessageData);
		stagingList = correspondenceStageDao.findAll();
		HashMap<String,CorrespondenceStaging> stagingMap = new HashMap<>() ;
		for(CorrespondenceStaging each : stagingList) {
			String stagingId = each.getId()+"" ;
			if(!stagingMap.containsKey(stagingId)){
				stagingMap.put(each.getId()+"",each) ;
			}
		}
		File folder = new File(stageFolder);
		boolean isStageDataCorrect = true ;
		File[] listOfFiles = folder.listFiles();
		for(File each : listOfFiles) {
			String stageId = each.getName() ;
			stagingMap.get(stageId).getStatus();
			if(!(stagingMap.containsKey(stageId) && Status.PENDING.equals(stagingMap.get(stageId).getStatus()))){
				isStageDataCorrect = false ;
				break ;
			}
		}
		Assert.assertEquals(isStageDataCorrect, true);
		deleteAndCreateFolder(stageFolder) ;
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
}
