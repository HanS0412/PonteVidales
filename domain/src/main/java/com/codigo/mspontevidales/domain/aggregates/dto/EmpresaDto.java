package com.codigo.mspontevidales.domain.aggregates.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDto{
    private Long id ;
    private String razonsocial;
    private String tipodocumento;
    private String numerodocumento;
    private Integer estado;
    private String condicion;
    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;
    private Boolean esagenteretencion;
    private String usuacrea;
    private Timestamp datecreate;
    private String usuamodif;
    private Timestamp datemodif;
    private String usuadelet;
    private Timestamp datedelet;
}
