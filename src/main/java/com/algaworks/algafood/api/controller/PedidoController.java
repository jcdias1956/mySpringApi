package com.algaworks.algafood.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;


@RestController
// o produces fica nos requests que nao retornam void para nao dar erro quando usar SpringFox 3.0 e Open API 3 Nullexception
@RequestMapping(path = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EmissaoPedidoService emissaoPedidoService;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@Autowired
	private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

// Usando MappingJacksonValue, requestparam para filtrar
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//		List<Pedido> todosPedidos = pedidoRepository.findAll();
//		List<PedidoResumoModel> pedidoResumoModel = pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
//		
//		MappingJacksonValue pedidoResumoWrapper = new MappingJacksonValue(pedidoResumoModel);
//		
//		SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
//		simpleFilterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//		
//		if (StringUtils.isNotBlank(campos)) {
//			simpleFilterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));;
//		}
//		
//		pedidoResumoWrapper.setFilters(simpleFilterProvider);
//		
//		return pedidoResumoWrapper;
//	}

//	@GetMapping
//	public List<PedidoResumoModel> listar() {
//		List<Pedido> todosPedidos = pedidoRepository.findAll();
//		
//		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
//	}
	
	// usando PedidoSpecs e com paginacao
	@ApiImplicitParams({
		@ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por virgula",
				name = "campos", paramType = "query", type = "string")
	})
	@GetMapping
	public PagedModel<PedidoResumoModel> pesquisar(@PageableDefault(size = 3) PedidoFilter pedidoFilter, Pageable pageable) {
		
		// paga converter propriedade do model para propriedade da entidade
		Pageable pageableTraduzido = traduzirPageable(pageable);
		
		Page<Pedido> pedidosPage = pedidoRepository
				.findAll(PedidoSpecs.usandoFiltro(pedidoFilter), pageableTraduzido);
		
		pedidosPage = new PageWrapper<>(pedidosPage, pageable);
		
		return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
	}

//	// usando PedidoSpecs e sem paginacao
//	@GetMapping
//	public List<PedidoResumoModel> pesquisar(PedidoFilter pedidoFilter) {
//		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(pedidoFilter));
//		
//		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
//	}

	@ApiImplicitParams({
		@ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por virgula",
				name = "campos", paramType = "query", type = "string")
	})
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
		
		return pedidoModelAssembler.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
		
		try {
			Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
			
			// TODO pegar usuario autenticado
			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(1L);
			
			novoPedido = emissaoPedidoService.emitir(novoPedido);
			
			return pedidoModelAssembler.toModel(novoPedido);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	private Pageable traduzirPageable(Pageable apiPageable) {
		
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
				);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
}
