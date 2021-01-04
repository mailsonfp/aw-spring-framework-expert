package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Cerveja;

@Repository
public interface CervejaRepository extends JpaRepository<Cerveja, Long> {

}
