package com.Incolnova.Personas.Service;

import com.Incolnova.Personas.Entity.PersonaEntity;
import com.Incolnova.Personas.Repository.EmpleadoRepository;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import com.Incolnova.Personas.dto.request.EmpleadoUpdateRequest;
import com.Incolnova.Personas.dto.response.EmpleadoResponse;
import com.Incolnova.Personas.exeption.ResourceNotFoundExeption;
import com.Incolnova.Personas.mapper.EmpleadoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImpServiceEmpleado implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponse> findAll(Pageable pageable) {
        Page<PersonaEntity> empleadosPage = empleadoRepository.findAll(pageable);
        return empleadosPage.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse findById(Long id) {
        PersonaEntity persona = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Empleado", "id", id));
        return mapper.toResponse(persona);
    }

    @Override
    @Transactional
    public EmpleadoResponse save(EmpleadoRequest request) {
        PersonaEntity nuevaPersona = mapper.toEntity(request);
        PersonaEntity guardado = empleadoRepository.save(nuevaPersona);
        return mapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public EmpleadoResponse update(Long id, EmpleadoUpdateRequest request) {
        PersonaEntity existente = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: No existe!"));
        mapper.updateEntityFromDto(request, existente);
        return mapper.toResponse(empleadoRepository.save(existente));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        PersonaEntity persona = empleadoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Empleado no encontrado"));
        empleadoRepository.delete(persona);
    }

    @Override
    public void restaurarEmpleado(Long id){
        empleadoRepository.restoreById(id);
    }
}
