package com.ezc.hsil.webapp.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.dto.PlaceMasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@SqlResultSetMapping(
	    name="PlaceMasterDtoMapping",
	    classes={ 
	        @ConstructorResult(
	            targetClass=PlaceMasterDto.class,
	            columns={
	            	@ColumnResult(name="EPM_ID"),
	                @ColumnResult(name="EPM_CITY"),
	                @ColumnResult(name="EPM_STATE"),
	                @ColumnResult(name="EPM_COUNTRY")
	                
	            }
	        )
	    }
	)

@NamedNativeQuery(name="EzPlaceMaster.cityDetails", query="SELECT * FROM EZC_PLACE_MASTER mm " 
							+  " WHERE mm.EPM_CITY = :city",  
							resultSetMapping="PlaceMasterDtoMapping")




@Entity
@Table(name="EZC_PLACE_MASTER",catalog="hsil")
@Data
@AllArgsConstructor
public class EzPlaceMaster{

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "EPM_ID", unique = true, nullable = false)
	private int id;
	
	@Column(name="EPM_CITY",length=40)
	private String city;
	
	@Column(name="EPM_STATE",length=40)
	private String state;
	
	@Column(name="EPM_COUNTRY",length=40)
	private String country;
		
	public EzPlaceMaster() {
		 
		
	}
		
}
