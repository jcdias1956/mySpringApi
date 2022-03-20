package com.algaworks.algafood.domain.exception;

public class CidadeNaoEcontradaException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public CidadeNaoEcontradaException(String mensagem) {
		super(mensagem);
	}

	public CidadeNaoEcontradaException(Long cidadeId) {
		this(String.format("Nao existe um cadastro de cidade com codigo %d.", cidadeId));
	}

}
