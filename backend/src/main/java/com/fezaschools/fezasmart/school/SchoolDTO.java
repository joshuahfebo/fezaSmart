package com.fezaschools.fezasmart.school;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SchoolDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String location;

    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Integer deletedBy;

    @Size(max = 255)
    private String restoreToken;

}
