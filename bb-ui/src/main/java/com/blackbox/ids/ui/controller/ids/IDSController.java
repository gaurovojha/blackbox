package com.blackbox.ids.ui.controller.ids;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.tags.NestedPathTag;
import org.springframework.web.servlet.tags.form.FormTag;

import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordsFilter;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingInProgressDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingReadyDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDS1449ReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSAttorneyApprovalDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSDrillDownInfoDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSFilingPackageDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSManuallyFiledDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSPendingApprovalDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.InitiateIDSRecordDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.N1449DetailDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.N1449PendingDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.UpdateRefStatusDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.ValidateRefStatusDTO;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.ids.IDSBuildService;
import com.blackbox.ids.services.ids.IDSDashboardService;
import com.blackbox.ids.services.ids.IDSWorkflowService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.ReferenceManagementService;
import com.blackbox.ids.ui.controller.BaseController;
import com.blackbox.ids.ui.form.ids.IDSCertificationForm;
import com.blackbox.ids.ui.form.ids.UpdateRefStatusForm;
import com.blackbox.ids.ui.validation.ids.AppUpdateValidator;
import com.blackbox.ids.ui.validation.ids.CertificationStatementValidator;
import com.blackbox.ids.workflows.ids.common.ControlFlags.AttorneyResponse;
import com.blackbox.ids.workflows.ids.common.ControlFlags.ParallegalAction;

@Controller
@RequestMapping(value = "/ids")
public class IDSController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(IDSController.class);

	private static final String APPLICATION_ID = "appId";
	private static final String IDS_ID = "idsID";
	private static final String REFERENCE_BASE_ID = "refBaseId";
	private static final String CORRESPONDENCE_ID = "corrId";
	private static final String REFERENCE_SOURCE_DOCUMENT = "referenceSourceDocument";
	private static final String SOURCE_DOCUMENT_OTHER_REFERENCES = "sourceDocOtherReferences";

	private static final Long NOTIFICATION_PAGE_NUMBER = 1L;
	private static final Long NOTIFICATIONS_PER_PAGE = 2L;
	private static final Long REFERENCES_PER_PAGE = 4L;

	private static final String KEY_SEARCH_RESULT = "searchResult";
	private static final String MODULE_NAME = "module";

	private static final String LIST_ITEMS = "listItems";
	private static final String IDS_NOTIFICATION = "idsNotifications";
	private static final String PENDING_APP_NOTIFICATION_LIST = "appNotificationsList";
	private static final String PENDING_FAMILY_NOTIFICATION_LIST = "familyNotificationsList";
	private static final String APP_CURRENT_PAGE = "currentPageApp";
	private static final String APP_NO_OF_PAGES = "noOfPagesApp";
	private static final String FAMILY_CURRENT_PAGE = "currentFamilyPage";
	private static final String FAMILY_NO_OF_PAGES = "noOfPagesFamily";
	private static final String APP_SKIP_NOTIFICATION_LIST = "skipAppNotificationList";
	private static final String FAMILY_SKIP_NOTIFICATION_LIST = "skipFamilyNotificationList";
	private static final String NOTIFICATION_ID = "notificationId";
	private static final String FAMILY_NOTIFICATION_DATA = "familyNotificationData";
	private static final String APP_NOTIFICATION_DATA = "appNotificationData";
	private static final String SELECTED_ACCORDION = "selectedAccordion";
	private static final String PAGE = "page";
	private static final String APP_ACCORDION = "collapseOne";
	private static final String FAMILY_ACCORDION = "collapseTwo";
	private static final String COUNT_NOTIFICATIONS = "countNotification";
	private static final String KEY_CORRESPONDENCE = "correspondenceId";
	private static final String KEY_REFERENCE_DTO = "referenceDTO";
	private static final String PROSECUTION_STATUS = "prosecutionStatus";
	private static final String REFERNCE_AGE = "referenceAge";
	private static final String REF_FLOWS_IDS = "refFlowsIds";
	private static final String US_PATENT      = "US Patents";
	private static final String US_PUBLICATION = "US Publication";
	private static final String FOREIGN 	   = "Foreign";
	private static final String NPL 	       = "NPL";
	private static final String US_PATENT_TYPE = "PUS";
	private static final String REFERENCE_COUNT = "countMap";
	private static final String REFERENCE_LIST = "referenceList";
	private static final String PARALLEGAL_COMMENTS = "comment";
	private static final String FILE_IDS_CERTIFICATE = "fileIdsCertificate";
	private static final String INITATE_IDS_CERTIFICATE = "initiateIdsCertificate";
	private static final String PENDING_APPROVAL_COUNT = "pendingApprovalCount";
	private static final String PENDING_RESPONSE_COUNT = "pendingResponseCount";
	private static final String IDS_PENDING_COUNT = "idsPendingCount";
	
	@Autowired
	private IDSDashboardService idsDashboardService;

	@Autowired
	private IDSBuildService idsBuildService;

	@Autowired
	private IDSWorkflowService idsWorkflowService;

	@Autowired
	private AppUpdateValidator applicationValidator;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private NotificationProcessService notificationService;

	@Autowired
	private ReferenceManagementService referenceManagementService;
	
	/** The master data service. */
	@Autowired
	private MasterDataService masterDataService;
	
	@Autowired
	private IDSWorkflowService iDSWorkflowService;

	@Autowired
	private CertificationStatementValidator certificateValidator;

	public enum RecordsView {
		APPLICATION_VIEW, FAMILY_VIEW;
		public static final String MODEL_KEY = "recordsView";
	}

	enum BuildIDSView {
		ADMIIN_FOREIGN_RECORDS("foreign"),
		FOREIGN_RECORDS("foreignTableRows"),
		ADMIIN_USPUBLICATION_RECORDS("usPublication"),
		USPUBLICATION_RECORDS("usTableRows"),
		ADMIIN_USPATENT_RECORDS("usPatent"),
		USPATENT_RECORDS("usTableRows"),
		ADMIIN_NPL_RECORDS("npl"),
		NPL_RECORDS("nplTableRows"),
		CONFIRMATION_PAGE("confirmationPage"),
		CERTIFICATION_STATEMENT("certificate"),
		SUCCESS("success");


		private String ViewName ;

		public final String viewName;


		private BuildIDSView(String viewName) {
			this.viewName = viewName;
		}
	}

	public enum ApplicationScreen {
		INIT, DETAILS;

		public static ApplicationScreen fromString(String strScreen) {
			ApplicationScreen screen = null;
			for (final ApplicationScreen e : ApplicationScreen.values()) {
				if (e.name().equalsIgnoreCase(strScreen)) {
					screen = e;
					break;
				}
			}
			return screen;
		}
	}

	public enum UserSelectedStatus {
		DO_NOT_FILE_IDS, I_HAVE_FILED_IDS
	}

	/*- ----------------------------------------------------------------
		My Dashboard - Active Records - Application View
	------------------------------------------------------------------- */
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String viewDashboard(final Model model, final HttpSession session) {
		model.addAttribute(RecordsView.MODEL_KEY, RecordsView.APPLICATION_VIEW.name());
		model.addAttribute(IDS_PENDING_COUNT, idsDashboardService.getIDSPendingApprovalRecordsCount());
		return "idsDashboard";
	}

	@RequestMapping(value = "/initiateIDSAllRecords/view", method = RequestMethod.POST)
	public String viewInitiateIDSAllRecords(final HttpServletRequest request, final Model model) {

		PaginationInfo pageInfo = getPaginationInfo(request);

		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName), ownedBy);

		SearchResult<InitiateIDSRecordDTO> result = idsDashboardService.getInitiateIDSRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "initiateIDSAllRecords";

	}

	@RequestMapping(value = "/initiateIDSUrgentRecords/view", method = RequestMethod.POST)
	public String viewInitiateIDSUrgentRecords(final HttpServletRequest request, final Model model) {

		PaginationInfo pageInfo = getPaginationInfo(request);

		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName),
				request.getParameter(DatatableAttribute.JURISDICTION.attrName),
				request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName),
				request.getParameter(DatatableAttribute.FAMILY_ID.attrName),
				request.getParameter(DatatableAttribute.DOCUMENT_DESCRIPTION.attrName),
				request.getParameter(DatatableAttribute.UPLOADED_BY.attrName),
				request.getParameter(DatatableAttribute.UPLOADED_ON.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName), ownedBy);

		SearchResult<InitiateIDSRecordDTO> result = idsDashboardService.getUrgentIDSRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "initiateIDSUrgentRecords";
	}

	/*- ----------------------------------------------------------------
	My Dashboard - All Family Records - Application View
	------------------------------------------------------------------- */
	@RequestMapping(value = "/initiateIDS/allFamilyDetails", method = RequestMethod.POST)
	public String getAllFamilyApplications(HttpServletRequest request, final Model model) {
		String familyId = request.getParameter("familyId");
		String appId = request.getParameter("applicationId");
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName), ownedBy);

		SearchResult<InitiateIDSRecordDTO> searchResult = idsDashboardService.getAllFamilyApplications(filter, familyId,
				Long.valueOf(appId));
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return "familyExpandedRecords";
	}

	@RequestMapping(value = "/pendingIDSRecords/view", method = RequestMethod.GET)
	public String viewpendingIDSRecords(final HttpServletRequest request, final Model model) {

		List<IDSPendingApprovalDTO> result = idsDashboardService.getPendingApprovalRecords();
		model.addAttribute(KEY_SEARCH_RESULT, result);
		model.addAttribute(PENDING_APPROVAL_COUNT, idsDashboardService.getPendingApprovalRecordsCount());
		model.addAttribute(PENDING_RESPONSE_COUNT,idsDashboardService.getPendingResponseRecordsCount());
		return "pendingApprovalIDS";

	}

	@RequestMapping(value = "/pendingResponseRecords/view", method = RequestMethod.GET)
	public String pendingResponseRecords(final HttpServletRequest request, final Model model) {

		List<IDSPendingApprovalDTO> result = idsDashboardService.getPendingResponseRecords() ;
		model.addAttribute(KEY_SEARCH_RESULT, result);
		model.addAttribute(PENDING_APPROVAL_COUNT, idsDashboardService.getPendingApprovalRecordsCount());
		model.addAttribute(PENDING_RESPONSE_COUNT,idsDashboardService.getPendingResponseRecordsCount());
		return "pendingResponse";

	}

	@RequestMapping(value = "/filingReadyRecords/view", method = RequestMethod.GET)
	public String filingReadyRecords(final HttpServletRequest request, final Model model) {

		List<FilingReadyDTO> result = idsDashboardService.getFilingReadyRecords();
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "filingReadyRecords";

	}

	/************************************** WIP USPTO FILING TAB *******************************/

	/******** Filing in Progress (1) ********/
	@RequestMapping(value = "/filingInProgressRecords/view", method = RequestMethod.GET)
	public String filingInProgressRecords(final HttpServletRequest request, final Model model) {

		List<FilingInProgressDTO> result = idsDashboardService.getFilingInProgressRecords();
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "filingInProgressRecords";

	}

	/************* Upload Manually Filed IDS (1) *******/
	@RequestMapping(value = "/filingUploadManualRecords/view", method = RequestMethod.GET)
	public String filingUploadManualRecords(final HttpServletRequest request, final Model model) {

		List<IDSManuallyFiledDTO> result = idsDashboardService.getManuallyFiledIdsRecords();
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "uploadManualRecords";

	}

	/************ Validate Reference Status (1) *************/
	@RequestMapping(value = "/filingValidateRefStatusRecords/view", method = RequestMethod.GET)
	public String filingValidateRefStatusRecords(final HttpServletRequest request, final Model model) {


		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;


		List<ValidateRefStatusDTO> result = idsDashboardService.getValidateRefStatusRecords();
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "validateRefStatusRecords";
	}

	@RequestMapping(value = "/1449PendingIDSRecords/view", method = RequestMethod.GET)
	public String get1449PendingIDS(HttpSession session, final Model model) {
		final List<N1449PendingDTO> items = idsDashboardService.get1449PendingIDS();
		model.addAttribute(LIST_ITEMS, items);
		return "1449PendingIDS";
	}

	@RequestMapping(value = "/referenceReview/{" + APPLICATION_ID + "}/{" + IDS_ID + "}", method = RequestMethod.GET)
	public String reviewReference(@PathVariable(APPLICATION_ID) final Long appId,@PathVariable(IDS_ID)final String idsId,final Model model,final HttpSession session)
	{
		final List<N1449DetailDTO> items = idsDashboardService.get1449Details(appId);
		final Map <String,Long> countMap =  idsDashboardService.getIDSReferenceCount(idsId);
		final List<IDS1449ReferenceDTO> referenceList = idsDashboardService.get1449ReferenceRecords(US_PATENT_TYPE, idsId);
		model.addAttribute(LIST_ITEMS,items);
		model.addAttribute(REFERENCE_COUNT,countMap);
		model.addAttribute(REFERENCE_LIST,referenceList);
		return "referenceReview";
	}

	@RequestMapping(value = "/getCountMap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Long> getReferenceCountMap(final HttpServletRequest request, final Model model)
	{
		String finalIdsId = request.getParameter("idsId");
		final Map <String,Long> countMap =  idsDashboardService.getIDSReferenceCount(finalIdsId);
		return countMap;
	}

	@RequestMapping(value = "/fetchReviewReferenceRecords" , method = RequestMethod.POST)
	public String fetchReviewReferenceRecords(final HttpServletRequest request, final Model model)
	{
		String refType = request.getParameter("refType");
		String finalIdsId = request.getParameter("idsId");
		final List<IDS1449ReferenceDTO> referenceList = idsDashboardService.get1449ReferenceRecords(refType, finalIdsId);
		model.addAttribute(REFERENCE_LIST,referenceList);
		String returnView = null;
		switch(ReferenceType.fromString(refType))
		{
		case PUS:
		case US_PUBLICATION:
			returnView = "usPublicationReviewRecords";
			break;
		case FP:
			returnView = "foreignReviewRecords";
			break;
		case NPL:
			returnView = "nplReviewRecords";
			break;
		default:
			break;
		}
		return returnView;
	}

	@RequestMapping(value = "/updateReferenceStatus", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public long updateReferenceStatus(@RequestBody Map<Long, String> idStatusMap)
	{
		return idsDashboardService.updateReferenceRecords(idStatusMap);
	}

	@RequestMapping(value = "/idsFilingPackageRecords/view", method = RequestMethod.GET)
	public String getIDSFilingPackageRecords(HttpSession session, final Model model) {
		final List<IDSFilingPackageDTO> items = idsDashboardService.getIDSFilingPackageRecords();
		model.addAttribute(LIST_ITEMS, items);
		return "idsFilingPackages";
	}

	private List<InitiateIDSRecordDTO> getPagedAppRecords(long offset, long limit) {
		List<InitiateIDSRecordDTO> records = new ArrayList<>();

		InitiateIDSRecordDTO record = null;

		for (long i = offset; i < offset + limit; ++i) {
			record = new InitiateIDSRecordDTO();
			record.setDbId(i);
			record.setFamilyId("F00011");
			record.setJurisdiction("US");
			record.setApplicationNo("14/800,234");
			record.setFilingDate(new Date());
			record.setUncitedReferences(10L);
			record.setUncitedReferencesAge(85L);
			record.setProsecutionStatus("First OA received");
			record.setLastOAResponse(180L);
			records.add(record);
		}

		return records;
	}

	/**
	 * Initiates form validator for application update.
	 *
	 * @param binder
	 *            Binder that allows for setting property values onto a target object, including support for validation
	 *            and binding result analysis.
	 */
	@InitBinder(ApplicationDetailsDTO.MODEL_NAME)
	protected void initApplicationBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.setValidator(applicationValidator);
	}

	/**
	 * Initiates form validator for certification statement.
	 *
	 * @param binder
	 *            Binder that allows for setting property values onto a target object, including support for validation
	 *            and binding result analysis.
	 */
	@InitBinder(IDSCertificationForm.MODEL_KEY)
	protected void initCertificationStatementBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.setValidator(certificateValidator);
	}

	@RequestMapping(value = "/buildIDS/{" + APPLICATION_ID + "}", method = RequestMethod.GET)
	public String buildIDS(@PathVariable(APPLICATION_ID) final Long appId, final Model model) {

		ApplicationDetailsDTO application = idsBuildService.fetchApplicationInfo(appId);
		model.addAttribute(ApplicationDetailsDTO.MODEL_NAME, application);

		fetchUSPatents(appId, application.getIdsID(), model);
		return "initiate";
	}

	private void fetchUSPatents(Long appId, Long idsID, Model model) {
		LOGGER.info(String.format("Listing US Patents for IDS {0}", idsID));
		SearchResult<ReferenceRecordDTO> usPatens = fetchReferenceFlows(appId, 0L, REFERENCES_PER_PAGE,
				ReferenceType.PUS);
		model.addAttribute(KEY_SEARCH_RESULT, usPatens);
		model.addAttribute(MODULE_NAME, "buildIDS");
	}

	@RequestMapping(value = "/saveApp", method = RequestMethod.POST)
	public String updateApplication(
			@Valid @ModelAttribute(ApplicationDetailsDTO.MODEL_NAME) final ApplicationDetailsDTO appForm,
			final BindingResult bindingResult, final Model model) {

		appForm.setEditView(bindingResult.hasErrors());
		if (!bindingResult.hasErrors()) {
			applicationService.updateApplication(appForm);
		}

		return "idsAppInfo";
	}

	@RequestMapping(value = "/resetApp/{" + APPLICATION_ID + "}", method = { RequestMethod.GET, RequestMethod.POST })
	public String resetApplicationDetails(@PathVariable(APPLICATION_ID) final Long appId, final Model model) {
		model.addAttribute(ApplicationDetailsDTO.MODEL_NAME, idsBuildService.fetchApplicationInfo(appId));
		return "idsAppInfo";
	}

	@ResponseBody
	@RequestMapping(value = "/discard/{" + IDS_ID + "}", method = RequestMethod.POST)
	public String discardIDS(@PathVariable(IDS_ID) final Long idsID, final Model model) {
		LOGGER.info(String.format("User requested to discard IDS {0}.", idsID));
		idsWorkflowService.processParalegalAction(idsID, ParallegalAction.DISCARD_IDS, null);
		return Boolean.TRUE.toString();
	}

	@RequestMapping(value = "/certificate/{" + APPLICATION_ID + "}/{" + IDS_ID + "}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String prepareCertificationStatement(@PathVariable(APPLICATION_ID) final Long application,
			@PathVariable(IDS_ID) final Long idsID, final Model model) {

		buildCertifcationStatement(idsID, application, INITATE_IDS_CERTIFICATE, model);

		List<Long> approvers = notificationService.getReceiverUsers(NotificationProcessType.IDS_APPROVAL_REQUEST,
				application);
		// XXX: Remove it.
		if (approvers.isEmpty()) {
			approvers = Arrays.asList(2L, 5L, 8L, 9L);
		}
		if (CollectionUtils.isNotEmpty(approvers)) {
			model.addAttribute("attorneys", idsBuildService.fetchAttorneyUsersDetails(approvers));
		}

		return BuildIDSView.CERTIFICATION_STATEMENT.viewName;
	}

	@RequestMapping(value = "/requestApproval", method = RequestMethod.POST)
	public String requestApproval(
			@Valid @ModelAttribute(IDSCertificationForm.MODEL_KEY) IDSCertificationForm form,
			final BindingResult bindingResult, final Model model) {

		String view = BuildIDSView.SUCCESS.viewName;
		final long idsID = form.getIds();
		final long attorney = form.getAttorney();
		LOGGER.info(String.format("Paralegal {0} requested for IDS {1} approval to Attorney {2}.", loggedInUser(),
				idsID, attorney));

		if (bindingResult.hasErrors()) {
			view = BuildIDSView.CERTIFICATION_STATEMENT.viewName;
		} else {
			idsBuildService.saveCertificationStatement(idsID, form.toEntity());
			idsWorkflowService.processParalegalAction(idsID, ParallegalAction.SUBMIT_FOR_APPROVAL, attorney);
		}

		return view;
	}

	@RequestMapping(value = "/saveIDS", method = RequestMethod.POST)
	public String saveIDS(@ModelAttribute(IDSCertificationForm.MODEL_KEY) final IDSCertificationForm certificationForm,
			final BindingResult bindingResult, final Model model) {

		return null;
	}

	@RequestMapping(value = "/usPatent/{" + APPLICATION_ID + "}", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewUSPatentsReferences(@PathVariable(APPLICATION_ID) final Long application,
			final HttpServletRequest request, final Model model) {
		LOGGER.info(String.format("us patents initialise for {0}", application));
		String offsetArg = ((request.getParameter("offset") == null) ? "0" : request.getParameter("offset"));
		Long offset = Long.parseLong(offsetArg);
		SearchResult<ReferenceRecordDTO> usPatens = fetchReferenceFlows(application, offset, REFERENCES_PER_PAGE,
				ReferenceType.PUS);
		model.addAttribute(KEY_SEARCH_RESULT, usPatens);
		model.addAttribute("offset", offset);
		model.addAttribute(MODULE_NAME, request.getParameter("sourceURL"));

		return ((offset > 0) ? BuildIDSView.USPATENT_RECORDS.viewName : BuildIDSView.ADMIIN_USPATENT_RECORDS.viewName);
	}

	@RequestMapping(value = "/usPublication/{" + APPLICATION_ID + "}", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String viewUSPublicationReferences(@PathVariable(APPLICATION_ID) final Long application,
			final HttpServletRequest request, final Model model) {
		LOGGER.info(String.format("us publication initialise for {0}", application));
		String offsetArg = ((request.getParameter("offset") == null) ? "0" : request.getParameter("offset"));
		Long offset = Long.parseLong(offsetArg);

		SearchResult<ReferenceRecordDTO> usPublication = fetchReferenceFlows(application, offset, REFERENCES_PER_PAGE,
				ReferenceType.US_PUBLICATION);
		model.addAttribute(KEY_SEARCH_RESULT, usPublication);
		model.addAttribute("offset", offset);
		model.addAttribute(MODULE_NAME, request.getParameter("sourceURL"));

		return ((offset > 0) ? BuildIDSView.USPUBLICATION_RECORDS.viewName
				: BuildIDSView.ADMIIN_USPUBLICATION_RECORDS.viewName);

	}


	@RequestMapping(value = "/foreign/{" + APPLICATION_ID + "}", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewForeignReferences(@PathVariable(APPLICATION_ID) final Long application,
			final HttpServletRequest request, final Model model) {
		LOGGER.info(String.format("foriegn initialise for {0}", application));
		// TODO: private method
		String offsetArg = ((request.getParameter("offset") == null) ? "0" : request.getParameter("offset"));

		Long offset = Long.parseLong(offsetArg);

		// TODO: Create constant for default page size.
		SearchResult<ReferenceRecordDTO> foreignRecords = fetchReferenceFlows(application, offset, REFERENCES_PER_PAGE,
				ReferenceType.FP);
		model.addAttribute(KEY_SEARCH_RESULT, foreignRecords);
		model.addAttribute("offset", offset);
		model.addAttribute(MODULE_NAME, request.getParameter("sourceURL"));
		// "adminForeignRecords", "foreignRecords"
		return (offset > 0) ? BuildIDSView.FOREIGN_RECORDS.viewName : BuildIDSView.ADMIIN_FOREIGN_RECORDS.viewName;

	}


	@RequestMapping(value = "/npl/{" + APPLICATION_ID + "}", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewNPLReferences(@PathVariable(APPLICATION_ID) final Long application,
			final HttpServletRequest request, final Model model) {
		LOGGER.info(String.format("foriegn initialise for {0}", application));
		String offsetArg = ((request.getParameter("offset") == null) ? "0" : request.getParameter("offset"));
		Long offset = Long.parseLong(offsetArg);
		String viewType = request.getParameter("sourceURL");
		SearchResult<ReferenceRecordDTO> nplRecords = null;
		if (viewType.equals("buildIDS")) {
			nplRecords = fetchReferenceFlows(application, offset, REFERENCES_PER_PAGE, ReferenceType.NPL);
		}
		else if (viewType.equals("previewIDS")) {
			PaginationInfo pageInfo = new PaginationInfo(REFERENCES_PER_PAGE, offset, null, true);
			ReferenceRecordsFilter refFilter = new ReferenceRecordsFilter(application, 13L, ReferenceType.NPL);
			nplRecords = idsBuildService.fetchNPLRecordsForPreview(refFilter, pageInfo);
		}
		else{

		}
		model.addAttribute(KEY_SEARCH_RESULT, nplRecords);
		model.addAttribute("offset", offset);
		model.addAttribute(MODULE_NAME, request.getParameter("sourceURL"));

		return ((offset > 0) ? BuildIDSView.NPL_RECORDS.viewName : BuildIDSView.ADMIIN_NPL_RECORDS.viewName);
	}

	@RequestMapping(value = "/confirmationPage/{" + APPLICATION_ID + "}",
			method = { RequestMethod.POST, RequestMethod.GET })
	public String viewConfirmPage(@PathVariable(APPLICATION_ID) final Long application,
			final HttpServletRequest request, final Model model) {

		LOGGER.info(String.format("confirm page initialise for {0}", application));

		String offsetArg = ((request.getParameter("offset") == null) ? "0" : request.getParameter("offset"));
		Long offset = Long.parseLong(offsetArg);

		PaginationInfo pageInfo = new PaginationInfo(REFERENCES_PER_PAGE, offset, null, true);
		ReferenceRecordsFilter refFilter = new ReferenceRecordsFilter(application, 13L, ReferenceType.NPL);
		SearchResult<ReferenceRecordDTO> nplRecords = idsBuildService.fetchSourceRefRecords(refFilter, pageInfo);

		model.addAttribute("searchResult", nplRecords);
		model.addAttribute("offset", offset);
		model.addAttribute("module", request.getParameter("sourceURL"));
		return BuildIDSView.CONFIRMATION_PAGE.viewName;
	}

	@RequestMapping(value = "/previewPage/{appId}", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewPreviewPage(@PathVariable("appId") final Long application, final HttpServletRequest request,
			final Model model) {

		LOGGER.info(String.format("preview page initialise for {0}", application));

		String idsId = "";
		idsId  =  request.getParameter(IDS_ID);
		long IDSId  = StringUtils.isNotEmpty(idsId) ? Long.parseLong(idsId) : 0L;

		ReferenceRecordsFilter refFilter = new ReferenceRecordsFilter(application, IDSId);

		Map<ReferenceType, Long> countResult = idsBuildService.countReferenceRecords(refFilter);
		Map<String, Long> countsMap = new HashMap<>(countResult.size());
		// countsMap.putAll(countResult);
		countResult.forEach((k, v) -> countsMap.put(k.toString(), v));
		
		/*************** For attorney approval new references ****************/

		Map<ReferenceType, Long> countNewRef = idsBuildService.countNewReferenceRecords(refFilter);
		Map<String, Long> countsMapNew = new HashMap<>(countNewRef.size());
		// countsMap.putAll(countResult);
		countNewRef.forEach((k, v) -> countsMapNew.put(k.toString(), v));
		model.addAttribute("refCountsNew", countsMapNew);
		
		/******************end**********************/
		
		model.addAttribute("refCounts", countsMap);
		model.addAttribute(MODULE_NAME, "previewIDS");
		return "previewReference";
	}

	private SearchResult<ReferenceRecordDTO> fetchReferenceFlows(final long application, Long offset,
			Long limit, ReferenceType refType) {
		PaginationInfo pageInfo = new PaginationInfo(limit, offset, null, true);
		ReferenceRecordsFilter refFilter = new ReferenceRecordsFilter(application,
				idsBuildService.findIdsInProgressOrApprovedForApplication(application), refType);
		return idsBuildService.fetchReferenceRecords(refFilter, pageInfo);
	}

	/***************************************** Attorney Approval *****************************************/
	/**
	 * To View attorney approval approveIDS
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/attorneyApproval/approveIDS", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewAttorneyApprovalApproveIDS(final HttpServletRequest request, final Model model)
			throws ServletRequestBindingException {
		Long idsId ;
		 Map<String, Long> countsMapNew =new HashMap<>();
		idsId = ServletRequestUtils.getLongParameter(request, "idsId");
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {
			String idsIdStr =  (String) model.asMap().get("idsId");
		   idsId = Long.valueOf(idsIdStr);
		}
		
		Long appId = idsBuildService.getApplicationIdForIDSId(idsId);
		ApplicationDetailsDTO application =  idsBuildService.fetchApplicationInfo(appId);
		Long newRefCount = idsBuildService.getNewReferenceCount(appId);
		model.addAttribute(ApplicationDetailsDTO.MODEL_NAME, application);
		model.addAttribute("IdsId", idsId);
		model.addAttribute("newRefCount", newRefCount);
		model.addAttribute("refCountsNew", countsMapNew);
		return "idsApproval";

	}


	/**
	 * To View attorney approval dashboard.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/attorneyApproval/dashboard", method = RequestMethod.GET)
	public String viewAttorneyApprovalDashboard(final HttpServletRequest request, final Model model) {
		model.addAttribute(RecordsView.MODEL_KEY, RecordsView.APPLICATION_VIEW.name());
		return "idsAttorneyApproval";
	}

	/**
	 * View attorney approval dashboard all records - application view.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/attorneyApproval/viewRecords", method = RequestMethod.POST)
	public String viewAttorneyApprovalDashboardRecords(final HttpServletRequest request, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName),
				request.getParameter(DatatableAttribute.JURISDICTION.attrName),
				request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName),
				request.getParameter(DatatableAttribute.FAMILY_ID.attrName),
				request.getParameter(DatatableAttribute.FILTER_DATE.attrName),
				request.getParameter(DatatableAttribute.FILTER_DATE_BY.attrName),
				request.getParameter(DatatableAttribute.UNCITATED_REFERENCE.attrName),
				request.getParameter(DatatableAttribute.PROSECUTION_STATUS.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName));

		SearchResult<IDSAttorneyApprovalDTO> result = idsDashboardService.getIDSAttorneyApprovalRecords(filter,
				pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "idsAttorneyApprovalRecords";
	}


	/**
	 * View attorney approval family view.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 */
	@RequestMapping(value = "/attorneyApproval/allFamilyDetails", method = RequestMethod.POST)
	public String viewAttorneyApprovalAllFamilyDetails(final HttpServletRequest request, final Model model)
			throws InstantiationException, IllegalAccessException {

		String familyId = request.getParameter("familyId");
		String applicationId= request.getParameter("applicationId");
		String notificationId =request.getParameter("notificationId");
		Long applicationID = Long.valueOf(applicationId);
		Long notificationID = Long.valueOf(notificationId);
		SearchResult<IDSAttorneyApprovalDTO> result = idsDashboardService.getIDSAttorneyApprovalAllFamilyRecords(familyId, applicationID,notificationID);
		model.addAttribute(KEY_SEARCH_RESULT, result);
		return "idsAttorneyApprovalFamilyRecords";
	}

	/**
	 * To view the reviewed references since IDS was prepared.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/attorneyApproval/viewIncludeAllReferences", method = RequestMethod.POST)
	public String viewAttorneyApprovalIncludeReferences(final HttpServletRequest request, final Model model, RedirectAttributes ra)
			throws ServletRequestBindingException {

		Long applicationId = ServletRequestUtils.getLongParameter(request, "appForm");
		String idsId = ServletRequestUtils.getStringParameter(request, "idsID");
		Long IDSId  = StringUtils.isNotEmpty(idsId) ? Long.parseLong(idsId) : 0L;
		idsBuildService.updateNewReferences(applicationId, IDSId);
		ra.addFlashAttribute("idsId", idsId);
		return "redirect:/ids/attorneyApproval/approveIDS";
	}
	
	/**
	 * Reference actions. --> Donot File & Drop from this IDS ( From Attorney User)
	 *
	 * @param request the request
	 * @param model the model
	 * @throws ServletRequestBindingException the servlet request binding exception
	 */
	@RequestMapping(value="/attorneyApproval/refActions" , method=RequestMethod.POST)
	public void  referenceActions(final HttpServletRequest request,
			final Model model) throws ServletRequestBindingException{
		
		String[] listOfRefrencesIds=request.getParameterValues("refIds[]");
		String[] listOfRefrencesIdsDrop=request.getParameterValues("refIdsDrop[]");
		String action = ServletRequestUtils.getStringParameter(request, "action");
	
		if(action.equalsIgnoreCase("DonotFile")){
			for(String refid : listOfRefrencesIds){
				Long refFlowId = Long.valueOf(refid);		
				LOGGER.info(String.format("User requested to drop reference {0} from all IDS.", refFlowId));
				idsBuildService.dropReferenceRecords(refFlowId);
			}
		}
		
        if(action.equalsIgnoreCase("DropFromIDS")){
        	for(String refid : listOfRefrencesIdsDrop){
				Long refFlowId = Long.valueOf(refid);		
				LOGGER.info(String.format("User requested to drop reference {0} from all IDS.", refFlowId));
				 idsBuildService.dropReferenceFromIDS(refFlowId);
			}
		}
	}
	
	@RequestMapping(value="/attorneyApproval/IDSActions", method=RequestMethod.POST)
	public String idsActions(final HttpServletRequest request,
			final Model model, RedirectAttributes ra) throws ServletRequestBindingException{
		
		String idsID= request.getParameter("idsId");
		Long idsId= Long.valueOf(idsID);
		String attorneyComments= request.getParameter("attorneyComments");
		String action = request.getParameter("action");
		String appId = ServletRequestUtils.getStringParameter(request, "appId");
		
		if(action.equalsIgnoreCase("approveIDS")){
        idsWorkflowService.processAttorneyAction(idsId, AttorneyResponse.APPROVE);
		}
		
		if(action.equalsIgnoreCase("requestChanges")){
		idsWorkflowService.processAttorneyAction(idsId, AttorneyResponse.REQUEST_CHANGE);
		}

		if(action.equalsIgnoreCase("donotFileIDS")){
        idsWorkflowService.processAttorneyAction(idsId, AttorneyResponse.DISCARD_IDS);			
		}
		
		if(action.equalsIgnoreCase("donotIncludeReference")){
			   idsBuildService.updateNotifyAttorneyFlag(appId);
	      }
		
		return "redirect:/ids/attorneyApproval/dashboard";
	}
	
	
	/**********************End *********************************************************/
	/**
	 * Initiate the IDS process
	 *
	 * @param session the session
	 * @param model the model
	 * @param appId the application Id
	 * @return the string
	 */

	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	public String initiateIDS(final HttpServletRequest request,final Model model, final HttpSession session) {

		// LOGGER.info(String.format("Initiating IDS build for application {0}.", appId));
		/*
		 * long idsID = idsWorkflowService.initiateIDSBuild(appId); model.addAttribute(IDS_ID, idsID);
		 */

		String applicationId = null;
		applicationId = request.getParameter(APPLICATION_ID);
		long appId = StringUtils.isNotEmpty(applicationId) ? Long.parseLong(applicationId) : 0L;

		String refAge = null;
		refAge = request.getParameter(REFERNCE_AGE);
		long referenceAge = StringUtils.isNotEmpty(refAge) ? Long.parseLong(refAge) : 0L;

		List<Long> skipAppNotificationList = null;
		if (session.getAttribute(APP_SKIP_NOTIFICATION_LIST) != null) {
			skipAppNotificationList = (List<Long>) session.getAttribute(APP_SKIP_NOTIFICATION_LIST);
		}
		List<Long> skipFamilyNotificationList = null;
		if (session.getAttribute(FAMILY_SKIP_NOTIFICATION_LIST) != null) {
			skipFamilyNotificationList = (List<Long>) session.getAttribute(FAMILY_SKIP_NOTIFICATION_LIST);
		}
		Map<String, Long> countNotifications = idsBuildService.countNotifications(appId, skipAppNotificationList,
				skipFamilyNotificationList);
		session.setAttribute(COUNT_NOTIFICATIONS, countNotifications);

		ApplicationDetailsDTO application = idsBuildService.fetchAppInfoForNotification(appId);
		model.addAttribute(ApplicationDetailsDTO.MODEL_NAME, application);
		model.addAttribute(PROSECUTION_STATUS, request.getParameter(PROSECUTION_STATUS));
		model.addAttribute(REFERNCE_AGE, referenceAge);

		return IDS_NOTIFICATION;
	}

	/**
	 * View Application Pending Notifications
	 *
	 * @param session the session
	 * @param request the request
	 * @param model the model
	 * @param appId the application Id
	 * @return the string
	 */
	@RequestMapping(value = "/initiate/appNotifications/{" + APPLICATION_ID + "}", method = RequestMethod.GET)
	public String viewApplicationNotifications(@PathVariable("appId") final Long appId,
			final Model model, final HttpServletRequest request, final HttpSession session) {
		long pageNumber = NOTIFICATION_PAGE_NUMBER;
		long notificationPerPage = NOTIFICATIONS_PER_PAGE;
		if (request.getParameter(PAGE) != null) {
			String page = request.getParameter(PAGE);
			pageNumber = Long.valueOf(page);
		}
		PaginationInfo pageInfo = new PaginationInfo(notificationPerPage, (pageNumber - 1) * notificationPerPage);
		List<Long> skipNotificationList = null;
		if (session.getAttribute(APP_SKIP_NOTIFICATION_LIST) != null) {
			skipNotificationList = (List<Long>) session.getAttribute(APP_SKIP_NOTIFICATION_LIST);
		}
		SearchResult<NotificationBaseDTO> notificationDTOsList = idsBuildService
				.getNotificationsOfApp(appId, pageInfo, skipNotificationList);
		int noOfPages = (int) Math.ceil(notificationDTOsList.getRecordsTotal() * 1.0 / notificationPerPage);
		model.addAttribute(PENDING_APP_NOTIFICATION_LIST, notificationDTOsList);
		model.addAttribute(APP_CURRENT_PAGE, pageNumber);
		model.addAttribute(APP_NO_OF_PAGES, noOfPages);
		return APP_NOTIFICATION_DATA;
	}

	/**
	 * View Family Members Pending Notifications
	 *
	 * @param session the session
	 * @param request the request
	 * @param model the model
	 * @param appId the application Id
	 * @return the string
	 */
	@RequestMapping(value = "/initiate/familyNotifications/{" + APPLICATION_ID + "}", method = RequestMethod.GET)
	public String viewFamilyMemberNotifications(@PathVariable(APPLICATION_ID) final Long appId,
			final Model model, final HttpServletRequest request, final HttpSession session) {
		long pageNumber = NOTIFICATION_PAGE_NUMBER;
		long notificationPerPage = NOTIFICATIONS_PER_PAGE;
		if (request.getParameter(PAGE) != null) {
			String page = request.getParameter(PAGE);
			pageNumber = Long.valueOf(page);
		}
		PaginationInfo pageInfo = new PaginationInfo(notificationPerPage, (pageNumber - 1) * notificationPerPage);

		List<Long> skipNotificationList = null;
		if (session.getAttribute(FAMILY_SKIP_NOTIFICATION_LIST) != null) {
			skipNotificationList = (List<Long>) session.getAttribute(FAMILY_SKIP_NOTIFICATION_LIST);
		}

		SearchResult<NotificationBaseDTO> notificationDTOsList = idsBuildService
				.getNotificationsOfFamily(appId, pageInfo, skipNotificationList);
		int noOfPages = (int) Math.ceil(notificationDTOsList.getRecordsTotal() * 1.0 / notificationPerPage);
		model.addAttribute(PENDING_FAMILY_NOTIFICATION_LIST, notificationDTOsList);
		model.addAttribute(FAMILY_CURRENT_PAGE, pageNumber);
		model.addAttribute(FAMILY_NO_OF_PAGES, noOfPages);
		return FAMILY_NOTIFICATION_DATA;
	}
	/**
	 * View Application Pending Notifications after removing some notifications
	 *
	 * @param session the session
	 * @param request the request
	 * @param model the model
	 * @param appId the application Id
	 * @return the string
	 */
	@RequestMapping(value = "/initiate/removeAppNotification/{" + APPLICATION_ID + "}", method = RequestMethod.GET)
	public String removeNotifications(@PathVariable(APPLICATION_ID) final Long appId, final Model model,
			final HttpServletRequest request, final HttpSession session) {
		long pageNumber = NOTIFICATION_PAGE_NUMBER;
		long notificationPerPage = NOTIFICATIONS_PER_PAGE;
		if (request.getParameter(PAGE) != null) {
			String page = request.getParameter(PAGE);
			pageNumber = Long.valueOf(page);
		}
		List<Long> skipNotificationList = null;
		if (request.getParameter(NOTIFICATION_ID) != null) {
			long notificationId = Long.parseLong(request.getParameter(NOTIFICATION_ID));
			String selectedAccordion = request.getParameter(SELECTED_ACCORDION);
			skipNotificationList = getSkipNotificationList(session, skipNotificationList, notificationId,
					selectedAccordion);
		}
		PaginationInfo pageInfo = new PaginationInfo(notificationPerPage, (pageNumber - 1) * notificationPerPage);
		SearchResult<NotificationBaseDTO> notificationDTOsList = idsBuildService
				.getNotificationsOfApp(appId, pageInfo, skipNotificationList);
		int noOfPages = (int) Math.ceil(notificationDTOsList.getRecordsTotal() * 1.0 / notificationPerPage);
		model.addAttribute(PENDING_APP_NOTIFICATION_LIST, notificationDTOsList);
		model.addAttribute(APP_CURRENT_PAGE, pageNumber);
		model.addAttribute(APP_NO_OF_PAGES, noOfPages);
		return APP_NOTIFICATION_DATA;
	}

	/**
	 * View Family Pending Notifications after removing some notifications
	 *
	 * @param session the session
	 * @param request the request
	 * @param model the model
	 * @param appId the application Id
	 * @return the string
	 */
	@RequestMapping(value = "/initiate/removeFamilyNotification/{" + APPLICATION_ID + "}", method = RequestMethod.GET)
	public String removeFamilyNotifications(@PathVariable(APPLICATION_ID) final Long appId, final Model model,
			final HttpServletRequest request, final HttpSession session) {
		long pageNumber = NOTIFICATION_PAGE_NUMBER;
		long notificationPerPage = NOTIFICATIONS_PER_PAGE;
		if (request.getParameter(PAGE) != null) {
			String page = request.getParameter(PAGE);
			pageNumber = Long.valueOf(page);
		}
		List<Long> skipNotificationList = null;
		if (request.getParameter(NOTIFICATION_ID) != null) {
			long notificationId = Long.parseLong(request.getParameter(NOTIFICATION_ID));
			String selectedAccordion = request.getParameter(SELECTED_ACCORDION);
			skipNotificationList = getSkipNotificationList(session, skipNotificationList, notificationId,
					selectedAccordion);

		}
		PaginationInfo pageInfo = new PaginationInfo(notificationPerPage, (pageNumber - 1) * notificationPerPage);
		SearchResult<NotificationBaseDTO> notificationDTOsList = idsBuildService
				.getNotificationsOfFamily(appId, pageInfo, skipNotificationList);
		int noOfPages = (int) Math.ceil(notificationDTOsList.getRecordsTotal() * 1.0 / notificationPerPage);
		model.addAttribute(PENDING_FAMILY_NOTIFICATION_LIST, notificationDTOsList);
		model.addAttribute(FAMILY_CURRENT_PAGE, pageNumber);
		model.addAttribute(FAMILY_NO_OF_PAGES, noOfPages);
		return FAMILY_NOTIFICATION_DATA;
	}

	/**
	 * View Family Pending Notifications after removing some notifications
	 *
	 * @param session the session
	 * @param skipNotificationList list of skipped notifications
	 * @param notificationId the notification Id to be skipped
	 * @param selectedAccordion application view or family view accordion
	 * @return the list
	 */
	private static List<Long> getSkipNotificationList(final HttpSession session, List<Long> skipNotificationList,
			final long notificationId, final String selectedAccordion) {
		switch (selectedAccordion) {
		case APP_ACCORDION:
			if (session.getAttribute(APP_SKIP_NOTIFICATION_LIST) != null) {
				skipNotificationList = (List<Long>) session.getAttribute(APP_SKIP_NOTIFICATION_LIST);
				skipNotificationList.add(notificationId);
			} else {
				skipNotificationList = new ArrayList<Long>();
				skipNotificationList.add(notificationId);
			}
			session.setAttribute(APP_SKIP_NOTIFICATION_LIST, skipNotificationList);
			break;
		case FAMILY_ACCORDION:
			if (session.getAttribute(FAMILY_SKIP_NOTIFICATION_LIST) != null) {
				skipNotificationList = (List<Long>) session.getAttribute(FAMILY_SKIP_NOTIFICATION_LIST);
				skipNotificationList.add(notificationId);
			} else {
				skipNotificationList = new ArrayList<Long>();
				skipNotificationList.add(notificationId);
			}
			session.setAttribute(FAMILY_SKIP_NOTIFICATION_LIST, skipNotificationList);
			break;
		default:
			break;
		}
		return skipNotificationList;
	}


	@RequestMapping(value = "/userAction/changeIdsStatus", method = RequestMethod.POST)
	@ResponseBody
	public String changeIdsStatus(final HttpServletRequest request, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);
		long idsId = Long.parseLong(request.getParameter("idsId"));
		String selectedStatus = request.getParameter("selectedStatus");

		String status = idsDashboardService.idsStatusChangeByUser(idsId, selectedStatus).name();
		// model.addAttribute("status", status);
		return status;
	}


	@RequestMapping(value = "/userAction/updateRefStatus/{idsId}/{notificationProcessId}", method = RequestMethod.GET)
	public String updateReferenceStatus(@ModelAttribute(UpdateRefStatusForm.MODEL_KEY) UpdateRefStatusForm form,
			@PathVariable("idsId") final Long idsId , @PathVariable("notificationProcessId") final Long notificationProcessId, final HttpServletRequest request, final Model model) {

		//idsDashboardService.getUrgentIDSRecords(filter, pageInfo)(idsId, selectedStatus).name();
		// String status = idsDashboardService.idsStatusChangeByUser(idsId, selectedStatus).name();
		// model.addAttribute("status", status);
		List<IDSReferenceDTO> items  = idsDashboardService.getIDSReferenceDetails(ReferenceType.PUS ,  idsId);
		model.addAttribute("idsId", idsId);
		model.addAttribute("notificationProcessId", notificationProcessId);
		model.addAttribute("items", items);
		model.addAttribute("refCount", idsDashboardService.getIDSReferenceCount(idsId));
		return "updateRefStatus";
	}

	@RequestMapping(value = "/userAction/updateRefStatus/getRefDetails", method = RequestMethod.POST)
	public String updateIDSReference(@ModelAttribute(UpdateRefStatusForm.MODEL_KEY) UpdateRefStatusForm form, final HttpServletRequest request, final Model model) {
		Long idsId = Long.parseLong(request.getParameter("idsId"));
		String referenceType =  request.getParameter("refType");
		// String status = idsDashboardService.idsStatusChangeByUser(idsId, selectedStatus).name();
		List<IDSReferenceDTO> items =  idsDashboardService.getIDSReferenceDetails(ReferenceType.fromString(referenceType) ,  idsId);

		//model.addAttribute(UpdateRefStatusForm.MODEL_KEY, new UpdateRefStatusForm());
		model.addAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, UpdateRefStatusForm.MODEL_KEY);
		model.addAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, UpdateRefStatusForm.MODEL_KEY + ".");



		model.addAttribute("items", items);


		// XXX: Try to have single return statement in a method.
		// XXX: Do switching rather.
		if(referenceType.equalsIgnoreCase(ReferenceType.US_PUBLICATION.name())) {
			return "refUsPublication";
		}
		else if(referenceType.equalsIgnoreCase(ReferenceType.FP.name())) {
			return "refFp";
		}
		else if(referenceType.equalsIgnoreCase(ReferenceType.NPL.name())) {
			return "refNpl";
		}
		return "updateRefStatus";
	}

	@ResponseBody
	@RequestMapping(value = "/dontFileReference", method = { RequestMethod.POST, RequestMethod.GET })
	public Long deleteReferenceForAllIds(final HttpServletRequest request, final Model model) {

		Long refFlowId = Long.parseLong(request.getParameter("refID"));
		LOGGER.info(String.format("User requested to drop reference {0} from all IDS.", refFlowId));
		return idsBuildService.dropReferenceRecords(refFlowId);
	}

	@ResponseBody
	@RequestMapping(value = "/dropRefFromIDS", method = { RequestMethod.POST, RequestMethod.GET })
	public Long deleteReferenceForThisIds(final HttpServletRequest request, final Model model) {

		Long refFlowId = Long.parseLong(request.getParameter("refID"));
		LOGGER.info(String.format("User requested to drop references {0} from IDS.", refFlowId));
		return idsBuildService.dropReferenceFromIDS(refFlowId);
	}

	/**
	 * Edit reference for review
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/editReference", method = RequestMethod.GET)
	public String editReference(HttpServletRequest request, Model model, RedirectAttributes ra) throws ServletRequestBindingException {

		Long correspondenceid = ServletRequestUtils.getLongParameter(request, KEY_CORRESPONDENCE);
		if(correspondenceid == null) {
			correspondenceid = (Long) ra.asMap().get(KEY_CORRESPONDENCE);
		}
		ReferenceBaseDTO referenceDTO = referenceManagementService.getReferenceWithApplicationDetailsAndFlowStatus(correspondenceid);
		model.addAttribute(KEY_REFERENCE_DTO, referenceDTO);
		return "editReference";
	}

	/**
	 * IDS drill down header section
	 *
	 * @param applicationId
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/drillDown/{" + APPLICATION_ID + "}",  method = { RequestMethod.POST, RequestMethod.GET })
	public String getIDSFiledApplicationData(@PathVariable(APPLICATION_ID) final Long appId, final HttpServletRequest request, final Model model) {
		request.setAttribute("app",appId);
		IDSDrillDownInfoDTO idsFiledRecord = idsDashboardService.getIDSDrillDownInfo(appId);
		List<IDSDrillDownInfoDTO> idsFilingDates = idsDashboardService.getIDSDrillDownFilingDates(appId);
		model.addAttribute(IDSDrillDownInfoDTO.MODEL_HEADER, idsFiledRecord);
		model.addAttribute(IDSDrillDownInfoDTO.MODEL_FILINGDATES,idsFilingDates);
		LOGGER.info(String.format("appId", appId));
		return "idsDrillDown";
	}

	@ResponseBody
	@RequestMapping(value = "/selectedSourceRef/{appId}", method = { RequestMethod.POST, RequestMethod.GET })
	public int selectedSourceReference(@PathVariable("appId") final Long application,
			@RequestParam(value = "selectedSourceRef[]", required = false) final Long[] srcRef,
			final HttpServletRequest request, final Model model) {

		LOGGER.info(String.format("preview page initialise for {0}", application));
		/*
		 * String[] selectedIndex = (request.getParameterValues("selectedSourceRef[]")); Long[] srcRef = new
		 * Long[selectedIndex.length]; for (int i = 0; i < selectedIndex.length; i++) { srcRef[i] =
		 * Long.parseLong(selectedIndex[i]); }
		 */
		int countRecordInsert = idsBuildService.insertSourceRefFillingInfo(srcRef, 13L);

		model.addAttribute("recordsCount", countRecordInsert);
		model.addAttribute("module", "previewIDS");
		return countRecordInsert;
	}




	/**
	 * IDS drill down citedRefernces section
	 *
	 * @param applicationId
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/drillDown/citedReferences/{"+ APPLICATION_ID +"}" ,  method = RequestMethod.POST)
	public @ResponseBody Map<String, List<IDSDrillDownInfoDTO>> getIDSCitedReferenceInfo(@PathVariable(APPLICATION_ID) final Long appId, @RequestParam(value = IDS_ID) String IDSId, final HttpServletRequest request, final Model model) {
		String app=request.getParameter("app");
		Map<String,List<IDSDrillDownInfoDTO>> citedRefInfo = new HashMap<String, List<IDSDrillDownInfoDTO>>();
		List<IDSDrillDownInfoDTO> idsReferenceTypeCounts = idsDashboardService.getIDSDrillDownReferenceTypeCounts(appId, Long.valueOf(IDSId));
		List<IDSDrillDownInfoDTO> idsReferenceTypeInfo = idsDashboardService.getIDSDrillDownReferenceTypes(appId, Long.valueOf(IDSId));
		citedRefInfo.put(IDSDrillDownInfoDTO.MODEL_REFERENCETYPES, idsReferenceTypeInfo);
		citedRefInfo.put(IDSDrillDownInfoDTO.MODEL_REFERENCETYPECOUNTS, idsReferenceTypeCounts);
		LOGGER.info(String.format("appId", appId));
		LOGGER.info(String.format("app", app));
		return citedRefInfo;
	}





	@RequestMapping(value = "/userAction/updateRefStatus/submit", method = RequestMethod.POST)
	public String updateReferenceStatusSave(@ModelAttribute(UpdateRefStatusForm.MODEL_KEY) UpdateRefStatusForm form, final HttpServletRequest request, final Model model) {
		//idsDashboardService.getUrgentIDSRecords(filter, pageInfo)(idsId, selectedStatus).name();
		// String status = idsDashboardService.idsStatusChangeByUser(idsId, selectedStatus).name();
		// model.addAttribute("status", status);
		//List<IDSReferenceDTO> items  = idsDashboardService.getIDSReferenceDetails(ReferenceType.PUS ,  filingInfoId);
		//model.addAttribute("items", items);
		UpdateRefStatusDTO updateRefStatusDTO = form.toDTO();
		idsDashboardService.updateReferenceStatus(updateRefStatusDTO);
		return "validateRefStatusRecords";
	}




	/**
	 * Initiate the IDS process
	 *
	 * @param appId the application Id
	 * @param model the model
	 * @return {@link Long} idsId the IDS ID
	 */
	@RequestMapping(value = "/newIDS/{" + APPLICATION_ID + "}", method = RequestMethod.GET)
	@ResponseBody
	public Long initiateNewIDS(@PathVariable(APPLICATION_ID) final Long appId) {
		LOGGER.info(MessageFormat.format("Initiating IDS build for application {0}.", appId));
		long idsId = idsWorkflowService.initiateIDSBuild(appId);
		return idsId;
	}

	/**
	 * Check the valid pop up name when IDS build
	 * is initiated from Notification Page
	 *
	 * @param request the HTTP Servlet Request
	 * @param model the model
	 * @return {@link Map} the map containing the popUpvalue and new references found list

	 */
	@RequestMapping(value = "/checkValidPopUp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkIDSConfirmPopUpToShow(final HttpServletRequest request) {
		String applicationId = null;
		applicationId = request.getParameter(APPLICATION_ID);

		long appId = StringUtils.isNotEmpty(applicationId) ? Long.parseLong(applicationId) : 0L;
		String prosStatus = request.getParameter(PROSECUTION_STATUS);
		String refAge = request.getParameter(REFERNCE_AGE);
		long referenceAge = StringUtils.isNotEmpty(refAge) ? Long.parseLong(refAge) : 0L;

		Map<String, Object> popUpMap = new HashMap<String,Object>();
		popUpMap = idsBuildService.getValipPopUpMap(appId, referenceAge, prosStatus);
		return popUpMap;

	}

	@RequestMapping(value = "/includeRef", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public Long includeRefCurrentIDS(@RequestBody final Map refIDMap) {

		Integer idsID =  (Integer) refIDMap.get(IDS_ID);
		long idsId = idsID.longValue();

		List<Integer> list = (List<Integer>) refIDMap.get(REF_FLOWS_IDS);
		List<Integer> refFlowsIds = list;
		List<Long> refFlowsIdsList = new ArrayList<Long>();
		for(Integer i: refFlowsIds){
			refFlowsIdsList.add(i.longValue());
		}

		return idsBuildService.includeRefInCurrentIDS(idsId,refFlowsIdsList);

	}


	@RequestMapping(value = "/buildIDS/checkRefCount/{" + APPLICATION_ID + "}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<ReferenceType, Boolean> checkReferenceCountPopUp(@PathVariable("appId") final Long application, final HttpServletRequest request) {

		String id = "";
		id  =  request.getParameter(IDS_ID);
		long idsId  = StringUtils.isNotEmpty(id) ? Long.parseLong(id) : 0L;

		ReferenceRecordsFilter refFilter = new ReferenceRecordsFilter(application, idsId);
		return idsBuildService.checkReferencesCountPopUp(refFilter);

	}
	
	//
	
	@RequestMapping(value = "/pendingApproval/emailResponse", method = RequestMethod.POST)
	public String submitEmailResponse(HttpServletRequest request, final Model model) {
		String id = "";
		id  =  request.getParameter(IDS_ID);
		long idsId  = StringUtils.isNotEmpty(id) ? Long.parseLong(id) : 0L;	
		
		String nId = "";
		nId =  request.getParameter(NOTIFICATION_ID);
		long notificationProcessId = StringUtils.isNotEmpty(nId) ? Long.parseLong(nId) : 0L;
		String comment = request.getParameter(PARALLEGAL_COMMENTS);
		idsDashboardService.submitEmailResponse(idsId, notificationProcessId, comment);

		return "filingReadyRecords";
	}
	
	
	@RequestMapping(value = "/fileIds/{" + APPLICATION_ID + "}/ {" + IDS_ID + "}", method = RequestMethod.GET)
	public String fileIds(@PathVariable(APPLICATION_ID) final Long appId, @PathVariable(IDS_ID) final Long IDSId , HttpServletRequest request, final Model model) {
		ApplicationDetailsDTO application = idsBuildService.fetchApplicationInfo(appId);
		application.setDbId(appId);
		application.setIdsID(IDSId);
		model.addAttribute(ApplicationDetailsDTO.MODEL_NAME, application);

		fetchUSPatents(appId, application.getIdsID(), model);
		/*viewPreviewPage(appId, request, model);*/
		
		ReferenceRecordsFilter refFilter = new ReferenceRecordsFilter(appId, IDSId);

		Map<ReferenceType, Long> countResult = idsBuildService.countReferenceRecords(refFilter);
		Map<String, Long> countsMap = new HashMap<>(countResult.size());
		// countsMap.putAll(countResult);
		countResult.forEach((k, v) -> countsMap.put(k.toString(), v));
		model.addAttribute("refCounts", countsMap);
		model.addAttribute(MODULE_NAME, "previewIDS");
		
		return "fileIds";
	}
	
	/**
	 * IDS drill down citedReference Source Document section
	 * 
	 * @param refflowId
	 * @param request
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/drillDown/referenceSourceDocument/" ,  method = RequestMethod.POST)
	public @ResponseBody List<IDSDrillDownInfoDTO> getIDSSourceDocumentInfo(@RequestParam(value = REFERENCE_BASE_ID) String refBaseId, final HttpServletRequest request, final Model model) {
		List<IDSDrillDownInfoDTO> idsReferenceSourceDoc = idsDashboardService.getIDSDrillDownReferenceSource(Long.valueOf(refBaseId));
		return idsReferenceSourceDoc;
	}
	
	/**
	 * IDS drill down citedReference Source Document other references section
	 * 
	 * @param correspondenceId
	 * @param request
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/drillDown/otherReferences/{"+ CORRESPONDENCE_ID +"}" ,  method = RequestMethod.POST)
	public @ResponseBody List<IDSDrillDownInfoDTO> getIDSOtherReferencesInfo(@PathVariable(value = CORRESPONDENCE_ID) final String corrId, final HttpServletRequest request, final Model model) {
		List<IDSDrillDownInfoDTO> idsSourceDocOtherReferences = idsDashboardService.getIDSDrillDownReferenceSourceOthers(Long.valueOf(corrId));
		return idsSourceDocOtherReferences;
	}
	
	@RequestMapping(value = "fileIDS/submit/{" + APPLICATION_ID + "}/{" + IDS_ID + "}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String prepareFileIDSCertification(@PathVariable(APPLICATION_ID) final Long application,
			@PathVariable(IDS_ID) final Long idsID, final Model model) {

		buildCertifcationStatement(idsID, application , FILE_IDS_CERTIFICATE, model);
		model.addAttribute(IDSCertificationForm.PRIVATE_PAIR_KEY,idsDashboardService.getPrivatePairKeys());

		return BuildIDSView.CERTIFICATION_STATEMENT.viewName;
	}
	
	private void buildCertifcationStatement(Long idsID, Long application ,String source,  Model model) {
		
		CertificationStatement dbCertificate = idsBuildService.fetchCertificationStatement(idsID);
		IDSCertificationForm certificate = new IDSCertificationForm(idsID, application, dbCertificate, FILE_IDS_CERTIFICATE);
		certificate.setFilingFee(idsBuildService.calculateIDSFilingFee(idsID));
		model.addAttribute(IDSCertificationForm.MODEL_KEY, certificate);
	}
	
	/*userAction/rejectUploadIDS*/
	
	@RequestMapping(value = "userAction/rejectUploadIDS/{" + NOTIFICATION_ID + "}/", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rejectUploadIDS(@PathVariable(NOTIFICATION_ID) final Long notificationId, final Model model) {

		idsDashboardService.rejectUploadManualIDS(notificationId);
		return "filingInProgressRecords";
	}
	
	
	@RequestMapping(value = "userAction/downloadIDS/{" + IDS_ID + "}/", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String downloadIDSAction(@PathVariable(IDS_ID) final Long idsID, final Model model) {

		idsDashboardService.downloadIDSUserAction(idsID);
		return "filingInProgressRecords";
	}
	
	@RequestMapping(value = "userAction/downloadIDSPackage/{" + IDS_ID + "}/", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String downloadIDSPackageAction(@PathVariable(IDS_ID) final Long idsID, final Model model) {

		idsDashboardService.downloadIDSPackageUserAction(idsID);
		return "filingInProgressRecords";
	}
}
