package com.codigo.mspontevidales.domain.aggregates.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class PersonaDto{
    private Long id;
    private String nombre;
    private String apellido;
    private String tipodocumento;
    private String numerodocumento;
    private String email;
    private String telefono;
    private String direccion;
    private Integer estado;
    private EmpresaDto empresa;
    private String usuacrea;
    private Timestamp datecreate;
    private String usuamodif;
    private Timestamp datemodif;
    private String usuadelet;
    private Timestamp datedelet;

}
