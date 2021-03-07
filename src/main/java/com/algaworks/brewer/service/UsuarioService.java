package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.repository.UsuarioRepository;
import com.algaworks.brewer.repository.filter.UsuarioFilter;
import com.algaworks.brewer.service.enums.StatusUsuario;
import com.algaworks.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.algaworks.brewer.service.exception.SenhaObrigatoriaUsuarioException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public void salvar(Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent()) {
			throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
		}
		
		
		if (usuario.isNovo() && ObjectUtils.isEmpty(usuario.getSenha())) {
			throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
		}
		
		if (usuario.isNovo()) {
				usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
				usuario.setConfirmacaoSenha(usuario.getSenha());
		}
		usuarioRepository.save(usuario);
	}
	
	public Optional<Usuario> buscarUsuarioAtivoPorEmail(String email){
		return usuarioRepository.buscarUsuarioAtivoPorEmail(email);
	}
	
	public Page<Usuario> pesquisar(UsuarioFilter filtro, Pageable pageable) {
		return usuarioRepository.pesquisar(filtro, pageable);
	}
	
	@Transactional
	public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
		statusUsuario.executar(codigos, usuarioRepository);
	}
}
