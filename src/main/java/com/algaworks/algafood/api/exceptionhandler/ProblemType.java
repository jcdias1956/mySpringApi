package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	DADOS_INVALIDOS("/dados-invalidos", "Dados invalidos"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade esta sendo usada"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	ERRO_NEGOCIO("/erro-negocio", "Violacao de regra de negocio"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parametro invalido"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreencivel", "Mensagem incompreensivel"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso nao encontrado");

	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}
}
