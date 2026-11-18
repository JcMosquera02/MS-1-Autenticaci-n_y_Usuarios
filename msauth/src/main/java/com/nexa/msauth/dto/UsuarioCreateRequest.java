package com.nexa.msauth.dto;

import com.nexa.msauth.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioCreateRequest {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El correo es obligatorio")
	@Email(message = "El correo no tiene un formato valido")
	private String correo;

	@NotBlank(message = "La contrasena es obligatoria")
	private String password;

	@NotNull(message = "El rol es obligatorio")
	private Rol rol;
}
