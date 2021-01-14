package com.algaworks.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.CervejaFotoDTO;

public class CervejaFotoStorageRunnable implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<CervejaFotoDTO> resultado;
	private CervejaFotoStorage fotoStorage;
	
	public CervejaFotoStorageRunnable(MultipartFile[] files, DeferredResult<CervejaFotoDTO> resultado, CervejaFotoStorage fotoStorage) {
		this.files = files;
		this.resultado = resultado;
		this.fotoStorage = fotoStorage;
	}

	@Override
	public void run() {				
		String nomeFoto = this.fotoStorage.salvarTemporariamente(files);
		String contentType = files[0].getContentType();
		resultado.setResult(new CervejaFotoDTO(nomeFoto, contentType));
	}

}
