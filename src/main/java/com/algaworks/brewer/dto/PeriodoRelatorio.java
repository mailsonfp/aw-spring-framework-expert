package com.algaworks.brewer.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PeriodoRelatorio {
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
}
