package com.algaworks.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface CervejaFotoStorage {

	public String salvarTemporariamente(MultipartFile[] files);
	
	public byte[] recuperarFotoTemporaria(String nome);
	
}