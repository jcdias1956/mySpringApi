package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEcontradaException  extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public FormaPagamentoNaoEcontradaException(String mensagem) {
		super(mensagem);
	}

	public FormaPagamentoNaoEcontradaException(Long formaPagamentoId) {
		this(String.format("Nao existe um cadastro de forma de pagamento com codigo %d.", formaPagamentoId));
	}

}
