package com.fezaschools.fezasmart.term;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TermDTO {

    private Integer id;

    @NotNull
    private Integer academicYearId;

    @NotNull
    private Integer termNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
