package com.algaworks.brewer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.EstiloRepository;

@Service
public class EstiloService {
	
	@Autowired
	private EstiloRepository estiloRepository;
	
	public List<Estilo> listar(){
		return estiloRepository.findAll();
	}
	
	@Transactional
	public void salvar(Estilo estilo) {
		estiloRepository.save(estilo);
	}
}
