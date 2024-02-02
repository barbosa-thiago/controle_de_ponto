package com.teste.controledeponto.service;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.model.ClockIn;
import com.teste.controledeponto.model.User;
import com.teste.controledeponto.repository.ClockInRepository;
import com.teste.controledeponto.clockvalidations.ClockInValidateChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClockInService {

    private final ClockInRepository clockInRepository;

    public ClockIn save(ClockinDTO body, User user) {

        var dateTime = LocalDateTime.parse(body.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        List<ClockIn> todayClockIns = clockInRepository.findByUserIdAndDate(user.getId(), dateTime.toLocalDate());

        var clockInValidateChain = new ClockInValidateChain();
        clockInValidateChain.getFirst()
            .validate(todayClockIns, dateTime);

        log.info("Batida de ponto validada para usuário {} às {}", user.getUsername(), dateTime);

        var clockIn = ClockIn.builder()
            .dateTime(dateTime)
            .user(user)
            .build();

        return clockInRepository.save(clockIn);
    }
}
