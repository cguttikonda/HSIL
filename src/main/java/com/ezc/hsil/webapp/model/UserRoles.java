package com.ezc.hsil.webapp.model;

public class UserRoles {
    private String roleId;
    private String roleDesc;
    
    public UserRoles(){}
    
    public UserRoles(String roleId, String roleDesc) {
        super();
        this.roleId = roleId;
        this.roleDesc = roleDesc;
    }
    
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
    
}
