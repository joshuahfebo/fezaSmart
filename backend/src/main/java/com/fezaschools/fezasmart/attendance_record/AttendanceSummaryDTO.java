package com.fezaschools.fezasmart.attendance_record;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class AttendanceSummaryDTO {

    private Integer studentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalDays;
    private long presentDays;
    private long absentDays;
    private long lateDays;
    private long excusedDays;
    private double attendancePercentage;

}
