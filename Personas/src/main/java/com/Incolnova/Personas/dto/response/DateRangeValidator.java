package com.Incolnova.Personas.dto.response;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;

public class DateRangeValidator  implements ConstraintValidator<DateRange, Object> {

    private String startFieldName;
    private String endFieldName;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startFieldName = constraintAnnotation.start();
        this.endFieldName = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object startDate = new BeanWrapperImpl(value).getPropertyValue(startFieldName);
            Object endDate = new BeanWrapperImpl(value).getPropertyValue(endFieldName);

            if (startDate == null || endDate == null) return true; // Deja que @NotNull se encargue

            // LA LÓGICA CORRECTA:
            // El inicio debe ser ANTES que el fin.
            // Si (inicio.isAfter(fin)) -> ¡ERROR!
            return ((LocalDateTime) startDate).isBefore((LocalDateTime) endDate);

        } catch (Exception e){
            return false;
        }
    }
}
