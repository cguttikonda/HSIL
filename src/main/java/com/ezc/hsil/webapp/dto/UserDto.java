package com.ezc.hsil.webapp.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ezc.hsil.webapp.validation.PasswordMatches;
import com.ezc.hsil.webapp.validation.ValidEmail;
import com.ezc.hsil.webapp.validation.ValidPassword;


@PasswordMatches
public class UserDto {
	
	@NotNull
    @Size(min = 1)
    private Long id;
	
    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String lastName;
    
    @NotNull
    @Size(min = 1, message = "{Size.userDto.userId}")
    private String userId;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private boolean isUsing2FA;
    
    private boolean enabled;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

/*    private Integer role;

    public Integer getRole() {
        return role;
    }
     public void setRole(final Integer role) {
        this.role = role;
    }
*/
    
    private String state;
    private String zone;
    private List<String> group;
    
    
    
    
    public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private String role;

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }
   

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }

    
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [firstName=").append(firstName).append(", lastName=").append(lastName).append(", password=").append(password).append(", matchingPassword=").append(matchingPassword).append(", userId=").append(userId).append(", email=").append(email).append(", isEnabled=").append(enabled).append(", isUsing2FA=")
                .append(isUsing2FA).append(", role=").append(role).append("]");
        return builder.toString();
    }

}
