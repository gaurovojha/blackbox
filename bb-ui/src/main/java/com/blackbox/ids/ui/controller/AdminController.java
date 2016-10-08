package com.blackbox.ids.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.services.usermanagement.AccessProfileService;
import com.blackbox.ids.services.usermanagement.RoleService;
import com.blackbox.ids.services.usermanagement.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	private static final String KEY_USER_DETAILS = "userDetails";

	private static final String KEY_USER_POPUP = "user-popup";

	private static final String KEY_ACCESS_PROFILE_ACTIVE = "accessProfileActive";

	private static final String KEY_ROLE_ACTIVE = "roleActive";

	private static final String KEY_USER_ACTIVE = "userActive";

	private static final Logger LOGGER = Logger.getLogger(AdminController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccessProfileService accessProfileService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public String initForm(HttpServletRequest request, Model model) {

		LOGGER.info("Admin page");

		String referrer = request.getHeader("referer");

		model.addAttribute(KEY_USER_ACTIVE, true);
		model.addAttribute(KEY_ROLE_ACTIVE, true);
		model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);

		if (referrer != null) {
			if (referrer.contains("/role/")) {
				model.addAttribute(KEY_USER_ACTIVE, false);
				model.addAttribute(KEY_ROLE_ACTIVE, true);
				model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, false);
			} else {
				if (referrer.contains("/accessprofile/")) {
					model.addAttribute(KEY_USER_ACTIVE, false);
					model.addAttribute(KEY_ROLE_ACTIVE, false);
					model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);
				} else {
					if (referrer.contains("/user/")) {
						model.addAttribute(KEY_USER_ACTIVE, true);
						model.addAttribute(KEY_ROLE_ACTIVE, false);
						model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, false);
					}
				}
			}
		}

		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {

			if (flashMap.get(KEY_USER_ACTIVE) != null) {
				model.addAttribute(KEY_USER_ACTIVE, true);
				model.addAttribute(KEY_ROLE_ACTIVE, false);
				model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, false);
			}

			if (flashMap.get(KEY_ACCESS_PROFILE_ACTIVE) != null) {
				model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, true);
				model.addAttribute(KEY_USER_ACTIVE, false);
				model.addAttribute(KEY_ROLE_ACTIVE, false);
			}

			if (flashMap.get(KEY_ROLE_ACTIVE) != null) {
				model.addAttribute(KEY_ROLE_ACTIVE, true);
				model.addAttribute(KEY_USER_ACTIVE, false);
				model.addAttribute(KEY_ACCESS_PROFILE_ACTIVE, false);
			}
		}

		return "admin";
	}

	@RequestMapping(value = "/listrole", method = { RequestMethod.GET })
	@ResponseBody public ModelAndView showAccessProfiles(ModelAndView mav) {
		List<RoleDTO> roleList = roleService.getAllRoles();
		mav.addObject("roleList", roleList);
		mav.setViewName("list-role");
		return mav;
	}

	@RequestMapping(value = "/listaccessprofile", method = { RequestMethod.GET })
	public @ResponseBody ModelAndView showRoles(ModelAndView mav) {
		List<AccessProfileDTO> accessProfileList = accessProfileService.getAllAccessProfiles();
		mav.addObject("accessProfileList", accessProfileList);
		mav.setViewName("list-access-profile");
		return mav;
	}

	@ModelAttribute("roleList")
	public List<RoleDTO> populateRoles() {
		return roleService.getAllRoles();
	}

	@ModelAttribute("accessProfileList")
	public List<AccessProfileDTO> populateAccessProfileList() {
		return accessProfileService.getAllAccessProfiles();
	}

	@ModelAttribute("userList")
	public List<UserDTO> populateUserList() {
		return userService.getUsers();
	}

	@RequestMapping(value = "/usersTable", method = { RequestMethod.GET })
	@ResponseBody public ModelAndView getUsersTable(ModelAndView mav) throws Exception {
		List<UserDTO> userList = userService.getUsers();
		mav.addObject("userList", userList);
		mav.setViewName("list-user");
		return mav;
	}

	@RequestMapping(value = "/userDetails", method = { RequestMethod.GET }, produces = "text/html")
	public ModelAndView populateUserDetailsForRole(@RequestParam("roleId") Long roleId, ModelAndView mav) {
		List<Long> idList = new ArrayList<Long>();
		idList.add(roleId);
		List<UserDTO> userDetails = userService.getUserDetails(idList);
		mav.addObject(KEY_USER_DETAILS, userDetails);
		mav.setViewName(KEY_USER_POPUP);
		return mav;
	}

	@RequestMapping(value = "/userDetailsByProfile", method = { RequestMethod.GET }, produces = "text/html")
	public ModelAndView populateUserDetailsForProfile(@RequestParam("accessProfileId") Long accessProfileId,
			ModelAndView mav) {
		List<UserDTO> userDetails = userService.getUserDetailsByProfileId(accessProfileId);
		mav.addObject(KEY_USER_DETAILS, userDetails);
		mav.setViewName(KEY_USER_POPUP);
		return mav;
	}
}