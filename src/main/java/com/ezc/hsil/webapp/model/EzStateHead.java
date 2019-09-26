package com.ezc.hsil.webapp.model;

public class EzStateHead {
	    private String stateHDCode;
	    private String stateHDDesc;
	    private String role;
	 
	    public EzStateHead() {
	 
	    }
	    
	    public EzStateHead(String stateHDCode, String stateHDDesc,String role) {
	        super();
	        this.stateHDCode = stateHDCode;
	        this.stateHDDesc = stateHDDesc;
	        this.role = role;
	    }

		public String getStateHDCode() {
			return stateHDCode;
		}

		public void setStateHDCode(String stateHDCode) {
			this.stateHDCode = stateHDCode;
		}

		public String getStateHDDesc() {
			return stateHDDesc;
		}

		public void setStateHDDesc(String stateHDDesc) {
			this.stateHDDesc = stateHDDesc;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		
	}
