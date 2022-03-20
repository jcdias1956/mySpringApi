package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.exception.CozinhaNaoEcontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaServive;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIntegrationIT {

	@Autowired
	private CadastroCozinhaServive cadastroCozinhaServive;
	
	@Test
	@DisplayName("Cadastro de cozinha com sucesso")
	@Order(1)
	public void deveAtribuirId_QuandoCadastrarComDadosCorretos() {
		
		// cenario
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// acao
		novaCozinha = cadastroCozinhaServive.salvar(novaCozinha);
		
		
		// validacao
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Cadastro de cozinha sem nome")
	@Order(2)
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {

		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		ConstraintViolationException erroEsperado = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			cadastroCozinhaServive.salvar(novaCozinha);
		});

		assertThat(erroEsperado).isNotNull();
	}

//	@Test
//	@Order(3)
//	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
//		
//		Long idCozinha = 1L;
//		
//		EntidadeEmUsoException erroEsperado = Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
//			cadastroCozinhaServive.excluir(idCozinha);
//		});
//
//		assertThat(erroEsperado).isNotNull();
//
//	}
	
	@Test
	@Order(4)
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		
		Long idCozinha = -1L;
		
		CozinhaNaoEcontradaException erroEsperado = Assertions.assertThrows(CozinhaNaoEcontradaException.class, () -> {
			cadastroCozinhaServive.excluir(idCozinha);
		});
		
		assertThat(erroEsperado).isNotNull();
		
	}
}
