package com.fezaschools.fezasmart.api_key;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiKeyDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String keyHash;

    private String permissions;

    private OffsetDateTime expiresAt;

    private OffsetDateTime createdAt;

    private Boolean revoked;

    private Integer school;

}
