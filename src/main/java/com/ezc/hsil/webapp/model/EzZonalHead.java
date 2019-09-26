package com.ezc.hsil.webapp.model;

public class EzZonalHead {
	    private String zonalHDCode;
	    private String zonalHDDesc;
	    private String role;
	 
	    public EzZonalHead() {
	 
	    }
	    
	    public EzZonalHead(String zonalHDCode, String zonalHDDesc,String role) {
	        super();
	        this.zonalHDCode = zonalHDCode;
	        this.zonalHDDesc = zonalHDDesc;
	        this.role = role;
	    }

		public String getZonalHDCode() {
			return zonalHDCode;
		}

		public void setZonalHDCode(String zonalHDCode) {
			this.zonalHDCode = zonalHDCode;
		}

		public String getZonalHDDesc() {
			return zonalHDDesc;
		}

		public void setZonalHDDesc(String zonalHDDesc) {
			this.zonalHDDesc = zonalHDDesc;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
		

	}
