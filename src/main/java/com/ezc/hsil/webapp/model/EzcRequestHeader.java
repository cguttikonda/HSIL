package com.ezc.hsil.webapp.model;
// Generated Sep 10, 2019 2:12:43 PM by Hibernate Tools 5.2.10.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * EzcRequestHeader generated by hbm2java
 */
@Entity 
@Table(name = "ezc_request_header", catalog = "hsil")
//@SecondaryTable(name = "EZC_USERS", pkJoinColumns=@PrimaryKeyJoinColumn(name="userId", referencedColumnName="erhRequestedBy"))
public class EzcRequestHeader implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String erhReqType;
	private String erhCreatedGroup;
	private String erhState;
	private String erhRequestedBy;
	private Date erhRequestedOn;
	private String erhStatus;
	private Date erhConductedOn;
	private String erhModifiedBy;
	private Date erhModifiedOn;
	private Integer erhNoOfAttendee;
	private String erhDistrubutor; 
	private String erhCity;
	private String erhPurpose;
	private String erhDistName;
	private String erhReqName;

	private String erhVenue;
	private String erhInstructions;
	private Double erhCostIncured;
	private Character erhDispatchFlag;
	private Date erhDispDate;
	private Set<EzcRequestItems> ezcRequestItems = new HashSet<EzcRequestItems>(0);
	private Set<EzcRetailerSales> ezcRetailerSales = new HashSet<EzcRetailerSales>(0);
	private Set<EzcRequestDealers> ezcRequestDealers = new HashSet<EzcRequestDealers>(0);
	private Set<RequestMaterials> requestMaterials = new HashSet<RequestMaterials>(0);
	private Set<EzcComments> ezcComments = new HashSet<EzcComments>(0);
	


	 
	public EzcRequestHeader() {
	}

	public EzcRequestHeader(String id) {
		this.id = id;
	}

	public EzcRequestHeader( String erhReqType, String erhCreatedGroup, String erhState, String erhRequestedBy,
			Date erhRequestedOn, String erhStatus, Date erhConductedOn, String erhModifiedBy, Date erhModifiedOn,
			Integer erhNoOfAttendee, Set<EzcRequestItems> ezcRequestItemses,
			Set<EzcRetailerSales> ezcRetailerSaleses,
			Set<EzcRequestDealers> ezcRequestDealersess) {
		//this.id = id;
		this.erhReqType = erhReqType;
		this.erhCreatedGroup = erhCreatedGroup;
		this.erhState = erhState;
		this.erhRequestedBy = erhRequestedBy;
		this.erhRequestedOn = erhRequestedOn;
		this.erhStatus = erhStatus;
		this.erhConductedOn = erhConductedOn;
		this.erhModifiedBy = erhModifiedBy;
		this.erhModifiedOn = erhModifiedOn;
		this.erhNoOfAttendee = erhNoOfAttendee;
		this.ezcRequestItems = ezcRequestItemses;
		this.ezcRetailerSales = ezcRetailerSaleses;
		this.ezcRequestDealers = ezcRequestDealersess;
		
	}

	@Id
	//@GeneratedValue(strategy = IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tpm_seq")
    @GenericGenerator(
        name = "tpm_seq",  
        strategy = "com.ezc.hsil.webapp.model.TpmSequenceGenerator", 
        parameters = {
	            @Parameter(name = TpmSequenceGenerator.INCREMENT_PARAM, value = "1")
	            //@Parameter(name = TpmSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "TPM-"),
            })
	@Column(name = "id", unique = true, nullable = false,length=20)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id; 
	}
	@Column(name = "ERH_DISPATCH_FLAG")
	public Character getErhDispatchFlag() {
		return erhDispatchFlag; 
	}

	public void setErhDispatchFlag(Character erhDispatchFlag) {
		this.erhDispatchFlag = erhDispatchFlag;
	}

	@Column(name = "ERH_REQ_TYPE", length = 5)
	public String getErhReqType() {
		return this.erhReqType;
	}

	public void setErhReqType(String erhReqType) {
		this.erhReqType = erhReqType;
	}

	@Column(name = "ERH_CREATED_GROUP", length = 10)
	public String getErhCreatedGroup() {
		return this.erhCreatedGroup;
	}

	public void setErhCreatedGroup(String erhCreatedGroup) {
		this.erhCreatedGroup = erhCreatedGroup;
	}

	@Column(name = "ERH_STATE", length = 10)
	public String getErhState() {
		return this.erhState;
	}

	public void setErhState(String erhState) {
		this.erhState = erhState;
	}

	@Column(name = "ERH_REQUESTED_BY", length = 20)
	public String getErhRequestedBy() {
		return this.erhRequestedBy;
	}

	public void setErhRequestedBy(String erhRequestedBy) {
		this.erhRequestedBy = erhRequestedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ERH_REQUESTED_ON", length = 19)
	public Date getErhRequestedOn() {
		return this.erhRequestedOn;
	}

	public void setErhRequestedOn(Date erhRequestedOn) {
		this.erhRequestedOn = erhRequestedOn;
	}

	@Column(name = "ERH_STATUS", length = 10)
	public String getErhStatus() {
		return this.erhStatus;
	}

	public void setErhStatus(String erhStatus) {
		this.erhStatus = erhStatus;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name = "ERH_CONDUCTED_ON", length = 19)
	public Date getErhConductedOn() { 
		return this.erhConductedOn;
	}

	public void setErhConductedOn(Date erhConductedOn) {
		this.erhConductedOn = erhConductedOn;
	}

	@Column(name = "ERH_MODIFIED_BY", length = 15)
	public String getErhModifiedBy() {
		return this.erhModifiedBy;
	}

	public void setErhModifiedBy(String erhModifiedBy) {
		this.erhModifiedBy = erhModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ERH_MODIFIED_ON", length = 19)
	public Date getErhModifiedOn() {
		return this.erhModifiedOn;
	}

	public void setErhModifiedOn(Date erhModifiedOn) {
		this.erhModifiedOn = erhModifiedOn;
	}

	@Column(name = "ERH_NO_OF_ATTENDEE")
	public Integer getErhNoOfAttendee() {
		return this.erhNoOfAttendee;
	}

	public void setErhNoOfAttendee(Integer erhNoOfAttendee) {
		this.erhNoOfAttendee = erhNoOfAttendee;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ezcRequestHeader") 
	@Cascade({CascadeType.ALL})
	public Set<EzcRequestItems> getEzcRequestItems() {
		return this.ezcRequestItems;
	}

	public void setEzcRequestItems(Set<EzcRequestItems> ezcRequestItemses) {
		this.ezcRequestItems = ezcRequestItemses;
	}
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ezcRequestHeader")
	@Cascade({CascadeType.ALL})
	public Set<EzcRetailerSales> getEzcRetailerSales() {
		return this.ezcRetailerSales;
	}

	public void setEzcRetailerSales(Set<EzcRetailerSales> ezcRetailerSaleses) {
		this.ezcRetailerSales = ezcRetailerSaleses;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ezcRequestHeader")
	@Cascade({CascadeType.ALL})
	public Set<EzcRequestDealers> getEzcRequestDealers() {
		return this.ezcRequestDealers;
	}

	public void setEzcRequestDealers(Set<EzcRequestDealers> ezcRequestDealerses) {
		this.ezcRequestDealers = ezcRequestDealerses;
	}

	@Column(name = "ERH_DISTRIBUTOR", length = 40)
	public String getErhDistrubutor() {
		return erhDistrubutor;
	}

	public void setErhDistrubutor(String erhDistrubutor) {
		this.erhDistrubutor = erhDistrubutor;
	}

	@Column(name = "ERH_CITY", length = 40)
	public String getErhCity() {
		return erhCity;
	}

	public void setErhCity(String erhCity) {
		this.erhCity = erhCity;
	}

	@Column(name = "ERH_INST", length = 500)
	public String getErhInstructions() {
		return erhInstructions;
	}

	public void setErhInstructions(String erhInstructions) {
		this.erhInstructions = erhInstructions;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ezcRequestHeader") 
	@Cascade({CascadeType.ALL})
	public Set<RequestMaterials> getRequestMaterials() {
		return requestMaterials;
	}

	public void setRequestMaterials(Set<RequestMaterials> requestMaterials) {
		this.requestMaterials = requestMaterials; 
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ezcRequestHeader") 
	@Cascade({CascadeType.ALL})
	public Set<EzcComments> getEzcComments() {
		return ezcComments;
	}

	public void setEzcComments(Set<EzcComments> ezcComments) {
		this.ezcComments = ezcComments;
	}

	@Column(name = "ERH_COST_INCURED")
	public Double getErhCostIncured() {
		return erhCostIncured;
	}

	public void setErhCostIncured(Double erhCostIncured) {
		this.erhCostIncured = erhCostIncured;
	}
	
	@Column(name = "ERH_VENUE",length=50)
	public String getErhVenue() {
		return erhVenue;
	}

	public void setErhVenue(String erhVenue) {
		this.erhVenue = erhVenue;
	}

	@Column(name = "ERH_PURPOSE",length=100)
	public String getErhPurpose() {
		return erhPurpose;
	}

	public void setErhPurpose(String erhPurpose) {
		this.erhPurpose = erhPurpose;
	}

	@Column(name = "ERH_DIST_NAME",length=50)
	public String getErhDistName() {
		return erhDistName;
	}

	public void setErhDistName(String erhDistName) {
		this.erhDistName = erhDistName;
	}

	@Column(name = "ERH_REQ_NAME",length=50)
	public String getErhReqName() { 
		return erhReqName;
	}

	public void setErhReqName(String erhReqName) {
		this.erhReqName = erhReqName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ERH_DISP_DATE")
	public Date getErhDispDate() {
		return erhDispDate;
	}

	public void setErhDispDate(Date erhDispDate) {
		this.erhDispDate = erhDispDate;
	}
	

	
	
	

}
