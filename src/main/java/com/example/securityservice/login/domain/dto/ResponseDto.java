package com.example.securityservice.login.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ResponseDto {
    private Integer status;
    private String errorCode;
    private String errorDesc;
    private String message;
    private Object data;

    public ResponseDto(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ResponseDto success(String message, Object data) {
        return new ResponseDto()
                .setStatus(200)
                .setErrorCode("000")
                .setMessage(message)
                .setData(data);
    }

    public static ResponseDto error(String errorCode, String errorDesc) {
        return new ResponseDto()
                .setStatus(200)
                .setErrorCode(errorCode)
                .setErrorDesc(errorDesc);
    }
}
