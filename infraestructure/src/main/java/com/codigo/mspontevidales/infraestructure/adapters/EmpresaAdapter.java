package com.codigo.mspontevidales.infraestructure.adapters;

import com.codigo.mspontevidales.domain.aggregates.constants.Constant;
import com.codigo.mspontevidales.domain.aggregates.dto.EmpresaDto;
import com.codigo.mspontevidales.domain.aggregates.dto.SunatDto;
import com.codigo.mspontevidales.domain.aggregates.enums.MesaggesEnum;
import com.codigo.mspontevidales.domain.aggregates.request.EmpresaRequest;
import com.codigo.mspontevidales.domain.aggregates.response.BaseResponse;
import com.codigo.mspontevidales.domain.ports.out.EmpresaServiceOut;
import com.codigo.mspontevidales.infraestructure.client.SunatClient;
import com.codigo.mspontevidales.infraestructure.dao.EmpresaRepository;
import com.codigo.mspontevidales.infraestructure.entity.Empresa;
import com.codigo.mspontevidales.infraestructure.mapper.ConvertEmpresaToDto;
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
public class EmpresaAdapter implements EmpresaServiceOut {

    private final EmpresaRepository empresaRepository;
    private final SunatClient sunatClient;
    private final RedisService redisService;

    @Value("${token.client}")
    private String token;

    @Override
    public ResponseEntity<BaseResponse> crearEmpresaOut(EmpresaRequest empresaRequest) {
        boolean exists = empresaRepository.existsByNumerodocumento(empresaRequest.getNumDoc());
        if(!exists){
            Empresa empresa = getEmpresa(empresaRequest , Constant.ACTION_CREATE, null);
            EmpresaDto empresaDto = ConvertEmpresaToDto.getEmpresaDto(empresaRepository.save(empresa));
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(empresaDto)));
        }else{
            BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.EMPRESA_EXISTS.getCode(),
                    MesaggesEnum.EMPRESA_EXISTS.getDescription(),
                    Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> buscarXIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id);
        if(!isNull(redisInfo)){
            EmpresaDto empresaDto = Util.convertJsonToDto(redisInfo,EmpresaDto.class);
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(empresaDto)));
        }else {
            Optional<Empresa> op = empresaRepository.findById(id);
            if (op.isPresent()){
                EmpresaDto dto = ConvertEmpresaToDto.getEmpresaDto(op.get());
                String json = Util.convertDtoToJson(dto);
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id,json,10);
                return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(dto)));
            }else{
                return ResponseEntity.ok(getBaseResponseNotFound(Optional.empty()));
            }
        }
    }

    @Override
    public ResponseEntity<BaseResponse> obtenerTodosOut() {
        List<Empresa> empresas = empresaRepository.findAll();
        if(!empresas.isEmpty()){
            List<EmpresaDto> empresaDto = ConvertEmpresaToDto.getListEmpresaDto(empresas);
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(empresaDto)));
        }else{
            BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.EMPRESAS_NOT_EXISTS.getCode(),
                    MesaggesEnum.EMPRESAS_NOT_EXISTS.getDescription(),
                    Optional.empty());
            return ResponseEntity.ok(baseResponse);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> actualizarOut(Long id, EmpresaRequest empresaRequest) {
        Optional<Empresa> op = empresaRepository.findById(id);
        if(op.isPresent()){
            Empresa empresa = getEmpresa(empresaRequest , Constant.ACTION_UPDATE,id);
            EmpresaDto empresaDto = ConvertEmpresaToDto.getEmpresaDto(empresaRepository.save(empresa));
            String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id);
            if(!isNull(redisInfo)){
                redisService.deleteKey(Constant.REDIS_KEY_OBTENEREMPRESA+id);
                String json = Util.convertDtoToJson(empresaDto);
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id,json,10);
            }
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(empresaDto)));
        }else{
            return ResponseEntity.ok(getBaseResponseNotFound(Optional.empty()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> deleteOut(Long id) {
        Optional<Empresa> op = empresaRepository.findById(id);
        if(op.isPresent()){
            Empresa empresa = op.get();
            empresa.setEstado(0);
            empresa.setUsuadelet(Constant.USERDELETE);
            empresa.setDatedelet(getTime());
            EmpresaDto empresaDto = ConvertEmpresaToDto.getEmpresaDto(empresaRepository.save(empresa));
            String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id);
            if(!isNull(redisInfo)){
                redisService.deleteKey(Constant.REDIS_KEY_OBTENEREMPRESA+id);
                String json = Util.convertDtoToJson(empresaDto);
                redisService.saveInRedis(Constant.REDIS_KEY_OBTENEREMPRESA+id,json,10);
            }
            return ResponseEntity.ok(getBaseResponseSuccess(Optional.of(empresaDto)));
        }else{
            return ResponseEntity.ok(getBaseResponseNotFound(Optional.empty()));
        }
    }

    private SunatDto getExecSunat(String numDoc){
        String authorization = "Bearer "+token;
        return sunatClient.getInfoSunat(numDoc,authorization);
    }

    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }

    private Empresa getEmpresa(EmpresaRequest empresaRequest, String action, Long id){
        SunatDto info = getExecSunat(empresaRequest.getNumDoc());
        Empresa empresa = Empresa.builder()
                .razonsocial(info.getRazonSocial())
                .tipodocumento(info.getTipoDocumento())
                .numerodocumento(info.getNumeroDocumento())
                .estado(info.getEstado().equalsIgnoreCase("ACTIVO") ? 1:0)
                .condicion(info.getCondicion())
                .direccion(info.getDireccion())
                .distrito(info.getDistrito())
                .provincia(info.getProvincia())
                .departamento(info.getDepartamento())
                .esagenteretencion(false)
                .build();

        switch (action){
            case "CREATE":
                empresa.setUsuacrea(Constant.USERCREATE);
                empresa.setDatecreate(getTime());
                break;
            case "UPDATE":
                Optional<Empresa> op = empresaRepository.findById(id);
                empresa.setId(id);
                empresa.setUsuacrea(op.get().getUsuacrea());
                empresa.setDatecreate(op.get().getDatecreate());
                empresa.setUsuamodif(Constant.USERUPDATE);
                empresa.setDatemodif(getTime());
                empresa.setUsuadelet(op.get().getUsuadelet());
                empresa.setDatedelet(op.get().getDatedelet());
                break;
        }
        return empresa;
    }
    private <T> BaseResponse getBaseResponseSuccess(T t){
        BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.OPERATION_OK.getCode(),
                MesaggesEnum.OPERATION_OK.getDescription(),
                Optional.of(t));
        return baseResponse;
    }
    private <T> BaseResponse getBaseResponseNotFound(T t){
        BaseResponse baseResponse = new BaseResponse<>(MesaggesEnum.EMPRESA_NOT_FOUND.getCode(),
                MesaggesEnum.EMPRESA_NOT_FOUND.getDescription(),
                Optional.of(t));
        return baseResponse;
    }
}
