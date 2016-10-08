package com.blackbox.ids.ui.controller;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.dto.NationalityDTO;
import com.blackbox.ids.dto.OTPStatusDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.dto.UserTypeDTO;
import com.blackbox.ids.services.common.JsonDataService;
import com.blackbox.ids.services.impl.common.OTPStatusTypeAdapter;
import com.blackbox.ids.services.usermanagement.RoleService;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.ui.form.UserDetailsForm;
import com.blackbox.ids.ui.validation.user.UserFormValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private Logger log = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    private UserFormValidator userFormValidator;

    @Autowired
    JsonDataService jsonDataService;

    @Autowired
    RoleService roleService;
    
	private UserService USER_SERVICE;

    /**
     * Initiates form validator for user management modules.
     *
     * @param binder
     *            Binder that allows for setting property values onto a target object, including support for validation
     *            and binding result analysis.
     */
    @InitBinder(UserDetailsForm.MODEL_NAME)
    protected void initUserBinder(final WebDataBinder binder) {
    	binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
        binder.setValidator(userFormValidator);
    }

    /**
     * Attach the custom validator to the Spring context
     */
    @RequestMapping(value = "/newuser", method = { RequestMethod.GET })
    public String newUser(HttpServletRequest request, Model model ,
    	@ModelAttribute(UserDetailsForm.MODEL_NAME) UserDetailsForm user, BindingResult result) throws Exception {
    	log.debug("Inside new user controller");
    	populateDTOs(model);
        return "create-new-user";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createNewUser(@Valid @ModelAttribute(UserDetailsForm.MODEL_NAME) UserDetailsForm userForm, BindingResult result,
    		RedirectAttributes ra, final Model model) throws ApplicationException {

    	final String module = "[New User Creation]";
    	final String userEmail = userForm.getEmailId();
    	log.info(format("{0}: User request received to create user {1}.", module, userEmail));
    	String view = "redirect:../admin/";

    	if (result.hasErrors()) {
    		log.info(format("{0}: Got data validation errors for user {1}.", module, userEmail));
    		populateDTOs(model);
    		view = "create-new-user";
    	} else {
    		log.info(format("{0}: Initiating data persistence for user {1}.", module, userEmail));
    		userService.createNewUser(userForm.toEntity());
    		log.info(format("{0}: User {1} saved successfully. Sending password.", module, userEmail));
    		//userService.generateAndSendPassword(userEmail);
    		ra.addFlashAttribute("userActive", true);
    	}

        return view;
    }

    @Transactional
    @RequestMapping(value = "/edit/{userId}", method = { RequestMethod.GET })
    public String editUser(@PathVariable("userId")Long userId, Model model) throws Exception {
    	populateUserForm(userId, model);
    	populateDTOs(model);
        return "editUser";
    }

    @Transactional
    @RequestMapping(value = "/view/{userId}", method = { RequestMethod.GET })
    public String viewUser(@PathVariable("userId") Long userId, Model model) throws Exception {
    	populateUserForm(userId, model);
        return "view-user";
    }

    private void populateUserForm(Long userId, Model model) {
    	UserDetailsForm userForm = new UserDetailsForm();
    	User user = userService.getUserById(userId);
    	userForm.load(user);

    	//populateDTOs(model);
    	model.addAttribute(UserDetailsForm.MODEL_NAME, userForm);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
    public String updateUser(Model model, @Valid @ModelAttribute(UserDetailsForm.MODEL_NAME) UserDetailsForm userForm,
    		BindingResult result, RedirectAttributes ra) throws ApplicationException {

		final String module = "[Update User Profile]";
    	final String userEmail = userForm.getEmailId();
    	log.info(format("{0}: User request received to update user {1}.", module, userEmail));
    	String view = "redirect:../admin/";

    	if (result.hasErrors()) {
    		log.info(format("{0}: Got data validation errors for user {1}.", module, userEmail));
    		populateDTOs(model);
    		view = "edit-user";
    	} else {
    		log.info(format("{0}: Initiating data update for user {1}.", module, userEmail));
    		userService.updateUser(userForm.toEntity());
    		log.info(format("{0}: User {1} updated successfully.", module, userEmail));
    		populateDTOs(model);
    		ra.addFlashAttribute("userActive", true);
    	}

		return view;
    }

    @RequestMapping(value = "/generateotp", method = { RequestMethod.GET })
    @ResponseBody
    public String generateOTP(@RequestParam("userId")Long userId, Model model) throws Exception {
    	log.debug("Inside new user controller");
    	OTPStatusDTO otpStatus = userService.generateOtp(userId);
    	Gson gson = new GsonBuilder().registerTypeAdapter(OTPStatusDTO.class, new OTPStatusTypeAdapter<OTPStatusDTO>()).create();
    	Type otpstatustype = new TypeToken<OTPStatusDTO>() {
        }.getType();
    	/*OTPStatus status = userService.generateOtp(userId);
    	Gson gson = new GsonBuilder().registerTypeAdapter(OTPStatus.class, new OTPStatusTypeAdapter<OTPStatus>()).create();
        Type otpstatustype = new TypeToken<OTPStatus>() {
        }.getType();*/
        return gson.toJson(otpStatus, otpstatustype);
    }

    @RequestMapping(value = "/disableaccess", method = { RequestMethod.POST })
    @ResponseBody
    public String disableUser(@RequestBody String userIds, Model model) throws Exception {
    	log.debug("Inside new user controller");
    	log.debug("size: " + userIds);
    	Gson gson = new Gson();
    	Type userIdType = new TypeToken<List<Long>>(){}.getType();
    	List<Long> userIdList = gson.fromJson(userIds, userIdType);
    	List<Long> disabledIds = userService.disableAccess(userIdList);
        return jsonDataService.parseLongList(disabledIds);
    }

    @RequestMapping(value = "/unlock/{userId}", method = { RequestMethod.GET })
    public String unlockUser(@PathVariable("userId")Long userId, Model model) throws Exception {
    	final UserDTO user = userService.unlockUser(userId);
    	model.addAttribute("user", user);
    	return "userAccessControl";
    }

    @RequestMapping(value = "/enable/{userId}", method = { RequestMethod.GET })
    public String enableUser(@PathVariable("userId")Long userId, Model model) throws Exception {
    	final UserDTO user = userService.enableUser(userId);
    	model.addAttribute("user", user);
    	return "userAccessControl";
    }

    @RequestMapping(value = "/drop", method = { RequestMethod.POST })
    @ResponseBody
    public String deleteUser(@RequestBody String userIds, Model model) throws Exception {
    	log.debug("Inside new user controller");
    	log.debug("size: " + userIds);
    	Gson gson = new Gson();
    	Type userIdType = new TypeToken<List<Long>>(){}.getType();
    	List<Long> userIdList = gson.fromJson(userIds, userIdType);
    	List<Long> deletedIds = userService.deleteUser(userIdList);
        return jsonDataService.parseLongList(deletedIds);
    }

    private void populateDTOs(Model model) {
    	List<NationalityDTO> nationalityDTOs = userService.getNationalities();
    	List<UserTypeDTO> userTypeDTOs = userService.getUserTypes();
    	List<RoleDTO> roleDTOs = roleService.getAllRoles();
    	model.addAttribute("nationalities", nationalityDTOs);
        model.addAttribute("userTypes", userTypeDTOs);
        model.addAttribute("roles",roleDTOs);
    }

    @ModelAttribute
	public void setActiveTabs(Model model) {
		model.addAttribute("userActive", false);
		model.addAttribute("roleActive", true);
		model.addAttribute("accessProfileActive", false);
	}

    private String format(final String message, final Object...params) {
    	return MessageFormat.format(message, params);
    }
    public static void main(String agrs[]){
    	final Student s1 = new Student("","");
    	
    	Student s2 = new Student("","");
    	s1.setName("Gaurov");
    	//s1 = s2;
    	System.out.println(s1.name);
    	
    }
    
    

}
class Student{
	String name;
	String fName;
	public  Student(String name, String fName){
		this.name = name;
		this.fName = fName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	
}