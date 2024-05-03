package com.codigo.mspontevidales.domain.ports.out;

import com.codigo.mspontevidales.domain.aggregates.request.PersonaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PersonaServiceOut {
    ResponseEntity<BaseResponse> crearPersonaOut(PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> buscarXIdOut(Long id);
    ResponseEntity<BaseResponse> obtenerTodosOut();
    ResponseEntity<BaseResponse> actualizarOut(Long id, PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> deleteOut(Long id);
}
