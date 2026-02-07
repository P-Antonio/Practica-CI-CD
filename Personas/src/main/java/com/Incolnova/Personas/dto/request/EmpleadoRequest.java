package com.Incolnova.Personas.dto.request;

import com.Incolnova.Personas.Entity.TipoDocumento;
import com.Incolnova.Personas.dto.response.DateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@DateRange(start = "inicioContrato", end = "finContrato", message = "Error: EL contrato no puede terminar antes de empezar")
public record EmpleadoRequest(
        @NotBlank(message = "Seleccionar el documento")
        TipoDocumento tipoDocumento,
        @NotBlank(message = "No puede ingresar sin el Numero de documento de identificacion")
        String numeroDocumento,
        @NotBlank(message = "El nombre no puede estar vacio")
        @Size(min = 2, max = 50, message = "El nombre debe tener por lo menos entre 2 y 50 caracteres")
        String name,
        @NotBlank(message = "El apellido no puede estar vacío")
        String lastName,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,
        @NotBlank(message = "La dirección es obligatoria")
        String address,
        @NotNull
        @Adult(message = "El empleado debe ser mayor de edad")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime fechaNacimiento,
        @NotNull(message = "La fecha de inicio de contrato es obligatoria")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime inicioContrato,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime finContrato
) {
}
