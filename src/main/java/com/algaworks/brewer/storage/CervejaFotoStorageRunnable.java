package com.algaworks.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.CervejaFotoDTO;

public class CervejaFotoStorageRunnable implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<CervejaFotoDTO> resultado;
	
	public CervejaFotoStorageRunnable(MultipartFile[] files, DeferredResult<CervejaFotoDTO> resultado) {
		this.files = files;
		this.resultado = resultado;
	}

	@Override
	public void run() {
		System.out.println(">>> files: " + files[0].getSize());
		// TODO: Salvar a foto no sistema de arquivos...
		String nomeFoto = files[0].getOriginalFilename();
		String contentType = files[0].getContentType();
		resultado.setResult(new CervejaFotoDTO(nomeFoto, contentType));
	}

}
