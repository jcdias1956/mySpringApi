package com.algaworks.algafood.domain.exception;

public class EstadoNaoEcontradoException  extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public EstadoNaoEcontradoException(String mensagem) {
		super(mensagem);
	}

	public EstadoNaoEcontradoException(Long estadoId) {
		this(String.format("Nao existe um cadastro de estado com codigo %d.", estadoId));
	}

}
