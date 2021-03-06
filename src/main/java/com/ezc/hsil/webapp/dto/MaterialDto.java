package com.ezc.hsil.webapp.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class MaterialDto {

	
	@NonNull
	@Pattern(regexp="(^([0-9]*)${50})" ,message="{Pattern.materialDto.materialCode}" )
	//@Range(min=3,max=50, message="Please provide a valid material code")
	private String materialCode;
	@NonNull
	@NotNull
	@Size(max=50, message="{Size.materialDto.materialDesc}")
	private String materialDesc;

	
	@NonNull
	//@Digits(fraction = 0, integer = 3)
	//@Pattern(regexp="(^$|[0-9])")
	@Positive 
	@NotNull
	@Min(1)
//	@Range(max=1000)
	private int quantity;
	
	@NonNull
	private String isActive;
	
	private Integer blockQty;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdON;
	
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date modifiedON;
	
	@NotNull
	private String stockLoc;
	
	public MaterialDto() {}
	
	
}
