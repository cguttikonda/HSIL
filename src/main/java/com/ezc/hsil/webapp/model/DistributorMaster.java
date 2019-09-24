package com.ezc.hsil.webapp.model;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@SqlResultSetMapping(
	    name="DistributorDtoMapping",
	    classes={
	        @ConstructorResult(
	            targetClass=DistributorDto.class,
	            columns={
	            	@ColumnResult(name="id"),
	            	@ColumnResult(name="name"),
	                @ColumnResult(name="contact"),
	                @ColumnResult(name="organisation"),
	                @ColumnResult(name="city")
	            }
	        )
	    }
	)

@NamedNativeQuery(name="DistributorMaster.distributorDetails", query="SELECT * FROM EZC_DISTRIBUTORS d " 
							+  " WHERE d.id = :id",  
							resultSetMapping="DistributorDtoMapping")
@Entity
@Table(name="EZC_DISTRIBUTORS", catalog="hsil")
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class DistributorMaster { 

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@NonNull
	private int id;
	
	@NonNull
	private String name;
	@NonNull
	private String contact;
	@NonNull
	private String organisation;
	@NonNull
	private String city;
	
	
	public DistributorMaster() {

	}
	
	 
	
	
	
	
}
