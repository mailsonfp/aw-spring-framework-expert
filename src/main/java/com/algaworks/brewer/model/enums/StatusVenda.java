package com.algaworks.brewer.model.enums;

public enum StatusVenda {
	
	ORCAMENTO("Orçamento"), 
	EMITIDA("Emitida"), 
	CANCELADA("Cancelada");

	private String descricao;

	StatusVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
