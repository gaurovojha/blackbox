package com.blackbox.ids.services.correspondence;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.ResponseDTO;
import com.blackbox.ids.dto.ResponseDTO.ResponseStatus;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class CreateNewCorrespondenceTest extends AbstractIntegrationTest {

	@Autowired
	private ICorrespondenceService correspondenceService;

	@Before
	public void setUp() {
		setTestUser();
	}

	@Test
	public void testCreateCorrespondenceWithData() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.SUCCESS, responseDTO.getStatus());

	}
	
	@Test
	public void testCreateCorrespondenceWithInvalidDataAndWOJurisdiction() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("WO");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertNotEquals(0, responseDTO.getErrors().size());

	}

	@Test
	public void testCreateCorrespondenceWithDataUrgent() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(true);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.SUCCESS, responseDTO.getStatus());

	}
	@Test
	public void testCreateCorrespondenceWithBookmarkB2() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB2.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}

	@Test
	public void testCreateCorrespondenceWithMultipleBookMark() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/multipleBookmark.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}
	
	@Test
	public void testCreateCorrespondenceWithNoBookMark() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/NoBookmark.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}
	
	@Test
	public void testCreateCorrespondenceWithInvalidBookMark() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/InvalidBookmark.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}
	
	@Test
	public void testCreateCorrespondenceWithWrongJurisdiction() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("UP");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}
	
	@Test
	public void testCreateCorrespondenceWithWrongMailingDate() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 22, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}
	
	@Test
	public void testCreateCorrespondenceWithDocumentDescription() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(5L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 20, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		responseDTO.getErrors().stream().forEach(p -> System.out.println(p.getErrorCode()));
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());

	}

	
	@Test
	public void testCreateApplicationRequestWithData() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933259");
		correspondenceFormDto.setJurisdiction("US");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 24, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createApplicationRequest(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.SUCCESS, responseDTO.getStatus());
	}
	
	@Test
	public void testCreateApplicationRequestWithWOJurisdictionAndInvalidData() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933259");
		correspondenceFormDto.setJurisdiction("WO");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 24, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createApplicationRequest(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());
	}
	
	@Test
	public void testCreateApplicationRequestWithInvalidJurisdiction() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933259");
		correspondenceFormDto.setJurisdiction("UP");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 24, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createApplicationRequest(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());
	}
	
	@Test
	public void testCreateApplicationRequestWithInvalidAssignee() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933259");
		correspondenceFormDto.setJurisdiction("UP");
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 24, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		correspondenceFormDto.setAssignee("ABC");
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/correspondence/pdf/BookmarkB1.pdf").getFile());
		correspondenceFormDto.setFile(inputFile.getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		ResponseDTO responseDTO = correspondenceService.createApplicationRequest(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());
	}
	
	@Test
	public void testSearchApplicationWithData() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14933258");
		correspondenceFormDto.setJurisdiction("US");
		ResponseDTO responseDTO = correspondenceService.searchApplication(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.SUCCESS, responseDTO.getStatus());
	}
	
	@Test
	public void testSearchApplicationWithInvalidApplicationNumber() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14");
		correspondenceFormDto.setJurisdiction("US");
		ResponseDTO responseDTO = correspondenceService.searchApplication(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());
	}

	@Test
	public void testSearchApplicationWithInvalidJurisdiction() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14");
		correspondenceFormDto.setJurisdiction("UP");
		ResponseDTO responseDTO = correspondenceService.searchApplication(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());
	}
	@Test
	public void testSearchApplicationWithWIPOJurisdiction() {
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber("14");
		correspondenceFormDto.setJurisdiction("WO");
		ResponseDTO responseDTO = correspondenceService.searchApplication(correspondenceFormDto);
		Assert.assertEquals(ResponseStatus.ERROR, responseDTO.getStatus());
	}

}
