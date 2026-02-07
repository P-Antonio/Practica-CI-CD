package com.Incolnova.Personas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmpleadoUpdateRequest(
        @NotNull(message = "El ID es necesario para actualizar")
        Long id,
        @Size(min = 2, max = 50)
        String name,
        String lastName,
        @Email(message = "Si provee un email, debe ser válido")
        String email
) {
}
