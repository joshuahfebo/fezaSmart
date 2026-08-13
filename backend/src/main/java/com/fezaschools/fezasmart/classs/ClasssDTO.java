package com.fezaschools.fezasmart.classs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClasssDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer school;

    @NotNull
    private Integer academicYear;

}
