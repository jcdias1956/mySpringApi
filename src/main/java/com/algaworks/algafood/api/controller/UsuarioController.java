package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.api.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController implements UsuarioControllerOpenApi {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<UsuarioModel> listar() {
	
		List<Usuario> todosUsuarios = usuarioRepository.findAll();
	
		return usuarioModelAssembler.toCollectionModel(todosUsuarios);
	}
	
	@GetMapping(path = "/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UsuarioModel buscar(@PathVariable Long usuarioId) {
		
		Usuario usuario =  cadastroUsuarioService.buscarOuFalhar(usuarioId);
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar (@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {

		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
		usuario = cadastroUsuarioService.salvar(usuario);
		
		return usuarioModelAssembler.toModel(usuario);
			
	}
	
	@PutMapping(path = "/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UsuarioModel atualizar(@PathVariable Long usuarioId,
			@RequestBody @Valid UsuarioInput usuarioInput) {
		
		Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		usuarioAtual = cadastroUsuarioService.salvar(usuarioAtual);
		
		return usuarioModelAssembler.toModel(usuarioAtual);
	}
	
	@PutMapping(path = "/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long usuarioId,
			@RequestBody @Valid SenhaInput senhaInput) {
		
		cadastroUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(),
				senhaInput.getNovaSenha());
	}
	
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long usuarioId) {
			cadastroUsuarioService.excluir(usuarioId);
	}

}
