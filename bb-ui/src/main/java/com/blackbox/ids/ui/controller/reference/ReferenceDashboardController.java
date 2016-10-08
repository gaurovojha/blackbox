package com.blackbox.ids.ui.controller.reference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.reference.JurisdictionDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.dto.reference.ReferenceEntryDTO;
import com.blackbox.ids.core.dto.reference.ReferenceEntryFilter;
import com.blackbox.ids.core.dto.reference.ReferenceRecordDTO;
import com.blackbox.ids.core.dto.user.UserDTO;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.OcrStatus;
import com.blackbox.ids.core.model.reference.ReferenceCategoryType;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.reference.ReferencePredicate;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.notification.NotificationService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.ReferenceDashboardService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.blackbox.ids.ui.common.BlackboxFileHelper;
import com.blackbox.ids.ui.common.Constants;
import com.mysema.query.types.Predicate;

/**
 * The Class ReferenceDashboardController.
 */
@Controller
@RequestMapping("/reference")
public class ReferenceDashboardController {

	private static final String KEY_REFERENCE_MANAGEMENT = "/reference/management";

	private static final String KEY_REFERENCE_DASHBOARD = "/reference/dashboard";

	private static final String KEY_NEW_REFERENCE_SELF_CITATION_SEARCH_LIST = "new-reference-self-citation-search-list";

	private static final String KEY_NOTIFICATION_DEBUG_MESSAGE = "This activity task has already been claimed : ";

	private static final String KEY_REDIRECT_DASHBOARD = "redirect:../dashboard";

	private static final String KEY_USER_ID = "userId";

	private static final String KEY_NOTIFICATION_MESSAGE = "notificationMessage";

	private static final String KEY_REFERENCE_ENTRY = "referenceEntry";

	private static final String KEY_MDM_DTO_LIST = "mdmDtoList";

	private static final String KEY_REQUEST_COME_FROM = "requestComeFrom";

	private static final String KEY_UPDATE_REFERENCE = "updateReference";

	private static final String KEY_DUPLICATE_CHECK = "duplicateCheck";

	private static final String KEY_DASHBOARD_SUB_MENU = "dashbordSubMenu";

	private static final String KEY_NOTIFIED_DATE = "notifiedDate";

	private static final String KEY_REFERENCE_BASE_ID = "referenceBaseId";

	private static final String KEY_NOTIFICATION_PROCESS_ID = "notificationProcessId";

	/** The log. */
	private static final Logger LOGGER = Logger.getLogger(ReferenceDashboardController.class);

	/** The Constant ZERO. */
	private static final Integer ZERO = 0;

	/** The Constant KEY_ATTORNEY. */
	private static final String KEY_ATTORNEY = "attdocketNo";

	/** The Constant KEY_JURIS_CODE. */
	private static final String KEY_JURIS_CODE = "jurisdictionCode";

	/** The Constant KEY_APPLICATION_NUMBER. */
	private static final String KEY_APPLICATION_NUMBER = "applicationNumber";

	/** The Constant KEY_FAMILY_VALUE. */
	private static final String KEY_FAMILY_VALUE = "familyValue";

	/** The Constant OCR_KEY_ID. */
	private static final String OCR_KEY_ID = "ocrid";

	/** The Constant FILE_NAME. */
	private static final String FILE_NAME = "india.pdf";

	/** The Constant USER_ME. */
	private static final String USER_ME = "ME";

	/** The Constant PUBLICATION. */
	private static final String PUBLICATION = "Publication";

	/** The master data service. */
	@Autowired
	private MasterDataService masterDataService;

	/** The user repository impl. */
	@Autowired
	private UserRepository userRepositoryImpl;

	/** The application service. */
	@Autowired
	private ApplicationService applicationService;

	/** The notification service. */
	@Autowired
	private NotificationService notificationService;

	/** The reference dashboard service. */
	@Autowired
	private ReferenceDashboardService referenceDashboardService;

	/** The reference service. */
	@Autowired
	private ReferenceService referenceService;
	
	/** The file helper. */
	@Autowired
	private BlackboxFileHelper fileHelper;

	/** The context. */
	@Autowired
	ServletContext context;

	@Autowired
	private NotificationProcessService notificationProcessService;

	/**
	 * This function is used to view the PDF attachment.
	 *
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/dashboard/downloadFile", method = RequestMethod.GET)
	public void viewPDFAttachment(HttpServletResponse response) throws IOException {
		File file = new File(context.getRealPath("/") + File.separator + FILE_NAME);
		fileHelper.viewFile(response, file);
	}

	/**
	 * Reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @return the string
	 */
	@RequestMapping("/")
	public String reference(HttpServletRequest request) {
		if (!BlackboxSecurityContextHolder.canAccessUrl(KEY_REFERENCE_DASHBOARD)) {
			if (!BlackboxSecurityContextHolder.canAccessUrl(KEY_REFERENCE_MANAGEMENT)) {
				if (!BlackboxSecurityContextHolder.canAccessUrl("/reference/flowRule")) {
					return "reference.noAccess";
				}
				return "redirect:flowRule";
			}
			return "redirect:management";
		}
		return "redirect:dashboard";
	}

	/**
	 * View dashboard.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping("/dashboard")
	public String viewDashboard(HttpServletRequest request, Model model) {
		model.addAttribute("fromDashboard", true);

		if (!BlackboxSecurityContextHolder.canAccessUrl("/reference*")) {
			return "reference.noAccess";
		}

		String notificationMessage = StringUtils.EMPTY;

		if (BlackboxSecurityContextHolder.canAccessUrl("/reference/dashboard/referenceEntry")) {
			model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);
			notificationMessage = notificationService
					.getNotificationMessage(NotificationProcessType.REFERENCE_MANUAL_ENTRY);
		} else if (BlackboxSecurityContextHolder.canAccessUrl("/reference/dashboard/updateReference")) {
			model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_UPDATE_REFERENCE);
			notificationMessage = notificationService.getNotificationMessage(NotificationProcessType.INPADOC_FAILED);
		} else if (BlackboxSecurityContextHolder.canAccessUrl("/reference/dashboard/duplicateCheck")) {
			model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_DUPLICATE_CHECK);
			notificationMessage = notificationService
					.getNotificationMessage(NotificationProcessType.NPL_DUPLICATE_CHECK);
		}

		model.addAttribute("referenceEntryCount", referenceDashboardService.getReferenceEntryCount());
		model.addAttribute("updateReferenceCount", referenceDashboardService.getUpdateReferenceCounts());
		model.addAttribute("duplicateCheckCount", referenceDashboardService.getDuplicateNplReferenceCounts());
		String requestComeFrom = request.getParameter(KEY_REQUEST_COME_FROM);

		if (StringUtils.isNotBlank(requestComeFrom)) {
			model.addAttribute(KEY_DASHBOARD_SUB_MENU, requestComeFrom);
		}

		model.addAttribute(KEY_NOTIFICATION_MESSAGE, notificationMessage);

		return "reference.dashboard";
	}

	/**
	 * View reference entry.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/referenceEntry", method = RequestMethod.POST)
	public String viewReferenceEntry(Model model) {
		if (!BlackboxSecurityContextHolder.canAccessUrl(KEY_REFERENCE_DASHBOARD)) {
			return "redirect:../management";
		}

		String notificationMessage = notificationService
				.getNotificationMessage(NotificationProcessType.REFERENCE_MANUAL_ENTRY);
		model.addAttribute(KEY_NOTIFICATION_MESSAGE, notificationMessage);
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);
		return "reference.entry.header";
	}

	/**
	 * Show reference entry records.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/dashboard/referenceEntry/records", method = RequestMethod.POST)
	public String showReferenceEntryRecords(HttpServletRequest request, Model model) {

		ReferenceEntryFilter referenceRecordFilter = createReferenceEntryFilter(request);

		Pageable pageInfo = getReferenceEntryPageInfo(request);

		Page<ReferenceEntryDTO> referenceGroup = referenceDashboardService.getRefEntryData(referenceRecordFilter,
				pageInfo);

		Page<ReferenceRecordDTO> referenceEntryRecords = populateReferenceEntryData(referenceGroup, pageInfo);

		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);
		model.addAttribute(KEY_USER_ID, BlackboxSecurityContextHolder.getUserId());
		model.addAttribute("referenceEntryRecords", referenceEntryRecords);

		return "reference.entry.records";
	}

	/**
	 * View update reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @return the string
	 */
	@RequestMapping("/dashboard/updateReference")
	public String viewUpdateReference(Model model) {
		if (!BlackboxSecurityContextHolder.canAccessUrl(KEY_REFERENCE_DASHBOARD)) {
			return "redirect:management";
		}

		String notificationMessage = notificationService.getNotificationMessage(NotificationProcessType.INPADOC_FAILED);

		model.addAttribute(KEY_NOTIFICATION_MESSAGE, notificationMessage);
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_UPDATE_REFERENCE);
		
		return "update.reference.header";
	}

	/**
	 * View update reference records.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/updateReference/records", method = RequestMethod.POST)
	public String viewUpdateReferenceRecords(HttpServletRequest request, Model model) {
		Pageable pageable = getUpdateReferencePageInfo(request);
		Predicate predicate = getUpdateReferencePredicate(request);
		Page<ReferenceDashboardDto> data = referenceDashboardService.getUpdaterRefData(predicate, pageable);

		model.addAttribute(KEY_USER_ID, BlackboxSecurityContextHolder.getUserId());
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_UPDATE_REFERENCE);
		model.addAttribute("page", data);

		return "update.reference.records";
	}

	/**
	 * View duplicate check.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/duplicateCheck", method = RequestMethod.POST)
	public String viewDuplicateCheck(Model model) {
		if (!BlackboxSecurityContextHolder.canAccessUrl(KEY_REFERENCE_DASHBOARD)) {
			return "redirect:management";
		}

		String notificationMessage = notificationService
				.getNotificationMessage(NotificationProcessType.NPL_DUPLICATE_CHECK);
		model.addAttribute(KEY_NOTIFICATION_MESSAGE, notificationMessage);
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_DUPLICATE_CHECK);

		return "duplicate.check.reference.header";
	}

	/**
	 * View duplicate check records.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/duplicateCheck/records", method = RequestMethod.POST)
	public String viewDuplicateCheckRecords(HttpServletRequest request, Model model) {
		Pageable pageable = getDuplicateCheckRefPageInfo(request);
		Predicate predicate = getDuplicateCheckRefPredicate(request);
		Page<ReferenceDashboardDto> data = referenceDashboardService.getDuplicateCheckRefData(predicate, pageable);

		model.addAttribute(KEY_USER_ID, BlackboxSecurityContextHolder.getUserId());
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_DUPLICATE_CHECK);
		model.addAttribute("page", data);
		return "duplicate.check.reference.records";
	}

	/**
	 * Verify duplicate check.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping("/dashboard/verifyDuplicateCheck")
	public String verifyDuplicateCheck(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes)
			throws ServletRequestBindingException {
		Long notificationProcessId = ServletRequestUtils.getLongParameter(request, KEY_NOTIFICATION_PROCESS_ID);
		Long referenceBaseId = ServletRequestUtils.getLongParameter(request, KEY_REFERENCE_BASE_ID);
		String notifiedDate = ServletRequestUtils.getStringParameter(request, KEY_NOTIFIED_DATE);

		// redirectAttributes.addFlashAttribute("notificationProcessId", notificationProcessId);
		// redirectAttributes.addFlashAttribute("referenceBaseId", referenceBaseId);
		// redirectAttributes.addFlashAttribute("notifiedDate", notifiedDate);

		try {
			notificationProcessService.assignNotificationToUser(BlackboxSecurityContextHolder.getUserId(),
					notificationProcessId);
		} catch (ActivitiTaskAlreadyClaimedException exception) {
			LOGGER.debug(KEY_NOTIFICATION_DEBUG_MESSAGE + notificationProcessId, exception);
			redirectAttributes.addAttribute(KEY_REQUEST_COME_FROM, KEY_DUPLICATE_CHECK);
			return KEY_REDIRECT_DASHBOARD;
		}

		ReferenceDashboardDto dto = referenceDashboardService.getDuplicateNPLReference(referenceBaseId);
		dto.setNotifiedDate(notifiedDate);
		dto.setReferenceBaseId(referenceBaseId);
		dto.setNotificationProcessId(notificationProcessId);
		List<ReferenceDashboardDto> dtos = referenceService.getNPLDuplicates(dto);
		model.addAttribute("dtos", dtos);
		model.addAttribute("dto", dto);
		return "verify-duplicate-npl";

	}

	/**
	 * Open duplicate check page.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param principal
	 *            the principal
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping("/dashboard/openDuplicateCheckPage")
	public String openDuplicateCheckPage(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes)
			throws ServletRequestBindingException {
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

		Long notificationProcessId = null;
		Long referenceBaseId = null;
		String notifiedDate = null;

		if (flashMap == null || flashMap.get(KEY_NOTIFICATION_PROCESS_ID) == null) {
			redirectAttributes.addAttribute(KEY_REQUEST_COME_FROM, KEY_DUPLICATE_CHECK);
			return KEY_REDIRECT_DASHBOARD;
		} else {
			notificationProcessId = (Long) flashMap.get(KEY_NOTIFICATION_PROCESS_ID);
			referenceBaseId = (Long) flashMap.get(KEY_REFERENCE_BASE_ID);
			notifiedDate = (String) flashMap.get(KEY_NOTIFIED_DATE);
		}

		ReferenceDashboardDto dto = referenceDashboardService.getDuplicateNPLReference(referenceBaseId);
		dto.setNotifiedDate(notifiedDate);
		dto.setReferenceBaseId(referenceBaseId);
		dto.setNotificationProcessId(notificationProcessId);
		List<ReferenceDashboardDto> dtos = referenceService.getNPLDuplicates(dto);
		model.addAttribute("dtos", dtos);
		model.addAttribute("dto", dto);
		return "verify-duplicate-npl";
	}

	/**
	 * Duplicate check action.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/dashboard/duplicateCheckAction", method = RequestMethod.POST)
	@ResponseBody
	public String duplicateCheckAction(HttpServletRequest request) throws ServletRequestBindingException {
		Long referenceBaseId = ServletRequestUtils.getLongParameter(request, KEY_REFERENCE_BASE_ID);
		Long notificationProcessId = ServletRequestUtils.getLongParameter(request, KEY_NOTIFICATION_PROCESS_ID);
		boolean isDuplicate = ServletRequestUtils.getBooleanParameter(request, "isDuplicate");
		referenceService.updateNPLReferenceManually(referenceBaseId, isDuplicate, notificationProcessId);

		return "true";
	}

	/**
	 * Delete update reference.
	 *
	 * @param request
	 *            the request
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/dashboard/deleteUpdateReference", method = RequestMethod.POST)
	public String deleteUpdateReference(HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws ServletRequestBindingException {
		Long referenceStagingId = ServletRequestUtils.getLongParameter(request, "referenceStagingId");
		Long notificationProcessId = ServletRequestUtils.getLongParameter(request, KEY_NOTIFICATION_PROCESS_ID);

		try {
			notificationProcessService.assignNotificationToUser(BlackboxSecurityContextHolder.getUserId(),
					notificationProcessId);
		} catch (ActivitiTaskAlreadyClaimedException exception) {
			LOGGER.debug(KEY_NOTIFICATION_DEBUG_MESSAGE + notificationProcessId, exception);
			redirectAttributes.addAttribute(KEY_REQUEST_COME_FROM, KEY_UPDATE_REFERENCE);
			return KEY_REDIRECT_DASHBOARD;
		}

		referenceDashboardService.deleteUpdateRefFromStaging(referenceStagingId, notificationProcessId);

		redirectAttributes.addAttribute(KEY_REQUEST_COME_FROM, KEY_UPDATE_REFERENCE);
		return KEY_REDIRECT_DASHBOARD;
	}

	/**
	 * Adds the details update reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @param referenceDTO
	 *            the reference dto
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/dashboard/addDetailsUpdateReference", method = RequestMethod.POST)
	public String addDetailsUpdateReference(HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes, @ModelAttribute("referenceDTO") ReferenceBaseDTO referenceDTO)
					throws ServletRequestBindingException {
		Long notificationProcessId = ServletRequestUtils.getLongParameter(request, KEY_NOTIFICATION_PROCESS_ID);
		ReferenceDashboardDto dto = referenceDashboardService.getUpdateRefDto(notificationProcessId);
		model.addAttribute("dto", dto);
		CorrespondenceDTO correspondence = new CorrespondenceDTO();
		correspondence.setId(dto.getCorrespondenceId());
		referenceDTO.setCorrespondenceId(correspondence);
		referenceDTO.setApplicationNumber(dto.getApplicationNumber());
		referenceDTO.setNotificationProcessId(dto.getNotificationProcessId());
		referenceDTO.setApplicationFilingDate(dto.getApplicationFilingDate());
		referenceDTO.setTypeOfNumber(dto.getTypeOfNumber());
		referenceDTO.setApplicationIssuedOn(dto.getApplicationIssuedOn());
		referenceDTO.setApplicationJurisdictionType(dto.getSourceJurisdictionCode());
		referenceDTO.setRefStagingId(dto.getReferenceStagingId());
		referenceDTO.setApplicationJurisdictionCode(dto.getSourceJurisdictionCode());

		try {
			notificationProcessService.assignNotificationToUser(BlackboxSecurityContextHolder.getUserId(),
					notificationProcessId);
		} catch (ActivitiTaskAlreadyClaimedException exception) {
			LOGGER.debug(KEY_NOTIFICATION_DEBUG_MESSAGE + notificationProcessId, exception);
			redirectAttributes.addAttribute(KEY_REQUEST_COME_FROM, KEY_UPDATE_REFERENCE);
			return KEY_REDIRECT_DASHBOARD;
		}

		return "add-reference-detail";
	}

	/**
	 * Submit add details update reference.
	 *
	 * @param referenceDTO
	 *            the reference dto
	 * @param result
	 *            the result
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @param model
	 *            the model
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/dashboard/submitAddDetailsUpdateReference", method = RequestMethod.POST)
	public String submitAddDetailsUpdateReference(@ModelAttribute("referenceDTO") ReferenceBaseDTO referenceDTO,
			RedirectAttributes redirectAttributes) throws ServletRequestBindingException {
		LOGGER.info(
				String.format("Reference is created for notification id %d", referenceDTO.getReferenceBaseDataId()));
		referenceDashboardService.addReferenceDetails(referenceDTO);

		redirectAttributes.addAttribute(KEY_REQUEST_COME_FROM, KEY_UPDATE_REFERENCE);
		return KEY_REDIRECT_DASHBOARD;
	}

	/**
	 * New self cited reference.
	 *
	 * @return the string
	 */
	@RequestMapping("/dashboard/newSelfCitedReference")
	public String newSelfCitedReference(final Model model) {
		model.addAttribute(Constants.LIST_JURISDICTIONS,
				populateJurisdictionList(masterDataService.getAllJurisdictions()));
		return "new-reference-self-citation";
	}

	/**
	 * Submit self cited reference.
	 *
	 * @param referenceDTO
	 *            the reference dto
	 * @param result
	 *            the result
	 * @param ra
	 *            the ra
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/submitSelfCitedReference", method = RequestMethod.POST)
	public String submitSelfCitedReference(@ModelAttribute("referenceDTO") ReferenceBaseDTO referenceDTO) {

		referenceDTO.setReferenceCatogory(ReferenceCategoryType.SELF_CITATION);
		referenceDTO.setSelfcited(true);
		if (referenceDTO.isManualAdd()) {
			referenceService.addReference(referenceDTO);
		} else {
			referenceService.addReferenceStagingData(referenceDTO);
		}

		return KEY_REDIRECT_DASHBOARD;
	}

	/**
	 * Creates the self cited reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/createSelfCitedReference", method = RequestMethod.POST)
	public String createSelfCitedReference(HttpServletRequest request, Model model) {
		String app = request.getParameter(KEY_APPLICATION_NUMBER);
		String juris = request.getParameter(KEY_JURIS_CODE);
		model.addAttribute(KEY_APPLICATION_NUMBER, app);
		model.addAttribute(KEY_JURIS_CODE, juris);
		ReferenceBaseDTO dto = new ReferenceBaseDTO();
		dto.setApplicationJurisdictionCode(juris);
		dto.setApplicationJurisdictionType(juris);
		dto.setApplicationNumber(app);

		// set jurisdiction
		JurisdictionDTO jdto = new JurisdictionDTO();
		jdto.setName(juris);
		dto.setJurisdiction(jdto);

		// add application details
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByApplicationNoAndJurisdiction(juris, app);

		dto.setApplicationFilingDate(
				BlackboxDateUtil.dateToStr(mdmDtoList.get(0).getFilingDate(), TimestampFormat.YYYYMMDD));
		dto.setTypeOfNumber(PUBLICATION);
		dto.setGrantDate(Calendar.getInstance());

		model.addAttribute("referenceDTO", dto);
		model.addAttribute(Constants.LIST_JURISDICTIONS,
				populateJurisdictionList(masterDataService.getAllJurisdictions()));
		return "create-reference-self-citation";
	}

	/**
	 * App details by application.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/appDetailsByApplication", method = RequestMethod.POST)
	public String appDetailsByApplication(HttpServletRequest request, Model model) {

		String jurisCode = request.getParameter(KEY_JURIS_CODE);
		String appNo = request.getParameter(KEY_APPLICATION_NUMBER);
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByApplicationNoAndJurisdiction(jurisCode,
				appNo);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return KEY_NEW_REFERENCE_SELF_CITATION_SEARCH_LIST;
	}

	/**
	 * App details.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/appDetailsByFamily", method = RequestMethod.POST)
	public String appDetails(HttpServletRequest request, Model model) {

		String familyId = request.getParameter(KEY_FAMILY_VALUE);
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByFamily(familyId);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return KEY_NEW_REFERENCE_SELF_CITATION_SEARCH_LIST;
	}

	/**
	 * App details by att docket.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/appDetailsByAttDocket", method = RequestMethod.POST)
	public String appDetailsByAttDocket(HttpServletRequest request, Model model) {

		String attorneyDocket = request.getParameter(KEY_ATTORNEY);
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByAttorneyDocket(attorneyDocket);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return KEY_NEW_REFERENCE_SELF_CITATION_SEARCH_LIST;
	}

	/**
	 * Adds the reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("/dashboard/addReference")
	public String addReference(HttpServletRequest request, Model model) {
		ReferenceRecordDTO referenceRecordDTO = fetchDataForAddingReference(request, model, true);

		ReferenceBaseDTO referenceDTO = new ReferenceBaseDTO();
		referenceDTO.setApplicationNumber(referenceRecordDTO.getApplicationNumber());
		referenceDTO.setApplicationJurisdictionCode(referenceRecordDTO.getJurisdictionCode());
		referenceDTO.setMailingDateStr(referenceRecordDTO.getMailingDate());
		referenceDTO.setDocumentDescription(referenceRecordDTO.getDocumentDescription());
		referenceDTO.setDocumentUploadedByUser(referenceRecordDTO.getUpdatedBy());
		referenceDTO.setDocumentUploadedOn(referenceRecordDTO.getUploadedOn());
		referenceDTO.setDocumentUpdatedOn(referenceRecordDTO.getUpdatedOn());
		referenceDTO.setDocumentUpdatedByUser(referenceRecordDTO.getUpdatedBy());
		referenceDTO.setOcrStatus(OcrStatus.valueOf(referenceRecordDTO.getOcrStatus()));
		referenceDTO.setCorrespondenceId(referenceRecordDTO.getCorrespondenceDto());
		referenceDTO.setApplicationJurisdictionCode(referenceRecordDTO.getJurisdictionCode());
		referenceDTO.setApplicationFilingDate(referenceRecordDTO.getApplicationFilingDate());
		referenceDTO.setTypeOfNumber(referenceRecordDTO.getTypeOfNumber());
		referenceDTO.setApplicationIssuedOn(referenceRecordDTO.getApplicationIssuedOn());
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);
		model.addAttribute("referenceDTO", referenceDTO);

		return "add-reference";
	}

	/**
	 * Adds the reference for failed.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("/dashboard/addReferenceForFailed")
	public String addReferenceForFailed(HttpServletRequest request, Model model) {
		fetchDataForAddingReference(request, model, true);
		model.addAttribute(Constants.LIST_JURISDICTIONS,
				populateJurisdictionList(masterDataService.getAllJurisdictions()));
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);
		return "add-reference-ocr-fail";
	}

	/**
	 * Adds the reference for done.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("/dashboard/addReferenceForDone")
	public String addReferenceForDone(HttpServletRequest request, Model model) {
		fetchDataForAddingReference(request, model, true);
		model.addAttribute(Constants.LIST_JURISDICTIONS,
				populateJurisdictionList(masterDataService.getAllJurisdictions()));
		model.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);
		return "add-reference-ocr-done";
	}

	/**
	 * Fetch data for adding reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param lockNotification
	 *            the lock notification
	 * @return the reference record dto
	 */
	private ReferenceRecordDTO fetchDataForAddingReference(HttpServletRequest request, Model model,
			boolean lockNotification) {
		String notificationProcessIdStr = request.getParameter(Constants.KEY_ID);
		String ocrDataIdStr = request.getParameter(OCR_KEY_ID);
		ReferenceRecordDTO referenceRecordDTO = null;
		if (!StringUtils.isEmpty(notificationProcessIdStr) && !StringUtils.isEmpty(ocrDataIdStr)) {
			Long notificationProcessId = Long.valueOf(notificationProcessIdStr);
			Long ocrDataId = Long.valueOf(ocrDataIdStr);
			Long lockedByUser = BlackboxSecurityContextHolder.getUserId();
			ReferenceEntryDTO referenceEntryDTO = null;

			// TODO to be uncommented.....currently we are not locking the notification.
			/*
			 * if (lockNotification) { referenceEntryDTO =
			 * referenceDashboardService.fetchAndLockNotification(notificationProcessId, ocrDataId, lockedByUser); }
			 * else { referenceEntryDTO = referenceDashboardService.fetchNotification(notificationProcessId, ocrDataId,
			 * lockedByUser); }
			 */
			// TODO to be removed once we started locking the notification...
			referenceEntryDTO = referenceDashboardService.fetchNotification(notificationProcessId, ocrDataId,
					lockedByUser);
			referenceRecordDTO = getReferenceRecord(referenceEntryDTO);
			setUpdatedByUserDetails(referenceEntryDTO, referenceRecordDTO);
			referenceRecordDTO.setNotificationProcessId(notificationProcessId);
			model.addAttribute(Constants.KEY_ID, notificationProcessIdStr);
			model.addAttribute(OCR_KEY_ID, ocrDataIdStr);
			model.addAttribute(Constants.CORRESPONDENCE_ID, referenceRecordDTO.getCorrespondenceDto().getId());
			model.addAttribute("referenceRecord", referenceRecordDTO);
			model.addAttribute(Constants.CLOSE_NOTIFICATION, true);
			model.addAttribute(Constants.REDIRECT_TO_DASHBOARD, true);
		}
		return referenceRecordDTO;
	}

	/**
	 * Delete document.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @return the string
	 */
	@RequestMapping(value = "/dashboard/deleteDocument", method = RequestMethod.POST)
	public String deleteDocument(HttpServletRequest request, RedirectAttributes redirectAttributes) {

		String notificationProcessIdStr = request.getParameter(Constants.KEY_ID);

		if (!StringUtils.isEmpty(notificationProcessIdStr)) {
			Long notificationProcessId = Long.valueOf(notificationProcessIdStr);
			referenceService.deleteDocument(notificationProcessId);
		}

		redirectAttributes.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);

		return KEY_REDIRECT_DASHBOARD;

	}

	/**
	 * Delete document confirmation.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("/dashboard/deleteDocumentConfirmation")
	public String deleteDocumentConfirmation(HttpServletRequest request, Model model) {

		String notificationProcessIdStr = request.getParameter(Constants.KEY_ID);
		model.addAttribute("notificationProcessIdStr", notificationProcessIdStr);
		return "delete-reference-popup";
	}

	/**
	 * Adds the details.
	 *
	 * @return the string
	 */
	@RequestMapping("/dashboard/addDetails")
	public String addDetails() {
		return "add-reference-detail";
	}

	/**
	 * Adds the reference auto add.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("/dashboard/addReferenceAutoAdd")
	public String addReferenceAutoAdd(HttpServletRequest request, Model model) {
		fetchDataForAddingReference(request, model, true);
		return "add-reference-auto-add";
	}

	/**
	 * Adds the null reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/dashboard/addNullReference", method = RequestMethod.POST)
	public String addNullReference(HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws ServletRequestBindingException {

		Long notificationProcessId = ServletRequestUtils.getLongParameter(request, Constants.KEY_ID);
		Long ocrDataId = ServletRequestUtils.getLongParameter(request, OCR_KEY_ID);
		Long correspondenceid = ServletRequestUtils.getLongParameter(request, Constants.CORRESPONDENCE_ID);
		String nplString = referenceDashboardService.createNPLStringForNullReferenceDoc(correspondenceid);
		referenceDashboardService.createNullReferenceDocumentAndCloseNotification(correspondenceid, nplString,
				ocrDataId, notificationProcessId);
		redirectAttributes.addAttribute(KEY_DASHBOARD_SUB_MENU, KEY_REFERENCE_ENTRY);

		return KEY_REDIRECT_DASHBOARD;
	}

	/**
	 * Gets the update reference predicate.
	 *
	 * @param request
	 *            the request
	 * @return the update reference predicate
	 */
	private static Predicate getUpdateReferencePredicate(final HttpServletRequest request) {
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		String juris = request.getParameter(DatatableAttribute.JURISDICTION.attrName);
		String dateRange = request.getParameter(DatatableAttribute.DATE_RANGE.attrName);
		return ReferencePredicate.getUpdateReferencePredicate(juris, myRecords, dateRange);
	}

	/**
	 * Gets the update reference page info.
	 *
	 * @param request
	 *            the request
	 * @return the update reference page info
	 */
	private static Pageable getUpdateReferencePageInfo(final HttpServletRequest request) {
		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);
		String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);

		final Integer limit = StringUtil.isBlank(dispStart) ? null : Integer.parseInt(dispStart);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);
		Integer offset = StringUtil.isBlank(dispLength) ? null : Integer.parseInt(dispLength);

		switch (sortBy) {
		case "0":
			sortBy = "jurisdiction.code";
			break;
		case "1":
			sortBy = "publicationNumber";
			break;
		case "3":
			sortBy = KEY_NOTIFIED_DATE;
			break;
		default:
			sortBy = "jurisdiction.code";
			break;
		}

		if (offset != ZERO) {
			offset = offset / limit;
		}

		Sort sort = isAsc ? new Sort(new Order(Direction.ASC, sortBy)) : new Sort(new Order(Direction.DESC, sortBy));
		PageRequest page = new PageRequest(offset, limit, sort);

		return page;
	}

	/**
	 * Gets the duplicate check ref predicate.
	 *
	 * @param request
	 *            the request
	 * @return the duplicate check ref predicate
	 */
	private static Predicate getDuplicateCheckRefPredicate(final HttpServletRequest request) {
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		String juris = request.getParameter(DatatableAttribute.JURISDICTION.attrName);
		String dateRange = request.getParameter(DatatableAttribute.DATE_RANGE.attrName);

		return ReferencePredicate.getDuplicateRefCheckPredicate(juris, myRecords, dateRange);
	}

	/**
	 * Gets the duplicate check ref page info.
	 *
	 * @param request
	 *            the request
	 * @return the duplicate check ref page info
	 */
	private static Pageable getDuplicateCheckRefPageInfo(final HttpServletRequest request) {
		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);
		String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);

		final Integer limit = StringUtil.isBlank(dispStart) ? null : Integer.parseInt(dispStart);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);
		Integer offset = StringUtil.isBlank(dispLength) ? null : Integer.parseInt(dispLength);

		switch (sortBy) {
		case "0":
			sortBy = "stringData";
			break;
		case "2":
			sortBy = KEY_NOTIFIED_DATE;
			break;
		default:
			sortBy = "stringData";
			break;
		}

		if (offset != ZERO) {
			offset = offset / limit;
		}

		Sort sort = isAsc ? new Sort(new Order(Direction.ASC, sortBy)) : new Sort(new Order(Direction.DESC, sortBy));
		PageRequest page = new PageRequest(offset, limit, sort);

		return page;
	}

	/**
	 * Gets the reference entry page info.
	 *
	 * @param request
	 *            the request
	 * @return the reference entry page info
	 */
	private static Pageable getReferenceEntryPageInfo(final HttpServletRequest request) {
		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);
		String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);

		final Integer limit = StringUtil.isBlank(dispStart) ? 5 : Integer.parseInt(dispStart);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);
		Integer offset = StringUtil.isBlank(dispLength) ? 1 : Integer.parseInt(dispLength);
		switch (sortBy) {
		case "0":
			sortBy = KEY_JURIS_CODE;
			break;
		case "1":
			sortBy = "correspondenceId.application.applicationNumber";
			break;
		case "2":
			sortBy = "correspondenceId.mailingDate";
			break;
		case "3":
			sortBy = "correspondenceId.documentCode.description";
			break;
		case "4":
			sortBy = "correspondenceId.updatedByUser";
			break;
		case "5":
			sortBy = "ocrStatus";
			break;
		case "6":
			sortBy = KEY_NOTIFIED_DATE;
			break;

		default:
			sortBy = KEY_JURIS_CODE;
			break;
		}

		if (offset != ZERO) {
			offset = offset / limit;
		}

		Sort sort = isAsc ? new Sort(new Order(Direction.ASC, sortBy)) : new Sort(new Order(Direction.DESC, sortBy));
		return new PageRequest(offset, limit, sort);
	}

	/**
	 * Populate reference entry data.
	 *
	 * @param referenceGroup
	 *            the reference group
	 * @param pageInfo
	 *            the page info
	 * @return the page
	 */
	private Page<ReferenceRecordDTO> populateReferenceEntryData(Page<ReferenceEntryDTO> referenceGroup,
			Pageable pageInfo) {

		List<ReferenceRecordDTO> referenceRecordList = new ArrayList<ReferenceRecordDTO>();
		ReferenceRecordDTO referenceRecordDTO = null;
		List<ReferenceEntryDTO> refEntryList = referenceGroup.getContent();
		for (ReferenceEntryDTO refEntryDTO : refEntryList) {
			referenceRecordDTO = getReferenceRecord(refEntryDTO, true);
			referenceRecordList.add(referenceRecordDTO);
		}

		return new PageImpl<ReferenceRecordDTO>(referenceRecordList, pageInfo, referenceGroup.getTotalElements());

	}

	/**
	 * Gets the reference record.
	 *
	 * @param referenceEntryDTO
	 *            the reference entry dto
	 * @return the reference record
	 */
	private ReferenceRecordDTO getReferenceRecord(ReferenceEntryDTO referenceEntryDTO) {
		ReferenceRecordDTO referenceRecordDTO = null;
		if (referenceEntryDTO != null) {
			referenceRecordDTO = new ReferenceRecordDTO();
			CorrespondenceDTO correspondenceDTO = new CorrespondenceDTO();
			correspondenceDTO.setId(referenceEntryDTO.getCorrespondenceId());
			Calendar mailingDate = referenceEntryDTO.getMailingDate();
			String mailingDateStr = BlackboxDateUtil.calendarToStr(mailingDate, TimestampFormat.MMMDDYYYY);
			Long CreatedByUserId = referenceEntryDTO.getCorrespondenceCreatedByUser();
			Long currentId = BlackboxSecurityContextHolder.getUserId();
			String createdByUserName = USER_ME;
			if (CreatedByUserId != currentId) {
				UserDTO createdByUser = userRepositoryImpl.getUserDetailsById(CreatedByUserId);
				createdByUserName = createdByUser.getFirstName() + " " + createdByUser.getLastName();
			}

			Calendar createdDate = referenceEntryDTO.getCorrespondenceCreatedDate();
			String createdDateStr = BlackboxDateUtil.calendarToStr(createdDate, TimestampFormat.MMMDDYYYY);
			referenceRecordDTO.setApplicationNumber(referenceEntryDTO.getApplicationNumber());
			referenceRecordDTO.setJurisdictionCode(referenceEntryDTO.getJurisdictionCode());
			referenceRecordDTO.setMailingDate(mailingDateStr);
			referenceRecordDTO.setDocumentDescription(referenceEntryDTO.getDocumentDescription());
			referenceRecordDTO.setOcrStatus(referenceEntryDTO.getOcrStatus().toString());
			referenceRecordDTO.setUploadedBy(createdByUserName);
			referenceRecordDTO.setUploadedOn(createdDateStr);
			referenceRecordDTO.setOcrDataId(referenceEntryDTO.getOcrDataId());
			referenceRecordDTO.setNotificationProcessId(referenceEntryDTO.getNotificationProcessId());
			referenceRecordDTO.setCorrespondenceDto(correspondenceDTO);
			referenceRecordDTO.setTypeOfNumber(referenceEntryDTO.getTypeOfNumber());
			referenceRecordDTO.setApplicationFilingDate(referenceEntryDTO.getApplicationFilingDate());
			referenceRecordDTO.setApplicationIssuedOn(referenceEntryDTO.getApplicationIssuedOn());
			// TODO Location is to be included. Fetch the base location from
			// context.
			// referenceRecordDTO.setDocumentLocation(documentLocation);
		}

		return referenceRecordDTO;
	}

	/**
	 * Sets the updated by user details.
	 *
	 * @param referenceEntryDTO
	 *            the reference entry dto
	 * @param referenceRecordDTO
	 *            the reference record dto
	 */
	private void setUpdatedByUserDetails(ReferenceEntryDTO referenceEntryDTO,
			final ReferenceRecordDTO referenceRecordDTO) {
		Long updatedByUserId = referenceEntryDTO.getCorrespondenceUpdatedByUser();
		if (updatedByUserId != null) {
			UserDTO updatedByUser = userRepositoryImpl.getUserDetailsById(updatedByUserId);
			String updatedByUserName = updatedByUser.getFirstName() + " " + updatedByUser.getLastName();
			Calendar updatedDate = referenceEntryDTO.getCorrespondenceUpdatedDate();
			String updatedDateStr = BlackboxDateUtil.calendarToStr(updatedDate, TimestampFormat.MMMDDYYYY);
			referenceRecordDTO.setUpdatedBy(updatedByUserName);
			referenceRecordDTO.setUpdatedOn(updatedDateStr);
		}
	}

	/**
	 * Gets the reference record.
	 *
	 * @param referenceEntryDTO
	 *            the reference entry dto
	 * @param addNotificationData
	 *            the add notification data
	 * @return the reference record
	 */
	private ReferenceRecordDTO getReferenceRecord(ReferenceEntryDTO referenceEntryDTO, boolean addNotificationData) {
		ReferenceRecordDTO referenceRecordDTO = getReferenceRecord(referenceEntryDTO);
		Long lockedByUserId = referenceEntryDTO.getLockedByUser();
		Long currentId = BlackboxSecurityContextHolder.getUserId();

		if (lockedByUserId != null && lockedByUserId != currentId) {
			referenceRecordDTO.setLocked(true);
			referenceRecordDTO.setLockedByUser(
					referenceEntryDTO.getLockedByUserLastName() + " " + referenceEntryDTO.getLockedByUserFirstName());
		} else {
			referenceRecordDTO.setLocked(false);

		}
		Calendar notifiedDate = referenceEntryDTO.getNotificationDate();
		String notifiedDateStr = BlackboxDateUtil.calendarToStr(notifiedDate, TimestampFormat.MMMDDYYYY);
		referenceRecordDTO.setNotifiedOn(notifiedDateStr);
		referenceRecordDTO.setNotificationProcessId(referenceEntryDTO.getNotificationProcessId());
		return referenceRecordDTO;
	}

	/**
	 * Creates the reference entry filter.
	 *
	 * @param request
	 *            the request
	 * @return the reference entry filter
	 */
	private ReferenceEntryFilter createReferenceEntryFilter(final HttpServletRequest request) {

		ReferenceEntryFilter filter = new ReferenceEntryFilter();

		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		String juris = request.getParameter(DatatableAttribute.JURISDICTION.attrName);
		String dateRange = request.getParameter(DatatableAttribute.DATE_RANGE.attrName);
		Set<Long> roles = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();

		filter.setMyRecords(myRecords);
		filter.setJurisdiction(juris);
		filter.setDateRange(dateRange);
		filter.setRoles(roles);

		return filter;
	}

	/**
	 * Populate jurisdiction list.
	 *
	 * @param jurisdictionList
	 *            the jurisdiction list
	 * @return the string
	 */
	private static String populateJurisdictionList(List<com.blackbox.ids.dto.JurisdictionDTO> jurisdictionList) {
		List<String> jurisdictions = new ArrayList<>();
		for (com.blackbox.ids.dto.JurisdictionDTO jurisdictionDTO : jurisdictionList) {
			jurisdictions.add(jurisdictionDTO.getName());
		}
		return StringUtils.join(jurisdictions, ";");
	}
}