package com.algaworks.brewer.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.algaworks.brewer.service.event.CervejaFotoEvent;
import com.algaworks.brewer.storage.CervejaFotoStorage;

@Component
public class CervejaListener {
	
	@Autowired
	private CervejaFotoStorage fotoStorage;
	
	@EventListener(condition = "#evento.temFoto()")
	public void cervejaSalva(CervejaFotoEvent evento) {
		fotoStorage.salvar(evento.getCerveja().getFoto());
	}
	
}
