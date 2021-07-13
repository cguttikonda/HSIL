package com.ezc.hsil.webapp.model;
//Generated Sep 10, 2019 2:12:43 PM by Hibernate Tools 5.2.10.Final
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="EZC_DOC_COMMENTS",catalog="hsil")
@Data
@AllArgsConstructor
public class EzDocComments extends Auditable<String> {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	
	@Column(name="EDC_COMMENTS",length=500)
	private String comments;
	
	@Column(name="EDC_TYPE",length=10)
	private String type;
	
	

	
	public EzDocComments() {
	}

	
	

}
