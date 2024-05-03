package com.codigo.mspontevidales.infraestructure.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Auditoria {
    private String usuacrea;
    private Timestamp datecreate;
    private String usuamodif;
    private Timestamp datemodif;
    private String usuadelet;
    private Timestamp datedelet;
}
