package com.fezaschools.fezasmart.fee_structure;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FeeStructureDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private Integer school;

    @NotNull
    private Integer academicYear;

}
