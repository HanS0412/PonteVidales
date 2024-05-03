package com.codigo.mspontevidales.infraestructure.mapper;

import com.codigo.mspontevidales.domain.aggregates.dto.EmpresaDto;
import com.codigo.mspontevidales.domain.aggregates.dto.PersonaDto;
import com.codigo.mspontevidales.infraestructure.entity.Empresa;
import com.codigo.mspontevidales.infraestructure.entity.Persona;

import java.util.List;

public class ConvertPersonaToDto {
    public static PersonaDto getPersonaDto(Persona persona){
        return PersonaDto.builder()
                .id(persona.getId())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .tipodocumento(persona.getTipodocumento())
                .numerodocumento(persona.getNumerodocumento())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .estado(persona.getEstado())
                .usuacrea(persona.getUsuacrea())
                .datecreate(persona.getDatecreate())
                .usuamodif(persona.getUsuamodif())
                .datemodif(persona.getDatemodif())
                .usuadelet(persona.getUsuadelet())
                .datedelet(persona.getDatedelet())
                .empresa(ConvertEmpresaToDto.getEmpresaDto(persona.getEmpresa()))
                .build();
    }
    public static List<PersonaDto> getListPersonaDto(List<Persona> p){
        return p.stream().map(ConvertPersonaToDto::getPersonaDto).toList();
    }
}
