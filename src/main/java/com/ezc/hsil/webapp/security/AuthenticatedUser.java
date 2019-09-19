package com.ezc.hsil.webapp.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.Users;

//@Configuration
public class AuthenticatedUser extends org.springframework.security.core.userdetails.User
{

	private static final long serialVersionUID = 1L;
	private Users user;
	
	public AuthenticatedUser(Users user)
	{
		super(user.getEmail(), user.getPassword(), getAuthorities(user));
		this.user = user;
	}
	
	public Users getUser()
	{
		return user;
	}
	
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
}
