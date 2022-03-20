package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEcontradoException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public RestauranteNaoEcontradoException(String mensagem) {
		super(mensagem);
	}

	public RestauranteNaoEcontradoException(Long restauranteId) {
		this(String.format("Nao existe um cadastro de restaurante com codigo %d.", restauranteId));
	}

}
