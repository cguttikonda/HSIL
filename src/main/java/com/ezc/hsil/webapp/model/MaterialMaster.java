package com.ezc.hsil.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.ezc.hsil.webapp.dto.MaterialDto;

import lombok.AllArgsConstructor;
import lombok.Data;
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
	                @ColumnResult(name="EMM_MAT_ISACTIVE"),
	                @ColumnResult(name="EMM_BLOCK_QTY"),
	                @ColumnResult(name="EMM_CREATED_ON"),
	                @ColumnResult(name="EMM_MODIFIED_ON"),
	                @ColumnResult(name="EMM_STOCK_LOC"),
	            }
	        )
	    }
	)

@NamedNativeQuery(name="MaterialMaster.materialDetails", query="SELECT * FROM EZC_MATERIAL_MASTER mm " 
							+  " WHERE mm.EMM_MAT_CODE = :materialCode and mm.EMM_STOCK_LOC = :stockLoc and mm.emm_mat_isactive='Y'",  
							resultSetMapping="MaterialDtoMapping")



@Entity
@Table(name="EZC_MATERIAL_MASTER",catalog="hsil")
@Data
@AllArgsConstructor
@IdClass(MaterialMasterKey.class)
public class MaterialMaster implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NonNull
	@Column(name="EMM_MAT_CODE",length=20)
	private String materialCode;
	
	@Id
	@NonNull
	@Column(name="EMM_STOCK_LOC",length=5)
	private String stockLoc;
	
	@NonNull
	@Column(name="EMM_MAT_DESC",length=50)
	private String materialDesc;
	
	@NonNull
	@Column(name="EMM_MAT_QUANTITY",length=5)
	private int quantity;
	
	@NonNull
	@Column(name="EMM_MAT_ISACTIVE",length=2)
	private String isActive;
	
	@Column(name="EMM_BLOCK_QTY")
	private Integer blockQty;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="EMM_CREATED_ON")
	private Date createdON;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="EMM_MODIFIED_ON")
	private Date modifiedON;
	
	public MaterialMaster() {
		
		
	}
		
}
