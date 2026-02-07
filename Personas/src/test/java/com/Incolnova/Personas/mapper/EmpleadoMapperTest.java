package com.Incolnova.Personas.mapper;

import com.Incolnova.Personas.Entity.PersonaEntity;
import com.Incolnova.Personas.Entity.TipoDocumento;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class EmpleadoMapperTest {

    private final EmpleadoMapper mapper = Mappers.getMapper(EmpleadoMapper.class);

    @Test
    void debeMapearEmpleadoRequestAPersonaEntity(){
        //Given
        EmpleadoRequest request = new EmpleadoRequest( TipoDocumento.CEDULA_CIUDADANIA, "8359759", "Pedro", "leon", "pedroleon@email.com", "El salitre",
                LocalDateTime.now(), LocalDateTime.of(1990, 12, 4, 12, 30),LocalDateTime.now().plusDays(10));

        //When
        PersonaEntity entity = mapper.toEntity(request);

        //Then
        assertEquals(request.name(), entity.getName());
        assertEquals(request.address(), entity.getAddress());
    }
}
