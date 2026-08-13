package com.fezaschools.fezasmart.student_point;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudentPointDTO {

    @Size(max = 255)
    @StudentPointPointTypeValid
    private String pointType;

    @Digits(integer = 5, fraction = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal currentPoints;

    @NotNull
    private Integer student;

}
