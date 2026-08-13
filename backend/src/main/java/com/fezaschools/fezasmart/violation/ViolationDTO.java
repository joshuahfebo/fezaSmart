package com.fezaschools.fezasmart.violation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ViolationDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    @Digits(integer = 5, fraction = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal pointDeduction;

    @NotNull
    @Size(max = 255)
    private String pointType;

}
