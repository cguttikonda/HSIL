package com.ezc.hsil.webapp.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="EZC_MKT_GIVE_AWAY",catalog="hsil")
@Data
@AllArgsConstructor
public class EzcMktGiveAway implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="EMGA_VEH_NO",length=40)
	private String vehNo;
	
	@Column(name="EMGA_PURPOSE",length=100)
	private String purpose;
	
	@Column(name="EMGA_SENT_TO",length=20)
	private String sentTo;
	
	@Column(name="EMGA_NAME",length=40)
	private String sentToName;
	
	@Column(name="EMGA_DISTRIBUTOR",length=40)
	private String distrubutor;
	
	@Column(name="EMGA_DIST_NAME",length=50)
	private String distName;
	
	@Column(name="EMGA_CREATED_BY",length=20)
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "EMGA_CREATED_ON", length = 19)
	private Date createdOn;
	
	@Column(name="EMGA_MODIFIED_BY",length=20)
	private String modifiedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "EMGA_MODIFIED_ON", length = 19)
	private Date modifiedOn;
	
	@Column(name="EMGA_MAT_CODE",length=20)
	private String matCode;
	 
	@Column(name="EMGA_MAT_DESC",length=50)
	private String matDesc;
	
	@Column(name="EMGA_QTY")
	private int qty;
	
	@Column(name="EMGA_STATUS",length=4)
	private String status; 
	
	@Column(name="EMGA_PRD_INV")
	private Double prdInv; 
	
	@Column(name="EMGA_VERTICAL",length=20)
	private String vertical;
	
	@Column(name="EMGA_ACK_COMMENTS",length=1000)
	private String ackComments;
	
	public EzcMktGiveAway() {
	}

	
	

}
