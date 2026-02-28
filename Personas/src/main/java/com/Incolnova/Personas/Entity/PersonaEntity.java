package com.Incolnova.Personas.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
@Entity
@Table (name = "Empleados")
@SQLDelete(sql = "UPDATE Empleados SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated (EnumType.STRING)
    @Column (name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;
    @Column (name = "numero_documento", unique = true, nullable = false, length = 50)
    private String numeroDocumento;
    @Column (name = "nombre", nullable = false, length = 100)
    private String name;
    @Column (name = "apellido", nullable = false, length = 100)
    private String lastName;
    @Column (name = "correo_electronico", unique = true ,nullable = false)
    private String email;
    @Column(name = "direccion", length = 255)
    private String address;
    @Column(name = "fecha_nacimiento")
    private LocalDateTime fechaNacimiento;
    @Column(name = "fecha_inicio_contrato")
    private LocalDateTime inicioContrato;
    @Column(name = "fecha_fin_contrato")
    private LocalDateTime finContrato;
    private boolean deleted = Boolean.FALSE;
}

