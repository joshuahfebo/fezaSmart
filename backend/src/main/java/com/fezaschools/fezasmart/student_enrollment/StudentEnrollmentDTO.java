package com.fezaschools.fezasmart.student_enrollment;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudentEnrollmentDTO {

    private Integer id;

    private LocalDate enrollmentDate;

    private Boolean isCurrent;

    @NotNull
    private Integer student;

    @NotNull
    private Integer classs;

    @NotNull
    private Integer academicYear;

}
