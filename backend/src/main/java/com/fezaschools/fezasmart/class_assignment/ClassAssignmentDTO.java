package com.fezaschools.fezasmart.class_assignment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClassAssignmentDTO {

    private Integer id;

    @NotNull
    private Integer classs;

    @NotNull
    private Integer staff;

    @NotNull
    @Size(max = 255)
    private String roleInClass;

    private OffsetDateTime assignedDate;

}
