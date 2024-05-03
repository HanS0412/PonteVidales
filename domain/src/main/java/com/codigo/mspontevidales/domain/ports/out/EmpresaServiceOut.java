package com.codigo.mspontevidales.domain.ports.out;

import com.codigo.mspontevidales.domain.aggregates.request.EmpresaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface EmpresaServiceOut {
    ResponseEntity<BaseResponse> crearEmpresaOut(EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> buscarXIdOut(Long id);
    ResponseEntity<BaseResponse> obtenerTodosOut();
    ResponseEntity<BaseResponse> actualizarOut(Long id, EmpresaRequest empresaRequest);
    ResponseEntity<BaseResponse> deleteOut(Long id);
}
