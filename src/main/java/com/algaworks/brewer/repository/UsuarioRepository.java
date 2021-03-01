package com.algaworks.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.repository.helper.usuarios.UsuarioRepositoryQueries;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQueries {
	
	Optional<Usuario> findByEmail(String email);
}
