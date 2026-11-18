package com.nexa.msauth.dto;

import com.nexa.msauth.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioUpdateRequest {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El correo es obligatorio")
	@Email(message = "El correo no tiene un formato valido")
	private String correo;

	private String password;

	private Rol rol;
}
