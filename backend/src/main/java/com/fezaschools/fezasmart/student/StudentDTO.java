package com.fezaschools.fezasmart.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudentDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String middleName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String controlNumber;

    private LocalDate dob;

    @Size(max = 255)
    private String gender;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Integer deletedBy;

    @Size(max = 255)
    private String restoreToken;

    @NotNull
    private Integer school;

    private Integer user;

    private List<Integer> studentParentParents;

}
