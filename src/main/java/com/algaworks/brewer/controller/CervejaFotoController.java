package com.algaworks.brewer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.CervejaFotoDTO;
import com.algaworks.brewer.storage.CervejaFotoStorageRunnable;

@RestController
@RequestMapping("/cervejas/fotos")
public class CervejaFotoController {

	@PostMapping
	public DeferredResult<CervejaFotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<CervejaFotoDTO> resultado = new DeferredResult<>();

		Thread thread = new Thread(new CervejaFotoStorageRunnable(files, resultado));
		thread.start();
		
		return resultado;
	}
	
}