package com.algaworks.brewer.model.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.algaworks.brewer.model.Grupo;
import com.algaworks.brewer.model.Usuario;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@Embeddable
public class UsuarioGrupoId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "codigo_usuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "codigo_grupo")
	private Grupo grupo;
}
