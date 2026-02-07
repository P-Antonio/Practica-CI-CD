package com.Incolnova.Personas.Service;

import com.Incolnova.Personas.Entity.PersonaEntity;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import com.Incolnova.Personas.dto.request.EmpleadoUpdateRequest;
import com.Incolnova.Personas.dto.response.EmpleadoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmpleadoService {


    Page<EmpleadoResponse> findAll(Pageable pageable);
    EmpleadoResponse findById(Long id);
    EmpleadoResponse save(EmpleadoRequest request);
    EmpleadoResponse update(Long id, EmpleadoUpdateRequest request);
    void deleteById(Long id);
    void restaurarEmpleado (Long id);
}
