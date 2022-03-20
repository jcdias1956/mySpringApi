package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository 
	extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries,
	JpaSpecificationExecutor<Restaurante> {

	@Query("from Restaurante r "
			+ "join fetch r.cozinha ")
//			+ "join fetch r.formasPagamento")
	List<Restaurante> findAll();
	
	List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal tacaFinal);
	
	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);
	
	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal);
}
