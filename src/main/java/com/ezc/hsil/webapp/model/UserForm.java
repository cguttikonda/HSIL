package com.ezc.hsil.webapp.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm extends Auditable<String> {
	
	private Long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String stateCode;
	private String role;
	private String zone;
	private List<String> group;
	
	
	@NotNull
	public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public UserForm() {
    }

	@NotNull
	@Size(min=2, max=10)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@NotNull
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	@NotNull
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}		
}
