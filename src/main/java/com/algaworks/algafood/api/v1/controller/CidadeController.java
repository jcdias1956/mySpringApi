package com.algaworks.algafood.api.v1.controller;

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

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.api.v1.openapi.controller.CidadeControllerOpenApi;
//import com.algaworks.algafood.core.web.AlgaMediaTypes;
import com.algaworks.algafood.domain.exception.EstadoNaoEcontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
//@RequestMapping(path = "/cidades", produces = AlgaMediaTypes.V1_APPLICATION_JSON_VALUE /*MediaType.APPLICATION_JSON_VALUE*/)
public class CidadeController implements CidadeControllerOpenApi {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;  
	
	@Deprecated
	@Override
	@GetMapping
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}

	@Deprecated
	@Override
	@GetMapping(path = "/{cidadeId}")
//	@GetMapping(path = "/{cidadeId}", produces = AlgaMediaTypes.V1_APPLICATION_JSON_VALUE)
	public CidadeModel buscar(
			@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);

		return cidadeModelAssembler.toModel(cidade);
	}
	
	@Deprecated
	@Override
	@PostMapping
//	@PostMapping(produces = AlgaMediaTypes.V1_APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar (
			@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			cidade = cadastroCidadeService.salvar(cidade);
			CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);
			
			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
			
			return cidadeModel;
		} catch (EstadoNaoEcontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@Deprecated
	@Override
	@PutMapping(path = "/{cidadeId}")
//	@PutMapping(path = "/{cidadeId}", produces = AlgaMediaTypes.V1_APPLICATION_JSON_VALUE)
	public CidadeModel atualizar(
			@PathVariable Long cidadeId,
			@RequestBody @Valid CidadeInput cidadeInput) {
		
		try {
			Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
			return cidadeModelAssembler.toModel(cidadeAtual);
			
//			BeanUtils.copyProperties(cidade, cidadeAtual, "id");
//			return cadastroCidadeService.salvar(cidadeAtual);
		} catch (EstadoNaoEcontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}
	
	@Deprecated
	@Override
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(
			@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);
	}

}
