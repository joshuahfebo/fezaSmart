package com.fezaschools.fezasmart.email_verification_token;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmailVerificationTokenDTO {

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
