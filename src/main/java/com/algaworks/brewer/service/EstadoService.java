package com.algaworks.brewer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Estado;
import com.algaworks.brewer.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	public List<Estado> listarEstados(){
		return estadoRepository.findAll();
	}
}
