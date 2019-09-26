package com.ezc.hsil.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name="EZC_MATERIAL_MASTER",catalog="hsil")
@Data
@AllArgsConstructor
public class MaterialMaster {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NonNull
	@Column(name="EMM_MAT_CODE",length=20)
	private String materialCode;
	@NonNull
	@Column(name="EMM_MAT_DESC",length=50)
	private String materialDesc;
	@NonNull
	@Column(name="EMM_MAT_QUANTITY",length=5)
	private int quantity;
	@NonNull
	@Column(name="EMM_MAT_ISACTIVE",length=2)
	private String isActive;
	
	public MaterialMaster() {
		
		
	}
		
}
