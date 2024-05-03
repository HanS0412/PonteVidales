package com.codigo.mspontevidales.infraestructure.dao;


import com.codigo.mspontevidales.infraestructure.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa,Long> {
    boolean existsByNumerodocumento(String numDoc);
    Optional<Empresa> findByNumerodocumento(String numDoc);
}
