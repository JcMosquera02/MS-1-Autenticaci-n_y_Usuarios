package com.nexa.msauth.controller;

import com.nexa.msauth.dto.UsuarioCreateRequest;
import com.nexa.msauth.dto.UsuarioResponse;
import com.nexa.msauth.dto.UsuarioUpdateRequest;
import com.nexa.msauth.model.Usuario;
import com.nexa.msauth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/perfil")
	public ResponseEntity<UsuarioResponse> obtenerPerfil() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String correo = authentication.getName();
		Usuario usuario = usuarioService.obtenerPorCorreo(correo);

		UsuarioResponse response = UsuarioResponse.builder()
				.id(usuario.getId())
				.nombre(usuario.getNombre())
				.correo(usuario.getCorreo())
				.rol(usuario.getRol())
				.estado(usuario.getEstado())
				.fechaCreacion(usuario.getFechaCreacion())
				.build();

		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateRequest request) {
		UsuarioResponse response = usuarioService.crear(request);
		return ResponseEntity.status(201).body(response);
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponse>> listar() {
		List<UsuarioResponse> usuarios = usuarioService.listar();
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
		UsuarioResponse response = usuarioService.obtenerPorId(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id,
													  @Valid @RequestBody UsuarioUpdateRequest request) {
		UsuarioResponse response = usuarioService.actualizar(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		usuarioService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
