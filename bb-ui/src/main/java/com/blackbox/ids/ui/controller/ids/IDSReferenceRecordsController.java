package com.blackbox.ids.ui.controller.ids;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordStatusCountDTO;
import com.blackbox.ids.core.dto.datatable.DatatableAttribute;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.dto.reference.ReferencePredicate;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.referenceflow.ReferenceFlowRuleService;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.controller.BaseController;
import com.mysema.query.types.Predicate;

@Controller
@RequestMapping(value = "/idsReference")
public class IDSReferenceRecordsController extends BaseController{
	
	/** The application service. */
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ReferenceFlowRuleService refFlowRule;
	
	private static final String KEY_MDM_DTO_LIST = "mdmDtoList";
	
	private final static Integer DEFAULT_OFFSET = 0;
	private final static Integer DEFAULT_PAGINATION_LIMIT = 20;
	
	@RequestMapping(value = "/referenceResult", method = RequestMethod.POST)
	public String viewReferenceSearchResult(HttpServletRequest request, Model model) {
		
		String familyId = request.getParameter("familyid");
		String jurisCode = request.getParameter("jurisdictionCode");
		String appNo = request.getParameter("applicationNumber");
		String attorneyDocket = request.getParameter("attdocketNo");
		
		List<MdmRecordDTO> mdmDtoList = null;
		
		if (familyId != null) {
			mdmDtoList = applicationService.fetchApplicationByFamily(familyId);
			mdmDtoList.get(0).setFilingDate(new Date());
			model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		} else if (attorneyDocket != null) {
			mdmDtoList = applicationService.fetchApplicationByAttorneyDocket(attorneyDocket);
			model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		} else {
			mdmDtoList = applicationService.fetchApplicationByApplicationNoAndJurisdiction(jurisCode,appNo);
			model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		}

		return "ids-reference-results";
	}
	
	@RequestMapping(value = "/viewReferenceRecords", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewReferenceRecords(HttpServletRequest request, Model model) {
		String familyId = request.getParameter("familyid");
		String jurisCode = request.getParameter("jurisdiction");
		String appNo = request.getParameter("applicationNumber");
		String attorneyDocket = request.getParameter("attorneyDocket");
		
		Predicate predicate = getPredicate(request);
				
		model.addAttribute("familyId", familyId);
		model.addAttribute("jurisCode", jurisCode);
		model.addAttribute("appNo", appNo);
		model.addAttribute("attorneyDocket", attorneyDocket);
		
		IDSReferenceRecordStatusCountDTO statusCount = refFlowRule.getStatusTabCount(predicate);
		model.addAttribute("statusCount",statusCount);
		
		return "main-record-page";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/citedHeader",method = RequestMethod.POST)
	public String viewReferenceRecordsCitedHeader(HttpServletRequest request, Model model) {
		
		Map<String, Long> filingDate = refFlowRule.getDistinctDateAndCount();
		IDSReferenceRecordDTO headerCount = refFlowRule.getPatentAndNplCounts();
		List<ReferenceFlowSubStatus> changeStatus = refFlowRule.getChangeSubStatus();
		model.addAttribute("filingDate",filingDate);
		model.addAttribute("headerCount",headerCount);
		model.addAttribute("changeStatus",changeStatus);
		return "cited-header-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/citedPatentData", method = RequestMethod.POST)
	public String viewReferenceRecordsCitedPatentData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> patentListCited = refFlowRule.getAllIDSPatentReferenceRecords(predicate, pageable);
		Map<String, Long> filingDate = refFlowRule.getDistinctDateAndCount();
		IDSReferenceRecordDTO headerCount = refFlowRule.getPatentAndNplCounts();
		//List<ReferenceFlowSubStatus> changeStatus = refFlowRule.getChangeSubStatus();
		model.addAttribute("patentListCited",patentListCited);
		model.addAttribute("filingDate",filingDate);
		model.addAttribute("headerCount",headerCount);
		//model.addAttribute("changeStatus",changeStatus);
		return "cited-patent-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/citedNplData",method = RequestMethod.POST)
	public String viewReferenceRecordsCitedNPLData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> nplListCited = refFlowRule.getAllIDSNPLReferenceRecords(predicate, pageable);
		model.addAttribute("nplListCited",nplListCited);
		return "cited-npl-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/uncitedPatentData", method = RequestMethod.POST)
	public String viewReferenceRecordsUnCitedPatentData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		//Page<IDSReferenceRecordDTO> tabList = refFlowRule.getAllIDSReferenceRecords(predicate, pageable);
		Page<IDSReferenceRecordDTO> patentListUnCited = refFlowRule.getAllIDSPatentReferenceRecords(predicate, pageable);
		model.addAttribute("patentListUnCited",patentListUnCited);
		return "uncited-patent-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/uncitedNplData",method = RequestMethod.POST)
	public String viewReferenceRecordsUnCitedNPLData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> nplListUnCited = refFlowRule.getAllIDSNPLReferenceRecords(predicate, pageable);
		model.addAttribute("nplListUnCited",nplListUnCited);
		return "uncited-npl-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/examinerCitedPatentData", method = RequestMethod.POST)
	public String viewReferenceRecordsExaminerCitedPatentData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		//Page<IDSReferenceRecordDTO> tabList = refFlowRule.getAllIDSReferenceRecords(predicate, pageable);
		Page<IDSReferenceRecordDTO> patentListExaminerCited = refFlowRule.getAllIDSPatentReferenceRecords(predicate, pageable);
		model.addAttribute("patentListExaminerCited",patentListExaminerCited);
		return "examiner-cited-patent-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/examinerCitedNplData",method = RequestMethod.POST)
	public String viewReferenceRecordsExaminerCitedNPLData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> nplListExaminerCited = refFlowRule.getAllIDSNPLReferenceRecords(predicate, pageable);
		model.addAttribute("nplListExaminerCited",nplListExaminerCited);
		return "examiner-cited-npl-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/citedInParentPatentData", method = RequestMethod.POST)
	public String viewReferenceRecordsCitedInParentPatentData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		//Page<IDSReferenceRecordDTO> tabList = refFlowRule.getAllIDSReferenceRecords(predicate, pageable);
		Page<IDSReferenceRecordDTO> patentListCitedInParent = refFlowRule.getAllIDSPatentReferenceRecords(predicate, pageable);
		model.addAttribute("patentListCitedInParent",patentListCitedInParent);
		return "cited-in-parent-patent-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/citedInParentNplData",method = RequestMethod.POST)
	public String viewReferenceRecordsCitedInParentNPLData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> nplListCitedInParent = refFlowRule.getAllIDSNPLReferenceRecords(predicate, pageable);
		model.addAttribute("nplListCitedInParent",nplListCitedInParent);
		return "cited-in-parent-npl-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/doNotFilePatentData", method = RequestMethod.POST)
	public String viewReferenceRecordsDoNotFilePatentData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		//Page<IDSReferenceRecordDTO> tabList = refFlowRule.getAllIDSReferenceRecords(predicate, pageable);
		Page<IDSReferenceRecordDTO> patentListDoNotFile = refFlowRule.getAllIDSPatentReferenceRecords(predicate, pageable);
		model.addAttribute("patentListDoNotFile",patentListDoNotFile);
		return "donotfile-patent-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/doNotFileNplData",method = RequestMethod.POST)
	public String viewReferenceRecordsDoNotFileNPLData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> nplListDoNotFile = refFlowRule.getAllIDSNPLReferenceRecords(predicate, pageable);
		model.addAttribute("nplListDoNotFile",nplListDoNotFile);
		return "donotfile-npl-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/deletedPatentData", method = RequestMethod.POST)
	public String viewReferenceRecordsDeletedPatentData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		//Page<IDSReferenceRecordDTO> tabList = refFlowRule.getAllIDSReferenceRecords(predicate, pageable);
		Page<IDSReferenceRecordDTO> patentListDeleted = refFlowRule.getAllIDSPatentReferenceRecords(predicate, pageable);
		model.addAttribute("patentListDeleted",patentListDeleted);
		return "deleted-patent-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/deletedNplData",method = RequestMethod.POST)
	public String viewReferenceRecordsDeletedNPLData(HttpServletRequest request, Model model) {
		
		Predicate predicate = getPredicate(request);
		Pageable pageable = getPageInfo(request);
		Page<IDSReferenceRecordDTO> nplListDeleted = refFlowRule.getAllIDSNPLReferenceRecords(predicate, pageable);
		model.addAttribute("nplListDeleted",nplListDeleted);
		return "deleted-npl-data";
	}
	
	@RequestMapping(value = "/viewReferenceRecords/viewFamilyMembers", method = RequestMethod.POST)
	public String familyMembersView(HttpServletRequest request, Model model) throws IOException {

		String familyId = null;
		familyId = request.getParameter("familyId");
		
		List<MdmRecordDTO> mdmDtoList = null;

		mdmDtoList = applicationService.fetchApplicationByFamily(familyId);
		model.addAttribute(KEY_MDM_DTO_LIST, mdmDtoList);
		return "family-member-data";
	}
	
	
	protected Predicate getPredicate(final HttpServletRequest request) {

		String familyId = request.getParameter("familyid");
		String appNo = request.getParameter("applicationNumber");
		String filingDate = request.getParameter("filingDate");
		String juris = request.getParameter(DatatableAttribute.JURISDICTION.attrName);
		String refType = request.getParameter("referenceType");
		String activeTab = request.getParameter("activeTab");		
		String refFlowSubStatus = request.getParameter("subStatus");
		String status = request.getParameter("status");

		Predicate predicate = ReferencePredicate.getIDSReferenceRecordPredicate(familyId, appNo,filingDate,juris,refType,activeTab,status, refFlowSubStatus);

		return predicate;
	}
	
	protected Pageable getPageInfo(final HttpServletRequest request) {

		String dispStart = request.getParameter(DatatableAttribute.LIMIT.attrName);
		String dispLength = request.getParameter(DatatableAttribute.OFFSET.attrName);
		String sortDir = request.getParameter(DatatableAttribute.SORT_ORDER.attrName);

		Integer limit = StringUtil.isBlank(dispStart) ? DEFAULT_PAGINATION_LIMIT : Integer.parseInt(dispStart);
		Integer offset = StringUtil.isBlank(dispLength) ? DEFAULT_OFFSET : Integer.parseInt(dispLength);
		final String sortBy = request.getParameter(DatatableAttribute.SORT_BY.attrName);
		final boolean isAsc = Constants.SPECIFIER_ASCENDING.equalsIgnoreCase(sortDir);

		if(offset != DEFAULT_OFFSET) {
			offset = offset / limit;
		}

		Sort sort = null;
		
		switch (sortBy==null ? "0" : sortBy) {

		case "0" :
		case "1":
//				sort = isAsc ? new Sort(new Order(Direction.ASC, "referenceBaseData.jurisdiction.code")) : new Sort(new Order(Direction.DESC, "referenceBaseData.jurisdiction.code"));
//				break;
		case "2":
//				sort = isAsc ? new Sort(new Order(Direction.ASC, "applicationNumber")) : new Sort(new Order(Direction.DESC, "applicationNumber"));
//				break;
		case "3":
		case "4":
			sort = isAsc ? new Sort(new Order(Direction.ASC, "filingDate")) : new Sort(new Order(Direction.DESC, "filingDate"));
				break;
		default:
				sort = isAsc ? new Sort(new Order(Direction.ASC, "filingDate")) : new Sort(new Order(Direction.DESC, "filingDate"));
		}
		
		PageRequest page = new PageRequest(offset, limit, sort);

		return page;
	}
	
	@RequestMapping(value = "/viewReferenceRecords/citedSourceDoc", method = RequestMethod.POST)
	public String viewCitedSourceDocument(HttpServletRequest request, Model model) throws IOException, ServletRequestBindingException {

		Long refFlowId = null;
		refFlowId = ServletRequestUtils.getLongParameter(request, "refFlowId");
		
		IDSReferenceRecordDTO sourceDocument = refFlowRule.getSourceDocumentData(refFlowId);
		model.addAttribute("sourceDocument", sourceDocument);
		return "cited-source-document";
	}
	
}
