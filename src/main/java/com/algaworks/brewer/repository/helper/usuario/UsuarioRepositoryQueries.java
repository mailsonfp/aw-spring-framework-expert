package com.algaworks.brewer.repository.helper.usuario;

import java.util.Optional;

import com.algaworks.brewer.model.Usuario;

public interface UsuarioRepositoryQueries {
	
	Optional<Usuario> buscarUsuarioAtivoPorEmail(String email);
}
