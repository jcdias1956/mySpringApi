package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta clase foi criada com o nome WebSecurityConfig e depois renomeada pra ResourceServerConfig
 * 
 * @author jcdias
 *
 */
@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Quem faz isto e o Authorization Server, porisso esta comentada
	 */
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("jose.dias")
//				.password(passwordEncoder().encode("123"))
//				.roles("ADMIN")
//				
//			.and()
//			.withUser("joao")
//				.password(passwordEncoder().encode("123"))
//				.roles("ADMIN");
//	}
	
	/**
	 * usado para configurar security nos requests quando nao usa Authorization Server
	 * por isso esta comentada
	 */
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		
//		http.httpBasic()
//			.and()
//			.authorizeRequests()
//				.antMatchers("/v1/cozinhas/**").permitAll()
//				.anyRequest().authenticated()
//		
//			.and()
//				.sessionManagement()
//					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//					
//			.and()
//				.csrf().disable();
//	}
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
				.anyRequest().authenticated()
		
			.and()
				.cors()
			.and()
				.oauth2ResourceServer().opaqueToken();
	}

	
}
