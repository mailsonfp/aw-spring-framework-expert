package com.algaworks.brewer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.dto.CervejaDTO;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.CervejaRepository;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.event.CervejaFotoEvent;
import com.algaworks.brewer.service.exception.CervejaNaoEncontradaException;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.algaworks.brewer.storage.CervejaFotoStorage;

@Service
public class CervejaService {
	
	@Autowired
	CervejaRepository cervejaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	CervejaFotoStorage fotoStorage;
	
	public Cerveja buscarPorCodigo(Long codigoCerveja) {
		return cervejaRepository.findById(codigoCerveja).orElseThrow(() -> new CervejaNaoEncontradaException("Não foi possível localizar a cerveja com o código informado"));
	}
	
	public List<Cerveja> listar(){
		return cervejaRepository.findAll();
	}
	
	public Page<Cerveja> listar(Pageable pageable){
		return cervejaRepository.findAll(pageable);
	}
	
	public Page<Cerveja> listarComFiltros(CervejaFilter filtro, Pageable pageable){
		return cervejaRepository.filtrar(filtro, pageable);
	}
	
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejaRepository.save(cerveja);
		
		publisher.publishEvent(new CervejaFotoEvent(cerveja));
	}

	public List<CervejaDTO> buscarPorSkuOuNome(String skuOuNome) {		
		return cervejaRepository.buscarPorSkuOuNome(skuOuNome);
	}
	
	@Transactional
	public void excluir(Cerveja cerveja) {
		try {
			String foto = cerveja.getFoto();
			cervejaRepository.deleteById(cerveja.getCodigo());	
			cervejaRepository.flush();
			fotoStorage.excluir(foto);
		} catch (DataIntegrityViolationException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cerveja. Já foi usada em alguma venda.");
		}
	}
}
