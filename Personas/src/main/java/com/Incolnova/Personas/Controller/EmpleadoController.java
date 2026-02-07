package com.Incolnova.Personas.Controller;

import com.Incolnova.Personas.Service.ImpServiceEmpleado;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import com.Incolnova.Personas.dto.request.EmpleadoUpdateRequest;
import com.Incolnova.Personas.dto.response.EmpleadoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Empleado", description = "Operaciones para el crud de los empleados")
@RestController
@RequestMapping("/empleado")
@RequiredArgsConstructor
public class EmpleadoController {

    private final ImpServiceEmpleado service;

    @Operation(summary = "Obtener todos los empleados", description = "Retorna una lista con todos los empleados registrados")
    @GetMapping("/All")
    ResponseEntity<Page<EmpleadoResponse>> obtenerEmpleados (@PageableDefault(size = 10, page = 0, sort = "name")Pageable pageable){
        Page<EmpleadoResponse> empleados = service.findAll(pageable);

        if (empleados.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empleados);
    }

    @Operation(summary = "Obtener un empleado especifico", description = "Retorna un empleado con id especifico")
    @GetMapping("/empleado/{id}")
    ResponseEntity<EmpleadoResponse> empleadoById (@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> create (@Valid @RequestBody EmpleadoRequest empleadoRequest){
        EmpleadoResponse creado = service.save(empleadoRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Actualizar empleado", description = "Actualiza campos específicos de un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpleadoResponse> actualizarEmpleado (@PathVariable Long id, @RequestBody @Valid EmpleadoUpdateRequest updateEmpleado){
        EmpleadoResponse actualizado = service.update(id, updateEmpleado);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar empleado", description = "Realiza un borrado lógico del empleado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurarEmpleado (@PathVariable Long id){
        service.restaurarEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}
