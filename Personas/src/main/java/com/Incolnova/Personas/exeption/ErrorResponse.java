package com.Incolnova.Personas.exeption;

import java.time.LocalDateTime;

public record ErrorResponse(
        String mensaje,
        int codigo,
        LocalDateTime fecha,
        String detalles
) {
}
