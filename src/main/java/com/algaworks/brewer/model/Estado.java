package com.algaworks.brewer.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Entity
public class Estado implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotBlank(message = "O nome do estado é obrigatório")
	@Size(max = 50, message = "O tamanho do nome não pode ser maior que {max} caracteres")
	private String nome;
	
	@NotBlank(message = "A sigla do estado é obrigatório")
	@Size(min = 2, max = 2, message = "O tamanho da sigla do estado deve ser de 2 caracteres")
	private String sigla;

}
