package com.fezaschools.fezasmart.session;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SessionDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String token;

    @Size(max = 255)
    private String refreshToken;

    @Size(max = 255)
    private String tokenType;

    @Size(max = 255)
    private String deviceInfo;

    @Size(max = 45)
    private String ipAddress;

    @NotNull
    private OffsetDateTime expiresAt;

    private OffsetDateTime createdAt;

    private Boolean revoked;

    @NotNull
    private Integer user;

}
