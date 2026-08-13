package com.fezaschools.fezasmart.student_scholarship;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudentScholarshipDTO {

    private Integer id;

    private LocalDate awardedDate;

    private LocalDate validUntil;

    @NotNull
    private Integer student;

    @NotNull
    private Integer scholarship;

    private Integer awardedBy;

}
