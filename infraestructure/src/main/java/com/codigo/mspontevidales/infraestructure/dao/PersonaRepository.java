package com.codigo.mspontevidales.infraestructure.dao;

import com.codigo.mspontevidales.infraestructure.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona,Long> {
    boolean existsByNumerodocumento(String numDoc);
    Optional<Persona> findByNumerodocumento(String numDoc);
}
