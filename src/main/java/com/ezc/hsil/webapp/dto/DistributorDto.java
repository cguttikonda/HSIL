package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class DistributorDto {

	@NonNull
	@NotNull(message="Please Enter name of the Distributor")
	@Size(min = 3, message = "{Size.userDto.firstName}")
	private String name;

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
