package com.blackbox.ids.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.tags.NestedPathTag;
import org.springframework.web.servlet.tags.form.FormTag;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.ChangeRequestDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.DraftDTO;
import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqAssigneeDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter.FamilyLinkage;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationScenario;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.services.common.EntityUserService;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.mdm.MdmDashboardService;
import com.blackbox.ids.ui.common.BlackboxFileHelper;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.form.mdm.ApplicationForm;
import com.blackbox.ids.ui.form.mdm.CreateApplicationRequestForm;
import com.blackbox.ids.ui.form.mdm.FamilySearchForm;
import com.blackbox.ids.ui.form.mdm.SingleApplicationForm;
import com.blackbox.ids.ui.validation.mdm.ApplicationFormValidator;

@Controller
@RequestMapping(value="/mdm")
public class MdmController extends BaseController {

	private final Logger log = Logger.getLogger(MdmController.class);

	private static final String KEY_SEARCH_RESULT = "searchResult";

	private static final String TRANSFER_RECORD = "transferRecord";

	private static final String ABANDON_RECORD = "abandonRecord";

	private static final String DELETE_RECORD = "deleteRecordPage";

	private static final String DACTIVATE_RECORD = "deactivateRecordPage";

	private static final String REACTIVE_RECORD = "reactivateRecord";

	private static final String VIEW_CREATE_APPLICATION = "newApplication";

	private static final String VIEW_APPLICATION_INIT = "application-init";

	private static final String VIEW_APPLICATION_DETAILS = "application-details";

	private static final String VIEW_APPLICATION = "application";

	private static final String REDIRECT_DASHBOARD = "redirect:/mdm/dashboard";

	private static final String SCREEN = "screen";

	private static final String DRAFT_AFTER = "draftAfterMS";

	private static final String MDM_COUNT = "mdmCount";

	private static final String ACTION_ITEMS_COUNT = "actionItemsCount";

	private static final String TAB_NAME = "tabName";

	private static final String LOV_SEPARATOR = ";";

	private static final String COUNT_FAMILY_MEMBER = "countFamilyMembers";

	@Autowired
	private MdmDashboardService dashboardService;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private ApplicationFormValidator applicationFormValidator;

	@Autowired
	private BlackboxFileHelper fileHelper;

	@Autowired
	private EntityUserService entityUserService;

	@Value("${manual.application.draft.after.minutes}")
	private int draftsAfterMinutes;

	public enum RecordsView {
		APPLICATION_VIEW,
		FAMILY_VIEW;
		public static final String MODEL_KEY = "recordsView";
	}

	public enum ApplicationScreen {
		INIT,
		DETAILS;

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

	/**
	 * Initiates form validator for application creation.
	 *
	 * @param binder
	 *            Binder that allows for setting property values onto a target object, including support for validation
	 *            and binding result analysis.
	 */
	@InitBinder(ApplicationForm.MODEL_KEY)
	protected void initApplicationBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.setValidator(applicationFormValidator);
	}

	/*- ----------------------------------------------------------------
	 			My Dashboard - Active Records - Application View
	 ------------------------------------------------------------------- */
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String viewDashboard(final Model model, final HttpSession session) {
		model.addAttribute(RecordsView.MODEL_KEY, RecordsView.APPLICATION_VIEW.name());
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		return "mdmDashboard";
	}

	@RequestMapping(value = "/activeRecords/view", method = RequestMethod.POST)
	public String viewActiveRecords(final HttpServletRequest request, final Model model) {

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
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);

		final SearchResult<MdmRecordDTO> searchResult = dashboardService.filterActiveRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT,searchResult);

		return "activeRecords";
	}

	/*- ----------------------------------------------------------------
				My Dashboard - Active Records - Family View
	------------------------------------------------------------------- */
	@RequestMapping(value = "/activeRecords/family", method = RequestMethod.POST)
	public String viewActiveRecordsFamilyView(final HttpServletRequest request, final Model model) {
		model.addAttribute(RecordsView.MODEL_KEY, RecordsView.FAMILY_VIEW.name());

		PaginationInfo pageInfo = getPaginationInfo(request);
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);

		SearchResult<MdmRecordDTO> searchResult = dashboardService.filterActiveRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);

		return "activeRecords";
	}

	/*- ----------------------------------------------------------------
				My Dashboard - InActive Records - Application View
	------------------------------------------------------------------- */
	@RequestMapping(value = "/inactiveRecords/view", method = RequestMethod.POST)
	public String viewInActiveRecords(final HttpServletRequest request, final Model model) {

		PaginationInfo pageInfo = getPaginationInfo(request);

		final String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		final Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		final ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);

		final SearchResult<MdmRecordDTO> searchResult = dashboardService.filterInActiveRecords(filter, pageInfo);
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);

		return "inactiveRecords";
	}

	/*- ----------------------------------------------------------------
			My Dashboard - All Family Records - Application View
	------------------------------------------------------------------- */
	@RequestMapping(value = "/activeRecords/allFamilyDetails/{familyId}/{applicationId}", method = RequestMethod.POST)
	public String getAllFamilyApplications(@PathVariable("familyId") String familyId, @PathVariable("applicationId") long appId,HttpServletRequest request, final Model model) {
		String viewName = request.getParameter("view");
		String myRecords = request.getParameter(DatatableAttribute.MY_RECORDS_ONLY.attrName);
		Long ownedBy = Boolean.valueOf(myRecords) ? loggedInUser() : null;

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);

		SearchResult<MdmRecordDTO>  searchResult = dashboardService.getAllFamilyApplications(filter, familyId , appId, viewName);
		model.addAttribute(TAB_NAME,viewName);
		model.addAttribute(KEY_SEARCH_RESULT,searchResult);
		return "allFamilyRecords";
	}

	/*- ----------------------------------------------
				Create Application - Manual
	------------------------------------------------ */

	@RequestMapping(value = "/createApp/createReq", method = RequestMethod.POST)
	public String createApplication(@ModelAttribute(CreateApplicationRequestForm.MODEL_NAME) final CreateApplicationRequestForm form,
			final Model model, final HttpSession session) {
		ApplicationForm applicationForm = new ApplicationForm(form);
		model.addAttribute(ApplicationForm.MODEL_KEY, applicationForm);
		return newApplication(applicationForm, model, session);
	}


	@RequestMapping(value = "/createApp/new", method = RequestMethod.GET)
	public String newApplication(@ModelAttribute(ApplicationForm.MODEL_KEY) final ApplicationForm applicationForm,
			final Model model, final HttpSession session) {

		processApplicationInitForm(applicationForm, model);
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		session.setAttribute(DRAFT_AFTER, draftsAfterMinutes * 60 * 1000);
		return VIEW_CREATE_APPLICATION;
	}

	@RequestMapping(value = "/createApp/details", method = RequestMethod.POST)
	public String addApplicationDetails(@Valid @ModelAttribute(ApplicationForm.MODEL_KEY) final ApplicationForm applicationForm,
			final BindingResult bindingResult, final Model model) {

		String view = null;
		ApplicationScreen screen = null;
		int countApplications = applicationForm.getApps().size();

		if (bindingResult.hasErrors()) {
			processApplicationInitForm(applicationForm, model);
			screen = ApplicationScreen.INIT;
			view = VIEW_APPLICATION_INIT;
		} else {
			processApplicationDetails(applicationForm, model);
			view = countApplications == 1 ? VIEW_APPLICATION_DETAILS : VIEW_APPLICATION;
			screen = ApplicationScreen.DETAILS;
		}

		applicationForm.setScreen(screen.name());
		model.addAttribute(SCREEN, screen);
		setApplicationFormPathPrefix(model, countApplications);
		return view;
	}

	private void setApplicationFormPathPrefix(Model model, int idxApplications) {
		model.addAttribute("countApplications", idxApplications);
		model.addAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, ApplicationForm.MODEL_KEY);
		model.addAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME,
				ApplicationForm.MODEL_KEY + "." + "apps[" + (idxApplications - 1) + "].");
	}

	@RequestMapping(value = "/createApp/anotherApp", method = RequestMethod.POST)
	public String viewApplication(
			@Valid @ModelAttribute(ApplicationForm.MODEL_KEY) final ApplicationForm applicationForm,
			final BindingResult bindingResult,
			final Model model) {

		ApplicationScreen screen = null;
		String view = VIEW_APPLICATION;
		int countApplications = applicationForm.getApps().size();

		if (bindingResult != null && bindingResult.hasErrors()) {
			processApplicationDetails(applicationForm, model);
			if (countApplications == 1) {
				view = VIEW_APPLICATION_DETAILS;
			}
			screen = ApplicationScreen.DETAILS;
			setApplicationFormPathPrefix(model, countApplications);
		} else {
			screen = ApplicationScreen.INIT;
			model.addAttribute("nonFirstOnly", true);
			model.addAttribute(Constants.LIST_JURISDICTIONS,
					populateJurisdictionList(masterDataService.getAllJurisdictions()));
			loadMasterData(model, ApplicationScreen.DETAILS);
		}

		model.addAttribute("countApplications", countApplications);
		model.addAttribute(SCREEN, screen);
		applicationForm.setScreen(screen.name());
		return view;
	}

	@RequestMapping(value = { "/createApp/save", "/editApp/save" }, method = RequestMethod.POST)
	public String saveApplication(@Valid @ModelAttribute(ApplicationForm.MODEL_KEY) final ApplicationForm applicationForm,
			final BindingResult bindingResult, final Model model) {

		String view = null;
		int numApps = applicationForm.getApps().size();

		if (bindingResult.hasErrors()) {
			processApplicationDetails(applicationForm, model);
			boolean updateRequest = applicationForm.getApps().get(0).getId() != null;
			view = updateRequest ? VIEW_APPLICATION : VIEW_APPLICATION_DETAILS;
			setApplicationFormPathPrefix(model, numApps);
		} else {
			final List<ApplicationBase> applications = applicationForm.toEntity();
			if (isUpdateRequest(applications)) {
				applicationService.updateApplication(applications.get(0),applicationForm.getNotificationId());
			} else {
				applicationService.saveManualApplications(applications, applicationForm.getNotificationId());
				deleteDrafts(applications);
			}
			view = "success";
		}
		return view;
	}


	private void deleteDrafts(List<ApplicationBase> applications) {
		Long currentUser = loggedInUser();
		List<DraftIdentityDTO> draftIds = new ArrayList<>();
		applications.forEach(e ->
		draftIds.add(new DraftIdentityDTO(e.getJurisdiction().getCode(),
				e.getAppDetails().getApplicationNumberRaw(), currentUser))
				);
		applicationService.deleteDrafts(draftIds);
	}

	private boolean isUpdateRequest(final List<ApplicationBase> applications) {
		return applications.size() == 1 && applications.get(0).getId() != null;
	}

	@RequestMapping(value = "/editApp/{app}", method = RequestMethod.GET)
	public String editApplication(@PathVariable("app") final Long applicationId,
			@RequestParam(value="notificationId", required = false) final Long notificationId,
			final Model model) {
		MdmRecordDTO application = applicationService.fetchApplicationDataForUpdate(applicationId);
		if(notificationId!=null) {
			application.setNotificationId(notificationId);
		}
		model.addAttribute(FamilySearchForm.KEY_FAMILY_LINKAGES, FamilyLinkage.values());
		model.addAttribute(COUNT_FAMILY_MEMBER, applicationService.countFamilyApplications(application.getFamilyId()));
		return editApplication(application, model);
	}

	@RequestMapping(value = "/leaveAppLock", method = RequestMethod.GET)
	@ResponseBody
	public String releaseApplicationLock() {
		entityUserService.releaseLocksHeldByUser(loggedInUser());
		return Boolean.TRUE.toString();
	}

	private String editApplication(MdmRecordDTO application, final Model model) {
		final ApplicationForm applicationForm = new ApplicationForm().update(application);
		if (application.getFamilyId() != null) {
			setFamilyDetails(application.getFamilyId(), applicationForm);
		}
		model.addAttribute(ApplicationForm.MODEL_KEY, applicationForm);
		setApplicationScenario(applicationForm.getApps().get(0));
		String view = viewApplication(applicationForm, null, model);
		applicationForm.setScreen(ApplicationScreen.DETAILS.name());
		return view;
	}

	private void setFamilyDetails(String familyId, ApplicationForm applicationForm) {
		SingleApplicationForm appForm = applicationForm.getApps().get(0);
		if (!ApplicationType.isFirstFiling(appForm.getApplicationType())) {
			FamilyDetailDTO familyDetails = applicationService.fetchFamilyDetails(familyId);
			applicationForm.setFamilyDetails(familyDetails);
		}else {
			FamilyDetailDTO familyDetails = applicationService.fetchFamilyDetails(familyId);
			applicationForm.setFamilyDetails(familyDetails);
		}
	}

	//TODO Use this to add edit family functionality
	@RequestMapping(value = "/editFamily/{familyId}", method = RequestMethod.POST)
	public String editFamily(@PathVariable("app") final Long applicationId, final Model model) {
		MdmRecordDTO application = applicationService.fetchApplicationDataForUpdate(applicationId);
		return editApplication(application, model);
	}

	@ResponseBody
	@RequestMapping(value = "/createApp/draftAutoSave", method = RequestMethod.POST)
	public String draftApplications(@ModelAttribute(ApplicationForm.MODEL_KEY) final ApplicationForm applicationForm) {

		Boolean status = Boolean.TRUE;
		List<ApplicationStage> drafts = applicationForm.toDrafts();
		try {
			applicationService.saveApplicationDrafts(drafts);
		} catch (final ApplicationException e) {
			log.error("Failed to save draft records.", e);
			status = Boolean.FALSE;
		}
		return status.toString();
	}

	private void processApplicationInitForm(ApplicationForm applicationForm, Model model) {
		applicationForm.setScreen(ApplicationScreen.INIT.name());
		loadMasterData(model, ApplicationScreen.INIT);
	}

	private void processApplicationDetails(ApplicationForm applicationForm, Model model) {
		applicationForm.getApps().forEach(e -> setApplicationScenario(e));

		final Long parentApplication = applicationForm.getParentApplication();
		if (parentApplication != null) {
			final MdmRecordDTO applicationDetails = applicationService.fetchApplicationDetails(parentApplication);
			applicationForm.getApps().get(0).updateData(applicationDetails);
		}

		loadMasterData(model, ApplicationScreen.DETAILS);
	}

	private ApplicationScenario setApplicationScenario(SingleApplicationForm applicationForm) {
		ApplicationScenario scenario = ApplicationScenario.getScenario(applicationForm.getJurisdictionName(),
				applicationForm.getApplicationType());
		applicationForm.setScenario(scenario.name());
		log.info(format("[Create Application] - Creating application creation for SCENARIO: {0}.", scenario));

		applicationForm.setFirstFiling(ApplicationType.isFirstFiling(applicationForm.getApplicationType()));
		return scenario;
	}

	private void loadMasterData(final Model model, ApplicationScreen screen) {

		switch (screen) {
		case INIT:
			model.addAttribute(Constants.LIST_JURISDICTIONS, populateJurisdictionList(masterDataService.getAllJurisdictions()));
			break;
		case DETAILS:
			model.addAttribute(Constants.LIST_ASSIGNEE,
					StringUtils.join(masterDataService.getUserAssignees(), LOV_SEPARATOR));
			model.addAttribute(Constants.LIST_ENTITIY,
					StringUtils.join(masterDataService.getAllEntity(), LOV_SEPARATOR));
			model.addAttribute(Constants.LIST_CUSTOMER,
					StringUtils.join(masterDataService.getUserCustomerNumbers(), LOV_SEPARATOR));
			break;
		default:
			break;
		}
		model.addAttribute(FamilySearchForm.KEY_FAMILY_LINKAGES, FamilyLinkage.values());
		loadApplicationTypesPerJurisdiction(model);
	}

	private static void loadApplicationTypesPerJurisdiction(Model model) {
		model.addAttribute(Constants.LIST_APPLICATION_TYPES_US, ApplicationType.usAppTypes());
		model.addAttribute(Constants.LIST_APPLICATION_TYPES_WO, ApplicationType.woAppTypes());
		model.addAttribute(Constants.LIST_APPLICATION_TYPES_OTHER, ApplicationType.otherAppTypes());
		model.addAttribute(Constants.LIST_APPLICATION_TYPES_UPDATE_FAMILY_LINKAGE,ApplicationType.updateFamilyLinkageAppTypes());
	}

	/* Service call to search a family. */
	@RequestMapping(value = "/createApp/familySearch", method = RequestMethod.POST)
	public String searchFamily(@ModelAttribute(FamilySearchForm.KEY_FORM) final FamilySearchForm familySearchForm,
			final Model model) {

		final FamilySearchFilter searchFilter = familySearchForm.toFilter();
		final List<FamilyDetailDTO> families = applicationService.fetchFamilyDetail(searchFilter);
		model.addAttribute(KEY_SEARCH_RESULT, families);
		return "family-search";
	}

	/*- ----------------------------------------------
				Master Data Store - Drafts
	------------------------------------------------ */
	@RequestMapping(value="/drafts", method = RequestMethod.GET)
	public String viewDraftsAdmin(HttpSession session, final Model model) {

		final List<DraftDTO> drafts = dashboardService.getCurrentUserDrafts();
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		model.addAttribute("listItems", drafts);
		return "drafts";
	}

	@RequestMapping(value="/createApp/deleteDraft/{id}", method = RequestMethod.GET)
	public String deleteDraftAdmin(@PathVariable("id") String draftId) {

		log.info(format("[DRAFT]: Delete request received for draft {0}.", draftId));
		dashboardService.deleteDraft(Integer.parseInt(draftId));
		log.info(format("[DRAFT]: Successfully deleted draft {0}.", draftId));
		return "redirect:/mdm/drafts";
	}

	@RequestMapping(value="/createApp/viewDraft/{id}", method = RequestMethod.GET)
	public String openDraftAdmin(@PathVariable("id") String draftId , final Model model) {

		log.info(format("[DRAFT]: OPEN request received for draft {0}.", draftId));
		MdmRecordDTO application = dashboardService.openDraft(Integer.parseInt(draftId));
		log.info(format("[DRAFT]: Successfully opened draft {0}.", draftId));
		return editApplication(application, model);
	}

	/*- ----------------------------------------------
			Master Data Store - Action Items
	------------------------------------------------ */

	@RequestMapping(value = "/actionItems", method = RequestMethod.GET)
	public String viewActionItems(HttpSession session) {
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		return "actionItems";
	}

	/*Service call to show create application record list*/
	@RequestMapping(value = "/actionItems/createRequestApp/view", method = RequestMethod.POST)
	public String createAppRecords(final HttpServletRequest request ,HttpSession session,  final Model model) {

		PaginationInfo pageInfo = getPaginationInfo(request);

		Long ownedBy = loggedInUser();

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);

		SearchResult<CreateReqApplicationDTO> searchResult = dashboardService.getCreateAppRecords(filter, pageInfo);
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		model.addAttribute(KEY_SEARCH_RESULT,searchResult);
		return "create-app-records";
	}


	/*Service call to show create family records list*/
	@RequestMapping(value = "/actionItems/createRequestFamily/view", method = RequestMethod.POST)
	public String createFamilyRecords(final HttpServletRequest request,HttpSession session, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);

		Long ownedBy = loggedInUser();

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);
		SearchResult<CreateReqFamilyDTO> searchResult = dashboardService.getCreateFamilyRecords(filter, pageInfo);
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		model.addAttribute(KEY_SEARCH_RESULT,searchResult);
		return "create-family-records";
	}


	/*Service call to show create family records list*/
	@RequestMapping(value = "/actionItems/changeRequest/view", method = RequestMethod.POST)
	public String changeRequestRecords(final HttpServletRequest request, HttpSession session, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);

		Long ownedBy = loggedInUser();

		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				ownedBy);
		SearchResult<ChangeRequestDTO> searchResult = dashboardService.getChangeRequestRecords(filter, pageInfo);
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		model.addAttribute(KEY_SEARCH_RESULT,searchResult);
		return "change-req-records";
	}


	/*Service call to show update application records list*/

	@RequestMapping(value = "/actionItems/updateRequestApp/view", method = RequestMethod.POST)
	public String updateAppRecords(final HttpServletRequest request, HttpSession session, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);
		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				loggedInUser());
		SearchResult<UpdateReqApplicationDTO> searchResult = dashboardService.getUpdateReqApplicationRecords(filter,pageInfo);
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return "updateAppRecords";
	}

	/*Service call to show update assignee records list*/

	@RequestMapping(value = "/actionItems/updateRequestAssignee/view", method = RequestMethod.POST)
	public String updateAssigneeRecords(final HttpServletRequest request, HttpSession session, final Model model) {

		PaginationInfo pageInfo = getPaginationInfo(request);
		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				loggedInUser());
		SearchResult<UpdateReqAssigneeDTO> searchResult = dashboardService.getUpdateReqAssigneeRecords(filter,pageInfo);
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return "updateAssigneeRecords";
	}

	/*Service call to show update family records list*/

	@RequestMapping(value = "/actionItems/updateRequestFamilyLinkage/view", method = RequestMethod.POST)
	public String updateFamilyRecords(final HttpServletRequest request, HttpSession session, final Model model) {
		PaginationInfo pageInfo = getPaginationInfo(request);
		ActiveRecordsFilter filter = new ActiveRecordsFilter(
				request.getParameter(DatatableAttribute.SEARCH_TEXT.attrName),
				request.getParameter(DatatableAttribute.DATE_RANGE.attrName),
				loggedInUser());
		SearchResult<UpdateReqFamilyDTO> searchResult = dashboardService.getUpdateReqFamilyRecords(filter, pageInfo);
		session.setAttribute(ACTION_ITEMS_COUNT, getActionItemsCountMap());
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return "updatefamilyRecords";
	}

	/*Service call to show transfer record pop up*/
	@RequestMapping(value = "/application/transfer/record", method = RequestMethod.GET, produces = "text/html")
	public String transferRecord(@RequestParam("recordId") Long recordId, final Model model) {
		MdmRecordDTO mdmRecordDTO = dashboardService.getMdmRecordDTO(recordId);
		model.addAttribute("mdmRecord", mdmRecordDTO);
		return TRANSFER_RECORD;
	}

	/*Service call to change the record Status to transfer*/
	@RequestMapping(value = "/application/transfer/recordStatus", method = RequestMethod.GET)
	public ModelAndView updateRecordStatusToTransfer(@ModelAttribute("mdmRecordStatus") MdmRecordDTO mdmRecordDTO, final HttpServletRequest request) {
		dashboardService.updateRecordStatusToTransfer(mdmRecordDTO, request.getParameter("recordId"), request.getParameter("familyId"), MDMRecordStatus.TRANSFERRED);
		return new ModelAndView(REDIRECT_DASHBOARD);
	}

	/*Service call to show activate record pop up*/
	@RequestMapping(value = "/application/reactivate/record", method = RequestMethod.GET, produces = "text/html")
	public String activateRecordPage(@RequestParam("recordId") Long recordId, final Model model) {
		final MdmRecordDTO mdmRecordDTO = dashboardService.getMdmRecordDTO(recordId);
		model.addAttribute("mdmRecord", mdmRecordDTO);
		return REACTIVE_RECORD;
	}

	/*Service call to change the record Status to activate*/
	@RequestMapping(value = "/application/reactivate/recordStatus", method = RequestMethod.GET)
	public ModelAndView updateRecordStatusToActivate(@ModelAttribute("mdmRecordStatus") MdmRecordDTO mdmRecordDTO, final HttpServletRequest request) {
		dashboardService.updateRecordStatusToActive(mdmRecordDTO, request.getParameter("recordId"), MDMRecordStatus.ACTIVE);
		return new ModelAndView(REDIRECT_DASHBOARD);
	}

	/*Service call to show abandon record pop up*/
	@RequestMapping(value = "/application/abandon/record", method = RequestMethod.GET, produces = "text/html")
	public String abandonRecordPage(@RequestParam("recordId") Long recordId, final Model model) {
		final MdmRecordDTO mdmRecordDTO = dashboardService.getMdmRecordDTO(recordId);
		model.addAttribute("mdmRecord", mdmRecordDTO);
		return ABANDON_RECORD;
	}

	/*Service call to change the record Status to abandon*/
	@RequestMapping(value = "/application/abandon/recordStatus", method = RequestMethod.GET)
	public ModelAndView abandonRecordStatus(@ModelAttribute("mdmRecordStatus") MdmRecordDTO mdmRecordDTO,  final HttpServletRequest request) {
		dashboardService.updateRecordStatusToAbandon(mdmRecordDTO, request.getParameter("recordId"), request.getParameter("familyId"), MDMRecordStatus.ALLOWED_TO_ABANDON);
		return new ModelAndView(REDIRECT_DASHBOARD);
	}

	/*Service call to show delete record pop up*/
	@RequestMapping(value = "/application/delete/record", method = RequestMethod.GET, produces = "text/html")
	public String droppedRecordStatusPage(@RequestParam("recordId") Long recordId, final Model model) {
		MdmRecordDTO mdmRecordDTO = dashboardService.getDeleteAffectedTransactions(recordId);
		model.addAttribute("mdmRecord", mdmRecordDTO);
		return DELETE_RECORD;
	}

	/*Service call to change the record Status to delete*/
	@RequestMapping(value = "/application/delete/recordStatus", method = RequestMethod.GET)
	public ModelAndView droppedRecordStatus(@RequestParam("recordId") Long recordId, MdmRecordDTO mdmRecordDTO) {
		try{
			Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
			dashboardService.createDroppedApprovalNotification(roleIds, recordId, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, mdmRecordDTO, MDMRecordStatus.DROPPED);
		}catch(ApplicationException ae){
			log.error("Notification cannot be genertaed due to System Errors", ae);
		}
		return new ModelAndView(REDIRECT_DASHBOARD);
	}

	/*Service call to show deactivate record pop up*/
	@RequestMapping(value = "/application/deactivate/record", method = RequestMethod.GET, produces = "text/html")
	public String deactivateRecord(@RequestParam("recordId") Long recordId, final Model model) {
		MdmRecordDTO mdmRecordDTO = dashboardService.getDeactivateAffectedTransactions(recordId);
		model.addAttribute("mdmRecord", mdmRecordDTO);
		return DACTIVATE_RECORD;
	}

	/*Service call to change the record Status to deactivate*/
	@RequestMapping(value = "/application/deactivate/recordStatus", method = RequestMethod.GET)
	public ModelAndView deactivateRecordStatus(@RequestParam("recordId") Long recordId, MdmRecordDTO mdmRecordDTO) {
		try{
			Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
			dashboardService.createDeactivateApprovalNotification(roleIds, recordId, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, mdmRecordDTO, MDMRecordStatus.DEACTIVATED);
		}catch(ApplicationException ae){
			log.error("Notification cannot be genertaed due to System Errors", ae);
		}
		return new ModelAndView(REDIRECT_DASHBOARD);
	}

	/*Service call to change the record Status to deactivate*/
	@RequestMapping(value = "/actions/deactivate/reject", method = RequestMethod.POST)
	public String rejectDeactivateStatusRequest(HttpServletRequest request, HttpSession session){
		dashboardService.rejectDeactivateStatusRequest(request.getParameter("notificationId"), request.getParameter("recordId"), MDMRecordStatus.BLANK);
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		return "change-req-records";
	}

	/*Service call to change the record Status to deactivate*/
	@RequestMapping(value = "/actions/drop/reject", method = RequestMethod.POST)
	public String rejectDroppedStatusRequest(HttpServletRequest request, HttpSession session){
		dashboardService.rejectDroppedStatusRequest(request.getParameter("notificationId"), request.getParameter("recordId"), MDMRecordStatus.BLANK);
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		return "change-req-records";
	}

	/*Service call to deactivate record*/
	@RequestMapping(value = "/actions/deactivate/approve", method = RequestMethod.POST)
	public String approveDeactivateStatusRequest(final HttpServletRequest request, HttpSession session){
		long recordId = Long.parseLong(request.getParameter("recordId"));
		long notificationId = Long.parseLong(request.getParameter("notificationId"));
		dashboardService.updateRecordStatusToDeactivate(request.getParameter("notificationId"),request.getParameter("recordId"), MDMRecordStatus.DEACTIVATED, MDMRecordStatus.BLANK);
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		return "change-req-records";
	}

	/*Service call to delete record*/
	@RequestMapping(value = "/actions/drop/approve", method = RequestMethod.POST)
	public String approveDroppedStatusRequest(final HttpServletRequest request, HttpSession session){
		dashboardService.updateRecordStatusToDelete(request.getParameter("notificationId"), request.getParameter("recordId"), MDMRecordStatus.DROPPED, MDMRecordStatus.BLANK);
		session.setAttribute(MDM_COUNT, getMdmCountMap());
		return "change-req-records";
	}

	@RequestMapping(value="/actionItems/createRequestApp/getPdf/{id}",method = RequestMethod.GET)
	public void getPdfpath(@PathVariable("id") long id,final HttpServletResponse response) throws IOException {
		final String pdfFilePath = BlackboxUtils.concat(FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("staging"),String.valueOf(id),File.separator,String.valueOf(id),".pdf");
		File file = new File(pdfFilePath);
		fileHelper.viewFile(response, file);
	}

	@RequestMapping(value="/actionItems/updateRequestAssignee/addAssignee",method = RequestMethod.POST)
	@ResponseBody
	public long updateAssignee(HttpServletRequest request) {
		long records = 0L;
		String familyId = request.getParameter("familyid");
		String assignee = request.getParameter("assignee");
		String notificationId = request.getParameter("notificationid");
		log.info(format("[Assignee]: Add request received for Assignee {0}.", familyId));
		if(!StringUtils.isEmpty(notificationId))
		{
			records =  masterDataService.updateAssignee(familyId, assignee,Long.valueOf(notificationId));
			log.info(format("[Assignee]: Successfully updated {0} assignee for {1}.", records,familyId));
		}
		return records;
	}

	@RequestMapping(value="/actionItems/createRequestApp/reject",method = RequestMethod.POST)
	@ResponseBody
	public long rejectCreateApplicationRequest(HttpServletRequest request)
	{
		long records = 0L;
		String entityId = request.getParameter("entityId");
		String entityName = request.getParameter("entityName");
		String notificationId = request.getParameter("notificationId");
		log.info(format("[Create Application Request]: reject request received for Application from Entity {0}.", entityName));
		if(!StringUtils.isEmpty(entityId) && !StringUtils.isEmpty(notificationId))
		{
			records =  applicationService.rejectCreateAppRequest(Long.valueOf(entityId), entityName,Long.valueOf(notificationId));
			log.info(format("[Create Application Request]: Successfully updated {0} records for {1}-{2}.", records,entityId,entityName));
		}
		return records;
	}

	//TODO just writing api ,  use this API and add functionality
	@RequestMapping(value="/actionItems/createRequestFamily/reject",method = RequestMethod.POST)
	@ResponseBody
	public long rejectCreateFamilyRequest(HttpServletRequest request)
	{
		long records = 0L;
		String entityId = request.getParameter("entityId");
		String entityName = request.getParameter("entityName");
		String notificationId = request.getParameter("notificationId");
		log.info(format("[Create Application Request]: reject request received for Application from Entity {0}.", entityName));
		if(!StringUtils.isEmpty(entityId) && !StringUtils.isEmpty(notificationId))
		{
			records =  applicationService.rejectCreateAppRequest(Long.valueOf(entityId), entityName,Long.valueOf(notificationId));
			log.info(format("[Create Application Request]: Successfully updated {0} records for {1}-{2}.", records,entityId,entityName));
		}
		return records;
	}

	@RequestMapping(value="/actionItems/updateRequestFamilyLinkage/reject",method = RequestMethod.POST)
	@ResponseBody
	public long rejectUpdateFamilyRequest(HttpServletRequest request)
	{
		long records = 0L;
		String entityId = request.getParameter("entityId");
		String entityName = request.getParameter("entityName");
		String notificationId = request.getParameter("notificationId");
		log.info(format("[Update Family Request]: reject request received for Family linkage from Entity {0}.", entityName));
		if(!StringUtils.isEmpty(entityId) && !StringUtils.isEmpty(notificationId))
		{
			records =  applicationService.rejectUpdateFamilyRequest(Long.valueOf(entityId), entityName,Long.valueOf(notificationId));
			log.info(format("[Update Family Request]: Successfully updated {0} records for {1}-{2}.", records,entityId,entityName));
		}
		return records;
	}

	@RequestMapping(value="/actionItems/updateRequestAssignee/getAllAssignees",method = RequestMethod.GET)
	public @ResponseBody List<String> getAllAssignee() {
		List<String> records =  masterDataService.getUserAssignees();
		return records;
	}


	/*service call to view a family*/

	@RequestMapping(value = "/viewFamily/{familyId}", method = RequestMethod.GET)
	public String viewFamily(@PathVariable("familyId") String familyId,
			final Model model) {

		FamilySearchFilter searchFilter = new FamilySearchFilter();
		searchFilter.setFamilyId(familyId);
		searchFilter.setFamilyLinker(FamilyLinkage.FAMILY_ID);
		searchFilter.setOnlyFirstFiling(false);
		List<FamilyDetailDTO> searchResult = applicationService.fetchFamilyDetail(searchFilter);
		model.addAttribute("familyId", familyId);
		model.addAttribute(KEY_SEARCH_RESULT, searchResult);
		return "family-view";
	}

	/*
	 * return ";" separated string which will be used to auto populate Jurisdiction field
	 */

	private String populateJurisdictionList(List<JurisdictionDTO> jurisdictionList) {
		List<String> jurisdictions = new ArrayList<>();
		for(JurisdictionDTO jurisdictionDTO : jurisdictionList) {
			jurisdictions.add(jurisdictionDTO.getName());
		}
		return StringUtils.join(jurisdictions,";");
	}

	private Map<String, Long> getMdmCountMap(){
		long countActionItems = dashboardService.countActionItems();
		long countDraftItems = dashboardService.getCurrentUserDrafts().size();
		Map<String, Long> mdmCounts = new HashMap<String, Long>();
		mdmCounts.put("countActionItems", countActionItems);
		mdmCounts.put("countDraftItems", countDraftItems);
		return mdmCounts;
	}

	private Map<String, Long> getActionItemsCountMap(){
		long createRequestCount = dashboardService.countCreateRequestRecords();
		long updateRequestCount = dashboardService.countUpdatRequestsRecords();
		long changeRequestCount = dashboardService.countChangeRequestRecords();
		Map<String, Long> mdmActionCounts = new HashMap<String, Long>();
		mdmActionCounts.put("createRequestCount", createRequestCount);
		mdmActionCounts.put("updateRequestCount", updateRequestCount);
		mdmActionCounts.put("changeRequestCount", changeRequestCount);
		return mdmActionCounts;
	}

}