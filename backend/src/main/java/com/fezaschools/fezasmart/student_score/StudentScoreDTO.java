package com.fezaschools.fezasmart.student_score;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudentScoreDTO {

    private Integer id;

    @NotNull
    private Integer student;

    @NotNull
    private Integer examSubject;

    @NotNull
    private BigDecimal score;

}
