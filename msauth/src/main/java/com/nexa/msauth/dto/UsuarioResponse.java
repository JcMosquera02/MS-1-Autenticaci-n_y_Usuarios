package com.nexa.msauth.dto;

import com.nexa.msauth.model.EstadoUsuario;
import com.nexa.msauth.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

	private Long id;
	private String nombre;
	private String correo;
	private Rol rol;
	private EstadoUsuario estado;
	private LocalDateTime fechaCreacion;
}
