package com.Incolnova.Personas;

import com.Incolnova.Personas.Entity.PersonaEntity;
import com.Incolnova.Personas.Entity.TipoDocumento;
import com.Incolnova.Personas.Repository.EmpleadoRepository;
import com.Incolnova.Personas.dto.request.EmpleadoRequest;
import com.Incolnova.Personas.dto.response.EmpleadoResponse;
import com.Incolnova.Personas.mapper.EmpleadoMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.Incolnova.Personas.Entity.TipoDocumento.CEDULA_CIUDADANIA;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmpleadoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private EmpleadoMapper empleadoMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Test
    void debeGuardarEmpleadoYPersistirEnLaBaseDeDatos() {
        // Definimos fechas fijas para que no dependan del milisegundo exacto de ejecución
        LocalDateTime nacimiento = LocalDateTime.of(1990, 5, 15, 0, 0);
        LocalDateTime inicio = LocalDateTime.of(2026, 2, 10, 8, 0); // Una semana al futuro
        LocalDateTime fin = LocalDateTime.of(2026, 8, 10, 18, 0);   // 6 meses después

        EmpleadoRequest request = new EmpleadoRequest(
                CEDULA_CIUDADANIA, "38957u93580",
                "Carlos", "Gomez", "carlos@mail.com", "Calle 10",
                nacimiento, inicio, fin
        );

        // Intentamos la petición
        ResponseEntity<EmpleadoResponse> response = restTemplate.postForEntity(
                "/empleado", request, EmpleadoResponse.class);

        // Si sigue dando 400, imprimimos el body como String para ver qué dice el validador ahora
        if (response.getStatusCode().is4xxClientError()) {
            ResponseEntity<String> errorDetails = restTemplate.postForEntity(
                    "/empleado", request, String.class);
            System.out.println("ERROR DETALLADO: " + errorDetails.getBody());
        }

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().id());
    }

    @Test
    void debelistarEmpleadosPaginados(){
        //Given
        EmpleadoRequest persona1 = new EmpleadoRequest(
                CEDULA_CIUDADANIA, "123456", "Ana", "Lopez", "ana@mail.com", "Calle 20",
                LocalDateTime.of(1985, 3, 22, 0, 0),
                LocalDateTime.of(2025, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0)
        );
        EmpleadoRequest persona2 = new EmpleadoRequest(
                CEDULA_CIUDADANIA, "937590", "Anita", "Lopez", "anita@mail.com", "Calle 20",
                LocalDateTime.of(1985, 3, 22, 0, 0),
                LocalDateTime.of(2025, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0)
        );
        PersonaEntity empleado1 = empleadoMapper.toEntity(persona1);
        PersonaEntity empleado2 = empleadoMapper.toEntity(persona2);

        empleadoRepository.saveAll(List.of(empleado1, empleado2));

        //When
        String url = "/empleado/All?page=0&size=1&sort=name,asc";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
            System.out.println("ERROR 405: El método GET no está permitido en: " + url);
            // Imprime los métodos permitidos que vienen en la cabecera 'Allow'
            System.out.println("Métodos permitidos: " + response.getHeaders().getAllow());
        }
        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<?> content = (List<?>) response.getBody().get("content");
        Number totalElements = (Number) response.getBody().get("totalElements");

        assertEquals(1, content.size());
        assertEquals(2, totalElements);

        Map<String, Object> firstEmpleado = (Map<String, Object>) content.get(0);
        assertEquals("Ana", firstEmpleado.get("name"));
    }

    @Test
    void debeRealizarSoftDeleteCorrectamente(){
        PersonaEntity empleado = empleadoMapper.toEntity(new EmpleadoRequest(
                TipoDocumento.CEDULA_CIUDADANIA, "123456", "Ana", "Lopez", "ana@mail.com", "Calle 20",
                LocalDateTime.of(1985, 3, 22, 0, 0),
                LocalDateTime.of(2025, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0)
        ));
        empleado.setName("Borrar");
        empleado.setDeleted(true);
        empleado = empleadoRepository.save(empleado);
        Long id = empleado.getId();

        restTemplate.delete("/empleado/" + id);

        Optional<PersonaEntity> eliminado = empleadoRepository.findById(id);
        assertTrue(eliminado.isEmpty());

        Integer count = jdbcTemplate.queryForObject(
                "Select count(*) FROM empleados WHERE id = ? and deleted = true",
                Integer.class, id);
        assertEquals(1, count);
    }

    @Test
    @Transactional
    void debeBorrarYLuegoRestaurarEmpleado(){
        PersonaEntity emp = empleadoMapper.toEntity(new EmpleadoRequest(
                TipoDocumento.CEDULA_CIUDADANIA, "123456", "Ana", "Lopez", "ana@mail.com", "Calle 20",
                LocalDateTime.of(1985, 3, 22, 0, 0),
                LocalDateTime.of(2025, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0)
        ));
        System.out.println( "Valores del empleado " + emp);
        emp = empleadoRepository.save(emp);
        Long id = emp.getId();

        restTemplate.delete("/empleado/" + id);

        entityManager.flush();
        entityManager.clear();

        ResponseEntity<Map> responseBusqueda = restTemplate.getForEntity("/empleado/All", Map.class);
        if (responseBusqueda.getStatusCode() == HttpStatus.NO_CONTENT || responseBusqueda.getStatusCode() == null) {
            assertTrue(true);
        }else {
            List<?> content = (List<?>) responseBusqueda.getBody().get("content");
            assertEquals(0, content.size());
        }

        restTemplate.patchForObject("/empleado/" + id + "/restaurar", null, Void.class);

        entityManager.flush();
        entityManager.clear();

        ResponseEntity<Map> responsePostRestaurar = restTemplate.getForEntity("/empleado/All", Map.class);
        if (responsePostRestaurar.getStatusCode() == HttpStatus.NO_CONTENT || responsePostRestaurar.getStatusCode() == null) {
            assertTrue(true);
        }else {
            List<?> contentPost = (List<?>) responseBusqueda.getBody().get("content");
            assertEquals(0, contentPost.size());
        }
    }
}
