package com.teste.controledeponto.mapper;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.dto.clockin.ClockinResponseDTO;
import com.teste.controledeponto.model.ClockIn;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClockinMapper {


    ClockIn dtoToEntity(ClockinDTO dto);

    ClockinResponseDTO entityToDto(ClockIn entity);
}
