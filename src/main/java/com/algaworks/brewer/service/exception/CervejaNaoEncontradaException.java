package com.algaworks.brewer.service.exception;

public class CervejaNaoEncontradaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CervejaNaoEncontradaException(String message) {
		super(message);
	}
}
