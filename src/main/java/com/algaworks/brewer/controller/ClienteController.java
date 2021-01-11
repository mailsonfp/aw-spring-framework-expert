package com.algaworks.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.algaworks.brewer.model.Cerveja;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@RequestMapping("/novo")
	public String novo(Cerveja cerveja) {
		return "cliente/CadastroCliente";
	}
}
