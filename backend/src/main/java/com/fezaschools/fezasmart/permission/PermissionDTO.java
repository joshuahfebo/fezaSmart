package com.fezaschools.fezasmart.permission;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PermissionDTO {

    private Integer id;

    @NotNull
    private Integer leaveRequestId;

    @NotNull
    private OffsetDateTime timeOutLimit;

    @NotNull
    private OffsetDateTime timeInLimit;

    private OffsetDateTime actualTimeOut;

    private OffsetDateTime actualTimeIn;

    private Boolean returned;

    @NotNull
    private Integer student;

    private Integer issuedByStaff;

    private Integer guardOutStaff;

    private Integer guardInStaff;

}
