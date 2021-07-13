package com.ezc.hsil.webapp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.UserDto;
import com.ezc.hsil.webapp.error.UserAlreadyExistException;
import com.ezc.hsil.webapp.model.PasswordResetToken;
import com.ezc.hsil.webapp.model.Privilages;
import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.UserDefaults;
import com.ezc.hsil.webapp.model.UserDefaultsKey;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.model.VerificationToken;
import com.ezc.hsil.webapp.model.WorkGroup_Users;
import com.ezc.hsil.webapp.model.Work_Groups;
import com.ezc.hsil.webapp.persistance.dao.EzUserCreationDefaults;
import com.ezc.hsil.webapp.persistance.dao.GroupRepository;
import com.ezc.hsil.webapp.persistance.dao.PasswordResetTokenRepository;
import com.ezc.hsil.webapp.persistance.dao.PrivilegeRepository;
import com.ezc.hsil.webapp.persistance.dao.RoleRepository;
import com.ezc.hsil.webapp.persistance.dao.UserDefRepository;
import com.ezc.hsil.webapp.persistance.dao.UserRepository;
import com.ezc.hsil.webapp.persistance.dao.VerificationTokenRepository;
import com.ezc.hsil.webapp.persistance.dao.WorkGroupUsersRepository;

@Service
@Transactional
public class UserServiceImpl implements IUserService{

	 @Autowired
	    private UserRepository userRepository;
	 
	 @Autowired
	    private UserDefRepository userDefRepo;
	 
	    @Autowired
	    private VerificationTokenRepository tokenRepository;

	    @Autowired
	    private PasswordResetTokenRepository passwordTokenRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    private RoleRepository roleRepository;
	    
	    @Autowired
	    private GroupRepository groupRepository;
	    
	    @Autowired
	    private WorkGroupUsersRepository groupUsersRepository;

	    @Autowired
	    private PrivilegeRepository privilegeRepository;
	    
	    @Autowired
	    EzUserCreationDefaults userDefaults;
	    
	    @Autowired
	    private SessionRegistry sessionRegistry;

	    public static final String TOKEN_INVALID = "invalidToken";
	    public static final String TOKEN_EXPIRED = "expired";
	    public static final String TOKEN_VALID = "valid";

	    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	    public static String APP_NAME = "SpringRegistration";

	    // API

	    @Override
	    public Users registerNewUserAccount(final UserDto accountDto) {
	        if (emailExists(accountDto.getEmail())) {
	            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
	        }
	        final Users user = new Users();
	        
	        
	        final Privilages readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
	        final Privilages writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
	        final Privilages passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");
	        
	        final List<Privilages> adminPrivileges = new ArrayList<Privilages>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
	        final List<Privilages> userPrivileges = new ArrayList<Privilages>(Arrays.asList(readPrivilege, passwordPrivilege));
	        final Roles userRole = createRoleIfNotFound(accountDto.getRole(), adminPrivileges);
	        List<String> userCategories=accountDto.getUserCategory();
	        
	        user.setFirstName(accountDto.getFirstName());
	        user.setLastName(accountDto.getLastName());
	        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
	        user.setEmail(accountDto.getEmail());
	//        user.setUsing2FA(accountDto.isUsing2FA());
	        user.setRoles(new ArrayList<Roles>(Arrays.asList(userRole)));
	        user.setUserId(accountDto.getUserId()); 
	        user.setEnabled(true);
	        
	        
	        System.out.println(":::::::accountDto:::::"+accountDto.getUserId());
	        Users outUser=userRepository.save(user);
	        if(outUser !=null)
	        {
	        	//Set<UserDefaults> userDefList=new HashSet<UserDefaults>();
	        	String tempStore = accountDto.getStore();
	        	if(tempStore != null && !"null".equals(tempStore) && !"".equals(tempStore))
	        	{
			        UserDefaults userDef=new UserDefaults();
			        userDef.setKey("STORE");
			        userDef.setValue(accountDto.getStore());
			        userDef.setUsers(outUser);
			        userDef.setUserId(outUser.getId());
			        //userDefList.add(userDef);
			        userDefRepo.save(userDef);
	        	}
	        	
	        	if(userCategories != null && !userCategories.isEmpty())
	        	{
	        		String userCatStr="";
	        		for(String tempStr:userCategories)
	        		{
	        			userCatStr+=tempStr+",";
	        		}
	        		if(userCatStr.indexOf(",") >= 0)
	        			userCatStr=userCatStr.substring(0, userCatStr.length() - 1);
	        		UserDefaults userDef=new UserDefaults();
			        userDef.setKey("CATEGORY");
			        userDef.setValue(userCatStr);
			        userDef.setUsers(outUser);
			        userDef.setUserId(outUser.getId());
			        userDefRepo.save(userDef);
	        	}
	        	
	        	List<String> userGrpList =  accountDto.getGroup();
	        	//String userGrp = accountDto.getGroup();
	        	String stHDGrp = "";
	        	String zonalUserGrp = "";
		        	for(String userGrp : userGrpList)
		        	{
			        	if("ROLE_REQ_CR".equals(accountDto.getRole()))
			        	{
			        		//userGrp = 	accountDto.getState()+"_GRP";
			        		stHDGrp =   userGrp+"_HD_GRP";
			        		zonalUserGrp = 	accountDto.getZone()+"_ZONE_GRP";
			        	}else if("ROLE_ST_HEAD".equals(accountDto.getRole()) || "ROLE_BD_MKTG".equals(accountDto.getRole())){
			        		userGrp = 	userGrp+"_HD_GRP";
			        		zonalUserGrp = accountDto.getZone()+"_ZONE_GRP";
			        	}else if("ROLE_ZN_HEAD".equals(accountDto.getRole())){
			        		userGrp = 	accountDto.getZone()+"_ZONE_GRP";
			        		zonalUserGrp = 	accountDto.getZone()+"_ZONE_GRP";
			        	}else if("ROLE_BD_MKT".equals(accountDto.getRole())){
			        		userGrp = 	userGrp+"_MK_GRP";
			        		zonalUserGrp = 	accountDto.getZone()+"_ZONE_GRP";
			        	}
			        	
			        	
		   	
				        //createGroupIfNotFound(userGrp,groupDesc,role);
			        	insertUserToGroupIfNotFound(userGrp,accountDto.getUserId(),stHDGrp,zonalUserGrp,"","");
		        	}
		        }	
	        
	        return userRepository.save(user);
	    }
	    
	    @Override
	    public List<Users> getUsersList() {    
	    	List<Users> usersList = userRepository.findAll();
	    	return usersList;
	    }
	    

	    @Override
	    public List<Work_Groups> getAllGroups() {    
	    	List<Work_Groups> wfGroups = groupRepository.findAll();
	    	System.out.println(":::::::getAllGroups-wfGroups:::::"+wfGroups);
	    	return wfGroups;
	    }
	    
	    @Override
	    public List<Work_Groups> getGroupsByRole(String role) {    
	    	List<Work_Groups> wfGroups = groupRepository.findByRole(role);
	    	System.out.println(":::::::getGroupsByRole-wfGroups:::::"+wfGroups);
	    	return wfGroups;
	    }
	    
	    @Override
	    public List<WorkGroup_Users> getGroupsByUserId(String userId) {    
	    	
	    	//System.out.println(":::::::userId:::::"+userId);
	    	
	    	List<WorkGroup_Users> userGroup = groupUsersRepository.getGroupByUserId(userId);
	    	
	    	//System.out.println(":::::::userGroups-getGroupId:::::"+userGroup.getGroupId());
	    	
	    	return userGroup;
	    }
	    

	    @Override
	    public Users getUser(final String verificationToken) {
	        final VerificationToken token = tokenRepository.findByToken(verificationToken);
	        if (token != null) {
	            return token.getUser();
	        }
	        return null; 
	    }

	    @Override
	    public VerificationToken getVerificationToken(final String VerificationToken) {
	        return tokenRepository.findByToken(VerificationToken);
	    }

	    @Override
	    public void editUser(final UserDto accountDto) {
	    	
	    	
	    	System.out.println("::::::::111111111111:::::::::::::;");
	    	
	    	final Privilages readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
	        final Privilages writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
	        final Privilages passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");
	        
	        final List<Privilages> adminPrivileges = new ArrayList<Privilages>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
	        final List<Privilages> userPrivileges = new ArrayList<Privilages>(Arrays.asList(readPrivilege, passwordPrivilege));
	        final Roles userRole = createRoleIfNotFound(accountDto.getRole(), adminPrivileges);
     
	        Optional<Users> userOpt=userRepository.findById(accountDto.getId());
	    	Users user = new Users();
	    	if(userOpt != null)
	    		user = userOpt.get();
	        
	    	//Users user = new Users();   	
	    	//user.setId(accountDto.getId());
	    	user.setUserId(accountDto.getUserId());
	    	user.setFirstName(accountDto.getFirstName());
	    	user.setLastName(accountDto.getLastName());
	    	user.setEmail(accountDto.getEmail());
	    	user.setRoles(new ArrayList<Roles>(Arrays.asList(userRole)));
	    	user.setEnabled(true);
	    	List<String> userGrpList =  accountDto.getGroup();
	    	
	    	//String userGrp = accountDto.getGroup();
        	String stHDGrp = "";
        	String zonalUserGrp = "";
        	for(String userGrp : userGrpList)
        	{
        		if("ROLE_REQ_CR".equals(accountDto.getRole()))
	        	{
	        		//userGrp = 	accountDto.getState()+"_GRP";
	        		stHDGrp =   userGrp+"_HD_GRP";
	        		zonalUserGrp = 	accountDto.getZone()+"_ZONE_GRP";
	        	}else if("ROLE_ST_HEAD".equals(accountDto.getRole()) || "ROLE_BD_MKTG".equals(accountDto.getRole())){
	        		userGrp = 	userGrp+"_HD_GRP";
	        		zonalUserGrp = accountDto.getZone()+"_ZONE_GRP";
	        	}else if("ROLE_ZN_HEAD".equals(accountDto.getRole())){
	        		userGrp = 	accountDto.getZone()+"_ZONE_GRP";
	        		zonalUserGrp = 	accountDto.getZone()+"_ZONE_GRP";
	        	}else if("ROLE_BD_MKT".equals(accountDto.getRole())){
	        		userGrp = 	userGrp+"_MK_GRP";
	        		zonalUserGrp = 	accountDto.getZone()+"_ZONE_GRP";
	        	}
	    	
        	updateUserToGroupIfNotFound(userGrp,accountDto.getUserId(),stHDGrp,zonalUserGrp);
        	}
        	List<String> userCategories=accountDto.getUserCategory();
        	if(userCategories != null && !userCategories.isEmpty())
        	{
        		String userCatStr="";
        		for(String tempStr:userCategories)
        		{
        			userCatStr+=tempStr+",";
        		}
        		if(userCatStr.indexOf(",") >= 0)
        			userCatStr=userCatStr.substring(0, userCatStr.length() - 1);
        		UserDefaultsKey userDefKey=new UserDefaultsKey(user.getId(), "CATEGORY");
        		if (userDefRepo.existsById(userDefKey)) 
        			userDefRepo.deleteById(userDefKey);
        		
        		UserDefaults userDef=new UserDefaults();
		        userDef.setKey("CATEGORY");
		        userDef.setValue(userCatStr);
		        userDef.setUsers(user);
		        userDef.setUserId(user.getId());
		        userDefRepo.save(userDef);
        	}
	        //userRepository.save(user);
	    }

	    @Override
	    public void deleteUser(final Users user) {
	        final VerificationToken verificationToken = tokenRepository.findByUser(user);

	        if (verificationToken != null) {
	            tokenRepository.delete(verificationToken);
	        }

	        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

	        if (passwordToken != null) {
	            passwordTokenRepository.delete(passwordToken);
	        }
	        
	        userDefRepo.deleteUserDefaults(user.getId());
	        groupUsersRepository.deleteUserGroups(user.getUserId());
	        userRepository.delete(user);
 
	    }

	    @Override
	    public void createVerificationTokenForUser(final Users user, final String token) {
	        final VerificationToken myToken = new VerificationToken(token, user);
	        tokenRepository.save(myToken);
	    }

	    @Override
	    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
	        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
	        vToken.updateToken(UUID.randomUUID()
	            .toString());
	        vToken = tokenRepository.save(vToken);
	        return vToken;
	    }

	    @Override
	    public void createPasswordResetTokenForUser(final Users user, final String token) {
	        final PasswordResetToken myToken = new PasswordResetToken(token, user);
	        passwordTokenRepository.save(myToken);
	    }

	    @Override
	    public Users findUserByEmail(final String email) {
	        return userRepository.findByEmail(email);
	    }

	    @Override
	    public PasswordResetToken getPasswordResetToken(final String token) {
	        return passwordTokenRepository.findByToken(token);
	    }

	    @Override
	    public Users getUserByPasswordResetToken(final String token) {
	        return passwordTokenRepository.findByToken(token)
	            .getUser();
	    }

	    
	    @Override
	    public List<WorkGroup_Users> getStateHeadSubGroups(String stateHDGrp) {
	    	
	    	System.out.println("::::stateHDGrp:::::::"+stateHDGrp);
	    	
	        return groupUsersRepository.getStateHeadSubGroups(stateHDGrp);
	    }
	    
	    @Override
	    public List<WorkGroup_Users> getZonalHeadSubGroups(String zonalGrp) {
	    	
	    	System.out.println("::::zonalGrp:::::::"+zonalGrp);
	        return groupUsersRepository.getZonalHeadSubGroups(zonalGrp);
	    }
	    
	    @Override
	    public Optional<Users> getUserByID(final long id) {
	        return userRepository.findById(id);
	    }

	    @Override
	    public void changeUserPassword(final Users user, final String password) {
	        user.setPassword(passwordEncoder.encode(password));
	        userRepository.save(user);
	    }

	    @Override
	    public boolean checkIfValidOldPassword(final Users user, final String oldPassword) {
	        return passwordEncoder.matches(oldPassword, user.getPassword());
	    }

	    @Override
	    public String validateVerificationToken(String token) {
	        final VerificationToken verificationToken = tokenRepository.findByToken(token);
	        if (verificationToken == null) {
	            return TOKEN_INVALID;
	        }

	        final Users user = verificationToken.getUser();
	        final Calendar cal = Calendar.getInstance();
	        if ((verificationToken.getExpiryDate()
	            .getTime()
	            - cal.getTime()
	                .getTime()) <= 0) {
	            tokenRepository.delete(verificationToken);
	            return TOKEN_EXPIRED;
	        }

	        user.setEnabled(true);
	        // tokenRepository.delete(verificationToken);
	        userRepository.save(user);
	        return TOKEN_VALID;
	    }

	    @Override
	    public String generateQRUrl(Users user) throws UnsupportedEncodingException {
	        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), APP_NAME), "UTF-8");
	    }

	    @Override
	    public Users updateUser2FA(boolean use2FA) {
	        final Authentication curAuth = SecurityContextHolder.getContext()
	            .getAuthentication();
	        Users currentUser = (Users) curAuth.getPrincipal();
	     //   currentUser.setUsing2FA(use2FA);
	        currentUser = userRepository.save(currentUser);
	        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
	        SecurityContextHolder.getContext()
	            .setAuthentication(auth);
	        return currentUser;
	    }

	    private boolean emailExists(final String email) {
	        return userRepository.findByEmail(email) != null;
	    }

	    @Override
	    public List<String> getUsersFromSessionRegistry() {
	        return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter((u) -> !sessionRegistry.getAllSessions(u, false)
	                .isEmpty())
	            .map(o -> {
	                if (o instanceof Users) {
	                    return ((Users) o).getEmail();
	                } else {
	                    return o.toString();
	                }
	            })
	            .collect(Collectors.toList());

	    }

		@Override
		public Users findUserByUserId(String userId) {

			
			System.out.println(":findUserByUserId:::userId:::::::"+userId);
			
			System.out.println(":return userRepository:::findByUserId:::::::"+userRepository.findByUserId(userId));
			
			return userRepository.findByUserId(userId);
		
		
		}

		@Override
		public Users findUserByUserIdOrEmail(String userId, String email) {
			// TODO Auto-generated method stub
			return userRepository.findByUserIdOrEmail(userId,email);
		}
		
		 @Transactional
		    private final Privilages createPrivilegeIfNotFound(final String name) {
		    	Privilages privilege = privilegeRepository.findByName(name);
		        if (privilege == null) {
		            privilege = new Privilages(name);
		            privilege = privilegeRepository.save(privilege);
		        }
		        return privilege;
		    }
		 
		 @Transactional
		    private final Roles createRoleIfNotFound(final String name, final Collection<Privilages> privileges) {
		        Roles role = roleRepository.findByName(name);
		        if (role == null) {
		            role = new Roles(name);
		        }
		        role.setPrivileges(privileges);
		        role = roleRepository.save(role);
		        return role;
		    }
		 
		 @Transactional
		    private void createGroupIfNotFound(final String name,String desc,String role) {
			 Work_Groups groups = groupRepository.findByGroup(name);
			 
		        if (groups == null) {
		        	groups = new Work_Groups(name,desc,role);
		        }
		        groups = groupRepository.save(groups);
		        System.out.println("::::getId:::::::::;"+groups.getId());
		        System.out.println("::::Name:::::::::;"+groups.getName());
		        System.out.println("::::Desc:::::::::;"+groups.getDesc());
		        //return groups;
		    }
		 
		 @Transactional
		    private void insertUserToGroupIfNotFound(final String groupId,final String userId,final String stateGrp,final String zonalGrp,final String ext1,final String ext2) {
			 WorkGroup_Users groupUsers = groupUsersRepository.findByUserANDGroup(groupId,userId);
			 
		        if (groupUsers == null) {
		        	groupUsers = new WorkGroup_Users(groupId,userId,stateGrp,zonalGrp,ext1,ext2);
		        }
		        groupUsers = groupUsersRepository.save(groupUsers);
		        
		        //System.out.println("::::getId:::::::::;"+groups.getId());
		       // System.out.println("::::Name:::::::::;"+groups.getName());
		      //  System.out.println("::::Desc:::::::::;"+groups.getDesc());
		        //return groups;
		    }
		 
		 
		 @Transactional
		    private void updateUserToGroupIfNotFound(final String groupId,final String userId,final String stateGrp,final String zonalGrp) {
			 	groupUsersRepository.updateUserGroups(groupId,userId,stateGrp,zonalGrp);
		    }

		@Override
		public List<Users> findUsersByRole(String role) {
			return userRepository.findUsersByRole(role);
		}

		@Override
		public Work_Groups findGroupByRoleAndGroup(String role, String group) {
			
			List<Work_Groups> groupList=groupRepository.findByRoleAndGroup(role,group); 
			if(groupList == null || groupList.isEmpty())
				return null;
			else
				return groupList.get(0);
		}

		@Override
		public Work_Groups saveWorkGroup(Work_Groups workGrp) {
			return groupRepository.save(workGrp);
		}

		@Override
		public List<WorkGroup_Users> getWorkGrpUsers() {
			// TODO Auto-generated method stub
			return groupUsersRepository.findAll();
		}
}
