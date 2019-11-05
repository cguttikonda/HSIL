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
	
	//@NonNull
	//@Column(name="ERM_ENTERED_BY",length=20)
	//private String enteredBy;
	
	//@NonNull
	//@Column(name="ERM_ENTERED_ON")
	//private Timestamp enteredOn;
	
	//@NonNull
	//@Column(name="ERM_MODIFIED_BY",length=20)
	//private String modifiedBy;
	
	//@NonNull
	//@Column(name="ERM_MODIFIED_ON")
	//private Timestamp modifiedOn;
	
	public RequestMaterials() {
		
		
	}
		
}
