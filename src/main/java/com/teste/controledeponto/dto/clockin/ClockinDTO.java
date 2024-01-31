package com.teste.controledeponto.dto.clockin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;

@Jacksonized
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockinDTO {


    @JsonProperty(value = "dataHora")
    @NotEmpty(message = "Campo obrigatório não informado")
    String dateTime;
}
