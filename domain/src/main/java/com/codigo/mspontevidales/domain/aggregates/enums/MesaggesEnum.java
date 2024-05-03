package com.codigo.mspontevidales.domain.aggregates.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MesaggesEnum {
    OPERATION_OK("0000", "Operacion realizada con exito."),
    EMPRESA_EXISTS("4001","La empresa ya se encuentra registrada"),
    EMPRESAS_NOT_EXISTS("4002","No existen empresas registradas"),
    EMPRESA_NOT_FOUND("4003","La empresa no se encuentra registrada"),
    PERSONA_EXISTS("4004","La persona ya se encuentra registrada"),
    PERSONAS_NOT_EXISTS("4005","No existen personas registradas"),
    PERSONA_NOT_FOUND("4006","La persona no se encuentra registrada"),
    PERSONA_PERTENECE_EMPRESA("4007","La persona con quien se quiere actualizar el registro ya pertenece a una empresa");

    private final String code;
    private final String description;
}
