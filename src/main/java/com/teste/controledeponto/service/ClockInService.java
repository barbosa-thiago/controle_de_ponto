package com.teste.controledeponto.service;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.model.ClockIn;
import com.teste.controledeponto.model.User;
import com.teste.controledeponto.repository.ClockInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClockInService {

    private final ClockInRepository clockInRepository;

    public ClockIn save(ClockinDTO body, User user) {

        var dateTime = LocalDateTime.parse(body.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        List<ClockIn> todayClockIns = clockInRepository.findByUserIdAndDate(user.getId(), dateTime.toLocalDate());

        boolean conflict = todayClockIns.stream()
            .anyMatch(clockIn -> clockIn.getDateTime().equals(dateTime));

        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já registrado");
        }

        if (todayClockIns.size() > 3) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Apenas 4 horários podem ser registrados por dia");
        }

        if (todayClockIns.size() == 2) {
            todayClockIns.sort(Comparator.comparing(ClockIn::getDateTime));
            if (todayClockIns.get(1).getDateTime().isAfter(dateTime.minusHours(1)))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Deve haver no mínimo 1 hora de almoço");
        }

        var dayOfWeek = dateTime.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Sábado e domingo não são permitidos como dia de trabalho");
        }

        var clockIn = ClockIn.builder()
            .dateTime(dateTime)
            .user(user)
            .build();

        return clockInRepository.save(clockIn);
    }
}
