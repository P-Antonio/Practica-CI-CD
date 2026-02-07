package com.Incolnova.Personas.mapper;

import com.Incolnova.Personas.Entity.PersonaEntity;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import com.Incolnova.Personas.dto.request.EmpleadoUpdateRequest;
import com.Incolnova.Personas.dto.response.EmpleadoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper (componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmpleadoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoDocumento", source = "tipoDocumento")
    PersonaEntity toEntity(EmpleadoRequest request);

    EmpleadoResponse toResponse(PersonaEntity entity);

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "inicioContrato", ignore = true)
    @Mapping(target = "finContrato", ignore = true)
    void updateEntityFromDto(EmpleadoUpdateRequest dto, @MappingTarget PersonaEntity entity);

    List<EmpleadoResponse> toResponseList(List<PersonaEntity> entities);
}