package com.ezc.hsil.webapp.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name="EZC_PLACE_MASTER",catalog="hsil")
@Data
public class EzPlaceMaster{

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "EPM_ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="EPM_CITY",length=40)
	private String city;
	
	@Column(name="EPM_STATE",length=40)
	private String state;
	
	@Column(name="EPM_COUNTRY",length=40)
	private String country;
		
	public EzPlaceMaster() {
		 
		
	}
		
}
