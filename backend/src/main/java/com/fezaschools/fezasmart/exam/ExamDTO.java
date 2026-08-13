package com.fezaschools.fezasmart.exam;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExamDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private LocalDate examDate;

    @NotNull
    private Integer term;

}
