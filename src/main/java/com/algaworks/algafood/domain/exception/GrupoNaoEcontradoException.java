package com.algaworks.algafood.domain.exception;

public class GrupoNaoEcontradoException  extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public GrupoNaoEcontradoException(String mensagem) {
		super(mensagem);
	}

	public GrupoNaoEcontradoException(Long grupoId) {
		this(String.format("Nao existe um cadastro de grupo com codigo %d.", grupoId));
	}

}
