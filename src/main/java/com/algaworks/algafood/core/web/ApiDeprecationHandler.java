package com.algaworks.algafood.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 1- para informar no header do response que a versao esta depreciada
 * 
 * 2- para desligar envie o response status GONE. 
 * 		Neste caso mude o nome da classe para ApiRetirementHandler para ficar coerente
 * 		e habilite o if correto ou exclua o do addHeader
 * 
 * 		e na classe SpringFoxConfig delete o metodo apiDocketV1
 * 
 * @author jcdias
 *
 */
@Component
public class ApiDeprecationHandler implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//1- para informar no header do response que a versao esta depreciada
		if (request.getRequestURI().startsWith("/v1/")) {
			response.addHeader("X-AlgaFood-Deprecated",
					"Essa versão da API está depreciada e deixará de existir a partir de 01/01/2021."
							+ "Use a versão mais atual da API.");
		}

		//2- para desligar envie o response status GONE
//		if (request.getRequestURI().startsWith("/v1/")) {
//			response.setStatus(HttpStatus.GONE.value());
//			return false;
//		}

		return true;
	}
}
