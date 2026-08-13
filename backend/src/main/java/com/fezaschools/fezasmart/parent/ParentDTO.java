package com.fezaschools.fezasmart.parent;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ParentDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String relationshipType;

    @Size(max = 255)
    private String gender;

    private LocalDate dob;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Integer deletedBy;

    @Size(max = 255)
    private String restoreToken;

    @NotNull
    private Integer user;

}
