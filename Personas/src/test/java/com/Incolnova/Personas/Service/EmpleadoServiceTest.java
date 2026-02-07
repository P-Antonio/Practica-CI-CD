package com.Incolnova.Personas.Service;

import com.Incolnova.Personas.Repository.EmpleadoRepository;
import com.Incolnova.Personas.exeption.ResourceNotFoundExeption;
import com.Incolnova.Personas.mapper.EmpleadoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock private EmpleadoRepository empleadoRepository;
    @Mock private EmpleadoMapper empleadoMapper;
    @InjectMocks private ImpServiceEmpleado serviceEmpleado;

    @Test
    void findById_debeLanzarException_CuandoNoExiste(){
        //Given
        when(empleadoRepository.findById(1L)).thenReturn(Optional.empty());

        //when & Then
        assertThrows(ResourceNotFoundExeption.class, ()-> serviceEmpleado.findById(1L));
    }
}
