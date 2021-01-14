package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.CervejaFotoDTO;
import com.algaworks.brewer.storage.CervejaFotoStorage;
import com.algaworks.brewer.storage.CervejaFotoStorageRunnable;

@RestController
@RequestMapping("/cervejas/fotos")
public class CervejaFotoController {
	
	@Autowired
	private CervejaFotoStorage fotoStorage;

	@PostMapping
	public DeferredResult<CervejaFotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<CervejaFotoDTO> resultado = new DeferredResult<>();

		Thread thread = new Thread(new CervejaFotoStorageRunnable(files, resultado, fotoStorage));
		thread.start();
		
		return resultado;
	}
	
	@GetMapping(path = "/temp/{nome:.*}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] recuperarFotoTemporaria(@PathVariable String nome) {
		return fotoStorage.recuperarFotoTemporaria(nome);
	}
	
}