package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

// para usar o @JsonFilter tem que habilitar o getmapping correspondente no PedidoController
//@JsonFilter("pedidoFilter")
@Getter
@Setter
public class PedidoResumoModel {

	private String codigo;
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private String status;
	private OffsetDateTime dataCriacao;
//	private OffsetDateTime dataConfirmacao;
//	private OffsetDateTime dataEntrega;
//	private OffsetDateTime dataCancelamento;
	private RestauranteResumoModel restaurante;
	private UsuarioModel cliente;
//	private FormaPagamentoModel formaPagamento;
//	private EnderecoModel enderecoEntrega;
//	private List<ItemPedidoModel> itens;
}
