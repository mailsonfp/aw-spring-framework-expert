package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.CervejaRepository;

@Service
public class CervejaService {
	
	@Autowired
	CervejaRepository cervejaRepository;
	
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejaRepository.save(cerveja);
	}
}
