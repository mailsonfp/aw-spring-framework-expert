package com.algaworks.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CidadeController {
	
	@RequestMapping(path = "cidades/novo")
	public String novo() {
		return "cidade/CadastroCidade";
	}
}
