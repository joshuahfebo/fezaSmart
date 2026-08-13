package com.fezaschools.fezasmart.two_factor_code;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TwoFactorCodeDTO {

    private Integer id;

    @NotNull
    @Size(max = 6)
    private String code;

    @NotNull
    private OffsetDateTime expiresAt;

    private Boolean used;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer user;

}
