package com.ezc.hsil.webapp.model;

import java.io.Serializable;

public class UserDefaultsKey implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public UserDefaultsKey() {
		
	}
	private long userId;
	private String key;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDefaultsKey other = (UserDefaultsKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	public UserDefaultsKey(long userId, String key) {
		super();
		this.userId = userId;
		this.key = key;
	}
			
	
	}
