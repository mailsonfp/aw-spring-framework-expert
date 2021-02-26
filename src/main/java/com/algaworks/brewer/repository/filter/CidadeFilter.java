package com.algaworks.brewer.repository.filter;

import com.algaworks.brewer.model.Estado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeFilter {
	
	private Estado estado;
	
	private String nome;
}
