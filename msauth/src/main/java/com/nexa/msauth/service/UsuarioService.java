package com.nexa.msauth.service;

import com.nexa.msauth.dto.UsuarioCreateRequest;
import com.nexa.msauth.dto.UsuarioResponse;
import com.nexa.msauth.dto.UsuarioUpdateRequest;
import com.nexa.msauth.exception.RecursoNoEncontradoException;
import com.nexa.msauth.exception.ReglaNegocioException;
import com.nexa.msauth.model.EstadoUsuario;
import com.nexa.msauth.model.Usuario;
import com.nexa.msauth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UsuarioResponse crear(UsuarioCreateRequest request) {
		if (usuarioRepository.existsByCorreo(request.getCorreo())) {
			throw new ReglaNegocioException("Ya existe un usuario con ese correo");
		}

		Usuario usuario = Usuario.builder()
				.nombre(request.getNombre())
				.correo(request.getCorreo())
				.password(passwordEncoder.encode(request.getPassword()))
				.rol(request.getRol())
				.estado(EstadoUsuario.ACTIVO)
				.build();

		usuario = usuarioRepository.save(usuario);
		return aResponse(usuario);
	}

	public List<UsuarioResponse> listar() {
		return usuarioRepository.findAll().stream()
				.filter(u -> u.getEstado() == EstadoUsuario.ACTIVO)
				.map(this::aResponse)
				.collect(Collectors.toList());
	}

	public UsuarioResponse obtenerPorId(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.filter(u -> u.getEstado() == EstadoUsuario.ACTIVO)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
		return aResponse(usuario);
	}

	public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request) {
		Usuario usuario = usuarioRepository.findById(id)
				.filter(u -> u.getEstado() == EstadoUsuario.ACTIVO)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

		usuario.setNombre(request.getNombre());
		usuario.setCorreo(request.getCorreo());

		if (request.getPassword() != null && !request.getPassword().isEmpty()) {
			usuario.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		if (request.getRol() != null) {
			usuario.setRol(request.getRol());
		}

		usuario = usuarioRepository.save(usuario);
		return aResponse(usuario);
	}

	public void eliminar(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.filter(u -> u.getEstado() == EstadoUsuario.ACTIVO)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

		usuario.setEstado(EstadoUsuario.INACTIVO);
		usuarioRepository.save(usuario);
	}

	public Usuario obtenerPorCorreo(String correo) {
		return usuarioRepository.findByCorreo(correo)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
	}

	private UsuarioResponse aResponse(Usuario usuario) {
		return UsuarioResponse.builder()
				.id(usuario.getId())
				.nombre(usuario.getNombre())
				.correo(usuario.getCorreo())
				.rol(usuario.getRol())
				.estado(usuario.getEstado())
				.fechaCreacion(usuario.getFechaCreacion())
				.build();
	}
}
