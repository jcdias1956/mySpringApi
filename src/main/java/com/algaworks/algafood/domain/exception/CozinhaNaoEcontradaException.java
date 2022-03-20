package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEcontradaException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public CozinhaNaoEcontradaException(String mensagem) {
		super(mensagem);
	}

	public CozinhaNaoEcontradaException(Long cozinhaId) {
		this(String.format("Nao existe um cadastro de cozinha com codigo %d.", cozinhaId));
	}

}
