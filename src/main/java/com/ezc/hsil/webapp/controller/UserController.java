package com.ezc.hsil.webapp.controller;
import com.ezc.hsil.webapp.model.*;
import com.ezc.hsil.webapp.persistance.dao.*;
import com.ezc.hsil.webapp.persistance.dao.EzUserCreationDefaults;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.security.ActiveUserStore;
import com.ezc.hsil.webapp.service.IUserService;

import lombok.extern.slf4j.Slf4j;

import com.ezc.hsil.webapp.dto.UserDto;

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
    	
    	System.out.println(":UserCreation GET:::::::");
    	
    	 List<EzStates> states = userDefaults.getStates();
    	 List<UserRoles> roles = userDefaults.getRoles();
    	 List<UserZones> zones = userDefaults.getZones();
    	    	 
    	 model.addAttribute("userForm", new UserForm());
	     model.addAttribute("states", states);
	     model.addAttribute("roles", roles);
	     model.addAttribute("zones", zones);

        return "user/ezUserCreationForm";
    }
    
    @RequestMapping(value = "/UserCreation", method = RequestMethod.POST) //@PostMapping("/UserCreation")
    public String saveUser(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, final Model model) {
   	
    	System.out.println(":UserCreation POST:::::::");
    	
    	 if (bindingResult.hasErrors()) { 
    		 
    		 List<EzStates> states = userDefaults.getStates();
        	 List<UserRoles> roles = userDefaults.getRoles();
        	 List<UserZones> zones = userDefaults.getZones();
        
    	     model.addAttribute("states", states);
    	     model.addAttribute("roles", roles);
    	     model.addAttribute("zones", zones);
    		 
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
    	//System.out.println(":::::::usersList:::::"+usersList);
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
    	 List<UserRoles> roles = userDefaults.getRoles();
    	 List<UserZones> zones = userDefaults.getZones();   
      	
    	 Optional<Users> user = iUserService.getUserByID(id); 
		 Users lv_User = (Users)user.get();
		 List<Roles> userRoles = (List<Roles>)lv_User.getRoles();
 
		 Roles userRole = null;
    	 
    	 WorkGroup_Users userGroups = (WorkGroup_Users) iUserService.getGroupsByUserId(lv_User.getUserId());
   
    	 List<Work_Groups> wfGroups = null;
      	 try{
    		 userRole = (Roles)userRoles.get(0);
    		 wfGroups = (List<Work_Groups>)iUserService.getGroupsByRole(userRole.getName());
    	 }catch(Exception e){}	
 
      	 
      	UserForm formFields = new UserForm();
      	
      	String userZone = userGroups.getZonalGrp();
      	
      	List<WorkGroup_Users> zonalHDSubGroups = (List<WorkGroup_Users>) iUserService.getZonalHeadSubGroups(userZone);
      	
      	System.out.println("::::zonalHDSubGroups:::::::"+zonalHDSubGroups);
      	 Iterator iter = zonalHDSubGroups.iterator();
      	 while(iter.hasNext())
      	 {
      		WorkGroup_Users lv_wgu = (WorkGroup_Users)iter.next();
      	    System.out.println(lv_wgu.getUserId()+":::::"+lv_wgu.getGroupId()+":::::"+lv_wgu.getStateGrp()+"::::"+lv_wgu.getZonalGrp());
      	 }
      	
      	try{
      		userZone = userZone.substring(0,userZone.indexOf("_"));
      	}catch(Exception e){}	
      	
      	
      	
      	formFields.setId(lv_User.getId());
      	formFields.setUserId(lv_User.getUserId());
      	formFields.setFirstName(lv_User.getFirstName());
      	formFields.setLastName(lv_User.getLastName());
      	formFields.setEmail(lv_User.getEmail());
      	formFields.setRole(userRole.getName());
      	formFields.setGroup(userGroups.getGroupId());
      	formFields.setZone(userZone);
 		 
      	 model.addAttribute("userForm", formFields);
    	 model.addAttribute("roles", roles);
	     model.addAttribute("zones", zones);
	     model.addAttribute("wfGroups", wfGroups);
	     //model.addAttribute("userRole", userRole); 
	     //model.addAttribute("userGroup", userGroups);
	     //model.addAttribute("userForm", user);
	     //model.addAttribute("formFields", new UserForm());

        return "user/ezEditUser";
    }
    
    @RequestMapping(value = "/editSave", method = RequestMethod.POST) //@PostMapping("/UserCreation")
    public String editSave(@ModelAttribute UserForm userForm, final Model model) {	
    	
    	
    	//System.out.println(":::::::userForm:::::"+userForm);
    	
    	UserDto userDto = new UserDto();
    	
    	userDto.setId(userForm.getId());
    	userDto.setFirstName(userForm.getFirstName());
    	userDto.setLastName(userForm.getLastName());
    	userDto.setEmail(userForm.getEmail());
    	userDto.setRole(userForm.getRole());
    	userDto.setUserId(userForm.getUserId());
    	userDto.setGroup(userForm.getGroup());
    	userDto.setZone(userForm.getZone());
    	
    	iUserService.editUser(userDto);
     	
    	List<Users> usersList = iUserService.getUsersList();
   	 	model.addAttribute("usersList", usersList);
       return "user/ezUsersList";
    }
    
    
    @RequestMapping(value = "/userByUserId/{userId}", method = RequestMethod.POST)
    public @ResponseBody String getUserByUserId(@PathVariable("userId") String userId) {   	    	
    	Users user = iUserService.findUserByUserId(userId); 
    	
    	System.out.println(":UsersUsersUsersUsers:::::::"+user);
    	System.out.println(":getUserByUserId:::userId:::::::"+user.getId());
    	
    	if(user==null)
    		return "N";	
    	else
            return "Y";
    }
}
