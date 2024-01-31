package com.teste.controledeponto.clockvalidations;

import com.teste.controledeponto.model.ClockIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

public class MaximumClockInValidate implements ClockInValidate {

    private ClockInValidate chain;

    @Override
    public void setNextChain(ClockInValidate clockInValidate) {
        this.chain = clockInValidate;
    }

    @Override
    public void validate(List<ClockIn> clockIns, LocalDateTime newClockInTime) {
        if (clockIns.size() > 3) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Apenas 4 horários podem ser registrados por dia");
        } else {
            this.chain.validate(clockIns, newClockInTime);
        }
    }
}
