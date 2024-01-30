package com.teste.controledeponto.service;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.mapper.ClockinMapper;
import com.teste.controledeponto.model.ClockIn;
import com.teste.controledeponto.model.User;
import com.teste.controledeponto.repository.ClockInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClockInService {

    private final ClockInRepository clockInRepository;
    private final ClockinMapper mapper;

    public ClockIn save(ClockinDTO body, User user) {

        //TODO validations

        var clockIn = mapper.dtoToEntity(body);
        clockIn.setUser(user);
        return clockInRepository.save(clockIn);
    }
}
