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

import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.dto.usermanagement.ModuleDTO;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.usermanagement.AccessProfileService;

@Controller
@RequestMapping(value = "/accessprofile")
public class AccessProfileController {

	private static final String KEY_CREATE_ACCESS_PROFILE = "create-access-profile";

	private static final String KEY_ACCESS_PROFILE_ACTIVE = "accessProfileActive";

	private static final String KEY_ACCESS_PROFILE_FORM = "accessProfileForm";

	private static final Logger LOGGER = Logger.getLogger(AccessProfileController.class);

	@Autowired
	private AccessProfileService accessProfileService;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private Validator accessProfileFormValidator;

	@RequestMapping(value = "/createDuplicate", method = { RequestMethod.GET })
	public String createDuplcate(@RequestParam("id") long id, Model model) {

		AccessProfileDTO form = accessProfileService.getAccessProfileById(Long.valueOf(id));
		model.addAttribute(KEY_ACCESS_PROFILE_FORM, form);
		return KEY_CREATE_ACCESS_PROFILE;
	}

	@RequestMapping(value = "/create", method = { RequestMethod.GET })
	public String create(@ModelAttribute(KEY_ACCESS_PROFILE_FORM) AccessProfileDTO form, Model model) {

		if (form != null) {
			model.addAttribute(KEY_ACCESS_PROFILE_FORM, form);
		} else {
			model.addAttribute(KEY_ACCESS_PROFILE_FORM, new AccessProfileDTO());
		}

		return KEY_CREATE_ACCESS_PROFILE;
	}

	@RequestMapping(value = "/submit", method = { RequestMethod.POST })
	public String submit(@ModelAttribute(KEY_ACCESS_PROFILE_FORM) AccessProfileDTO accessProfileForm,
			BindingResult result, RedirectAttributes ra) {

		// validate access profile form
		accessProfileFormValidator.validate(accessProfileForm, result);

		if (result.hasErrors()) {
			LOGGER.info("Returning create-access-profile.jsp page");
			return KEY_CREATE_ACCESS_PROFILE;
		}
		accessProfileService.createAccessProfile(accessProfileForm);
		LOGGER.info("New Access Profile Created");
		ra.addFlashAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);
		return "redirect:../admin/";
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public String update(@ModelAttribute(KEY_ACCESS_PROFILE_FORM) AccessProfileDTO accessProfileForm,
			BindingResult result, RedirectAttributes ra) {

		// validate access profile form
		accessProfileFormValidator.validate(accessProfileForm, result);

		if (result.hasErrors()) {
			LOGGER.info("Returning create-access-profile.jsp page");
			return "edit-access-profile";
		}
		accessProfileService.updateAccessProfile(accessProfileForm);
		LOGGER.info("Access Profile with id " + accessProfileForm.getId() + "updated");
		ra.addFlashAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);
		return "redirect:../admin/";
	}

	@RequestMapping(value = "/edit", method = { RequestMethod.GET })
	public String edit(@RequestParam("id") long id, Model model, RedirectAttributes ra) {
		AccessProfileDTO form = accessProfileService.getAccessProfileById(id);
		if (form.isSeeded()) {
			ra.addFlashAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);
			return "redirect:../admin/";
		}
		model.addAttribute(KEY_ACCESS_PROFILE_FORM, form);
		return "edit-access-profile";
	}

	@RequestMapping(value = "/view", method = { RequestMethod.GET })
	public String view(@RequestParam("id") long id, Model model) {
		AccessProfileDTO form = accessProfileService.getAccessProfileById(id);
		model.addAttribute("accessProfile", form);
		return "view-access-profile";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.GET })
	public String delete(@RequestParam("id") long id, Model model, RedirectAttributes ra) {
		accessProfileService.deactivate(id);
		populateActiveTabs(model);
		LOGGER.info("Access Profile with id " + id + "is deactivated");
		ra.addFlashAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);
		return "redirect:../admin/";
	}

	@ModelAttribute("moduleList")
	public List<ModuleDTO> populateModuleList() {
		return masterDataService.getAllModules();
	}

	@ModelAttribute("accessProfileList")
	public List<AccessProfileDTO> populateAccessProfileList() {
		return accessProfileService.getAllAccessProfiles();
	}

	@RequestMapping(value = "/getAccessProfile", method = { RequestMethod.GET }, produces = "text/html")
	@ResponseBody
	public ModelAndView getAccessProfile(@RequestParam("id") Long id, ModelAndView mav) {

		AccessProfileDTO accessProfile = accessProfileService.getAccessProfileById(id);
		mav.addObject(KEY_ACCESS_PROFILE_FORM, accessProfile);
		mav.addObject("moduleList", populateModuleList());
		mav.setViewName("view-access-profile");
		return mav;
	}

	public void populateActiveTabs(Model model) {
		model.addAttribute("userActive", false);
		model.addAttribute("roleActive", true);
		model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, false);
	}
}