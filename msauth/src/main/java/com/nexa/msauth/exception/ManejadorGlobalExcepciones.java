package com.nexa.msauth.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
	}

	@ExceptionHandler(ReglaNegocioException.class)
	public ResponseEntity<Map<String, Object>> manejarReglaNegocio(ReglaNegocioException ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<Map<String, Object>> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", "Error de validacion");

		Map<String, String> errores = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errores.put(error.getField(), error.getDefaultMessage());
		}
		cuerpo.put("errores", errores);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(AccessDeniedException ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", "Acceso denegado. No tiene permisos suficientes.");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(cuerpo);
	}

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<Map<String, Object>> manejarJwtInvalido(JwtException ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", "Token JWT invalido");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("mensaje", "Error interno del servidor");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
	}
}
