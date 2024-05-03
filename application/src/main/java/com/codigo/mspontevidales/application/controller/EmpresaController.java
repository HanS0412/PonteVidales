package com.codigo.mspontevidales.application.controller;

import com.codigo.mspontevidales.domain.aggregates.request.EmpresaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import com.codigo.mspontevidales.domain.ports.in.EmpresaServiceIn;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ms-ponte-vidales/v1/empresa")
@AllArgsConstructor
public class EmpresaController {
    private final EmpresaServiceIn empresaServiceIn;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getEmpresa(@PathVariable Long id){
        return empresaServiceIn.buscarXIdIn(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getEmpresas(){
        return empresaServiceIn.obtenerTodosIn();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> crearEmpresa(@RequestBody EmpresaRequest empresaRequest){
        return empresaServiceIn.crearEmpresaIn(empresaRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> modificarEmpresa(@PathVariable Long id, @RequestBody EmpresaRequest empresaRequest){
        return empresaServiceIn.actualizarIn(id,empresaRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminarEmpresa(@PathVariable Long id){
        return empresaServiceIn.deleteIn(id);
    }
}
