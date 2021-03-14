package com.algaworks.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface CervejaFotoStorage {

	public String salvarTemporariamente(MultipartFile[] files);
	
	public byte[] recuperarFotoTemporaria(String nome);
	
	public void salvar(String foto);

	public byte[] recuperar(String foto);
	
	public byte[] recuperarThumbnail(String fotoCerveja);
	
	public void excluir(String foto);
}
