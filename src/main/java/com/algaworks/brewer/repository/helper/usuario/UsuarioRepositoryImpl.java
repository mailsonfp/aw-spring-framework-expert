package com.algaworks.brewer.repository.helper.usuario;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.algaworks.brewer.model.Usuario;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Optional<Usuario> buscarUsuarioAtivoPorEmail(String email) {
		return manager
				.createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
				.setParameter("email", email).getResultList().stream().findFirst();
	}

}
