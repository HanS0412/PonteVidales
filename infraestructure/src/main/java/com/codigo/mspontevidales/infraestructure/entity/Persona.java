package com.codigo.mspontevidales.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Persona extends Auditoria{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String tipodocumento;
    private String numerodocumento;
    private String email;
    private String telefono;
    private String direccion;
    private Integer estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id" , nullable = false)
    private Empresa empresa;

}
