package com.teste.controledeponto.dto.clockin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Jacksonized
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClockinResponseDTO {

    @NotNull
    LocalDateTime dateTime;
}
