package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ezc.hsil.webapp.model.DistributorMaster;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class DistributorDto {

	@NonNull
	private int id;
	
	@NonNull
	@NotNull(message="Please Enter name of the Distributor")
	@Size(min = 3, message = "{Size.userDto.firstName}")
	private String name;

	@NonNull
	@NotNull
	@Size(min = 10, max=10, message = "{Size.userDto.firstName}")
	@Pattern(regexp="(^$|[0-9]{10})",message="Invalid Phone No")
	private String phone;

	@NonNull
	@NotNull
	@Size(min = 5, message = "{Size.userDto.firstName}")
	private String organisation;

	@NonNull
	@NotNull
	@NotEmpty
	@Size(min = 2, message = "{Size.userDto.firstName}")
	private String city;

	
	private List<String> cities;

	public DistributorDto() {

		cities = new ArrayList<String>();
		
		
		cities.add("Hyderabad");
		cities.add("Chennai");
		cities.add("Delhi");
		cities.add("Mumbai");
		cities.add("Banglore");
		cities.add("Cochin");
		cities.add("Vijayawada");

	}

}
