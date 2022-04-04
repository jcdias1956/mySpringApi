package com.algaworks.algafood.infrastructure.repository.spec;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;

public class PedidoSpecs {

	public static Specification<Pedido> usandoFiltro(PedidoFilter pedidoFilter) {
		return (root, query, builder) -> {

			// para nao dar exception quando usa paginacao, e nao fazer fetch quando for o count
			if (Pedido.class.equals(query.getResultType())) {
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
			}

			var predicates = new ArrayList<Predicate>();

			if (pedidoFilter.getClienteId() != null) {
				predicates.add(builder.equal(root.get("cliente"), pedidoFilter.getClienteId()));
			}

			if (pedidoFilter.getRestauranteId() != null) {
				predicates.add(builder.equal(root.get("restaurante"), pedidoFilter.getRestauranteId()));
			}

			if (pedidoFilter.getDataCriacaoInicio() != null) {
				predicates.add(
						builder.greaterThanOrEqualTo(root.get("dataCriacao"), pedidoFilter.getDataCriacaoInicio()));
			}

			if (pedidoFilter.getDataCriacaoFim() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), pedidoFilter.getDataCriacaoFim()));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};

	}

}
