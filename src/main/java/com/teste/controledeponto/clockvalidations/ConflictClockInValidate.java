package com.teste.controledeponto.clockvalidations;

import com.teste.controledeponto.model.ClockIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

public class ConflictClockInValidate implements ClockInValidate {

    private ClockInValidate chain;

    @Override
    public void setNextChain(ClockInValidate clockInValidate) {
        this.chain = clockInValidate;
    }

    @Override
    public void validate(List<ClockIn> clockIns, LocalDateTime newClockInTime) {
        boolean conflict = clockIns.stream()
            .anyMatch(clockIn -> clockIn.getDateTime().equals(newClockInTime));

        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já registrado");
        } else {
            this.chain.validate(clockIns, newClockInTime);
        }
    }
}
