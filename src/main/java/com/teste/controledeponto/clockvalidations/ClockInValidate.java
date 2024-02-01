package com.teste.controledeponto.clockvalidations;

import com.teste.controledeponto.model.ClockIn;

import java.time.LocalDateTime;
import java.util.List;

public interface ClockInValidate {

    void setNextChain(ClockInValidate clockInValidate);

    void validate(List<ClockIn> clockIns, LocalDateTime newClockInTime);
}
