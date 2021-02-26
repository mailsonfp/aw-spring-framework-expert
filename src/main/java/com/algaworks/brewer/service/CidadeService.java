package com.algaworks.brewer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.CidadeRepository;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradaException;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> listarCidadesPorEstado(Long codigoEstado){
		return cidadeRepository.findByEstadoCodigo(codigoEstado);
	}
	
	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable){
		return cidadeRepository.filtrar(filtro, pageable);
	}
	
	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> cidadeExistente = cidadeRepository.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
		if (cidadeExistente.isPresent()) {
			throw new NomeCidadeJaCadastradaException("Nome de cidade já cadastrado");
		}
		
		cidadeRepository.save(cidade);
	}
	
}
