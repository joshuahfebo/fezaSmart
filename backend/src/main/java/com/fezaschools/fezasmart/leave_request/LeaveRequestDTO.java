package com.fezaschools.fezasmart.leave_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LeaveRequestDTO {

    private Long id;

    private String reason;

    @Size(max = 255)
    private String status;

    private OffsetDateTime requestedAt;

    private OffsetDateTime processedAt;

    @NotNull
    private Integer student;

    @NotNull
    private Integer requesterUser;

    private Integer processedByStaff;

    private Integer permission;

}
