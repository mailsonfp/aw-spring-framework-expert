package com.algaworks.brewer.model.enums;

import lombok.Getter;

@Getter
public enum TipoPessoa {
	
	FISICA("Física", "CPF", "000.000.000-00"), 
	JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00");

	private String descricao;
	private String documento;
	private String mascara;

	TipoPessoa(String descricao, String documento, String mascara) {
		this.descricao = descricao;
		this.documento = documento;
		this.mascara = mascara;
	}
}
