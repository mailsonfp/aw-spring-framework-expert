package com.algaworks.brewer.repository.filter;

import java.util.List;

import com.algaworks.brewer.model.Grupo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioFilter {
	
	private String nome;
	private String email;
	private List<Grupo> grupos;
	
}
