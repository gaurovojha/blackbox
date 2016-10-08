package com.blackbox.ids.ui.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.blackbox.ids.dto.AssigneeDTO;
import com.blackbox.ids.dto.CustomerDTO;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.dto.OrganizationDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.TechnologyGroupDTO;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleDTO;
import com.blackbox.ids.dto.notification.NotificationDTO;
import com.blackbox.ids.dto.notification.NotificationEscalationRoleDTO;
import com.blackbox.ids.services.notification.NotificationService;
import com.blackbox.ids.services.usermanagement.RoleService;
import com.blackbox.ids.ui.common.DataTableJsonObject;
import com.blackbox.ids.ui.common.SystemAttributesIds;
import com.blackbox.ids.ui.validation.NotificationSetUpValidator;

// TODO: Auto-generated Javadoc
/*
 * This class is the Controller of Notification Flow
 * @author Nagarro
 */

/**
 * The Class WorkflowNotificationController.
 */
@Controller
public class WorkflowNotificationController {

	/** The logger. */
	private Logger logger = Logger.getLogger(WorkflowNotificationController.class);

	/** The Constant NOTIFICATION_DASHBOARD. */
	private static final String NOTIFICATION_DASHBOARD = "notificationsDashboard";
	
	/** The Constant REDIRECT_DASHBOARD. */
	private static final String REDIRECT_DASHBOARD = "redirect:dashBoard";
	
	/** The Constant SYSTEM_INITIATED. */
	private static final String SYSTEM_INITIATED = "Action Item - System initiated";
	
	/** The Constant YES. */
	private static final String YES = "Yes";
	
	/** The Constant ON. */
	private static final String ON = "on";

	/** The notification service. */
	@Autowired
	private NotificationService notificationService;

	/** The role service. */
	@Autowired
	private RoleService roleService;

	/** The notification set up validator. */
	@Autowired
	private NotificationSetUpValidator notificationSetUpValidator;

	/**
	 * Inits the page.
	 *
	 * @param notificationDTO the notification dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return Return the Notification Default View.
	 */

	@RequestMapping(value = "/notification/dashBoard", method = RequestMethod.GET)
	public String initPage(@ModelAttribute("notificationDTO") NotificationDTO notificationDTO, BindingResult result,
			ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Request for Notification Dashboard ");

		return NOTIFICATION_DASHBOARD;

	}

	/**
	 * Spring pagination data tables.
	 *
	 * @param request the request
	 * @return Returns the Populated Data table of Notification Metadata
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/notification/workflowManagementAjaxCall", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody DataTableJsonObject springPaginationDataTables(HttpServletRequest request) throws IOException {

		logger.info("Request for Loading Data Table received.. ");

		List<NotificationDTO> notificationDTOList = notificationService.getAllNotification();

		DataTableJsonObject dataTableJsonObject = new DataTableJsonObject();
		dataTableJsonObject.setiTotalDisplayRecords(notificationDTOList.size());
		dataTableJsonObject.setiTotalRecords(notificationDTOList.size());
		dataTableJsonObject.setAaData(notificationDTOList);

		return dataTableJsonObject;
	}

	/**
	 * Sets the up notifications.
	 *
	 * @param notificationId the notification id
	 * @return It returns the metadata of a Notification identified by notificationId
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	@RequestMapping(value = "/notification/setUpNotification", method = RequestMethod.GET)
	public ModelAndView setUpNotifications(@RequestParam("notificationId") int notificationId) throws IOException {

		ModelAndView modelAndView = new ModelAndView("notificationSetup");
		NotificationDTO notificationDTO = notificationService.getNotificationById(notificationId);
		notificationDTO.setRoleDTOs(roleService.getAllRoles());
		modelAndView.addObject("setUpNotificationObject", notificationDTO);

		return modelAndView;
	}

	/**
	 * It updates the Notification Metadata updated by the Admin User.
	 *
	 * @param model the model
	 * @param notificationDTO the notification dto
	 * @param result the result
	 * @param ra the ra
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/notification/submitNotificationForm", method = RequestMethod.POST)
	public String updateNotification(Model model,
			@ModelAttribute("setUpNotificationObject") @Validated NotificationDTO notificationDTO, BindingResult result,
			RedirectAttributes ra) throws IOException {

		notificationSetUpValidator.validate(notificationDTO, result);

		if (result.hasErrors()) {
			model.addAttribute(notificationDTO);
			setNotificationValues(notificationDTO);
			setEscalationRoles(notificationDTO);
			notificationDTO.setRoleDTOs(roleService.getAllRoles());
			return "notificationSetup";
		}

		if (null == notificationDTO.getEscalation()) {
			notificationDTO.setEscalationRoles(null);
			notificationDTO.setEscalationSelectedRoles("");
			notificationDTO.setNoOfPastDueDays(0);
		} else {
			setEscalationRoles(notificationDTO);
		}

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		notificationDTO.setUpdatedOn(Calendar.getInstance());
		notificationDTO.setUpdatedByUsename(user);
		notificationService.saveOrUpdateNotification(notificationDTO);

		return REDIRECT_DASHBOARD;
	}

	/**
	 * Sets the Notification Metadata values.
	 *
	 * @param notificationDTO the new notification values
	 */

	private void setNotificationValues(NotificationDTO notificationDTO) {

		if ((notificationDTO.getDispalyWhilePreparingIDS() != null)
				&& notificationDTO.getDispalyWhilePreparingIDS().equalsIgnoreCase(ON)) {
			notificationDTO.setDispalyWhilePreparingIDS(YES);
		}
		if ((notificationDTO.getEmailNotification() != null)
				&& notificationDTO.getEmailNotification().equalsIgnoreCase(ON)) {
			notificationDTO.setEmailNotification(YES);
		}
		if ((notificationDTO.getReminder() != null) && notificationDTO.getReminder().equalsIgnoreCase(ON)) {
			notificationDTO.setReminder(YES);
		}
		if ((notificationDTO.getEscalation() != null) && notificationDTO.getEscalation().equalsIgnoreCase(ON)) {
			notificationDTO.setEscalation(YES);
		}
	}

	/**
	 * Sets the escalation roles on Notification.
	 *
	 * @param notificationDTO            the new escalation roles
	 */
	private void setEscalationRoles(NotificationDTO notificationDTO) {

		if (!StringUtils.isBlank(notificationDTO.getEscalationSelectedRoles())) {
			String[] EscalationRoleIdAndValue = notificationDTO.getEscalationSelectedRoles().split(",");

			for (String roleString : EscalationRoleIdAndValue) {
				String[] roleIdAndName = roleString.split("#");
				NotificationEscalationRoleDTO escalationRoleDto = new NotificationEscalationRoleDTO();
				escalationRoleDto.setRoleId(Long.valueOf(roleIdAndName[0]));
				escalationRoleDto.setName(roleIdAndName[1]);
				notificationDTO.getEscalationRoles().add(escalationRoleDto);
			}
		}
	}

	/**
	 * Updated the Business Rules associated with Notification.
	 *
	 * @param businessRuleDTO the business rule dto
	 * @return id of updatedRule
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	@RequestMapping(value = "/notification/editRoles", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public long editBussinessRule(@RequestBody NotificationBusinessRuleDTO businessRuleDTO) throws IOException {

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		businessRuleDTO.setCreatedOn(Calendar.getInstance());
		businessRuleDTO.setUpdatedByUsename(user);

		long updatedOrSavedRuleId = notificationService.saveOrUpdateNotificationBusinessRule(businessRuleDTO);
		return updatedOrSavedRuleId;
	}

	/**
	 * Updated the Business Rules associated with non System Notification.
	 *
	 * @param notificationDTO the notification dto
	 * @return id of Updated Notification
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/notification/editNotification", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public long editnotification(@RequestBody NotificationDTO notificationDTO) throws IOException {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();

		NotificationDTO notificationDto = notificationService.getNotificationById(notificationDTO.getNotificationId());
		notificationDto.setUpdatedOn(Calendar.getInstance());
		notificationDto.setUpdatedByUsename(user);
		notificationDto.setNotificationLevelNo(notificationDTO.getNotificationLevelNo());
		notificationDto.setBusinessRuleDtos(notificationDTO.getBusinessRuleDtos());
		long notificationId = notificationService.saveOrUpdateNotification(notificationDto);
		return notificationId;
	}

	/**
	 * Updated the Business Rules associated with System Notification.
	 *
	 * @param businessRuleDTO the business rule dto
	 * @return Id of updated System Rule
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/notification/editRuleForSystem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public long editBussinessRuleForSystem(@RequestBody NotificationBusinessRuleDTO businessRuleDTO)
			throws IOException {

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		businessRuleDTO.setCreatedOn(Calendar.getInstance());
		businessRuleDTO.setUpdatedByUsename(user);
		businessRuleDTO.setSystemInitiated(true);
		long updatedOrSavedRuleId = notificationService.saveOrUpdateNotificationBusinessRule(businessRuleDTO);

		return updatedOrSavedRuleId;
	}

	/**
	 * It deletes buisness Rule.
	 *
	 * @param request the request
	 * @param response the response
	 * @return Id of Deleted Notification
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/notification/deleteRule", method = RequestMethod.POST)
	@ResponseBody
	public Long deleteRule(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		String ruleId = request.getParameter("ruleId");
		String notificationId = request.getParameter("notificationId");
		Long notId = notificationService.deleteNotificationBusinessRule(Long.parseLong(notificationId),
				Long.parseLong(ruleId), Calendar.getInstance(), user);
		return notId;

	}

	/**
	 * Edits the business rule of Notification Id.
	 *
	 * @param notificationId the notification id
	 * @return the model and view
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	@RequestMapping(value = "/notification/editBussinessRules", method = RequestMethod.GET)
	public ModelAndView editBussinessRule(@RequestParam("notificationId") int notificationId) throws IOException {

		ModelAndView modelAndView = null;
		NotificationDTO notificationDTO = notificationService.getNotificationById(notificationId);
		List<RoleDTO> roleDTOs = roleService.getAllActiveRoles();
		notificationDTO.setRoleDTOs(roleDTOs);
		if (SYSTEM_INITIATED.equalsIgnoreCase(notificationDTO.getNotificationType())) {

			List<AssigneeDTO> assignees = roleService.getAllAssignee();
			List<CustomerDTO> customers = roleService.getAllCustomer();
			List<JurisdictionDTO> jurisdictions = roleService.getAllJurisdiction();
			List<OrganizationDTO> organizations = roleService.getAllOrganization();
			List<TechnologyGroupDTO> technologygroups = roleService.getAllTechnologyGroup();

			modelAndView = new ModelAndView("notificationSetUpForSystem");

			modelAndView.addObject("assignees", assignees);
			modelAndView.addObject("customers", customers);
			modelAndView.addObject("jurisdictions", jurisdictions);
			modelAndView.addObject("organizations", organizations);
			modelAndView.addObject("technologygroups", technologygroups);

		} else {
			modelAndView = new ModelAndView("notificationSetUpForUser");
		}

		modelAndView.addObject("editBussinessRules", notificationDTO);
		return modelAndView;
	}

	/**
	 * Gets the roles by attributes.
	 *
	 * @param systemAttributesIds the system attributes ids
	 * @return It returns the System Attributes
	 */

	@RequestMapping(value = "/notification/getRolesByAttributes", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public List<RoleDTO> getRolesByAttributes(@RequestBody SystemAttributesIds systemAttributesIds) {

		List<RoleDTO> roles = roleService.findRolesForSystem(systemAttributesIds.getJuricIds(),
				systemAttributesIds.getAssigneeIds(), systemAttributesIds.getTechGrpIds(),
				systemAttributesIds.getCustmNoIds(), systemAttributesIds.getOrganIds());

		return roles;
	}
	
	/**
	 * Save defaults role.
	 *
	 * @param notificationDTO the notification dto
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/notification/saveDefaultsRole", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public long saveDefaultsRole(@RequestBody NotificationDTO notificationDTO) throws IOException {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		NotificationDTO dto = notificationService.getNotificationById(notificationDTO.getNotificationId());
		dto.setUpdatedOn(Calendar.getInstance());
		dto.setUpdatedByUsename(user);
		dto.setDefaultRoles(notificationDTO.getDefaultRoles());
		long notificationId = notificationService.saveOrUpdateNotification(dto);
		
		return notificationId;
	}
}




  