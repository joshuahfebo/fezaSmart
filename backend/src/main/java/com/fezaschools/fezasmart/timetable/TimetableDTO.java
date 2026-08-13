package com.fezaschools.fezasmart.timetable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TimetableDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private Integer schoolId;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer academicYear;

}
