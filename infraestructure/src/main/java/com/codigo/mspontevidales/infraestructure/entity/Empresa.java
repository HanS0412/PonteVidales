package com.codigo.mspontevidales.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EmpresaInfo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Empresa extends Auditoria{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
}
