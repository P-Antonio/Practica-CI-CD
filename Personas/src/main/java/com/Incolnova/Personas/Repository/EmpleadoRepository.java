package com.Incolnova.Personas.Repository;

import com.Incolnova.Personas.Entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<PersonaEntity, Long> {

    Optional findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE empleados SET deleted = false WHERE id = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);
}
