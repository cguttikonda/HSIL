package com.ezc.hsil.webapp.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name="EZC_NULLIFIED_STOCK",catalog="hsil")
@Data
public class EzNullifiedStock extends Auditable<String>{

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	

	@Column(name="ENS_LEFTOVER_ID")
	private Integer leftOverId;
	

	@Column(name="ENS_MATERIAL",length=20)
	private String material;
	
	@Column(name="ENS_USER_ID",length=20)
	private String userId;
	
	@Column(name="ENS_NULLIFIED_QTY")
	private Integer qty;
	
	@Column(name="ENS_REASON",length=30)
	private String reason;
	
	@Column(name="ENS_COMMENTS",length=500)
	private String comments;
	
	public EzNullifiedStock() {
		 
		
	}
		
}
