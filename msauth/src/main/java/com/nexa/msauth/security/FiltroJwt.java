package com.nexa.msauth.security;

import com.nexa.msauth.service.JwtService;
import com.nexa.msauth.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroJwt extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		if (!jwtService.validarToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		String correo = jwtService.getCorreo(token);
		String rol = jwtService.getRol(token);

		var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
		var authentication = new UsernamePasswordAuthenticationToken(correo, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
