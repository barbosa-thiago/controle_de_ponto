package com.teste.controledeponto.clockvalidations;

import com.teste.controledeponto.model.ClockIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class NotSaturdayNorSundayClockInValidate implements ClockInValidate {

    private ClockInValidate chain;

    @Override
    public void setNextChain(ClockInValidate clockInValidate) {
        this.chain = clockInValidate;
    }

    @Override
    public void validate(List<ClockIn> clockIns, LocalDateTime newClockInTime) {
        var dayOfWeek = newClockInTime.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Sábado e domingo não são permitidos como dia de trabalho");
        }
    }
}
