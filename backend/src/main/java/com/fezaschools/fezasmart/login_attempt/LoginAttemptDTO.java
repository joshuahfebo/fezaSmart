package com.fezaschools.fezasmart.login_attempt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginAttemptDTO {

    private Integer id;

    @Size(max = 45)
    private String ipAddress;

    private OffsetDateTime attemptedAt;

    @NotNull
    private Boolean success;

    private Integer user;

}
