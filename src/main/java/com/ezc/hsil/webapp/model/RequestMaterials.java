package com.ezc.hsil.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name="EZC_REQUEST_MATERIALS",catalog="hsil")
@Data
@AllArgsConstructor
public class RequestMaterials  extends Auditable<String> {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	
	@NonNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ERM_REQ_ID")
	private EzcRequestHeader ezcRequestHeader;
	
	@NonNull
	@Column(name="ERM_MAT_CODE",length=20)
	private String matCode;
	
	@NonNull
	@Column(name="ERM_MAT_DESC",length=50)
	private String matDesc;
	
	@NonNull
	@Column(name="ERM_APPR_QTY",length=5)
	private int apprQty;
	
	@Column(name="ERM_USED_QTY",length=5)
	private int usedQty;
	
	@Column(name="ERM_LEFTOVER_QTY",length=5)
	private int leftOverQty;
	
	@Column(name="ERM_FREE_QTY",length=5)
	private int freeQty;
	
	@Column(name="ERM_IS_NEW",length=1)
	private char isNew;
	
	@Column(name="ERM_ALLOC_ID")
	private int allocId;
	 
	
	
	public RequestMaterials() {
		
		
	}
		
}
