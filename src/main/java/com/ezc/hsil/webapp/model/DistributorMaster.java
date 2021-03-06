package com.ezc.hsil.webapp.model;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.ezc.hsil.webapp.dto.DistributorDto;

import lombok.Data;
import lombok.NonNull;


@SqlResultSetMapping(
	    name="DistributorDtoMapping",
	    classes={
	        @ConstructorResult(
	            targetClass=DistributorDto.class,
	            columns={
	            	@ColumnResult(name="code"),
	            	@ColumnResult(name="name"),
	                @ColumnResult(name="contact"),
	                @ColumnResult(name="organisation"),
	                @ColumnResult(name="city"),
	                @ColumnResult(name="type")
	            }
	        )
	    }
	)

@NamedNativeQuery(name="DistributorMaster.distributorDetails", query="SELECT * FROM EZC_DISTRIBUTORS d " 
		+  " WHERE d.code = :code",   
		resultSetMapping="DistributorDtoMapping")

@Entity
@Table(name="EZC_DISTRIBUTORS", catalog="hsil")
@Data
public class DistributorMaster { 

	@Id
	@NonNull 
	private String code;
	@NonNull
	private String name;
	@NonNull
	private String contact;
	@NonNull
	private String organisation;
	@NonNull
	private String city;
	
	@NonNull
	private String type;
	
	
	public DistributorMaster() {

	}
	
	 
	
	
	
	
}
