package com.algaworks.brewer.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.algaworks.brewer.model.enums.StatusVenda;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaFilter {
	
	private Long codigo;
	private StatusVenda status;

	private LocalDate desde;
	private LocalDate ate;
	private BigDecimal valorMinimo;
	private BigDecimal valorMaximo;

	private String nomeCliente;
	private String cpfOuCnpjCliente;

}
