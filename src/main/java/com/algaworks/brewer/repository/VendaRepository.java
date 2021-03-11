package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.helper.venda.VendaRepositoryQueries;

public interface VendaRepository extends JpaRepository<Venda, Long>, VendaRepositoryQueries {

}
