package com.algaworks.algafood.domain.exception;

public class FotoProdutoNaoEcontradaException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public FotoProdutoNaoEcontradaException(String mensagem) {
		super(mensagem);
	}

	public FotoProdutoNaoEcontradaException(Long restauranteId, Long produtoId) {
		this(String.format("Nao existe um cadastro de foto do produto com código %d para o restaurante de código %d", produtoId, restauranteId));
	}

}
