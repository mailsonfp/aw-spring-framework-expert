package com.algaworks.brewer.service.event;

import com.algaworks.brewer.model.Cerveja;

public class CervejaFotoEvent {
	
	private Cerveja cerveja;

	public CervejaFotoEvent(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}
	
	public boolean temFoto() {
		return !cerveja.getFoto().isEmpty();
	}
	
}
