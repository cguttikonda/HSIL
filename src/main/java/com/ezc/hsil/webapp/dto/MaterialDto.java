package com.ezc.hsil.webapp.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.NonNull;

@Data
public class MaterialDto {

	
	@NonNull
	private int id;
	
	@NonNull
	@Pattern(regexp="(^([0-9]*)$)" ,message="Please provide a valid material code" )
	//@Range(min=3,max=50, message="Please provide a valid material code")
	private String materialCode;
	@NonNull
	@NotNull
	@Size(max=50, message="")
	private String materialDesc;
	
	@NonNull
	//@Digits(fraction = 0, integer = 3)
	//@Pattern(regexp="(^$|[0-9])")
	@Positive 
	@NotNull
	@Max(3)
	private int quantity;
	
	@NonNull
	private String isActive;
	
	
	public MaterialDto() {}
	
	
}
