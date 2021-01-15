package com.algaworks.brewer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.CervejaRepository;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.event.CervejaFotoEvent;

@Service
public class CervejaService {
	
	@Autowired
	CervejaRepository cervejaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	public List<Cerveja> listar(){
		return cervejaRepository.findAll();
	}
	
	public List<Cerveja> listarComFiltros(CervejaFilter filtro){
		return cervejaRepository.filtrar(filtro);
	}
	
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejaRepository.save(cerveja);
		
		publisher.publishEvent(new CervejaFotoEvent(cerveja));
	}
}
