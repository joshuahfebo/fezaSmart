package com.fezaschools.fezasmart.department;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DepartmentDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer school;

    private Integer headStaff;

}
