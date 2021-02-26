package com.algaworks.brewer.model.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.model.Estado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String logradouro;
	
	private String numero;
	
	private String complemento;
	
	private String cep;
	
	@Transient
	private Estado estado;
	
	@ManyToOne
	@JoinColumn(name = "codigo_cidade")
	private Cidade cidade;
	
	public String getNomeCidadeSiglaEstado() {
		if (this.cidade != null) {
			return this.cidade.getNome() + "/" + this.cidade.getEstado().getSigla();
		}
		
		return null;
	}
}
