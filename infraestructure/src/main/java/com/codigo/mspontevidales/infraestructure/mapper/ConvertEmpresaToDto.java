package com.codigo.mspontevidales.infraestructure.mapper;

import com.codigo.mspontevidales.domain.aggregates.dto.*;
import com.codigo.mspontevidales.infraestructure.entity.Empresa;

import java.util.List;

public class ConvertEmpresaToDto {
    public static EmpresaDto getEmpresaDto(Empresa empresa){
        return EmpresaDto.builder()
                .id(empresa.getId())
                .razonsocial(empresa.getRazonsocial())
                .tipodocumento(empresa.getTipodocumento())
                .numerodocumento(empresa.getNumerodocumento())
                .estado(empresa.getEstado())
                .condicion(empresa.getCondicion())
                .direccion(empresa.getDireccion())
                .distrito(empresa.getDistrito())
                .provincia(empresa.getProvincia())
                .departamento(empresa.getDepartamento())
                .esagenteretencion(empresa.getEsagenteretencion())
                .usuacrea(empresa.getUsuacrea())
                .datecreate(empresa.getDatecreate())
                .usuamodif(empresa.getUsuamodif())
                .datemodif(empresa.getDatemodif())
                .usuadelet(empresa.getUsuadelet())
                .datedelet(empresa.getDatedelet())
                .build();
    }
    public static List<EmpresaDto> getListEmpresaDto(List<Empresa> e){
        return e.stream().map(ConvertEmpresaToDto::getEmpresaDto).toList();
    }
}
