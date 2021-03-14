package com.algaworks.brewer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VendaOrigem {
	
	private String mes;
	
	private Integer totalNacional;
	
	private Integer totalInternacional;
}
