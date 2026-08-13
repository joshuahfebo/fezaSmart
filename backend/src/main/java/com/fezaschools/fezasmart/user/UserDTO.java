package com.fezaschools.fezasmart.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 255)
    private String hashedPassword;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String phone;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Boolean emailVerified;

    private Boolean phoneVerified;

    private Boolean twoFactorEnabled;

    @Size(max = 255)
    private String twoFactorMethod;

    @Size(max = 255)
    private String twoFactorSecret;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime lastLoginAt;

    private OffsetDateTime deletedAt;

    private Integer deletedBy;

    @Size(max = 255)
    private String restoreToken;

    private List<Integer> userRoleRoles;

}
