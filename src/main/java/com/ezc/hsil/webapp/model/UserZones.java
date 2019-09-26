package com.ezc.hsil.webapp.model;

public class UserZones {
    private String zonalId;
    private String zonalName;
    
    public UserZones(){}
    
    public UserZones(String zonalId, String zonalName) {
        super();
        this.zonalId = zonalId;
        this.zonalName = zonalName;
    }
    
	public String getZonalId() {
		return zonalId;
	}
	public void setZonalId(String zonalId) {
		this.zonalId = zonalId;
	}
	public String getZonalName() {
		return zonalName;
	}
	public void setZonalName(String zonalName) {
		this.zonalName = zonalName;
	}

    
}
