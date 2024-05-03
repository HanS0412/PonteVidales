package com.codigo.mspontevidales.infraestructure.adapters;

import com.codigo.mspontevidales.domain.aggregates.constants.Constant;
import com.codigo.mspontevidales.domain.aggregates.dto.PersonaDto;
import com.codigo.mspontevidales.domain.aggregates.dto.ReniecDto;
import com.codigo.mspontevidales.domain.aggregates.enums.MesaggesEnum;
import com.codigo.mspontevidales.domain.aggregates.request.PersonaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import com.codigo.mspontevidales.domain.ports.out.PersonaServiceOut;
import com.codigo.mspontevidales.infraestructure.client.ReniecClient;
import com.codigo.mspontevidales.infraestructure.dao.EmpresaRepository;
import com.codigo.mspontevidales.infraestructure.dao.PersonaRepository;
import com.codigo.mspontevidales.infraestructure.entity.Empresa;
import com.codigo.mspontevidales.infraestructure.entity.Persona;
import com.codigo.mspontevidales.infraestructure.mapper.ConvertPersonaToDto;
import com.codigo.mspontevidales.infraestructure.redis.RedisService;
import com.codigo.mspontevidales.infraestructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import static java.util.Objects.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonaAdapter implements PersonaServiceOut {
    private final EmpresaRepository empresaRepository;
    private final PersonaRepository personaRepository;
    private final ReniecClient reniecClient;
    private final RedisService redisService;

    @Value("${token.client}")
    private String token;

    @Override
    public ResponseEntity<BaseResponse> crearPersonaOut(PersonaRequest personaRequest) {
        boolean exists = personaRepository.existsByNumerodocumento(personaRequest.getNumDoc());
        if(!exists){
            Persona persona = getPersona(personaRequest , Constant.ACTION_CREATE, null);
            if(!isNull(persona)) {
                PersonaDto personaDto = ConvertPersonaToDto.getPersonaDto(personaRepository.save(persona));
                return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(personaDto)));
            }else{
                BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.EMPRESA_NOT_FOUND.getCode(),
                            MesaggesEnum.EMPRESA_NOT_FOUND.getDescription(),
                            Optional.empty());
                return ResponseEntity.ok(baseResponse);
            }
        }else{
            BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.PERSONA_EXISTS.getCode(),
                    MesaggesEnum.PERSONA_EXISTS.getDescription(),
                    Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> buscarXIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENERPERSONA+id);
        if(!isNull(redisInfo)){
            PersonaDto personaDto = Util.convertJsonToDto(redisInfo,PersonaDto.class);
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(personaDto)));
        }else {
            Optional<Persona> op = personaRepository.findById(id);
            if (op.isPresent()){
                PersonaDto dto = ConvertPersonaToDto.getPersonaDto(op.get());
                String json = Util.convertDtoToJson(dto);
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENERPERSONA+id,json,10);
                return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(dto)));
            }else{
                return ResponseEntity.ok(getBaseResponseNotFound(Optional.empty()));
            }
        }
    }

    @Override
    public ResponseEntity<BaseResponse> obtenerTodosOut() {
        List<Persona> personas = personaRepository.findAll();
        if(!personas.isEmpty()){
            List<PersonaDto> personaDto = ConvertPersonaToDto.getListPersonaDto(personas);
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(personaDto)));
        }else{
            BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.PERSONAS_NOT_EXISTS.getCode(),
                    MesaggesEnum.PERSONAS_NOT_EXISTS.getDescription(),
                    Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> actualizarOut(Long id, PersonaRequest personaRequest) {
        Optional<Persona> op = personaRepository.findById(id);
        if(op.isPresent()){
            Optional<Persona> personaToUpdate = personaRepository.findByNumerodocumento(personaRequest.getNumDoc());
            if(!personaToUpdate.isPresent()){
                Persona persona = getPersona(personaRequest , Constant.ACTION_UPDATE,id);
                if(!isNull(persona)){
                    PersonaDto personaDto = ConvertPersonaToDto.getPersonaDto(personaRepository.save(persona));
                    String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENERPERSONA+id);
                    if(!isNull(redisInfo)){
                        redisService.deleteKey(Constant.REDIS_KEY_OBTENERPERSONA+id);
                        String json = Util.convertDtoToJson(personaDto);
                        redisService.saveInRedis(Constant.REDIS_KEY_OBTENERPERSONA+id,json,10);
                    }
                    return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(personaDto)));
                }else{
                    BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.EMPRESA_NOT_FOUND.getCode(),
                            MesaggesEnum.EMPRESA_NOT_FOUND.getDescription(),
                            Optional.empty());
                    return ResponseEntity.ok(baseResponse);
                }
            }else{
                BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.PERSONA_PERTENECE_EMPRESA.getCode(),
                        MesaggesEnum.PERSONA_PERTENECE_EMPRESA.getDescription(),
                        Optional.empty());
                return ResponseEntity.ok(baseResponse);
            }

        }else{
            return ResponseEntity.ok(getBaseResponseNotFound(Optional.empty()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> deleteOut(Long id) {
        Optional<Persona> op = personaRepository.findById(id);
        if(op.isPresent()){
            Persona persona = op.get();
            persona.setEstado(0);
            persona.setUsuadelet(Constant.USERDELETE);
            persona.setDatedelet(getTime());
            PersonaDto personaDto = ConvertPersonaToDto.getPersonaDto(personaRepository.save(persona));
            String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENERPERSONA+id);
            if(!isNull(redisInfo)){
                redisService.deleteKey(Constant.REDIS_KEY_OBTENERPERSONA+id);
                String json = Util.convertDtoToJson(personaDto);
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENERPERSONA+id,json,10);
            }
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(personaDto)));
        }else {
            return ResponseEntity.ok(getBaseResponseNotFound(Optional.empty()));
        }
    }

    private ReniecDto getExecReniec(String numDoc){
        String authorization = "Bearer "+token;
        return reniecClient.getInfoReniec(numDoc,authorization);
    }
    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }

    private Persona getPersona(PersonaRequest personaRequest, String action, Long id){
        ReniecDto info = getExecReniec(personaRequest.getNumDoc());
        Optional<Empresa> empresa = empresaRepository.findByNumerodocumento(personaRequest.getEmpresa());
        if (empresa.isPresent()){
            Persona persona = Persona.builder()
                    .nombre(info.getNombres())
                    .apellido(info.getApellidoPaterno()+" "+info.getApellidoMaterno())
                    .tipodocumento(info.getTipoDocumento())
                    .numerodocumento(info.getNumeroDocumento())
                    .email(info.getApellidoPaterno().substring(0,2).toLowerCase()+info.getNumeroDocumento()+"@example.com")
                    .telefono(null)
                    .direccion("Lorem Ipsu")
                    .estado(1)
                    .empresa(empresa.get())
                    .build();

            switch (action){
                case "CREATE":
                    persona.setUsuacrea(Constant.USERCREATE);
                    persona.setDatecreate(getTime());
                    break;
                case "UPDATE":
                    Optional<Persona> op = personaRepository.findById(id);
                    persona.setId(id);
                    persona.setUsuacrea(op.get().getUsuacrea());
                    persona.setDatecreate(op.get().getDatecreate());
                    persona.setUsuamodif(Constant.USERUPDATE);
                    persona.setDatemodif(getTime());
                    persona.setUsuadelet(op.get().getUsuadelet());
                    persona.setDatedelet(op.get().getDatedelet());
                    break;
            }
            return persona;
        }else {
            return null;
        }
    }
    private <T> BaseResponse getBaseResponseSuccess(T t){
        BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.OPERATION_OK.getCode(),
                MesaggesEnum.OPERATION_OK.getDescription(),
                Optional.of(t));
        return baseResponse;
    }
    private <T> BaseResponse getBaseResponseNotFound(T t){
        BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.PERSONA_NOT_FOUND.getCode(),
                MesaggesEnum.PERSONA_NOT_FOUND.getDescription(),
                Optional.of(t));
        return baseResponse;
    }
}
