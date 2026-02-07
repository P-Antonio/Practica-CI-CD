package com.Incolnova.Personas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmpleadoResponse (
        @Schema(example = "1") Long id,
        @Schema(example = "Pedro") String name,
        @Schema(example = "Leon") String lastName,
        @Schema(example = "pedroleon@email.com") String email
){
}
