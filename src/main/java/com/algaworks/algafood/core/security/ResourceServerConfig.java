package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Esta clase foi criada com o nome WebSecurityConfig e depois renomeada pra ResourceServerConfig
 * 
 * @author jcdias
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
//			.authorizeRequests()
//				.antMatchers(HttpMethod.POST, "/v1/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
//				.antMatchers(HttpMethod.PUT, "/v1/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
//				.antMatchers(HttpMethod.GET, "/v1/cozinhas/**").authenticated()
//				.anyRequest().denyAll()
//			.and()
			.csrf().disable()
			.cors()
			.and()
//				.oauth2ResourceServer()
//					.opaqueToken(); //usado na instrospeccao
				.oauth2ResourceServer()
					.jwt() // usado com o jwt
					.jwtAuthenticationConverter(jwtAuthenticationConverter());
	}

	/**
	 * para o Spring (Resource Server) entender a lista de authorities vindas no token jwd
	 * @return JwtAuthenticationConverter
	 */
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			var authorities = jwt.getClaimAsStringList("authorities");
			
			if (authorities == null) {
				authorities = Collections.emptyList();
			}
			
			// para pegar as authorities dos scopes e acrescenta as authorities das permissoes
			var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorites = scopesAuthoritiesConverter.convert(jwt);
			
			grantedAuthorites.addAll(authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
			
			return grantedAuthorites;
			
			// pega a lista de authorities e converte para uma lista SimpleGrantedAuthority
//			return authorities.stream()
//					.map(SimpleGrantedAuthority::new)
//					.collect(Collectors.toList());
		});
		
		return jwtAuthenticationConverter;
	}
	
	// usado com chave simetrica
//	@Bean
//	public JwtDecoder jwtDecoder() {
//		/* mesma chave usada no authorization server - jwtAccessTokenConverter.setSigningKey("algaworks");*/
//		var secretKey = new SecretKeySpec("xSZdhkYKWGFbPQFq0mi6bYd2wtqfqhBa".getBytes(), "HmacSHA256");
//		
//		return NimbusJwtDecoder.withSecretKey(secretKey).build();
//	}
	
}
