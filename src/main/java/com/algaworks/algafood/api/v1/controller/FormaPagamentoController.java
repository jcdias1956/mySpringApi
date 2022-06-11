package com.algaworks.algafood.api.v1.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.v1.assembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping(path = "v1/formas-pagamento")
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoServive;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

	@Autowired
	private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler; 
	
	@CheckSecurity.FormasPagamento.PodeConsultar
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest servletWebRequest) {
		
		// usar Deep ETags precisa desabilitar o ShallowEtagHeaderFilter
		// na pratica, o Deeo ETags para forma de pagmento eh muito preciosismo. Tem muito poudo beneficio
		ShallowEtagHeaderFilter.disableContentCaching(servletWebRequest.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		// ja temos condicoes de saber se continua ou nao com o processamento
		if (servletWebRequest.checkNotModified(eTag)) {
			return null;
		}
		
		List<FormaPagamento> todasFormasPagamento = formaPagamentoRepository.findAll();
		
		CollectionModel<FormaPagamentoModel> formasPagamentosModel = 
				formaPagamentoModelAssembler.toCollectionModel(todasFormasPagamento);
		
		return ResponseEntity.ok()
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).noCache())
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).noStore())
				.eTag(eTag)
				.body(formasPagamentosModel);
	}
	
	@CheckSecurity.FormasPagamento.PodeConsultar
	@GetMapping(path = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId,
			ServletWebRequest servletWebRequest) {
		
		// usar Deep ETags precisa desabilitar o ShallowEtagHeaderFilter
		// na pratica, o Deeo ETags para forma de pagmento eh muito preciosismo. Tem muito poudo beneficio
		ShallowEtagHeaderFilter.disableContentCaching(servletWebRequest.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataAtualizacaoById(formaPagamentoId);
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		// ja temos condicoes de saber se continua ou nao com o processamento
		if (servletWebRequest.checkNotModified(eTag)) {
			return null;
		}
		

		FormaPagamento formaPagamento = cadastroFormaPagamentoServive.buscarOuFalhar(formaPagamentoId);
		
		FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler.toModel(formaPagamento);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.eTag(eTag)
				.body(formaPagamentoModel);
	}
	
	@CheckSecurity.FormasPagamento.PodeEditar
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagammento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
		
		formaPagammento = cadastroFormaPagamentoServive.salvar(formaPagammento);
		
		return formaPagamentoModelAssembler.toModel(formaPagammento);
	}
	
	@CheckSecurity.FormasPagamento.PodeEditar
	@PutMapping(path = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
			@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoServive.buscarOuFalhar(formaPagamentoId);
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
		formaPagamentoAtual = cadastroFormaPagamentoServive.salvar(formaPagamentoAtual);
		return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
		
//		BeanUtils.copyProperties(FormaPagamento, formaPagamentoAtual, "id");		
//		return cadastroFormaPagamentoServive.salvar(formaPagamentoAtual);
	}
	
	@CheckSecurity.FormasPagamento.PodeEditar
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long formaPagamentoId) {
		cadastroFormaPagamentoServive.excluir(formaPagamentoId);
	}
}
