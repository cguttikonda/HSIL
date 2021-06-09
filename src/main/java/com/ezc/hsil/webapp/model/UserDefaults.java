package com.ezc.hsil.webapp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="EZC_USER_DEFAULTS",catalog="hsil")
@Data
@AllArgsConstructor
@IdClass(UserDefaultsKey.class)
public class UserDefaults implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EUD_USER_ID",length=20)
	private long userId;
	
	@Id
	@Column(name="EUD_KEY",length=10)
	private String key;
	
	@ManyToOne
    @JoinColumns({
            @JoinColumn(name="EUD_USER_ID", referencedColumnName="id",insertable = false,updatable = false),
    })
    
    private Users users;
	
	
	@Column(name="EUD_VALUE",length=100)
	private String value;
		
	public UserDefaults() {
		
		
	}
		
}
