package com.algaworks.brewer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.EstiloRepository;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

@Service
public class EstiloService {
	
	@Autowired
	private EstiloRepository estiloRepository;
	
	public List<Estilo> listar(){
		return estiloRepository.findAll();
	}
	
	public Page<Estilo> filtrar(EstiloFilter filtro, Pageable pageable) {
		return estiloRepository.filtrar(filtro, pageable);
	}
	
	@Transactional
	public Estilo salvar(Estilo estilo) {
		Optional<Estilo> estiloExistente = estiloRepository.findByNomeIgnoreCase(estilo.getNome());
		if(estiloExistente.isPresent()){
			throw new NomeEstiloJaCadastradoException("O estilo informado já foi cadastrado.");
		}
		
		return estiloRepository.save(estilo);
	}
}
