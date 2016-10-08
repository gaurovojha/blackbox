package com.blackbox.ids.ui.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.dto.usermanagement.ModuleDTO;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.usermanagement.AccessProfileService;
import com.blackbox.ids.services.usermanagement.RoleService;

@Controller
@RequestMapping(value = "/role")
public class RoleController {

	private static final String KEY_ROLE_ACTIVE = "roleActive";

	private static final String KEY_CREATE_ROLE = "create-role";

	private static final String KEY_ROLE_FORM = "roleForm";

	private static final Logger LOGGER = Logger.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccessProfileService accessProfileService;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private Validator roleFormValidator;

	@RequestMapping(value = "/createDuplicate", method = { RequestMethod.GET })
	public String createFromDuplicate(@RequestParam("id") long id, Model model) {

		RoleDTO form = roleService.getRoleById(Long.valueOf(id));
		model.addAttribute(KEY_ROLE_FORM, form);
		return KEY_CREATE_ROLE;
	}

	@RequestMapping(value = "/create", method = { RequestMethod.GET })
	public String create(@ModelAttribute(KEY_ROLE_FORM) RoleDTO form, Model model) {

		if (form != null) {
			model.addAttribute(KEY_ROLE_FORM, form);
		} else {
			model.addAttribute(KEY_ROLE_FORM, new RoleDTO());
		}

		return KEY_CREATE_ROLE;
	}

	@RequestMapping(value = "/submit", method = { RequestMethod.POST })
	public String submit(@ModelAttribute(KEY_ROLE_FORM) RoleDTO roleForm, BindingResult result, RedirectAttributes ra) {

		// in case of duplicate remove role id
		roleForm.setId(null);

		// validate role form
		roleFormValidator.validate(roleForm, result);

		if (result.hasErrors()) {
			LOGGER.info("Returning create-role.jsp page");
			return KEY_CREATE_ROLE;
		}
		roleService.createRole(roleForm);
		ra.addFlashAttribute(KEY_ROLE_ACTIVE, true);
		LOGGER.info("New Role Created Successfully");
		return "redirect:../admin/";
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public String update(@ModelAttribute(KEY_ROLE_FORM) RoleDTO roleForm, BindingResult result, RedirectAttributes ra) {

		// validate role form
		roleFormValidator.validate(roleForm, result);

		if (result.hasErrors()) {
			LOGGER.info("Returning edit-role.jsp page");
			return "edit-role";
		}
		roleService.updateRole(roleForm);
		LOGGER.info("Role with id " + roleForm.getId() + "updated");
		ra.addFlashAttribute(KEY_ROLE_ACTIVE, true);
		return "redirect:../admin/";
	}

	@RequestMapping(value = "/edit", method = { RequestMethod.GET })
	public String edit(@RequestParam("id") long id, Model model, RedirectAttributes ra) {
		RoleDTO form = roleService.getRoleById(id);
		if (form.isSeeded()) {
			ra.addFlashAttribute(KEY_ROLE_ACTIVE, true);
			return "redirect:../admin/";
		}
		model.addAttribute(KEY_ROLE_FORM, form);
		model.addAttribute("accessProfile", form.getAccessProfile());

		return "edit-role";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.GET })
	public String delete(@RequestParam("id") long id, RedirectAttributes ra) {
		roleService.deactivate(id);
		LOGGER.info("Role with id " + id + " is deactivated");
		ra.addFlashAttribute(KEY_ROLE_ACTIVE, true);
		return "redirect:../admin/";
	}

	@RequestMapping(value = "/view", method = { RequestMethod.GET })
	public String view(@RequestParam("id") long id, Model model) {
		RoleDTO form = roleService.getRoleById(id);
		model.addAttribute(KEY_ROLE_FORM, form);
		return "view-role";
	}

	@ModelAttribute("accessProfileList")
	public List<AccessProfileDTO> populateAccessProfileList() {
		return accessProfileService.getAllAccessProfiles();
	}

	@ModelAttribute("activeAccessProfileList")
	public List<AccessProfileDTO> populateActiveAccessProfileList() {
		return accessProfileService.getActiveAccessProfiles();
	}

	@ModelAttribute("moduleList")
	public List<ModuleDTO> populateModuleList() {
		return masterDataService.getAllModules();
	}

	@ModelAttribute("jurisdictionMasterList")
	public List<JurisdictionDTO> populateJurisdictionList() {
		return masterDataService.getAllJurisdictions();
	}

	@ModelAttribute("assigneeMasterList")
	public List<AssigneeDTO> populateAssigneeList() {
		return masterDataService.getAllAssignees();
	}

	@ModelAttribute("customerMasterList")
	public List<CustomerDTO> populateCustomerList() {
		return masterDataService.getAllCustomers();
	}

	@ModelAttribute("technologyGroupMasterList")
	public List<TechnologyGroupDTO> populateTechnologyGroupList() {
		return masterDataService.getAllTechnologyGroups();
	}

	@ModelAttribute("organizationMasterList")
	public List<OrganizationDTO> populateOrganizationList() {
		return masterDataService.getAllOrganizations();
	}

	@RequestMapping(value = "/accessProfile", method = { RequestMethod.GET }, produces = "text/html")
	@ResponseBody
	public ModelAndView getAccessProfile(@RequestParam("id") Long id, ModelAndView mav) {

		AccessProfileDTO accessProfile = new AccessProfileDTO();
		// if option is selected as Select on UI
		if (id != 0) {
			accessProfile = accessProfileService.getAccessProfileById(id);
		}

		mav.addObject("accessProfile", accessProfile);
		mav.addObject("moduleList", populateModuleList());
		mav.setViewName("access-profile-view");
		return mav;
	}

	@ModelAttribute
	public void setActiveTabs(Model model) {
		model.addAttribute("userActive", false);
		model.addAttribute(KEY_ROLE_ACTIVE, true);
		model.addAttribute("accessProfileActive", false);
	}

}
