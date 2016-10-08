package com.blackbox.ids.ui.controller.reference;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.dto.reference.ReferencePredicate;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.referenceflow.ReferenceFlowRuleService;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.controller.BaseController;
import com.mysema.query.types.Predicate;

/**
 * The Class ReferenceFlowRuleController.
 */
@Controller
@RequestMapping("/reference")
public class ReferenceFlowRuleController extends BaseController {

	private static final String KEY_FAMILY_ID = "familyid";

	private static final Logger LOGGER = Logger.getLogger(ReferenceFlowRuleController.class);

	private static final String KEY_APPLICATION_ID = "applicationId";

	private final static Integer DEFAULT_OFFSET = 0;
	private final static Integer DEFAULT_PAGINATION_LIMIT = 20;
	
	/** The Constant KEY_ATTORNEY. */
	private static final String KEY_ATTORNEY = "attdocketNo";

	/** The Constant KEY_JURIS_CODE. */
	private static final String KEY_JURIS_CODE = "jurisdictionCode";

	/** The Constant KEY_APPLICATION_NUMBER. */
	private static final String KEY_APPLICATION_NUMBER = "applicationNumber";
	
	private static final String KEY_MDM_DTO_LIST = "mdmDtoList";
	
	private static final String KEY_NEW_RULE_SUBJECT_MATTER_SEARCH_LIST = "new_rule_subject_matter_search_list";
	
	/** The Constant KEY_FAMILY_VALUE. */
	private static final String KEY_FAMILY_VALUE = "familyValue";


	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private ReferenceFlowRuleService refFlowRule;

	@Autowired
	private ApplicationService applicationService;

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

	@RequestMapping("/flowRule")
	public String viewDashboard(HttpServletRequest request, Model model, Principal principal) throws Exception {

		model.addAttribute("fromReferenceFlowRule", true);

		String notificationMessage = StringUtils.EMPTY;

		model.addAttribute("refFlowSubMenu", "allRecords");
		notificationMessage = "Hi this is the test message";

		model.addAttribute("referenceAllrecordsCount", 1);
		String requestComeFrom = request.getParameter("requestComeFrom");

		if (StringUtils.isNotBlank(requestComeFrom)) {
			model.addAttribute("refFlowSubMenu", requestComeFrom);
		}

		model.addAttribute("notificationMessage", notificationMessage);
		return "reference-flow-rule-navigation";
	}

	/**
	 * This method will fetch all the records of the reference flow page under
	 * the all records tab
	 * 
	 * @param request
	 * @param model
	 * @param principal
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/flowRule/allRecords", method = RequestMethod.POST)
	public String showReferenceFlowAllRecords(HttpServletRequest request, Model model, Principal principal)
			throws Exception {

		Predicate predicate = getPredicate(request);
		Pageable pageInfo = getFlowRulePageInfo(request);
		Page<ReferenceFlowDTO> refList = refFlowRule.getAllRecords(predicate, pageInfo);
		model.addAttribute("familyGroup", refList);
		model.addAttribute("refFlowSubMenu", "allRecords");

		return "flowrule-allrecords-data";
	}

	/**
	 * Similar to above method but Creates a pageable object specific to
	 * SpringDataJPA
	 *
	 * @param request
	 *            Contains request information for HTTP servlets.
	 * @return {@link Pageable}
	 */
	private Pageable getFlowRulePageInfo(HttpServletRequest request) {

		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);

		Integer limit = StringUtils.isBlank(dispStart) ? DEFAULT_PAGINATION_LIMIT : Integer.parseInt(dispStart);
		Integer offset = StringUtils.isBlank(dispLength) ? DEFAULT_OFFSET : Integer.parseInt(dispLength);
		final String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);

		if (offset != DEFAULT_OFFSET) {
			offset = offset / limit;
		}

		Sort sort = null;

		switch (sortBy == null ? "0" : sortBy) {

		case "0":
			sort = isAsc ? new Sort(new Order(Direction.ASC, "familyId"))
					: new Sort(new Order(Direction.DESC, "familyId"));
			break;
		default:
			sort = isAsc ? new Sort(new Order(Direction.ASC, "familyId"))
					: new Sort(new Order(Direction.DESC, "familyId"));
		}

		PageRequest page = new PageRequest(offset, limit, sort);

		return page;
	}

	protected Predicate getPredicate(final HttpServletRequest request) {

		String applicationNo = request.getParameter(DatatableAttribute.APPLICATION_NUMBER.attrName);
		String juris = request.getParameter(DatatableAttribute.JURISDICTION.attrName);
		String attorneyDoc = request.getParameter(DatatableAttribute.ATTORNEY_DOCKET_NUMBER.attrName);
		String familyId = request.getParameter(DatatableAttribute.FAMILY_ID.attrName);
		String uploadedOn = request.getParameter(DatatableAttribute.UPLOADED_ON.attrName);
		String docDesc = request.getParameter(DatatableAttribute.DOCUMENT_DESCRIPTION.attrName);
		String uploadedBy = request.getParameter(DatatableAttribute.UPLOADED_BY.attrName);

		Predicate predicate = ReferencePredicate.getReferenceFlowRulePredicate(applicationNo, juris, attorneyDoc,
				familyId, uploadedOn, docDesc, uploadedBy);

		return predicate;
	}

	private String populateJurisdictionList(List<com.blackbox.ids.dto.JurisdictionDTO> jurisdictionList) {
		List<String> jurisdictions = new ArrayList<>();
		for (com.blackbox.ids.dto.JurisdictionDTO jurisdictionDTO : jurisdictionList) {
			jurisdictions.add(jurisdictionDTO.getName());
		}
		return StringUtils.join(jurisdictions, ";");
	}

	/**
	 * This method is for redirecting the page to the review page on click of
	 * review button from the all records page of reference flow
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/flowRule/reviewFamily", method = { RequestMethod.POST, RequestMethod.GET })
	public String reviewReference(HttpServletRequest request, Model model) throws IOException {

		String familyId = null;
		familyId = request.getParameter(KEY_FAMILY_ID);
		List<FamilyDetailDTO> referenceSource = applicationService
				.fetchFamilyDetail(new FamilySearchFilter(familyId, true));
		model.addAttribute("referenceSource", referenceSource);
		model.addAttribute("familyLinkTab", "allRecords");
		model.addAttribute("familyId", familyId);
		return "reference-review-family-rules-edit";
	}

	/**
	 * This method will bring the family exception data. This will be call 
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/flowRule/reviewFamily/fetchFamilyException", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String fetchFamilyExceptionData(HttpServletRequest request, Model model) throws IOException {

		String familyId = null;
		familyId = request.getParameter(KEY_FAMILY_ID);

		List<ReferenceFlowRuleDTO> exclusionList = refFlowRule.getReferenceFlowRuleExclusionList(familyId);
		model.addAttribute("referenceDestination", exclusionList);
		model.addAttribute("familyId", familyId);
		return "family-exception-data";
	}

	@RequestMapping(value = "/flowRule/reviewFamily/dest", method = { RequestMethod.POST, RequestMethod.GET })
	public String fetchDestinationData(HttpServletRequest request, Model model)
			throws IOException, ServletRequestBindingException {

		String familyId = request.getParameter(KEY_FAMILY_ID);
		Long applicationId = ServletRequestUtils.getLongParameter(request, KEY_APPLICATION_ID);

		Predicate predicate = getPredicate(request);
		Pageable pageInfo = getFlowRulePageInfo(request);

		Page<ReferenceFlowRuleDTO> referenceDestination = refFlowRule.getFamilyLinks(predicate, pageInfo, familyId,
				applicationId);

		model.addAttribute("referenceDestination", referenceDestination);
		model.addAttribute("familyLinkTab", "allRecords");
		model.addAttribute("destListSize", referenceDestination.getContent().size());

		model.addAttribute("familyId", familyId);
		return "family-link-dest-data";
	}

	@RequestMapping(value = "/flowRule/reviewFamily/familyStatus", method = { RequestMethod.POST })
	public String submitStatusCommentsFamily(HttpServletRequest request, Model model)
			throws IOException, ServletRequestBindingException {
		String sourceFamilyId = request.getParameter("sourceFamilyId");
		String targetFamilyId = request.getParameter("sourceFamilyId");
		Long sourceAppId = ServletRequestUtils.getLongParameter(request, "sourceApp");
		Long targetAppId = ServletRequestUtils.getLongParameter(request, "targetApp");
		String comments = request.getParameter("txtcomments");
		refFlowRule.updateReferenceFlowRuleExclusionStatus(sourceAppId, targetAppId, sourceFamilyId, targetFamilyId,
				comments);
		LOGGER.debug("Changing status for : " + sourceAppId + " " + targetAppId + " " + comments);

		return "family-link-dest-data";
	}
	
	@RequestMapping(value = "/flowRule/reviewFamily/smlStatus", method = { RequestMethod.POST })
	public String submitStatusCommentsSml(HttpServletRequest request, Model model)
			throws IOException, ServletRequestBindingException {
		String sourceFamilyId = request.getParameter("sourceFamilyId");
		String targetFamilyId = request.getParameter("targetFamilyId");
		Long sourceAppId = ServletRequestUtils.getLongParameter(request, "sourceApp");
		Long targetAppId = ServletRequestUtils.getLongParameter(request, "targetApp");
		String comments = request.getParameter("txtcomments");
		refFlowRule.updateReferenceFlowRuleExclusionStatus(sourceAppId, targetAppId, sourceFamilyId, targetFamilyId,
				comments);
		LOGGER.debug("Changing status for : " + sourceAppId + " " + targetAppId + " " + comments);

		return "family-link-dest-data";
	}

	/**
	 * This method will fetch the list of target family id in SML and will
	 * return the "sml header" page.
	 * 
	 * @param request
	 * @param model
	 * @return String value for the sml header page.
	 */
	@RequestMapping(value = "/flowRule/reviewFamily/targetFamily", method = RequestMethod.POST)
	public String getTargetFamilyIdListComments(HttpServletRequest request, Model model) {
		String sourceFamilyId = request.getParameter("sourceFamilyId");
		List<String> idList = refFlowRule.getTargetFamilyIdList(sourceFamilyId);
		model.addAttribute("targetFamilyIdList", idList);
		LOGGER.debug("Changing status for : " + sourceFamilyId);
		return "sml-header";
	}

	/**
	 * This method will delete the link between the source and target family.
	 * This method will be called on the click of the "Delete Link" from the
	 * "sml header" page.
	 * 
	 * @param request
	 * @param model
	 */
	@RequestMapping(value = "/flowRule/reviewFamily/deleteLink", method = RequestMethod.POST)
	public void deleteSMLLink(HttpServletRequest request, Model model) {
		String sourceFamilyId = request.getParameter("sourceFamilyId");
		String targetFamilyId = request.getParameter("targetFamilyId");
		refFlowRule.deleteSML(sourceFamilyId, targetFamilyId);
		LOGGER.debug("Delete link for : " + sourceFamilyId + " with " + targetFamilyId + " is successful.");
	}

	/**
	 * View r1.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 */
	@RequestMapping("/viewReferenceFlowRule")
	public String viewR1(HttpServletRequest request) {
		BlackboxSecurityContextHolder.getUserDetails().getAuthorities();
		SecurityContextHolder.getContext().getAuthentication();
		request.isUserInRole("lkl");
		return null;
	}

	/**
	 * From ref flow rule.
	 *
	 * @return true, if successful
	 */
	@ModelAttribute("fromRefFlowRule")
	public boolean fromRefFlowRule() {
		return true;
	}

	/**
	 * This method is for SML page.
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/flowRule/reviewFamily/sml", method = { RequestMethod.POST, RequestMethod.GET })
	public String reviewReferenceSml(HttpServletRequest request, Model model) throws IOException {

		String familyId = null;
		familyId = request.getParameter(KEY_FAMILY_ID);

		List<FamilyDetailDTO> smlSource = applicationService.fetchFamilyDetail(new FamilySearchFilter(familyId, true));

		model.addAttribute("smlSource", smlSource);
		model.addAttribute("familyId", familyId);
		return "sml-source-data";
	}

	@RequestMapping(value = "/flowRule/reviewFamily/dest/sml", method = { RequestMethod.POST, RequestMethod.GET })
	public String fetchSmlDestinationData(HttpServletRequest request, Model model)
			throws IOException, ServletRequestBindingException {

		String sourceFamilyId = request.getParameter("sourceFamilyId");
		Long applicationId = ServletRequestUtils.getLongParameter(request, KEY_APPLICATION_ID);

		Predicate predicate = getPredicate(request);
		Pageable pageInfo = getFlowRulePageInfo(request);

		Page<ReferenceFlowRuleDTO> referenceDestination = refFlowRule.getSubjectMatterLinks(predicate, pageInfo,
				sourceFamilyId, applicationId);

		model.addAttribute("referenceDestination", referenceDestination);
		model.addAttribute("familyLinkTab", "allRecords");
		model.addAttribute("smlDestListSize", referenceDestination.getContent().size());

		model.addAttribute("familyId", sourceFamilyId);
		return "sml-destination-data";
	}
	
	/**
	 * This method controls the New Rule page of the reference flow page.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/flowRule/newRuleSearch")
	public String newRuleSubjectMatterSearch(final Model model) {
		model.addAttribute(Constants.LIST_JURISDICTIONS,
				populateJurisdictionList(masterDataService.getAllJurisdictions()));
		return "reference-new-rule-sub-matter-main";
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
	@RequestMapping(value = "/flowrule/appDetailsByApplication", method = RequestMethod.POST)
	public String appDetailsByApplication(HttpServletRequest request, Model model) {

		String jurisCode = request.getParameter(KEY_JURIS_CODE);
		String appNo = request.getParameter(KEY_APPLICATION_NUMBER);
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByApplicationNoAndJurisdiction(jurisCode,
				appNo);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return KEY_NEW_RULE_SUBJECT_MATTER_SEARCH_LIST;
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
	@RequestMapping(value = "/flowrule/appDetailsByFamily", method = RequestMethod.POST)
	public String appDetails(HttpServletRequest request, Model model) {

		String familyId = request.getParameter(KEY_FAMILY_VALUE);
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByFamily(familyId);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return KEY_NEW_RULE_SUBJECT_MATTER_SEARCH_LIST;
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
	@RequestMapping(value = "/flowrule/appDetailsByAttDocket", method = RequestMethod.POST)
	public String appDetailsByAttDocket(HttpServletRequest request, Model model) {

		String attorneyDocket = request.getParameter(KEY_ATTORNEY);
		List<MdmRecordDTO> mdmDtoList = applicationService.fetchApplicationByAttorneyDocket(attorneyDocket);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return KEY_NEW_RULE_SUBJECT_MATTER_SEARCH_LIST;
	}
	
	@RequestMapping(value = "/flowRule/reviewFamily/selectedFamilyDetails", method = RequestMethod.POST)
	public String familyLinkingData(HttpServletRequest request, Model model) {
		String familyId = null;
		familyId = request.getParameter(KEY_FAMILY_ID);
		List<FamilyDetailDTO> selectedFamilyDetails = applicationService.fetchFamilyDetail(new FamilySearchFilter(familyId, true));
		model.addAttribute("selectedFamilyDetails", selectedFamilyDetails);
		model.addAttribute("familyId", familyId);
		return "new-rule-selected-family-data";
	}
	
	@RequestMapping(value = "/flowRule/reviewFamily/newrule/link", method = RequestMethod.POST)
	public String generateLinkBetweenFamilies(HttpServletRequest request, Model model) throws ServletRequestBindingException {
		String sourceFamilyId = request.getParameter("sourceFamilyId");
		String targetFamilyId = request.getParameter("targetFamilyId");
		Long sourceAppId = ServletRequestUtils.getLongParameter(request, "sourceAppId");
		Long targetAppId = ServletRequestUtils.getLongParameter(request, "targetAppId");
		boolean isSourceLinkage = Boolean.parseBoolean(request.getParameter("sourceLinkage")); 
		boolean isTargetLinkage = Boolean.parseBoolean(request.getParameter("targetLinkage")); 
		
		refFlowRule.createReferenceFlowSmlExclusionNewRule(sourceFamilyId, targetFamilyId, sourceAppId, targetAppId, isSourceLinkage, isTargetLinkage);
		LOGGER.debug("New Rule Linkage for : " + sourceFamilyId + " with " + targetFamilyId + " is successful.");
		
		return "reference-new-rule-sub-matter-main";
	}

}