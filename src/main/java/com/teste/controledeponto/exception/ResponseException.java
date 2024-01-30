package com.teste.controledeponto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;

@With
@Getter
@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseException {
    HttpStatus status;
    String message;

    public int getStatusCode() {
        return status.value();
    }
}
