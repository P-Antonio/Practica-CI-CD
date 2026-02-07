package com.Incolnova.Personas.dto.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Lógica del validador
public class AdultValidator implements ConstraintValidator<Adult, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return ChronoUnit.YEARS.between(value, LocalDateTime.now()) >= 18;
    }
}
