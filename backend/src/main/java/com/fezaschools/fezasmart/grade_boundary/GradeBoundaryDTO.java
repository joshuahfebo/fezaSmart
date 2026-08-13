package com.fezaschools.fezasmart.grade_boundary;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GradeBoundaryDTO {

    private Integer id;

    @NotNull
    private BigDecimal minPercentage;

    @NotNull
    private BigDecimal maxPercentage;

    @Size(max = 5)
    private String letterGrade;

    private BigDecimal pointGrade;

    @Size(max = 255)
    private String remark;

    @NotNull
    @Size(max = 255)
    private String type;

    private Integer school;

    private Integer subject;

    private Integer exam;

}
