package com.ezc.hsil.webapp.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezc.hsil.webapp.dto.PasswordDto;
import com.ezc.hsil.webapp.dto.UserDto;
import com.ezc.hsil.webapp.error.InvalidOldPasswordException;
import com.ezc.hsil.webapp.model.EzStates;
import com.ezc.hsil.webapp.model.EzStores;
import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.UserDefaults;
import com.ezc.hsil.webapp.model.UserForm;
import com.ezc.hsil.webapp.model.UserRoles;
import com.ezc.hsil.webapp.model.UserZones;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.model.WorkGroup_Users;
import com.ezc.hsil.webapp.model.Work_Groups;
import com.ezc.hsil.webapp.persistance.dao.EzUserCreationDefaults;
import com.ezc.hsil.webapp.security.ActiveUserStore;
import com.ezc.hsil.webapp.security.ISecurityUserService;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.IUserService;
import com.ezc.hsil.webapp.util.GenericResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    IUserService userService;
    
    @Autowired
    EzUserCreationDefaults userDefaults;
    
    @Autowired
    private IUserService iUserService;
    
    @Autowired
    private IMasterService iMasterService;
    
    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ISecurityUserService securityService;
    
    @Autowired
    private Environment env;
    

    @RequestMapping(value = "/loggedUsers", method = RequestMethod.GET)
    public String getLoggedUsers(final Locale locale, final Model model) {
      model.addAttribute("users", activeUserStore.getUsers());
      return "Content";
  }
    
    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public String nextPage(final Locale locale, final Model model) {
      model.addAttribute("users", activeUserStore.getUsers());
      return "Content2";
  }    
    
    @RequestMapping(value = "/loggedUsersFromSessionRegistry", method = RequestMethod.GET)
    public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
        model.addAttribute("users", userService.getUsersFromSessionRegistry());
        return "users";
    }
    
    @RequestMapping(value = "/UserCreation", method = RequestMethod.GET) //@GetMapping("/UserCreation")
    public String test(final Locale locale, final Model model) {
    	
    	log.debug(":UserCreation GET:::::::");
    	userDefaults.initData();
    	 List<EzStates> states = userDefaults.getStates();
    	 List<UserRoles> roles = userDefaults.getRoles();
    	 List<UserZones> zones = userDefaults.getZones();
    	 List<String> categories = userDefaults.getCategories();
    	 List<EzStores> storeList=iMasterService.listStores();
 		 
    	 model.addAttribute("categories", categories);
    	 model.addAttribute("storeList",storeList);   	 
    	 model.addAttribute("userForm", new UserForm());
	     model.addAttribute("states", states);
	     model.addAttribute("roles", roles);
	     model.addAttribute("zones", zones);

        return "user/ezUserCreationForm";
    }
    
    @RequestMapping(value = "/UserCreation", method = RequestMethod.POST) //@PostMapping("/UserCreation")
    public String saveUser(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, final Model model) {
   	
    	log.debug(":UserCreation POST:::::::");
    	
    	 if (bindingResult.hasErrors()) { 
    		 userDefaults.initData();
    		 List<EzStates> states = userDefaults.getStates();
        	 List<UserRoles> roles = userDefaults.getRoles();
        	 List<UserZones> zones = userDefaults.getZones();
        	 List<String> categories = userDefaults.getCategories();
        	 
    	     model.addAttribute("states", states);
    	     model.addAttribute("roles", roles);
    	     model.addAttribute("zones", zones);
    	     model.addAttribute("categories", categories);
    	     
             return "user/ezUserCreationForm";
         }
    	
    	List<EzStates> list = userDefaults.getStates();
    	
    	UserDto userDto = new UserDto();
    	
    	userDto.setFirstName(userForm.getFirstName());
    	userDto.setLastName(userForm.getLastName());
    	userDto.setPassword("portal");
    	userDto.setEmail(userForm.getEmail());
    	userDto.setRole(userForm.getRole());
    	userDto.setUserId(userForm.getUserId());
    	userDto.setGroup(userForm.getGroup());
    	userDto.setZone(userForm.getZone());
    	userDto.setStore(userForm.getStore());
    	userDto.setUserCategory(userForm.getUserCategory());
    	userDto.setEnabled(true);
    	
    	iUserService.registerNewUserAccount(userDto);
    	
    	List<Users> usersList = iUserService.getUsersList();
   	 	model.addAttribute("usersList", usersList);
       return "user/ezUsersList";
    }
 
    @RequestMapping(value = "/usersList", method = RequestMethod.GET) //@GetMapping("/UserCreation")
    public String getUsrsList(final Locale locale, final Model model) {   	
    	List<Users> usersList = iUserService.getUsersList();
    	 model.addAttribute("usersList", usersList);
    	//log.debug(":::::::usersList:::::"+usersList);
        return "user/ezUsersList";
    }
    
    @PostMapping(value = "/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id,final Locale locale, final Model model) {   
    	
    	Optional<Users> lv_user = iUserService.getUserByID(id); 
		Users lv_User = (Users)lv_user.get();
    	
    	Users user = new Users();
    	user.setId(id);
    	user.setUserId(lv_User.getUserId());
    	
    	iUserService.deleteUser(user);
    	
    	List<Users> usersList = iUserService.getUsersList();
   	 	model.addAttribute("usersList", usersList);
       return "user/ezUsersList";
    }
    
    @GetMapping(value = "/editUser/{id}")
    public String editUser(@PathVariable("id") Long id,final Locale locale, final Model model) {
    	 userDefaults.initData();
    	 List<UserRoles> roles = userDefaults.getRoles();
    	 List<UserZones> zones = userDefaults.getZones();   
    	 List<String> categories = userDefaults.getCategories();
    	 
    	 Optional<Users> user = iUserService.getUserByID(id); 
		 Users lv_User = (Users)user.get();
		 List<Roles> userRoles = (List<Roles>)lv_User.getRoles();
		 Set<UserDefaults> userDefList=lv_User.getUserDefaults();
		 List<String> userCatList=new ArrayList<String>(); 
		 if(userDefList != null)
		 {
			 for(UserDefaults userDef:userDefList)
			 {
				 if("CATEGORY".equals(userDef.getKey()))
				 {
					 String [] tempArr=userDef.getValue().split(",");
					 for(String tempStr:tempArr)
						 userCatList.add(tempStr);
				 }
			 }
		 }
		 Roles userRole = null;
    	 
		 List<WorkGroup_Users> userGroupList =  iUserService.getGroupsByUserId(lv_User.getUserId());
		 List<String> userGroups = new ArrayList<String>();
    	 List<Work_Groups> wfGroups = null;
      	 try{
    		 userRole = (Roles)userRoles.get(0);
    		 //wfGroups = (List<Work_Groups>)iUserService.getGroupsByRole(userRole.getName());
    		 wfGroups = (List<Work_Groups>)iUserService.getGroupsByRole("ROLE_REQ_CR");
    	 }catch(Exception e){}	
 
      	 
      	UserForm formFields = new UserForm();
      	
      	String userZone = "";
      	for(WorkGroup_Users userGroup : userGroupList)
      	{
      		userZone =userGroup.getZonalGrp(); 
      		userGroups.add(userGroup.getGroupId()+"");
      	}
      	
      	List<WorkGroup_Users> zonalHDSubGroups = (List<WorkGroup_Users>) iUserService.getZonalHeadSubGroups(userZone);
      	
		/*
		 * log.debug("::::zonalHDSubGroups:::::::"+zonalHDSubGroups); Iterator iter =
		 * zonalHDSubGroups.iterator(); while(iter.hasNext()) { WorkGroup_Users lv_wgu =
		 * (WorkGroup_Users)iter.next();
		 * log.debug(lv_wgu.getUserId()+":::::"+lv_wgu.getGroupId()+":::::"+lv_wgu.
		 * getStateGrp()+"::::"+lv_wgu.getZonalGrp()); }
		 */
      	
      	try{
      		userZone = userZone.substring(0,userZone.indexOf("_"));
      	}catch(Exception e){}	
      	
      	log.debug("::::::::::userGroups"+userGroups);
      	
      	formFields.setId(lv_User.getId());
      	formFields.setUserId(lv_User.getUserId());
      	formFields.setFirstName(lv_User.getFirstName());
      	formFields.setLastName(lv_User.getLastName());
      	formFields.setEmail(lv_User.getEmail());
      	formFields.setRole(userRole.getName());
      	formFields.setGroup(userGroups);
      	formFields.setZone(userZone);
      	formFields.setUserCategory(userCatList);
 		 
      	 model.addAttribute("userForm", formFields);
    	 model.addAttribute("roles", roles);
	     model.addAttribute("zones", zones);
	     model.addAttribute("wfGroups", wfGroups);
	     model.addAttribute("categories", categories);
	     //model.addAttribute("userRole", userRole); 
	     //model.addAttribute("userGroup", userGroups);
	     //model.addAttribute("userForm", user);
	     //model.addAttribute("formFields", new UserForm());

        return "user/ezEditUser";
    }
    
    @RequestMapping(value = "/editSave", method = RequestMethod.POST) //@PostMapping("/UserCreation")
    public String editSave(@ModelAttribute UserForm userForm, final Model model) {	
    	
    	
    	//log.debug(":::::::userForm:::::"+userForm);
    	
    	UserDto userDto = new UserDto();
    	
    	userDto.setId(userForm.getId());
    	userDto.setFirstName(userForm.getFirstName());
    	userDto.setLastName(userForm.getLastName());
    	userDto.setEmail(userForm.getEmail());
    	userDto.setRole(userForm.getRole());
    	userDto.setUserId(userForm.getUserId());
    	userDto.setGroup(userForm.getGroup());
    	userDto.setZone(userForm.getZone());
    	userDto.setUserCategory(userForm.getUserCategory());
    	
    	iUserService.editUser(userDto);
     	
    	List<Users> usersList = iUserService.getUsersList();
   	 	model.addAttribute("usersList", usersList);
       return "user/ezUsersList";
    }
    
    
    @RequestMapping(value = "/userByUserId/{userId}", method = RequestMethod.GET)
    public @ResponseBody String getUserByUserId(@PathVariable("userId") String userId) {   	    	
    	Users user = iUserService.findUserByUserId(userId); 
    	
    	log.debug(":UsersUsersUsersUsers:::::::"+user);
    	log.debug(":getUserByUserId:::userId:::::::"+user.getId());
    	
    	if(user==null)
    		return "N";	
    	else
            return "Y";
    }
    
    // Reset password
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        final Users user = userService.findUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
        }
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = securityService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/savePassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    // change user password
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final Users user = userService.findUserByEmail(((Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

    

    /******************Non API Methods**********************/
    
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    


    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final Users user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, Users user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
