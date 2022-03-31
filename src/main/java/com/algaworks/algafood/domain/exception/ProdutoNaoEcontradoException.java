package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEcontradoException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public ProdutoNaoEcontradoException(String mensagem) {
		super(mensagem);
	}

	public ProdutoNaoEcontradoException(Long restauranteId, Long produtoId) {
		this(String.format("Nao existe um cadastro de produto com codigo %d para o restaurante de código %d", produtoId, restauranteId));
	}

}
