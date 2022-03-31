package com.algaworks.algafood.domain.exception;

public class PedidoNaoEcontradoException  extends NegocioException {

	private static final long serialVersionUID = 1L;
	

	public PedidoNaoEcontradoException(String codigoPedido) {
		super(String.format("Nao existe um cadastro de pedido com codigo %s", codigoPedido));
	}

}
