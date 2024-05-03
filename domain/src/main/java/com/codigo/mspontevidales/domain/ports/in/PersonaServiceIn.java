package com.codigo.mspontevidales.domain.ports.in;

import com.codigo.mspontevidales.domain.aggregates.request.PersonaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PersonaServiceIn {
    ResponseEntity<BaseResponse> crearPersonaIn(PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> buscarXIdIn(Long id);
    ResponseEntity<BaseResponse> obtenerTodosIn();
    ResponseEntity<BaseResponse> actualizarIn(Long id, PersonaRequest personaRequest);
    ResponseEntity<BaseResponse> deleteIn(Long id);
}
