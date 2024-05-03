package com.codigo.mspontevidales.application.controller;

import com.codigo.mspontevidales.domain.aggregates.request.PersonaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import com.codigo.mspontevidales.domain.ports.in.PersonaServiceIn;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ms-ponte-vidales/v1/persona")
@AllArgsConstructor
public class PersonaController {
    private final PersonaServiceIn personaServiceIn;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPersona(@PathVariable Long id){
        return personaServiceIn.buscarXIdIn(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getPersonas(){
        return personaServiceIn.obtenerTodosIn();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> crearPersona(@RequestBody PersonaRequest personaRequest){
        return personaServiceIn.crearPersonaIn(personaRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> modificarPersona(@PathVariable Long id, @RequestBody PersonaRequest personaRequest){
        return personaServiceIn.actualizarIn(id,personaRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> eliminarPersona(@PathVariable Long id){
        return personaServiceIn.deleteIn(id);
    }
}
