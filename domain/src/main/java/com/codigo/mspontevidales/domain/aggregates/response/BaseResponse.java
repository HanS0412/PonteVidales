package com.codigo.mspontevidales.domain.aggregates.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResponse<T> {
    private String code;
    private String message;
    private Optional<T> response;
}
