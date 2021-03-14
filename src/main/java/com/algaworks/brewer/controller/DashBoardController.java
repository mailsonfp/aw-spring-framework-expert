package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.service.CervejaService;
import com.algaworks.brewer.service.ClienteService;
import com.algaworks.brewer.service.VendaService;

@Controller
public class DashBoardController {

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	CervejaService cervejaService;
	
	@Autowired
	ClienteService clienteService;
	
	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		
		mv.addObject("vendasNoAno", vendaService.valorTotalNoAno());
		mv.addObject("vendasNoMes", vendaService.valorTotalNoMes());
		mv.addObject("ticketMedio", vendaService.valorTicketMedioNoAno());
		
		mv.addObject("valorItensEstoque", cervejaService.valorItensEstoque());
		mv.addObject("totalClientes", clienteService.quantidadeTotalDeCliente());
		
		return mv;
	}
	
}
