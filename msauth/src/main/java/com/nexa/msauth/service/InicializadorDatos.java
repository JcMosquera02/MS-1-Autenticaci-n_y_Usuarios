package com.nexa.msauth.service;

import com.nexa.msauth.model.EstadoUsuario;
import com.nexa.msauth.model.Rol;
import com.nexa.msauth.model.Usuario;
import com.nexa.msauth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InicializadorDatos implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${ms1.admin.password:}")
	private String adminPassword;

	@Override
	public void run(String... args) {
		if (adminPassword == null || adminPassword.isEmpty()) {
			return;
		}

		if (usuarioRepository.existsByCorreo("admin@nexa.com")) {
			return;
		}

		Usuario admin = Usuario.builder()
				.nombre("Administrador")
				.correo("admin@nexa.com")
				.password(passwordEncoder.encode(adminPassword))
				.rol(Rol.ADMIN)
				.estado(EstadoUsuario.ACTIVO)
				.build();

		usuarioRepository.save(admin);
	}
}
