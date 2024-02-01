package com.teste.controledeponto.clockvalidations;

import lombok.Getter;

@Getter
public class ClockInValidateChain {

    private final ClockInValidate first;

    public ClockInValidateChain() {
        this.first = new ConflictClockInValidate();
        ClockInValidate second = new LunchTimeClockInValidate();
        ClockInValidate third = new MaximumClockInValidate();
        ClockInValidate fourth = new NotSaturdayNorSundayClockInValidate();

        first.setNextChain(second);
        second.setNextChain(third);
        third.setNextChain(fourth);

    }
}
