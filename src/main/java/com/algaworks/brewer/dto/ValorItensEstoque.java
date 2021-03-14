package com.algaworks.brewer.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ValorItensEstoque {
	
	private BigDecimal valor;
	
	private Long totalItens;
}
