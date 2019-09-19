package com.ezc.hsil.webapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name="EZC_DISTRIBUTORS", catalog="hsil")
@Data
//@NoArgsConstructor
public class DistributorMaster { 

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NonNull
	private String name;
	@NonNull
	private String contact;
	@NonNull
	private String organisation;
	@NonNull
	private String city;
	
	
	public DistributorMaster() {

	}
	
	
	
	
	
	
}
