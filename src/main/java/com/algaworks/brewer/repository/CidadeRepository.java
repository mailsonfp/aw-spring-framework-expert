package com.algaworks.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.model.Estado;
import com.algaworks.brewer.repository.helper.cidade.CidadeRepositoryQueries;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>, CidadeRepositoryQueries {
	
	public Optional<Cidade> findByNomeAndEstado(String nome, Estado estado);
	
	public List<Cidade> findByEstadoCodigo(Long codigoEstado);
}
