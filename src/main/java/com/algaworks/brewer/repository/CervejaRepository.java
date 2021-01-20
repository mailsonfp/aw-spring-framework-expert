package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.helper.cerveja.CervejaRepositoryQueries;

@Repository
public interface CervejaRepository
		extends JpaRepository<Cerveja, Long>, CervejaRepositoryQueries {

}
