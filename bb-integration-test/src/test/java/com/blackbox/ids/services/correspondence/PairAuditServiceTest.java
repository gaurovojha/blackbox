package com.blackbox.ids.services.correspondence;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.correspondence.IJobDAO;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.repository.email.MessageDataRepository;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class PairAuditServiceTest extends AbstractIntegrationTest{


	@Autowired
	private IPairAuditService pairAuditService;
	
	@Autowired
	private MessageDataRepository msgDataRepository;
	
	@Autowired
	private IJobDAO jobDAO;

	@Before
	public void setUp() {
		setTestUser();
	}
	
	@Test
	public void testValidateInputXML() throws FileNotFoundException, ApplicationException{

		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pair-audit/test-pair-audit.xml").getFile());
		Map<String, String> datesMap= new HashMap<String,String>();
		Map<String, String> datesMapResult = new HashMap<String,String>();
		datesMapResult.put("InitialDate" , "May-13-2010");
		datesMapResult.put("FinalDate", "Sep-17-2017");
		datesMap = pairAuditService.validateInputXML(inputFile);
		Assert.assertEquals(datesMapResult, datesMap);
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testValidateInputXMLNotExists() throws FileNotFoundException, ApplicationException{
		File inputFile = new File("test-pair-audit-exception.xml");
		pairAuditService.validateInputXML(inputFile);
	}

	@Test
	public void testUploadFileToSpecificLocation() throws IOException{
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pair-audit/test-pair-audit.xml").getFile());
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		MultipartFile multipartFile = new MockMultipartFile("file",inputFile.getName(), "text/plain", IOUtils.toByteArray(fileInputStream));
		final String fileUploadBasePath = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("jobs");
		List<Job> job= jobDAO.findAll();
		int jobSize  = job.size();
		pairAuditService.uploadFileToSpecificLocation(fileUploadBasePath, multipartFile);
		List<Job> newJobsList = jobDAO.findAll();
		int newJobSize = newJobsList.size();
		int jobInserted =newJobSize - jobSize;
		assert(1==jobInserted);	
	}

	@Test
	public void testProcessManualAuditJobFiles() throws FileNotFoundException, ApplicationException{
			try {
				List<MessageData> messageDataList = msgDataRepository.findAll();
				int initialMessageDataCount = messageDataList.size() ;
				pairAuditService.processManualAuditJobFiles();
				messageDataList = msgDataRepository.findAll() ;
				int finalMessageDataCount = messageDataList.size() ;
				int rowsAddedToMessageData = finalMessageDataCount - initialMessageDataCount ;
				Assert.assertEquals(3, rowsAddedToMessageData);
			} catch (Exception e) {
				Assert.assertFalse(e.getMessage(), true);
			}
	}

}