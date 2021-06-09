package com.ezc.hsil.webapp.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;



@Entity
@Table(name="EZC_STORES",catalog="hsil")
@Data
@AllArgsConstructor
public class EzStores{

	
	@Id
	@Column(name = "ES_LOCATION_ID", unique = true, nullable = false)
	private String locationId;
	
	@Column(name="ES_LOCATION_NAME",length=40)
	private String locationName;
	
	@Column(name="ES_LOCATION_ADDR",length=100)
	private String address;
	
	
	public EzStores() {
		 
		
	}
		
}
