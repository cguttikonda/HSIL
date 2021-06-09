package com.ezc.hsil.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockTransferDto {

	private String materialCode;
	private String fromStore;
	private String toStore;
	private int quantity;
	
}
