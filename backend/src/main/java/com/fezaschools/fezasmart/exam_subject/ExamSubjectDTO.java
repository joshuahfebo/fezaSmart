package com.fezaschools.fezasmart.exam_subject;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExamSubjectDTO {

    private Integer id;

    @NotNull
    private Integer exam;

    @NotNull
    private Integer subject;

    @NotNull
    private BigDecimal maxScore;

}
