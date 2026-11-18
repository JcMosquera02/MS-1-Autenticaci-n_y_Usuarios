package com.nexa.msauth.service;

import com.nexa.msauth.dto.LoginRequest;
import com.nexa.msauth.dto.LoginResponse;
import com.nexa.msauth.exception.CredencialesInvalidasException;
import com.nexa.msauth.model.EstadoUsuario;
import com.nexa.msauth.model.Usuario;
import com.nexa.msauth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public LoginResponse login(LoginRequest request) {
		Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
				.orElseThrow(() -> new CredencialesInvalidasException("Correo o contrasena incorrectos"));

		if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
			throw new CredencialesInvalidasException("Correo o contrasena incorrectos");
		}

		if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
			throw new CredencialesInvalidasException("La cuenta esta inactiva");
		}

		String token = jwtService.generarToken(usuario.getId(), usuario.getCorreo(), usuario.getRol().name());

		return LoginResponse.builder()
				.token(token)
				.tipo("Bearer")
				.id(usuario.getId())
				.nombre(usuario.getNombre())
				.correo(usuario.getCorreo())
				.rol(usuario.getRol().name())
				.build();
	}
}
