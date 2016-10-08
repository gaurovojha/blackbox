package com.blackbox.ids.ui.controller;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.impl.ApplicationBaseDaoImpl;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.model.correspondence.Job.Type;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.dto.ResponseDTO;
import com.blackbox.ids.dto.ResponseDTO.ResponseStatus;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto.CorrespondenceFormFieldErrors;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto.CorrespondenceFormFields;
import com.blackbox.ids.exception.BBError;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.services.assignee.IAssigneeService;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.services.correspondence.IPairAuditService;
import com.blackbox.ids.services.document.IDocumentService;
import com.blackbox.ids.services.jurisdiction.IJurisdictionService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.ui.common.BBPermissionURLs;
import com.blackbox.ids.ui.common.BlackboxFileHelper;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.controller.endpoints.CorrespondenceEndpoints;
import com.blackbox.ids.ui.form.correspondence.CorrespondenceForm;
import com.blackbox.ids.ui.form.mdm.ApplicationForm;
import com.blackbox.ids.ui.form.mdm.SingleApplicationForm;
import com.blackbox.ids.ui.validation.CorrespondenceFormValidator;
import com.blackbox.ids.ui.validation.SearchApplicationFormValidator;

@Controller
@RequestMapping("/correspondence")
public class CorrespondenceController extends BaseController {

	private static final String KEY_SEARCH_RESULT = "searchResult";

	private static final Logger LOGGER = Logger.getLogger(CorrespondenceController.class);
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	CorrespondenceFormValidator correspondenceFormValidator;
	@Autowired
	SearchApplicationFormValidator searchApplicationFormValidator;
	@Autowired
	IDocumentService documentService;
	@Autowired
	NumberFormatValidationService numberFormatValidationService;
	@Autowired
	ApplicationBaseDaoImpl applicationBaseDaoImpl;
	@Autowired
	IAssigneeService assigneeService;

	@Autowired
	private IPairAuditService pairAuditService;
	@Autowired
	private ICorrespondenceService correspondenceService;
	@Autowired
	private BlackboxFileHelper fileHelper;

	@Autowired
	IJurisdictionService jurisdictionService;

	/**
	 * Correspondence Dashboard
	 * 
	 * @param request
	 * @param model
	 * @return string
	 * @throws Exception
	 */

	@RequestMapping(value = "/dashboard")
	public String correspondenceDashboard( Model model) throws ApplicationException {
			model.addAttribute(Constants.CORRESPONDENCE_ACTION_ITEM_COUNT, correspondenceService.fetchActionItemCount());
			setSelectedTab(model, Constants.CONSTANT_CORRESPONDENCE_DASHBOARD_TAB);
			List<String> jurisdictions = jurisdictionService.findAllJurisdictionsValues();
			String jurisdictionCommaSeparated = "";
			for (String jurisdiction2 : jurisdictions) {
				jurisdictionCommaSeparated = jurisdictionCommaSeparated + "," + jurisdiction2;
			}
			model.addAttribute("jurisdictionData", jurisdictionCommaSeparated);
			return CorrespondenceEndpoints.MAIN_CORRESPONDENCE;
	}

	/****************************************************************************************
	 * CREATE NEW CORRESPONDENCE
	 ****************************************************************************************/
	/**
	 * Add Correspondence Service method
	 * 
	 * @param correspondenceForm
	 *  	Form object for binding
	 *  
	 * @param result
	 * 		An object of BindingResult
	 * 
	 * @param request
	 * 
	 * @param model
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String addCorrespondence(@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm,
			BindingResult result, Model model) throws ApplicationException {
		
		correspondenceFormValidator.validate(correspondenceForm, result);
		String uploadDirectory = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath(generateTempFolderName());
		model.addAttribute("correspondenceForm", correspondenceForm);
		List<String> jurisdictions = jurisdictionService.findAllJurisdictionsValues();
		String jurisdictionCommaSeparated = "";
		for (String jurisdiction2 : jurisdictions) {
			jurisdictionCommaSeparated = jurisdictionCommaSeparated + "," + jurisdiction2;
		}
		List<DocumentCode> documentCode = documentService.findAllDocumentCodes();
		model.addAttribute("jurisdictionData", jurisdictionCommaSeparated);
		model.addAttribute("documentDescriptions", documentCode);
		File uploadTempdirectory = new File(uploadDirectory);
		if (!uploadTempdirectory.exists()) {
			uploadTempdirectory.mkdirs();
		}
		String path = uploadDirectory + correspondenceForm.getFile().getOriginalFilename();
		File dest = new File(path);
		try {
			correspondenceForm.getFile().transferTo(dest);
		} catch (Exception e) {
			result.rejectValue(CorrespondenceFormFields.FILE, CorrespondenceFormFieldErrors.FAILED_TO_UPLOAD_FILE);
			LOGGER.error(e);
		}
		if (result.hasErrors()) {
			return CorrespondenceEndpoints.NEW_CORRESPONDENCE;
		}

		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber(correspondenceForm.getApplicationNumber());
		correspondenceFormDto.setJurisdiction(correspondenceForm.getJurisdiction());
		correspondenceFormDto.setDocumentCode(correspondenceForm.getDocumentDescription());
		correspondenceFormDto.setMailingDate(
				BlackboxDateUtil.strToDate(correspondenceForm.getMailingDate(), correspondenceForm.DATE_FORMAT));
		correspondenceFormDto.setUrgent(false);
		correspondenceFormDto.setFile(path);
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);

		ResponseDTO errorDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
		if (ResponseStatus.ERROR.equals(errorDTO.getStatus())) {
			for (BBError bbError : errorDTO.getErrors()) {

				result.rejectValue(bbError.getField(), bbError.getErrorCode());
			}
		}
		if (result.hasErrors())

		{
			return CorrespondenceEndpoints.NEW_CORRESPONDENCE;
		} else

		{
			model.addAttribute("success", "true");
			return CorrespondenceEndpoints.NEW_CORRESPONDENCE;
		}

	}

	/**
	 *
	 * @param correspondenceForm the correspondence form
	 * @param result the result
	 * @param request the request
	 * @param model the model
	 * @return the string
	 *       returns urgent correspondence form
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/add/urgent", method = { RequestMethod.POST })
	public String createCorrespondenceUrgent(
			@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm, BindingResult result,
			Model model) throws ApplicationException {
			correspondenceFormValidator.validate(correspondenceForm, result);
			List<DocumentCode> documentCode = documentService.findAllDocumentCodes();
			List<String> docDescription = new ArrayList<>();
			for (DocumentCode documentCode2 : documentCode) {
				docDescription.add(documentCode2.getDescription());
	
			}
			List<String> jurisdictions = jurisdictionService.findAllJurisdictionsValues();
			String jurisdictionCommaSeparated = "";
			for (String jurisdiction2 : jurisdictions) {
				jurisdictionCommaSeparated = jurisdictionCommaSeparated + "," + jurisdiction2;
			}
			model.addAttribute("documentDescriptions", documentCode);
			model.addAttribute("jurisdictionData", jurisdictionCommaSeparated);
			model.addAttribute("correspondenceForm", correspondenceForm);
			if (result.hasErrors()) {
				model.addAttribute("correspondenceForm", correspondenceForm);
				return CorrespondenceEndpoints.URGENT_REQUEST;
			}
			String uploadDirectory = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath(generateTempFolderName());
			File uploadTempdirectory = new File(uploadDirectory);
			if (!uploadTempdirectory.exists()) {
				uploadTempdirectory.mkdirs();
			}
			String path = uploadDirectory + correspondenceForm.getFile().getOriginalFilename();
			File dest = new File(path);
			try {
				correspondenceForm.getFile().transferTo(dest);
			} catch (Exception e) {
				result.rejectValue(CorrespondenceFormFields.FILE, CorrespondenceFormFieldErrors.FAILED_TO_UPLOAD_FILE);
				LOGGER.error(e);
			}
			CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
			correspondenceFormDto.setApplicationNumber(correspondenceForm.getApplicationNumber());
			correspondenceFormDto.setJurisdiction(correspondenceForm.getJurisdiction());
			correspondenceFormDto.setDocumentCode(correspondenceForm.getDocumentDescription());
			correspondenceFormDto.setMailingDate(
					BlackboxDateUtil.strToDate(correspondenceForm.getMailingDate(), correspondenceForm.DATE_FORMAT));
			correspondenceFormDto.setUrgent(correspondenceForm.isUrgent());
			correspondenceFormDto.setFile(path);
			correspondenceFormDto.setSource(Source.MANUAL);
			correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		
			ResponseDTO errorDTO = correspondenceService.createManualCorrespondence(correspondenceFormDto);
			if (ResponseStatus.ERROR.equals(errorDTO.getStatus())) {
				for (BBError bbError : errorDTO.getErrors()) {
					result.rejectValue(bbError.getField(), bbError.getErrorCode());
				}
			}
	
			if (result.hasErrors())
	
			{
				return CorrespondenceEndpoints.URGENT_REQUEST;
			} else
	
			{
				model.addAttribute("success", "true");
				return CorrespondenceEndpoints.URGENT_REQUEST;
			}
	}

	/**
	 * Search application form.
	 *
	 * @param correspondenceForm the correspondence form
	 * @param result the result
	 * @param model the model
	 * @return the string 
	 *       returns search application form
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/searchApplicationForm", method = { RequestMethod.GET })
	public String searchApplicationForm(@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm,
			BindingResult result, Model model) throws ApplicationException {
		LOGGER.info("searchApplicationForm") ;
		return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;

	}

	/***********************************************************************************
	 * CREATE APPLICATION REQUEST FORM
	 ************************************************************************************/
	/**
	 * 
	 * @param correspondenceForm
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createApplicationRequestForm", method = { RequestMethod.GET })
	public String createApplicationRequestForm(
			@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm,
			Model model) throws ApplicationException {
		List<DocumentCode> documentCode = documentService.findAllDocumentCodes();
		Set<Assignee> assignees = assigneeService
				.getAssigneesByIds(BlackboxSecurityContextHolder.getUserAuthData().getAssigneeIds());
		Set<String> assigneeNames = assignees.stream().map(Assignee::getName).collect(Collectors.toSet());
		model.addAttribute("assigneeData", StringUtils.join(assigneeNames, ","));

		model.addAttribute("documentDescriptions", documentCode);
		model.addAttribute("correspondenceForm", correspondenceForm);
		return CorrespondenceEndpoints.CREATE_APPLICATION_REQUEST_FORM;
	}

	/**
	 *
	 * @param correspondenceForm the correspondence form
	 * @param result the result
	 * @param ra the ra
	 * @param model the model
	 * @return the string
	 * 			returns create application url
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/createApplication", method = { RequestMethod.POST })
	public String createApplication(@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm,
			RedirectAttributes ra) throws ApplicationException {
			ApplicationForm applicationForm = new ApplicationForm();
			SingleApplicationForm appForm = new SingleApplicationForm();
			appForm.setApplicationNo(correspondenceForm.getApplicationNumber());
			appForm.setJurisdictionName(correspondenceForm.getJurisdiction());
			applicationForm.getApps().add(appForm);
			ra.addFlashAttribute("applicationForm", applicationForm);
			return "redirect:" + BBPermissionURLs.Correspondence.CREATEAPPLICATION;
	}

	/**
	 *
	 * @param correspondenceForm the correspondence form
	 * @param result the result
	 * @param model the model
	 * @return the string
	 *         returns create application request form
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/createApplicationRequest", method = { RequestMethod.POST })
	public String createApplicationRequest(@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm,
			BindingResult result, Model model) throws ApplicationException {
		correspondenceFormValidator.validate(correspondenceForm, result);
		List<DocumentCode> documentCode = documentService.findAllDocumentCodes();
		List<String> docDescription = new ArrayList<>();
		for (DocumentCode documentCode2 : documentCode) {
			docDescription.add(documentCode2.getDescription());

		}

		Set<Assignee> assignees = assigneeService
				.getAssigneesByIds(BlackboxSecurityContextHolder.getUserAuthData().getAssigneeIds());
		String commaseparatedAssignees = "";
		for (Assignee assignee : assignees) {
			commaseparatedAssignees = commaseparatedAssignees + "," + assignee.getName();
		}
		model.addAttribute("assigneeData", commaseparatedAssignees);
		model.addAttribute("documentDescriptions", documentCode);
		model.addAttribute("correspondenceForm", correspondenceForm);
		if (result.hasErrors()) {
			model.addAttribute("correspondenceForm", correspondenceForm);
			return CorrespondenceEndpoints.CREATE_APPLICATION_REQUEST_FORM;
		}
		String uploadDirectory = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath(generateTempFolderName());
		File uploadTempdirectory = new File(uploadDirectory);
		if (!uploadTempdirectory.exists()) {
			uploadTempdirectory.mkdirs();
		}
		String path = uploadDirectory + correspondenceForm.getFile().getOriginalFilename();
		File dest = new File(path);
		try {
			correspondenceForm.getFile().transferTo(dest);
		} catch (Exception e) {
			result.rejectValue(CorrespondenceFormFields.FILE, CorrespondenceFormFieldErrors.FAILED_TO_UPLOAD_FILE);
			LOGGER.error(e);
		}
		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber(correspondenceForm.getApplicationNumber());
		correspondenceFormDto.setJurisdiction(correspondenceForm.getJurisdiction());
		correspondenceFormDto.setDocumentCode(correspondenceForm.getDocumentDescription());
		correspondenceFormDto.setMailingDate(
				BlackboxDateUtil.strToDate(correspondenceForm.getMailingDate(), correspondenceForm.DATE_FORMAT));
		correspondenceFormDto.setUrgent(correspondenceForm.isUrgent());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		correspondenceFormDto.setFile(path);
		correspondenceFormDto.setAssignee(correspondenceForm.getAssignee());

		
		ResponseDTO errorDTO = correspondenceService.createApplicationRequest(correspondenceFormDto);
		if (ResponseStatus.ERROR.equals(errorDTO.getStatus())) {
			for (BBError bbError : errorDTO.getErrors()) {
				result.rejectValue(bbError.getField(), bbError.getErrorCode());
			}
		}

		if (result.hasErrors())

		{
			return CorrespondenceEndpoints.CREATE_APPLICATION_REQUEST_FORM;
		} else

		{
			model.addAttribute("success", "true");
			return CorrespondenceEndpoints.CREATE_APPLICATION_REQUEST_FORM;
		}

	}

	/**
	 * Search application.
	 *
	 * @param correspondenceForm
	 *            the correspondence form
	 * @param result
	 *            the result
	 * @param model
	 *            the model
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/searchApplication", method = { RequestMethod.POST })
	public String searchApplication(@ModelAttribute("correspondenceForm") CorrespondenceForm correspondenceForm,
			BindingResult result, Model model) throws ApplicationException {
			searchApplicationFormValidator.validate(correspondenceForm, result);
			model.addAttribute("correspondenceForm", correspondenceForm);
			if (result.hasErrors()) {
				model.addAttribute("correspondenceForm", correspondenceForm);
				return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;
			}
			ResponseDTO responseDTO = new ResponseDTO();
			CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
			correspondenceFormDto.setApplicationNumber(correspondenceForm.getApplicationNumber());
			correspondenceFormDto.setJurisdiction(correspondenceForm.getJurisdiction());
			responseDTO = correspondenceService.searchApplication(correspondenceFormDto);
			List<DocumentCode> documentCode = documentService.findAllDocumentCodes();
			List<String> jurisdictions = jurisdictionService.findAllJurisdictionsValues();
			String jurisdictionCommaSeparated = "";
			for (String jurisdiction2 : jurisdictions) {
				jurisdictionCommaSeparated = jurisdictionCommaSeparated + "," + jurisdiction2;
			}
			model.addAttribute("documentDescriptions", documentCode);
			model.addAttribute("jurisdictionData", jurisdictionCommaSeparated);
			boolean isApplicationValid = true;
			if (ResponseStatus.ERROR.equals(responseDTO.getStatus())) {
				List<BBError> bbErrors = responseDTO.getErrors();
				for (BBError bbError : bbErrors) {
					result.rejectValue(bbError.getField(), bbError.getErrorCode());
					if (CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_NOTFOUND_APPLICATION
							.equals(bbError.getErrorCode())) {
						isApplicationValid = false;
	
					}
					if (CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_NOTACCESS_APPLICATION
							.equals(bbError.getErrorCode())) {
						return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;
	
					}
	
				}
			}
	
			if (isApplicationValid && responseDTO.getErrors().size() > 0) {
				return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;
			}
			if (isApplicationValid && responseDTO.getErrors().size() == 0) {
				if (BlackboxSecurityContextHolder.canAccessUrl(BBPermissionURLs.Correspondence.URGENTREQUEST)) {
					return CorrespondenceEndpoints.URGENT_REQUEST;
				}
				return CorrespondenceEndpoints.NEW_CORRESPONDENCE;
			}
			if (!isApplicationValid) {
				if (responseDTO.getErrors().size() > 1) {
					return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;
				}
				if (BlackboxSecurityContextHolder.canAccessUrl(BBPermissionURLs.Correspondence.CREATEAPPLICATION)) {
					return CorrespondenceEndpoints.CREATE_APPLICATION_FORM;
				} else if (BlackboxSecurityContextHolder
						.canAccessUrl(BBPermissionURLs.Correspondence.CREATEAPPLICATIONREQUEST)) {
	
					return CorrespondenceEndpoints.CREATE_APPLICATION_NO_ACCESS_FORM;
	
				} else {
					return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;
				}
			}
	
			return CorrespondenceEndpoints.SEARCH_APPLICATION_FORM;

	}

	/**************************************************************************
	 * Save Bulk uploaded files to a directory which is taken from properties
	 * file and add entry into BB_JOBS table
	 **************************************************************************/
	@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
	public String readFiles(final HttpServletRequest request) throws ApplicationException{
			LOGGER.debug("Inside Correspondence readFiles method started");
			LOGGER.info("reques-" + request.getContentLength());
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("file0");
			String uploadDirectory = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("jobs");

			Job job = correspondenceService.addEntryToJobs(Type.BULK);
			long id = job.getId();
			uploadDirectory = uploadDirectory + File.separator + job.getId() + File.separator;
			String fileName = id + ".zip";
			if (uploadDirectory != null) {
				File file = new File(uploadDirectory);
				if (!file.exists()) {
					file.mkdirs();
				}
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					try {
						File convFile = File.createTempFile(multipartFile.getOriginalFilename(), ".zip");
						multipartFile.transferTo(convFile);
						file = new File(uploadDirectory + File.separator + fileName);
						FileUtils.copyFile(convFile, file);
						convFile.deleteOnExit();
						LOGGER.debug("File Uploaded to specified location");
					} catch (MultipartException ex) {
						LOGGER.info("failed upload " + multipartFile.getName());
						LOGGER.error(ex);
					} catch (IOException e) {
						job.setStatus(Job.Status.ERROR);
						job.setComments(e.getLocalizedMessage());
						correspondenceService.updateStatusInJobs(job);
						LOGGER.info("io exception "+e);
					}
				}
			}
		return "mainCorrespondence";
	}

	@RequestMapping(value = "/pairAudit/validate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, String> validate(final MultipartHttpServletRequest request)
			throws ApplicationException, IOException{
		Map<String, String> parsedDates = new HashMap<String, String>();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			MultipartFile multipartFile = request.getFile("fileUpload");
			final String completePath = BlackboxUtils.concat(
					FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("temp"), multipartFile.getOriginalFilename());
			File convFile = File.createTempFile(completePath, ".xml");
			multipartFile.transferTo(convFile);
			parsedDates = pairAuditService.validateInputXML(convFile);
			convFile.deleteOnExit();
			if (MapUtils.isEmpty(parsedDates)) {
				throw new XMLOrPDFCorruptedException("Exception occured while processing the file. File is corrupted. No data is present");
			}
		}
		return parsedDates;
	}

	@RequestMapping(value = "/pairAudit/upload", method = RequestMethod.POST)
	public String uploadAuditFile(final MultipartHttpServletRequest request) throws IOException {
		LOGGER.info("Uploading the input file to the specific location");
		final String fileUploadBasePath = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("jobs");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			MultipartFile multipartFile = request.getFile("fileUpload");
			pairAuditService.uploadFileToSpecificLocation(fileUploadBasePath, multipartFile);
			LOGGER.info(MessageFormat.format("Validation of input Xml{0} File for Date Range Completes",multipartFile.getName()));
		}
		return CorrespondenceEndpoints.MAIN_CORRESPONDENCE;
	}

	/*- ----------------------------------------------------------------
	My Dashboard - Active Documents
	------------------------------------------------------------------- */
	@RequestMapping(value = "/activeDocuments", method = RequestMethod.POST)
	public String viewActiveDocuments(final HttpServletRequest request, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);
		String myDocuments = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myDocuments) ? loggedInUser() : null;

			CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(
				request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName),
				request.getParameter(DatatableAttribute.JURISDICTION.attrName),
				request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName),
				request.getParameter(DatatableAttribute.FAMILY_ID.attrName),
				request.getParameter(DatatableAttribute.DOCUMENT_DESCRIPTION.attrName),
				request.getParameter(DatatableAttribute.UPLOADED_BY.attrName),
				request.getParameter(DatatableAttribute.UPLOADED_ON.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);
		
		SearchResult<CorrespondenceRecordDTO> correspondenceList = correspondenceService.filterActiveDocuments(filter,
				pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, correspondenceList);

		return CorrespondenceEndpoints.ACTIVE_DOCUMENTS;
	}

	/*- ----------------------------------------------------------------
	My Dashboard - Inactive Documents
	------------------------------------------------------------------- */
	@RequestMapping(value = "/inactiveDocuments", method = RequestMethod.POST)
	public String viewInactiveDocuments(final HttpServletRequest request, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);

		String myDocuments = request.getParameter("myRecords");
		Long ownedBy = Boolean.valueOf(myDocuments) ? loggedInUser() : null;

		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(
			request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName),
			request.getParameter(DatatableAttribute.JURISDICTION.attrName),
			request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName),
			request.getParameter(DatatableAttribute.FAMILY_ID.attrName),
			request.getParameter(DatatableAttribute.DOCUMENT_DESCRIPTION.attrName),
			request.getParameter(DatatableAttribute.UPLOADED_BY.attrName),
			request.getParameter(DatatableAttribute.UPLOADED_ON.attrName),
			request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
			ownedBy);

		SearchResult<CorrespondenceRecordDTO> correspondenceList = correspondenceService.filterInactiveDocuments(filter,
				pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, correspondenceList);

		return CorrespondenceEndpoints.INACTIVE_DOCUMENTS;
	}

	/*- ----------------------------------------------------------------
	My Dashboard - View Correspondence
	------------------------------------------------------------------- */
	@RequestMapping(value = "/activeDocuments/viewDoc", method = RequestMethod.GET, produces = "text/html")
	public String viewDoc(@RequestParam("correspondenceId") final Long correspondenceId, final Model model) {
		CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceService
				.getCorrespondenceRecord(correspondenceId);
		model.addAttribute("correspondenceDocument", correspondenceRecordDTO);
		return CorrespondenceEndpoints.VIEW_DOCUMENT;
	}

	/*- ----------------------------------------------------------------
	My Dashboard - Delete Correspondence
	------------------------------------------------------------------- */
	@RequestMapping(value = "/activeDocuments/deleteCorrespondence", method = RequestMethod.GET, produces = "text/html")
	public ModelAndView deleteCorrespondence(@RequestParam("correspondenceId") final Long correspondenceId,
			final HttpServletRequest request) {
		String comments = request.getParameter("comments");
		correspondenceService.deleteCorrespondenceRecord(correspondenceId, comments);
		return new ModelAndView("redirect:/correspondence/dashboard");
	}

	/*- ----------------------------------------------------------------
	My Dashboard - Get All Correspondences of Active Document
	------------------------------------------------------------------- */
	@RequestMapping(value = "/activeDocuments/getActiveCorrespondences/{correspondenceId}", method = RequestMethod.GET)
	public String getActiveCorrespondences(@PathVariable("correspondenceId") final Long correspondenceId,
			final HttpServletRequest request, final Model model) {

		String myDocuments = request.getParameter("myRecords");
		Long ownedBy = Boolean.valueOf(myDocuments) ? loggedInUser() : null;

		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(
			request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName),
			request.getParameter(DatatableAttribute.JURISDICTION.attrName),
			request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName),
			request.getParameter(DatatableAttribute.FAMILY_ID.attrName),
			request.getParameter(DatatableAttribute.DOCUMENT_DESCRIPTION.attrName),
			request.getParameter(DatatableAttribute.UPLOADED_BY.attrName),
			request.getParameter(DatatableAttribute.UPLOADED_ON.attrName),
			request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
			ownedBy);
		SearchResult<CorrespondenceRecordDTO> correspondenceList = correspondenceService
				.getActiveCorrespondences(filter, correspondenceId);

		model.addAttribute("correspondenceList", correspondenceList);
		return CorrespondenceEndpoints.ACTIVE_DOCUMENTS_EXPAND;
	}

	/*- ----------------------------------------------------------------
	My Dashboard -Get All Correspondences of InActive Document
	------------------------------------------------------------------- */
	@RequestMapping(value = "/inactiveDocuments/getInActiveCorrespondences/{correspondenceId}", method = RequestMethod.GET)
	public String getInactiveCorrespondences(@PathVariable("correspondenceId") final Long correspondenceId,
			final HttpServletRequest request, final Model model) {

		String myDocuments = request.getParameter("myRecords");
		Long ownedBy = Boolean.valueOf(myDocuments) ? loggedInUser() : null;

		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(
			request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName),
			request.getParameter(DatatableAttribute.JURISDICTION.attrName),
			request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName),
			request.getParameter(DatatableAttribute.FAMILY_ID.attrName),
			request.getParameter(DatatableAttribute.DOCUMENT_DESCRIPTION.attrName),
			request.getParameter(DatatableAttribute.UPLOADED_BY.attrName),
			request.getParameter(DatatableAttribute.UPLOADED_ON.attrName),
			request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
			ownedBy);
		
		SearchResult<CorrespondenceRecordDTO> correspondenceList = correspondenceService
				.getInactiveCorrespondences(filter, correspondenceId);

		model.addAttribute("correspondenceList", correspondenceList);
		return CorrespondenceEndpoints.INACTIVE_DOCUMENTS_EXPAND;
	}

	/*- ----------------------------------------------------------------
	My Dashboard - View Attached Pdf of a correspondence
	------------------------------------------------------------------- */
	@RequestMapping(value = "/viewDocument/viewPdf/{correspondenceId}", method = RequestMethod.GET)
	public void viewPdfFile(@PathVariable("correspondenceId") final Long correspondenceId,
			final HttpServletResponse response) throws IOException {
		final String pdfFilePath = BlackboxUtils.concat(FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("base"),
				String.valueOf(correspondenceId), File.separator, String.valueOf(correspondenceId), ".pdf");
		File file = new File(pdfFilePath);
		fileHelper.viewFile(response, file);
	}

	String generateTempFolderName() {

		int max = 999999999;
		return "temp" + File.separator + (int) (Math.random() * (max));

	}
}
