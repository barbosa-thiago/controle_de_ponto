package com.teste.controledeponto.service;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.mapper.ClockinMapper;
import com.teste.controledeponto.model.ClockIn;
import com.teste.controledeponto.model.User;
import com.teste.controledeponto.repository.ClockInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClockInService {

    private final ClockInRepository clockInRepository;
    private final ClockinMapper mapper;

    public ClockIn save(ClockinDTO body, User user) {

        List<ClockIn> todayClockIns = clockInRepository.findByUserIdAndDate(user.getId(), body.getDateTime().toLocalDate());

        boolean conflict = todayClockIns.stream()
            .anyMatch(clockIn -> clockIn.getDateTime().equals(body.getDateTime()));

        if(conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já registrado");
        }

        if (todayClockIns.size() > 3) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Apenas 4 horários podem ser registrados por dia");
        }

        if(todayClockIns.size() == 2) {
            todayClockIns.sort(Comparator.comparing(ClockIn::getDateTime));
            if(todayClockIns.get(1).getDateTime().isAfter(body.getDateTime().minusHours(1)))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Horário de almoço não pode ser menor que 1 hora");
        }

        var dayOfWeek = body.getDateTime().getDayOfWeek();
        if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Sábado e domingo não são permitidos como dia de trabalho");
        }

        var clockIn = mapper.dtoToEntity(body);
        clockIn.setUser(user);
        return clockInRepository.save(clockIn);
    }
}
