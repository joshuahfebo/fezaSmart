package com.fezaschools.fezasmart.staff;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StaffDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    private LocalDate dob;

    @Size(max = 255)
    private String gender;

    @Size(max = 255)
    private String staffNumber;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Integer deletedBy;

    @Size(max = 255)
    private String restoreToken;

    @NotNull
    private Integer school;

    @NotNull
    private Integer user;

    private Integer department;

    private List<Integer> teacherSubjectSubjects;

}
