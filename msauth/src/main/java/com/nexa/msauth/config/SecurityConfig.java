package com.nexa.msauth.config;

import com.nexa.msauth.security.AccesoDenegadoHandler;
import com.nexa.msauth.security.FiltroJwt;
import com.nexa.msauth.security.PuntoEntradaNoAutenticado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private FiltroJwt filtroJwt;

	@Autowired
	private PuntoEntradaNoAutenticado puntoEntradaNoAutenticado;

	@Autowired
	private AccesoDenegadoHandler accesoDenegadoHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint(puntoEntradaNoAutenticado)
						.accessDeniedHandler(accesoDenegadoHandler)
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/login").permitAll()
						.requestMatchers("/api/usuarios/perfil").authenticated()
						.requestMatchers("/api/usuarios/**").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
