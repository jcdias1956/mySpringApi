package com.algaworks.algafood.domain.exception;

public class PermissaoNaoEcontradaException  extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public PermissaoNaoEcontradaException(String mensagem) {
		super(mensagem);
	}

	public PermissaoNaoEcontradaException(Long permissaoId) {
		this(String.format("Nao existe um cadastro de permissao com codigo %d.", permissaoId));
	}

}
