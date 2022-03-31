package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEcontradoException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public UsuarioNaoEcontradoException(String mensagem) {
		super(mensagem);
	}

	public UsuarioNaoEcontradoException(Long usuarioId) {
		this(String.format("Nao existe um cadastro de usuario com codigo %d.", usuarioId));
	}

}
