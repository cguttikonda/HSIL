package com.ezc.hsil.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="EZC_USERS")
public class Users extends Auditable<String> {


	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="EMAIL")
	private String email;
	
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="IS_ENABLED")
	private boolean enabled;

	@OneToMany(mappedBy="users",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<UserDefaults> userDefaults;	
	
	 @ManyToMany(fetch = FetchType.EAGER)
	 @JoinTable(name = "users_roles", joinColumns = 
	 			@JoinColumn(name = "user_id", referencedColumnName = "id"), 
	 			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	 )
	 private Collection<Roles> roles;
	
	
		public Users() {
	    }

	public Collection<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Roles> roles) {
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
	    this.firstName = (firstName != null) ? firstName.trim() : null;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = (lastName != null) ? lastName.trim() : null;
	}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<UserDefaults> getUserDefaults() {
		return userDefaults;
	}

	public void setUserDefaults(Set<UserDefaults> userDefaults) {
		this.userDefaults = userDefaults;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", password=" + password + ", enabled=" + enabled + "]";
	}

	public Users(String userId, String firstName, String lastName, String email, String password, boolean enabled) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}
	
	public List<String> getUserCategories()
	{
		Set<UserDefaults> userDefSet=this.getUserDefaults();
		List<String> userDefList=new ArrayList<String>(); 
		if(userDefSet != null)
		{
			Optional<UserDefaults> userCat = userDefSet.stream().filter(p -> p.getKey().equals("CATEGORY")).findFirst();
			if(userCat.isPresent())
			{
				userDefList =  Arrays.asList(userCat.get().getValue().split(","));
			}
		}
		return userDefList;
	}
}
