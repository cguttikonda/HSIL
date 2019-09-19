package com.ezc.hsil.webapp.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.model.Privilages;
import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.UserRepository;



@Service("userDetailsService")
@Transactional
public class CustomUserServiceImpl implements UserDetailsService  {

	
	  @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private LoginAttemptService loginAttemptService;

	    @Autowired
	    private HttpServletRequest request;

	    public CustomUserServiceImpl() {
	        super();
	    }

	    // API

	    @Override
	    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
	        final String ip = getClientIP();
	        if (loginAttemptService.isBlocked(ip)) {
	            throw new RuntimeException("blocked");
	        }

	        try {
	            final Users user = userRepository.findByUserId(userId);
	        	
	        	
	            if (user == null) {
	                throw new UsernameNotFoundException("No user found with username: " + userId);
	            }

//	            user.getEmail()
	            return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user)); //user.getRoles()
	        } catch (final Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    
	    
	    
	    
	    //	    @Override
//	    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//	        final String ip = getClientIP();
//	        if (loginAttemptService.isBlocked(ip)) {
//	            throw new RuntimeException("blocked");
//	        }
//
//	        try {
//	            final Users user = userRepository.findByUserId(email);
//	            if (user == null) {
//	                throw new UsernameNotFoundException("No user found with username: " + email);
//	            }
//
////	            user.getEmail()
//	            return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
//	        } catch (final Exception e) {
//	            throw new RuntimeException(e);
//	        }
//	    }

	    // UTIL

	    private static Collection<? extends GrantedAuthority> getAuthorities(Users user)
		{
			Set<String> roleAndPermissions = new HashSet<>();
			List<Roles> roles = (List<Roles>) user.getRoles();
			
			for (Roles role : roles)
			{
				roleAndPermissions.add(role.getName());
			}
			String[] roleNames = new String[roleAndPermissions.size()];
			Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleAndPermissions.toArray(roleNames));
			return authorities;
		}
	    
	    
	    
	    @SuppressWarnings("unused")
		private final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Roles> roles) {
	        return getGrantedAuthorities(getPrivileges(roles));
	    }

	    private final List<String> getPrivileges(final Collection<Roles> roles) {
	        final List<String> privileges = new ArrayList<String>();
	        final List<Privilages> collection = new ArrayList<Privilages>();
	        for (final Roles role : roles) {
	            collection.addAll(role.getPrivileges());
	        }
	        for (final Privilages item : collection) {
	            privileges.add(item.getName());
	        }
	        	
	        return privileges;
	    }

	    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
	        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	        for (final String privilege : privileges) {
	            authorities.add(new SimpleGrantedAuthority(privilege));
	        }
	        return authorities;
	    }

	    private final String getClientIP() {
	        final String xfHeader = request.getHeader("X-Forwarded-For");
	        if (xfHeader == null) {
	            return request.getRemoteAddr();
	        }
	        return xfHeader.split(",")[0];
	    }

	
	
	
}
