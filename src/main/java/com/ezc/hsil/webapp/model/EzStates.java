package com.ezc.hsil.webapp.model;

public class EzStates { 
	    private String code;
	    private String desc;
	    private String role;
	 
	    public EzStates() {
	 
	    }
	    
	    public EzStates(String code, String desc,String role) {
	        super();
	        this.code = code;
	        this.desc = desc;
	        this.role = role;
	    }

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	}