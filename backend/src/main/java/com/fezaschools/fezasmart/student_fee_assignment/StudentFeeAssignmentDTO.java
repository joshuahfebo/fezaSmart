package com.fezaschools.fezasmart.student_fee_assignment;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudentFeeAssignmentDTO {

    private Integer id;

    private OffsetDateTime assignedAt;

    @NotNull
    private Integer student;

    @NotNull
    private Integer feeStructure;

    private Integer assignedBy;

}
