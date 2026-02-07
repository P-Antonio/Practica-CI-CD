package com.Incolnova.Personas.dto.response;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface DateRange {

    String message () default "La fecha de fin de contrato debe ser posterior a la fecha de inicio";
    Class<?> groups()[] default {};
    Class<? extends Payload> [] payload () default {};

    String start();
    String end();
}
