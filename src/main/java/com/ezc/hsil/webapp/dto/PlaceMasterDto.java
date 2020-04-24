package com.ezc.hsil.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
@Data
@AllArgsConstructor
public class PlaceMasterDto {
		
		
		private int id; 
		@NotNull
		@Pattern(regexp="(^([^0-9]*)$)" ,message="Please provide a valid country" )
		private String country;
		@NotNull
		@Pattern(regexp="(^([^0-9]*)$)" ,message="Please provide a valid city" )
		private String city;
		@NotNull
		@Pattern(regexp="(^([^0-9]*)$)" ,message="Please provide a valid state" )
		private String state;
		
		
		public PlaceMasterDto()
		{
			
		}
		
				

}



