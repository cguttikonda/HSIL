package com.ezc.hsil.webapp.model;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.MaterialDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@SqlResultSetMapping(
	    name="MaterialDtoMapping",
	    classes={ 
	        @ConstructorResult(
	            targetClass=MaterialDto.class,
	            columns={
	            	@ColumnResult(name="EMM_MAT_CODE"),
	                @ColumnResult(name="EMM_MAT_DESC"),
	                @ColumnResult(name="EMM_MAT_QUANTITY"),
	                @ColumnResult(name="EMM_MAT_ISACTIVE")
	            }
	        )
	    }
	)

@NamedNativeQuery(name="MaterialMaster.materialDetails", query="SELECT * FROM EZC_MATERIAL_MASTER mm " 
							+  " WHERE mm.EMM_MAT_CODE = :materialCode and mm.emm_mat_isactive='Y'",  
							resultSetMapping="MaterialDtoMapping")



@Entity
@Table(name="EZC_MATERIAL_MASTER",catalog="hsil")
@Data
@AllArgsConstructor
public class MaterialMaster {

	
	@Id
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
