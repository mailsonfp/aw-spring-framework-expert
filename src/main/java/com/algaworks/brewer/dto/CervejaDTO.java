package com.algaworks.brewer.dto;

import java.math.BigDecimal;

import com.algaworks.brewer.model.enums.Origem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CervejaDTO {
	
	private Long codigo;
	private String sku;
	private String nome;
	private Origem origemObj;	
	private BigDecimal valor;
	private String foto;
	
	public String getOrigem() {
		return origemObj.getDescricao();
	}
	
	public String getFotoOuMock() {
		return !foto.isEmpty() ? foto : "cerveja-mock.png";
	}
}
