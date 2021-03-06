package com.algaworks.brewer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.model.enums.StatusVenda;
import com.algaworks.brewer.repository.VendaRepository;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.service.event.venda.VendaEvent;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable){
		return vendaRepository.filtrar(filtro, pageable);
	}
	
	@Transactional
	public Venda salvar(Venda venda) {
		
		if (venda.isSalvarProibido()) {
			throw new RuntimeException("Usuário tentando salvar uma venda proibida");
		}
		
		try {
			if (venda.isNova()) {
				venda.setDataCriacao(LocalDateTime.now());
			}else {
				Venda vendaExistente = buscarPorCodigo(venda.getCodigo());
				venda.setDataCriacao(vendaExistente.getDataCriacao());
			}
			
			if (venda.getDataEntrega() != null) {
				venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega()
						, venda.getHorarioEntrega() != null ? venda.getHorarioEntrega() : LocalTime.NOON));
			}
			
			return vendaRepository.saveAndFlush(venda);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Venda();
	}
	
	@Transactional
	public void emitir(Venda venda) {
		venda.setStatus(StatusVenda.EMITIDA);
		salvar(venda);
		
		publisher.publishEvent(new VendaEvent(venda));
	}

	public Venda buscarPorCodigo(Long codigo) {
		return vendaRepository.findById(codigo).orElse(null);
	}
	
	@PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
	@Transactional
	public void cancelar(Venda venda) {
		Venda vendaExistente = buscarPorCodigo(venda.getCodigo());
		
		vendaExistente.setStatus(StatusVenda.CANCELADA);
		vendaRepository.save(vendaExistente);
	}
	
	public BigDecimal valorTotalNoAno() {
		return vendaRepository.valorTotalNoAno();
	}
	
	public BigDecimal valorTotalNoMes() {
		return vendaRepository.valorTotalNoMes();
	}
	
	public BigDecimal valorTicketMedioNoAno() {
		return vendaRepository.valorTicketMedioNoAno();
	}

	public List<VendaMes> totalPorMes() {
		return vendaRepository.totalPorMes();
	}

	public List<VendaOrigem> totalPorOrigem() {		
		return vendaRepository.totalPorOrigem();
	}	

}
