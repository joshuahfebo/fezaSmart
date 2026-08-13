package com.fezaschools.fezasmart.audit_log;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuditLogDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String entity;

    private Integer entityId;

    @NotNull
    @Size(max = 255)
    private String action;

    private String oldValue;

    private String newValue;

    @Size(max = 45)
    private String ipAddress;

    private OffsetDateTime createdAt;

    private Integer user;

}
