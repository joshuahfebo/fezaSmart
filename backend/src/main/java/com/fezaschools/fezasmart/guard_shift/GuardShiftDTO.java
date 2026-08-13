package com.fezaschools.fezasmart.guard_shift;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GuardShiftDTO {

    private Integer id;

    @NotNull
    private LocalDate shiftDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Integer staff;

}
