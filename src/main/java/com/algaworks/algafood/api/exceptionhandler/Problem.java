package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Problema")
@Getter
@Builder()
@JsonInclude(value = Include.NON_NULL)
public class Problem {

	@ApiModelProperty(example = "400", position = 1)
	private Integer status;
	
	@ApiModelProperty(example = "2022-04-28T14:46:38.206113Z", position = 5)
	private OffsetDateTime timestamp;
	
	@ApiModelProperty(example = "https://algafood.com.br/dados-invalidos", position = 10)
	private String type;
	
	@ApiModelProperty(example = "Dados invalidos", position = 15)
	private String title;
	
	@ApiModelProperty(example = "Um ou mais campos estao invalidos. Faca o preenchimento correto e tente novamente.", position = 20)
	private String detail;
	
	@ApiModelProperty(example = "Um ou mais campos estao invalidos. Faca o preenchimento correto e tente novamente.", position = 25)
	private String userMessage;
	
	@ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro(opcional)", position = 30)
	private List<Object> objects;
	
	@ApiModel("ObjetoProblema")
	@Getter
	@Builder
	public static class Object {
		
		@ApiModelProperty(example = "preco")
		private String name;
		
		@ApiModelProperty(example = "o preco e obrigatorio")
		private String usrMessage;
	}
}
