package com.algaworks.algafood.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//@ApiModel(value = "Cidade", description = "Repesenta uma cidade")
@Getter
@Setter
public class CidadeModel {
	
//	@ApiModelProperty(value = "ID da cidade", example = "1")
	@ApiModelProperty(example = "2")
	private Long id;

	@ApiModelProperty(example = "Belo Horizonte")
	private String nome;

	private EstadoModel estado;

}
