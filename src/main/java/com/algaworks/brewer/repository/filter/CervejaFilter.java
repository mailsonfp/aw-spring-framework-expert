package com.algaworks.brewer.repository.filter;

import java.math.BigDecimal;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.model.enums.Origem;
import com.algaworks.brewer.model.enums.Sabor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CervejaFilter {
	
	private String sku;
	private String nome;
	private Estilo estilo;
	private Sabor sabor;
	private Origem origem;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	
}
