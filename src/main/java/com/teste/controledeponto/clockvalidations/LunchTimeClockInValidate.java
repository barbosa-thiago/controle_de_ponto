package com.teste.controledeponto.clockvalidations;

import com.teste.controledeponto.model.ClockIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class LunchTimeClockInValidate implements ClockInValidate {

    private ClockInValidate chain;

    @Override
    public void setNextChain(ClockInValidate clockInValidate) {
        this.chain = clockInValidate;
    }

    @Override
    public void validate(List<ClockIn> clockIns, LocalDateTime newClockInTime) {
        if (clockIns.size() == 2) {
            clockIns.sort(Comparator.comparing(ClockIn::getDateTime));
            if (clockIns.get(1).getDateTime().isAfter(newClockInTime.minusHours(1)))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Deve haver no mínimo 1 hora de almoço");
        } else {
            this.chain.validate(clockIns, newClockInTime);
        }
    }
}
