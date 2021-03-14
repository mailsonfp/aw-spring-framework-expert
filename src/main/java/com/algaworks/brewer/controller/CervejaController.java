package com.algaworks.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.dto.CervejaDTO;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.enums.Origem;
import com.algaworks.brewer.model.enums.Sabor;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.CervejaService;
import com.algaworks.brewer.service.EstiloService;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/cervejas")
public class CervejaController {
	
	@Autowired
	CervejaService cervejaService;
	
	@Autowired
	private EstiloService estiloService;
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");
		mv.addObject("estilos", estiloService.listar());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());
		
		PageWrapper<Cerveja> pagina = new PageWrapper<>(cervejaService.listarComFiltros(cervejaFilter, pageable), httpServletRequest);	
		mv.addObject("pagina", pagina);
		
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Cerveja cerveja) {
		ModelAndView mv = novo(cerveja);
		mv.addObject(cerveja);
		return mv;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CervejaDTO> pesquisarPorSkuOuNome(String skuOuNome) {
		return cervejaService.buscarPorSkuOuNome(skuOuNome);
	}
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cerveja cerveja) {
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
		mv.addObject("sabores", Sabor.values());
		mv.addObject("estilos", estiloService.listar());
		mv.addObject("origens", Origem.values());
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			return novo(cerveja);
		}		 
		
		cervejaService.salvar(cerveja);
		attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");
		return new ModelAndView("redirect:/cervejas/novo");
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cerveja cerveja) {
		try {
			cervejaService.excluir(cerveja);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
}
