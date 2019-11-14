package com.ezc.hsil.webapp.dto;

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
	@Size(min = 0, max=10)
	private String code;
	
	@NonNull
	@NotNull(message="Please Enter name of the Distributor")
	@Size(min = 3, message = "{Size.userDto.firstName}")
	private String name;

	@NonNull
	@NotNull
	@Size(min = 10, max=10)
	@Pattern(regexp="(^$|[0-9]{10})",message="Invalid Phone No")
	private String phone;

	@NonNull
	@NotNull
	@Size(min = 5)
	private String organisation;

	@NonNull
	@NotNull
	@NotEmpty
	@Size(min = 2)
	private String city;
	
	public DistributorDto() {
		// TODO Auto-generated constructor stub
	}
}
