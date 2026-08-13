package com.fezaschools.fezasmart.attendance_record;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttendanceRecordDTO {

    private Integer id;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(max = 255)
    private String status;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer student;

    @NotNull
    private Integer classs;

    private Integer markedByStaff;

}
