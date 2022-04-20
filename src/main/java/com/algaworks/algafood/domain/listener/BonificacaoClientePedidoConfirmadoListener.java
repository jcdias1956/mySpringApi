package com.algaworks.algafood.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;

@Component
public class BonificacaoClientePedidoConfirmadoListener {
	
	@EventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		
		System.out.println("Calculando pontos para o cliente " + event.getPedido().getCliente().getNome());
	}

}
