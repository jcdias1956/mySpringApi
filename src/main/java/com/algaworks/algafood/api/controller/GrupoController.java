package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.api.openapi.controller.GrupoControllerOpenApi;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroGrupoService cadastroGrupoServive;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;

	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler; 
	
	@GetMapping
	public List<GrupoModel> listar() {
		List<Grupo> todosGrupos = grupoRepository.findAll();
		
		return grupoModelAssembler.toCollectionModel(todosGrupos);
	}
	
	@GetMapping(value = "/{grupoId}")
	public GrupoModel buscar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupoServive.buscarOuFalhar(grupoId);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
		
		grupo = cadastroGrupoServive.salvar(grupo);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@PutMapping("/{grupoId}")
	public GrupoModel atualizar(@PathVariable Long grupoId,
			@RequestBody @Valid GrupoInput grupoInput) {
		
		Grupo grupoAtual = cadastroGrupoServive.buscarOuFalhar(grupoId);
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		grupoAtual = cadastroGrupoServive.salvar(grupoAtual);
		return grupoModelAssembler.toModel(grupoAtual);
		
//		BeanUtils.copyProperties(Grupo, grupoAtual, "id");		
//		return cadastroGrupoServive.salvar(grupoAtual);
	}
	
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long grupoId) {
		cadastroGrupoServive.excluir(grupoId);
	}
}
