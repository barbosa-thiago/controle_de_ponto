package com.teste.controledeponto.dto.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Value
@Getter
@Setter
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    String jwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
