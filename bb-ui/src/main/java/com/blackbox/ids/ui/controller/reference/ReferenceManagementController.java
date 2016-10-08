package com.blackbox.ids.ui.controller.reference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.reference.ReferenceDashboardService;
import com.blackbox.ids.services.reference.ReferenceManagementService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.blackbox.ids.ui.common.BlackboxFileHelper;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.controller.BaseController;
import com.mysema.query.types.Predicate;

/**
 * The Class ReferenceManagementController.
 */
@Controller
@RequestMapping("/reference")
public class ReferenceManagementController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(ReferenceManagementController.class);

	/** The Constant KEY_JURISDICTION. */
	private static final String KEY_JURISDICTION = "jurisdiction";

	/** The Constant KEY_APPLICATION. */
	private static final String KEY_APPLICATION = "application";

	/** The Constant KEY_CORRESPONDENCE. */
	private static final String KEY_CORRESPONDENCE = "correspondence";

	/** The Constant KEY_CORRESPONDENCE. */
	private static final String KEY_CORRESPONDENCE_ID = "correspondenceId";

	/** The Constant KEY_VIEW_NAME. */
	private static final String KEY_VIEW_NAME = "view";

	/** The Constant KEY_VIEW_NAME_ALL_RECORD. */
	private static final String KEY_VIEW_NAME_ALL_RECORD = "all-records-detail";

	/** The Constant KEY_VIEW_NAME_PENDING_RECORD. */
	private static final String KEY_VIEW_NAME_PENDING_RECORD = "pending-reference-detail";

	/** The Constant KEY_REDIRECT_URL. */
	private static final String KEY_REDIRECT_URL = "redirectUrl";

	/** The Constant KEY_REFERENCE_SUBMENU. */
	private static final String KEY_REFERENCE_SUBMENU = "referenceSubMenu";

	/** The Constant KEY_ALL_RECORDS. */
	private static final String KEY_ALL_RECORDS = "allRecords";

	/** The Constant KEY_PENDING_RECORDS. */
	private static final String KEY_PENDING_RECORDS = "pendingRecords";

	/** The Constant KEY_HIDE. */
	private static final String KEY_HIDE = "hide";

	/** The Constant KEY_REFERENCE_DTO. */
	private static final String KEY_REFERENCE_DTO = "referenceDTO";

	/** The master data service. */
	@Autowired
	private MasterDataService masterDataService;

	/** The reference service. */
	@Autowired
	private ReferenceService referenceService;

	/** The reference management service. */
	@Autowired
	private ReferenceManagementService referenceManagementService;

	/** The reference dashboard service. */
	@Autowired
	private ReferenceDashboardService referenceDashboardService;

	/** The file helper. */
	@Autowired
	private BlackboxFileHelper fileHelper;

	/**
	 * View management.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping("/management")
	public String viewManagement(Model model) {

		LOGGER.debug("View Reference Management page");
		model.addAttribute("fromReference", true);

		if (!BlackboxSecurityContextHolder.canAccessUrl("/reference*")) {
			return "reference.noAccess";
		}

		if (BlackboxSecurityContextHolder.canAccessUrl("/reference/management/allRecords")) {
			model.addAttribute(KEY_REFERENCE_SUBMENU, KEY_ALL_RECORDS);
		} else {
			if (BlackboxSecurityContextHolder.canAccessUrl("/reference/management/pendingRecords")) {
				model.addAttribute(KEY_REFERENCE_SUBMENU, KEY_PENDING_RECORDS);
			} else {
				model.addAttribute(KEY_REFERENCE_SUBMENU, "");
			}
		}

		return "reference.management";
	}

	/**
	 * Adds the null reference in the Null Reference document for a correspondence document.
	 * 
	 * @param request
	 * @return
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/management/addNullReference", method = RequestMethod.POST)
	public String addNullReference(HttpServletRequest request, Model model) throws ServletRequestBindingException {

		LOGGER.debug("Adding null reference in  Database");
		Long correspondenceid = ServletRequestUtils.getLongParameter(request, KEY_CORRESPONDENCE);
		String nplString = referenceDashboardService.createNPLStringForNullReferenceDoc(correspondenceid);
		referenceService.updateNullReference(correspondenceid, true, null, nplString);
		return "redirect:../management";
	}

	/**
	 * Used to the document details and the reference details.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/management/allRecords", method = RequestMethod.POST)
	public String showAllRecords(HttpServletRequest request, Model model) {

		LOGGER.debug("Showing all records");
		if (!BlackboxSecurityContextHolder.canAccessUrl("/reference/management")) {
			return "redirect:dashboard";
		}

		Predicate predicate = getPredicate(request);
		Pageable pageInfo = getPageInfo(request);
		Page<CorrespondenceDTO> correspondenceList = referenceManagementService.getAllRecords(predicate, pageInfo);
		model.addAttribute("referenceGroup", correspondenceList);
		model.addAttribute(KEY_REFERENCE_SUBMENU, KEY_ALL_RECORDS);
		return "all-records";
	}

	/**
	 * Show pending records from staging table.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/management/pendingRecords", method = RequestMethod.POST)
	public String showPendingRecords(HttpServletRequest request, Model model) {

		LOGGER.debug("Showing pending records");
		if (!BlackboxSecurityContextHolder.canAccessUrl("/reference/management")) {
			return "redirect:dashboard";
		}

		Predicate predicate = getPredicate(request);
		Pageable pageInfo = getPageInfo(request);
		model.addAttribute("pendingList", referenceManagementService.getPendingReferences(predicate, pageInfo));
		model.addAttribute(KEY_REFERENCE_SUBMENU, KEY_PENDING_RECORDS);
		return "pending-reference";
	}

	/**
	 * Ajax call to display relevant correspondence details.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the correspondence
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/management/getCorrespondence", method = RequestMethod.GET)
	public String getCorrespondence(HttpServletRequest request, Model model) {

		String corrId = request.getParameter(KEY_CORRESPONDENCE);
		String jurisCode = request.getParameter(KEY_JURISDICTION);
		String appNo = request.getParameter(KEY_APPLICATION);
		String view = request.getParameter(KEY_VIEW_NAME);

		Long corr = null;
		if (StringUtil.isNotBlank(corrId)) {
			corr = Long.valueOf(corrId);
		}
		List<CorrespondenceDTO> correspondenceList = null;
		if (view.equalsIgnoreCase(KEY_VIEW_NAME_ALL_RECORD)) {
			correspondenceList = referenceManagementService.getCorrespondence(jurisCode, appNo);
		}

		if (view.equalsIgnoreCase(KEY_VIEW_NAME_PENDING_RECORD)) {
			correspondenceList = referenceManagementService.getCorrespondenceFromStaging(jurisCode, appNo);
		}

		// remove already displayed correspondence data
		if (corr != null) {
			Iterator<CorrespondenceDTO> ite = correspondenceList.iterator();
			while (ite.hasNext()) {
				if (corr.compareTo(ite.next().getId()) == 0) {
					ite.remove();
				}
			}
		}

		model.addAttribute("correspondenceList", correspondenceList);

		return view;
	}

	/**
	 * Creates the reference.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 */
	@RequestMapping("/management/create")
	public String createReference() {
		return "add-reference-detail";
	}

	/**
	 * Review reference which are already extracted.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/management/review", method = { RequestMethod.POST, RequestMethod.GET })
	public String reviewReference(HttpServletRequest request, Model model) throws IOException {

		String corrId = null;
		corrId = request.getParameter(KEY_CORRESPONDENCE);
		Long id = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;

		/** to get the attributes from redirected url **/
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {
			corrId = (String) model.asMap().get(KEY_CORRESPONDENCE);
			id = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;
		}
		getModelWithReferenceDTO(id, model);
		model.addAttribute(KEY_REDIRECT_URL, "redirect:../management/review");
		return "review-references";
	}

	/**
	 * Delete the reviewed reference.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/management/reviewUpdate", method = RequestMethod.POST)
	public String deleteReviewReference(HttpServletRequest request, Model model, RedirectAttributes ra) throws ServletRequestBindingException {
		Long refId = ServletRequestUtils.getLongParameter(request, "id");
		String corrId = ServletRequestUtils.getStringParameter(request, KEY_CORRESPONDENCE);
		Long corrid = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;
		model.addAttribute(KEY_CORRESPONDENCE, corrid);
		if (refId != null) {
			referenceService.removeReference(refId);
		}
		getModelWithReferenceDTO(corrid, model);
		String referrer = request.getHeader("referer");
		if(referrer.contains("/ids/")) {
			ra.addFlashAttribute(KEY_CORRESPONDENCE_ID, corrid);
			return "redirect:../ids/editReference";
		}
		return "review-references";
	}

	/**
	 * This function is used to view the PDF attachment for document.
	 *
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/management/download", method = { RequestMethod.POST, RequestMethod.GET })
	public void viewPDFAttachment(HttpServletResponse response, HttpServletRequest request)
			throws ServletRequestBindingException, IOException {
		Long correspondenceId = ServletRequestUtils.getLongParameter(request, "correspondenceId");
		final String pdfFilePath = BlackboxUtils.concat(FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("base"),
				String.valueOf(correspondenceId), File.separator, String.valueOf(correspondenceId), ".pdf");
		File file = new File(pdfFilePath);
		fileHelper.viewFile(response, file);
	}

	/**
	 * This function is used to view the PDF attachment for reference.
	 *
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping(value = "/management/download/reference", method = { RequestMethod.POST, RequestMethod.GET })
	public void viewReferencePDFAttachment(HttpServletResponse response, HttpServletRequest request)
			throws ServletRequestBindingException, IOException {
		Long referenceId = ServletRequestUtils.getLongParameter(request, "id");
		ReferenceType referenceType = ReferenceType
				.valueOf(ServletRequestUtils.getStringParameter(request, "referenceType"));
		final String pdfFilePath = referenceService.getReferenceAttachmentUploadPath(referenceId, referenceType);
		File file = new File(pdfFilePath);
		fileHelper.viewFile(response, file);
	}

	/**
	 * From ref mgmt.
	 *
	 * @return true, if successful
	 */
	@ModelAttribute("fromRefMgmt")
	public boolean fromRefMgmt() {
		return true;
	}

	/**
	 * Creates a new reference.
	 *
	 * @param referenceDTO
	 *            the reference dto
	 * @param result
	 *            the result
	 * @param ra
	 *            the ra
	 * @param model
	 *            the model
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ApplicationException
	 *             the application exception
	 * @throws IOException
	 */
	@RequestMapping(value = "/management/addReference", method = { RequestMethod.POST, RequestMethod.GET })
	public String createReference(@ModelAttribute("referenceDTO") ReferenceBaseDTO referenceDTO, RedirectAttributes ra,
			HttpServletRequest request, Model model, BindingResult bindingResult) throws ApplicationException {

		String navigateTo = request.getParameter(KEY_REDIRECT_URL);
		if (StringUtils.isEmpty(navigateTo)) {
			navigateTo = "redirect:/reference/management";
		}
		String hide = request.getParameter(KEY_HIDE);
		String closeNotification = request.getParameter(Constants.CLOSE_NOTIFICATION);
		String redirectToDashboard = request.getParameter(Constants.REDIRECT_TO_DASHBOARD);
		String notificationProcessIdStr = request.getParameter(Constants.KEY_ID);
		Long notificationProcessId = null;
		if (!StringUtils.isEmpty(notificationProcessIdStr)) {
			notificationProcessId = Long.valueOf(notificationProcessIdStr);
		} else {
			notificationProcessId = referenceDTO.getNotificationProcessId();
		}

		boolean notify = false;
		if (!StringUtils.isEmpty(closeNotification) && Boolean.TRUE.equals(Boolean.valueOf(closeNotification))) {
			notify = true;
		}

		if (referenceDTO.isManualAdd()) {
			try {
				createReferenceBase(referenceDTO, request, notificationProcessId, notify);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			createReferenceStaging(referenceDTO, notificationProcessId, notify);
		}

		if (redirectToDashboard != null && Boolean.TRUE.equals(Boolean.valueOf(redirectToDashboard))) {
			navigateTo = "redirect:/reference/dashboard";
		}

		String corrid = String.valueOf(referenceDTO.getCorrespondenceNumber());
		ra.addFlashAttribute(KEY_CORRESPONDENCE, corrid);
		ra.addFlashAttribute(KEY_HIDE, hide);
		return navigateTo;

	}

	/**
	 * @param referenceDTO
	 * @param notificationProcessId
	 * @param notify
	 */
	private void createReferenceStaging(ReferenceBaseDTO referenceDTO, Long notificationProcessId, boolean notify) {
		if (notify) {
			referenceDashboardService.createStagingReferenceAndCloseNotification(referenceDTO, notificationProcessId);
		} else {
			referenceService.addReferenceStagingData(referenceDTO);
		}
	}

	/**
	 * @param referenceDTO
	 * @param request
	 * @param notificationProcessId
	 * @param notify
	 * @throws IOException
	 */
	private void createReferenceBase(ReferenceBaseDTO referenceDTO, HttpServletRequest request,
			Long notificationProcessId, boolean notify) throws IOException {
		ReferenceBaseDTO referenceBaseDTO;
		if (notify) {
			referenceDashboardService.createReferenceAndCloseNotification(referenceDTO, notificationProcessId);
		} else {
			referenceBaseDTO = referenceService.createReference(referenceDTO);
			if (request.getContentType() != null && !referenceBaseDTO.getFile().isEmpty()) {
				referenceService.uploadPDFAttachment(referenceBaseDTO);
			}
		}
	}

	/**
	 * Show add reference pending entry page from pending entry list.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/management/addRefPending", method = { RequestMethod.POST, RequestMethod.GET })
	public String showAddReferencePendingEntry(HttpServletRequest request, Model model) {
		String hide = null;
		String corrId = request.getParameter(KEY_CORRESPONDENCE);
		Long id = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;
		hide = request.getParameter(KEY_HIDE);

		/** to get the attributes from redirected url **/
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {
			corrId = (String) model.asMap().get(KEY_CORRESPONDENCE);
			id = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;
			hide = (String) model.asMap().get(KEY_HIDE);
		}
		CorrespondenceDTO correspondenceDTO = referenceManagementService.getCorrespondenceById(id);
		model.addAttribute("correspondenceDTO", correspondenceDTO);
		model.addAttribute("isReferenceExist", referenceManagementService.isReferenceExist(id));

		/** To set the hide attribute for hiding the AddReference button **/
		if (hide == null) {
			model.addAttribute(KEY_HIDE, "true");
		}
		getModelWithReferenceDTO(id, model);
		model.addAttribute(KEY_REDIRECT_URL, "redirect:../management/addRefPending");
		return "add-reference-pending-entry";
	}

	@RequestMapping(value = "/management/addReferenceManualAdd", method = { RequestMethod.POST, RequestMethod.GET })
	public String addReferenceAjaxCall(HttpServletRequest request, Model model) {
		String corrId = request.getParameter(KEY_CORRESPONDENCE);
		Long id = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;

		/** to get the attributes from redirected url **/
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {
			corrId = (String) model.asMap().get(KEY_CORRESPONDENCE);
			id = StringUtils.isNotEmpty(corrId) ? Long.parseLong(corrId) : null;
		}
		CorrespondenceDTO correspondenceDTO = referenceManagementService.getCorrespondenceById(id);
		model.addAttribute("correspondenceDTO", correspondenceDTO);
		model.addAttribute("isReferenceExist", referenceManagementService.isReferenceExist(id));
		model.addAttribute("correspondenceId", corrId);

		model = getModelWithReferenceDTO(id, model);
		model.addAttribute(KEY_REDIRECT_URL, "redirect:../management/addRefPending");
		return "add-reference-pending-entry-list";
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

	/**
	 * Gets the model with reference dto attribute added.
	 *
	 * @param corrId
	 *            the corr id
	 * @param model
	 *            the model
	 * @return the model with reference dto
	 */
	private Model getModelWithReferenceDTO(Long corrId, Model model) {
		model.addAttribute(Constants.LIST_JURISDICTIONS,
				populateJurisdictionList(masterDataService.getAllJurisdictions()));
		ReferenceBaseDTO referenceDTO = referenceManagementService.getReferenceWithApplicationDetails(corrId);
		model.addAttribute(KEY_REFERENCE_DTO, referenceDTO);
		return model;
	}

	/**
	 * Review reference.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/management/reviewReference", method = RequestMethod.POST)
	@ResponseBody
	public String editReviewReference(HttpServletRequest request, Model model, RedirectAttributes ra) throws ServletRequestBindingException {
		long[] refId = ServletRequestUtils.getLongParameters(request, "markReviewed[]");
		if (refId != null && refId.length > 0) {
			referenceManagementService.updateReviewedReferences(refId);
		}
		
		long corrId = ServletRequestUtils.getLongParameter(request, KEY_CORRESPONDENCE_ID);
		model.addAttribute(KEY_CORRESPONDENCE_ID, corrId);
		ra.addFlashAttribute(KEY_CORRESPONDENCE_ID, corrId);
		return "redirect:../ids/editReference";
	}

	/**
	 * Review reference.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/management/updateComment", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateReferenceComment(HttpServletRequest request) throws ServletRequestBindingException {
		Long refId = ServletRequestUtils.getLongParameter(request, "id");
		String comments = ServletRequestUtils.getStringParameter(request, "comments");
		if (refId != null) {
			referenceManagementService.updateComments(refId, comments);
		}
		return true;
	}

	/**
	 * update translation flag reference.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/management/updateTranslationFlag", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateTranslationFlag(HttpServletRequest request) throws ServletRequestBindingException {
		Long refId = ServletRequestUtils.getLongParameter(request, "id");
		boolean translationFlag = ServletRequestUtils.getBooleanParameter(request, "englishTranslationFlag");
		if (refId != null) {
			referenceManagementService.updateTranslationFlag(refId, translationFlag);
		}
		return true;
	}

	/**
	 * update translation flag reference.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/management/deleteAttachment", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteReferenceAttachment(HttpServletRequest request) throws ServletRequestBindingException {
		Long referenceId = ServletRequestUtils.getLongParameter(request, "id");
		ReferenceType referenceType = ReferenceType
				.valueOf(ServletRequestUtils.getStringParameter(request, "referenceType"));

		if (referenceId != null) {
			referenceManagementService.deleteAttachment(referenceId, referenceType);
		}
		return true;
	}

	/**
	 * Upload attachment
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws ServletRequestBindingException
	 *             the servlet request binding exception
	 */
	@RequestMapping(value = "/management/uploadAttachment", method = RequestMethod.POST)
	@ResponseBody
	public boolean uploadReferenceAttachment(HttpServletRequest request) throws ServletRequestBindingException {
		Long referenceId = Long.getLong(request.getParameter("id"));
		ReferenceType referenceType = ReferenceType.valueOf(request.getParameter("referenceType"));
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("attachment");

		ReferenceBaseDTO dto = new ReferenceBaseDTO();
		dto.setReferenceBaseDataId(referenceId);
		dto.setReferenceType(referenceType);
		dto.setFile(file);

		if (request.getContentType() != null) {
			referenceService.uploadPDFAttachment(dto);
		}

		return true;
	}
}