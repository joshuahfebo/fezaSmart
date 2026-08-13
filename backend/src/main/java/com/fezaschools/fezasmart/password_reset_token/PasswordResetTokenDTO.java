package com.fezaschools.fezasmart.password_reset_token;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordResetTokenDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String token;

    @NotNull
    private OffsetDateTime expiresAt;

    private Boolean used;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer user;

}
