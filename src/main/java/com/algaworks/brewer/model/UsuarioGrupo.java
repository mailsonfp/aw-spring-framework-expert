package com.algaworks.brewer.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.algaworks.brewer.model.embeddable.UsuarioGrupoId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "usuario_grupo")
public class UsuarioGrupo {
	
	@EmbeddedId
	private UsuarioGrupoId id;
}
