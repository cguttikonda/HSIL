package com.ezc.hsil.webapp.model;
// Generated Sep 10, 2019 2:12:43 PM by Hibernate Tools 5.2.10.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * EzcRequestItems generated by hbm2java
 */
@Entity
@Table(name = "ezc_request_items", catalog = "hsil")
public class EzcRequestItems implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private EzcRequestHeader ezcRequestHeader;
	private String eriPartType;
	private String eriPartName;
	private String eriPlumberName;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date eriDob;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date eriDoa;
	private String eriContact;
	private String eriDealer;
	private String erilocation;
	private String eriGift;
	private String eriProfDet;
	private Integer eriDealerId;
	private String eriQuantity;



	public EzcRequestItems() {
	}

	public EzcRequestItems(EzcRequestHeader ezcRequestHeader, String eriPartType, String eriPartName,
			String eriPlumberName, Date eriDob, Date eriDoa, String eriContact,String eriDealer,String eriGift,String erilocation,String eriProfDet) {
		this.ezcRequestHeader = ezcRequestHeader;
		this.eriPartType = eriPartType;
		this.eriPartName = eriPartName;
		this.eriPlumberName = eriPlumberName;
		this.eriDob = eriDob;
		this.eriDoa = eriDoa;
		this.eriContact = eriContact;
		this.eriDealer = eriDealer;
		this.erilocation = erilocation;
		this.eriGift = eriGift;
		this.eriGift = eriProfDet;
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
 
	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ERI_REQ_ID")
	//@Cascade({CascadeType.SAVE_UPDATE})
	public EzcRequestHeader getEzcRequestHeader() {
		return this.ezcRequestHeader;
	}

	public void setEzcRequestHeader(EzcRequestHeader ezcRequestHeader) {
		this.ezcRequestHeader = ezcRequestHeader;
	}

	@Column(name = "ERI_PART_TYPE", length = 10)
	public String getEriPartType() {
		return this.eriPartType;
	}

	public void setEriPartType(String eriPartType) {
		this.eriPartType = eriPartType;
	}

	@Column(name = "ERI_PART_NAME", length = 45)
	public String getEriPartName() {
		return this.eriPartName;
	}

	public void setEriPartName(String eriPartName) {
		this.eriPartName = eriPartName;
	}

	@Column(name = "ERI_PLUMBER_NAME", length = 45)
	public String getEriPlumberName() {
		return this.eriPlumberName;
	}

	public void setEriPlumberName(String eriPlumberName) {
		this.eriPlumberName = eriPlumberName;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "ERI_DOB", length = 19)
	public Date getEriDob() {
		return this.eriDob;
	}

	public void setEriDob(Date eriDob) {
		this.eriDob = eriDob;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "ERI_DOA", length = 19)
	public Date getEriDoa() {
		return this.eriDoa;
	}

	public void setEriDoa(Date eriDoa) {
		this.eriDoa = eriDoa;
	}

	
	
	
	@Column(name = "ERI_CONTACT", length = 10)
	public String getEriContact() {
		return this.eriContact;
	}

	public void setEriContact(String eriContact) {
		this.eriContact = eriContact;
	}
	
	@Column(name = "ERI_DEALER_NAME", length = 50)
	public String getEriDealer() {
		return eriDealer;
	}

	public void setEriDealer(String eriDealer) {
		this.eriDealer = eriDealer;
	}

	@Column(name = "ERI_LOCATION", length = 100)
	public String getErilocation() {
		return erilocation;
	}

	public void setErilocation(String erilocation) {
		this.erilocation = erilocation;
	}

	@Column(name = "ERI_GIFT", length = 100)
	public String getEriGift() {
		return eriGift;
	}

	public void setEriGift(String eriGift) {
		this.eriGift = eriGift;
	}

	@Column(name = "ERI_PROF_DET", length = 500)
	public String getEriProfDet() {
		return eriProfDet;
	}

	public void setEriProfDet(String eriProfDet) {
		this.eriProfDet = eriProfDet;
	}

	@Column(name = "ERI_DEALER_ID", length = 11)
	public Integer getEriDealerId() {
		return eriDealerId;
	}

	public void setEriDealerId(Integer eriDealerId) {
		this.eriDealerId = eriDealerId;
	}
	@Column(name = "ERI_QUANTITY", length = 10)
	public String getEriQuantity() {
		return eriQuantity;
	}

	public void setEriQuantity(String eriQuantity) {
		this.eriQuantity = eriQuantity;
	}

	
	
}
