package com.teste.controledeponto.dto.jwt;

import lombok.*;

import java.io.Serializable;

@Value
@Getter
@Setter
@RequiredArgsConstructor
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    String jwtToken;
}
