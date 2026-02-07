package com.Incolnova.Personas.Controller;

import com.Incolnova.Personas.Entity.TipoDocumento;
import com.Incolnova.Personas.Service.EmpleadoService;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import com.Incolnova.Personas.dto.response.EmpleadoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private EmpleadoService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_Debe_Retornar201_CuandoEsValido() throws Exception {
        //Given
        EmpleadoRequest request = new EmpleadoRequest( TipoDocumento.CEDULA_CIUDADANIA, "343565", "Ana", "Gomez", "ana@mail.com", "Carrera 10",
                LocalDateTime.of(1990, 12, 4, 12, 30), LocalDateTime.now(), LocalDateTime.now().plusDays(20));
        EmpleadoResponse response = new EmpleadoResponse(1L, "Ana", "Gomez", "ana@mail.com");

        when(service.save(any(EmpleadoRequest.class))).thenReturn(response);

        //When & Then
        mockMvc.perform(post("/empleado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Ana"))
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void create_DebeRetornar400_CuandoEmailEsInvalido()throws Exception{
        //Given (Email mal formado)
        EmpleadoRequest requestInvalido = new EmpleadoRequest(TipoDocumento.CEDULA_CIUDADANIA, "3435356","Ana", "Gomez", "email-falso", "Calle 1",
                LocalDateTime.of(1990, 12, 4, 12, 30), LocalDateTime.now(), LocalDateTime.now().plusDays(200));
        //When & Then
        mockMvc.perform(post("/empleado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void create_DebeRetornar400_CuandoEmpleadoEsMenorDeEdad()throws Exception{
        //Given
        EmpleadoRequest menorDeEdad = new EmpleadoRequest(
                TipoDocumento.CEDULA_CIUDADANIA,
                "3984uy95",
                "Pepito",
                "Perez",
                "Pepito@mail.com",
                "Calle Falsa 1234",
                LocalDateTime.now(),
                LocalDateTime.of(2020, 1, 1 ,0, 0),
                LocalDateTime.now().plusDays(200)
        );

        //When & Then
        mockMvc.perform(post("/empleado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menorDeEdad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(containsString("Mayor de 18 años")));
    }

}
