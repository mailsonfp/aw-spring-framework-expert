package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.ClienteRepository;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.service.exception.CpfCnpjClienteJaCadastradoException;

@Service
public class ClienteService {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
		return clienteRepository.filtrar(filtro, pageable);
	}
	
	@Transactional
	public void salvar(Cliente cliente) {
		Optional<Cliente> clienteExistente = clienteRepository.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		if (clienteExistente.isPresent()) {
			throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		
		clienteRepository.save(cliente);
	}	
	
}
