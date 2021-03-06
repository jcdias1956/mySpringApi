package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaServive;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/v1/cozinhas")
public class CozinhaController implements CozinhaControllerOpenApi {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaServive cadastroCozinhaServive;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler; 
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	// usando page
	// o valor default do @PageableDefault e 10
	//@PodeConsultarCozinhas
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<CozinhaModel> listar(@PageableDefault(size = 2) Pageable pageable) {
		
//		System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		log.info("Consultando cozinhas com paginas de {} registros", pageable.getPageSize());
		
		// teste para lancar exception junto com o registro de log....
//		if (true) {
//			throw new RuntimeException("Teste de exception...");
//		}
		
		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
		
		PagedModel<CozinhaModel> cozinhasPagedModel = pagedResourcesAssembler
			.toModel(cozinhasPage, cozinhaModelAssembler);		
		
		return cozinhasPagedModel;
	}
	
//	// sem usar page
//	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<CozinhaModel> listar() {
//		List<Cozinha> todasCozinhas = cozinhaRepository.findAll();
//		return cozinhaModelAssembler.toCollectionModel(todasCozinhas);
//	}
		
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinhaServive.buscarOuFalhar(cozinhaId);
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	//@PodeEditarCozinhas
	@CheckSecurity.Cozinhas.PodeEditar
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		cozinha = cadastroCozinhaServive.salvar(cozinha);
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModel atualizar(@PathVariable Long cozinhaId,
			@RequestBody @Valid CozinhaInput cozinhaInput) {
		
		Cozinha cozinhaAtual = cadastroCozinhaServive.buscarOuFalhar(cozinhaId);
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		cozinhaAtual = cadastroCozinhaServive.salvar(cozinhaAtual);
		return cozinhaModelAssembler.toModel(cozinhaAtual);
		
//		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
//		return cadastroCozinhaServive.salvar(cozinhaAtual);
	}
		
	@CheckSecurity.Cozinhas.PodeEditar
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaServive.excluir(cozinhaId);
	}
		
}
