package com.codigo.mspontevidales.domain.ports.in;

import com.codigo.mspontevidales.domain.aggregates.request.EmpresaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface EmpresaServiceIn {
    ResponseEntity<BaseResponse> crearEmpresaIn(EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> buscarXIdIn(Long id);
    ResponseEntity<BaseResponse> obtenerTodosIn();
    ResponseEntity<BaseResponse> actualizarIn(Long id, EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> deleteIn(Long id);
}
