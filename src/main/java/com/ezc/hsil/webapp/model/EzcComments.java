package com.ezc.hsil.webapp.model;
//Generated Sep 10, 2019 2:12:43 PM by Hibernate Tools 5.2.10.Final
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
@Table(name="EZC_COMMENTS",catalog="hsil")
@Data
@AllArgsConstructor
public class EzcComments extends Auditable<String> {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	
	@NonNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EC_REQ_ID")
	private EzcRequestHeader ezcRequestHeader;
	
	
	@Column(name="EC_COMMENTS",length=500)
	private String comments;
	
	@Column(name="EC_TYPE",length=10)
	private String type;
	
	

	
	public EzcComments() {
	}

	
	

}
