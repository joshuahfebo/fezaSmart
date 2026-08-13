package com.fezaschools.fezasmart.staff_role;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StaffRoleDTO {

    private Long id;

    private OffsetDateTime assignedAt;

    @NotNull
    private Integer staff;

    @NotNull
    private Integer role;

}
