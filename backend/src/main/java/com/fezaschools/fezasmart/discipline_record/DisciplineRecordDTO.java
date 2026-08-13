package com.fezaschools.fezasmart.discipline_record;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DisciplineRecordDTO {

    private Integer id;

    @NotNull
    @Digits(integer = 5, fraction = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal pointsDeducted;

    private String comment;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer student;

    @NotNull
    private Integer violation;

    private Integer staff;

}
