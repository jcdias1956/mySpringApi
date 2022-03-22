package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		// usando as regras do ModelMapper
		return new ModelMapper();
		
		// para customizar alguma mapeamento que o modelMapper nao consegue
//		var modelMapper = new ModelMapper();
//		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//			.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
//		return modelMapper;
	}
}
