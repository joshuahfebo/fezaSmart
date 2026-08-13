package com.fezaschools.fezasmart.attendance_record;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<AttendanceSummaryDTO> getStudentAttendanceSummary(
            @PathVariable Integer studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceSummary(studentId, startDate, endDate));
    }

    @GetMapping("/student/{studentId}/records")
    public ResponseEntity<List<AttendanceRecordDTO>> getStudentAttendanceRecords(
            @PathVariable Integer studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceRecords(studentId, startDate, endDate));
    }

    @GetMapping("/class/{classsId}/date/{date}")
    public ResponseEntity<List<AttendanceRecordDTO>> getClassAttendanceForDate(
            @PathVariable Integer classsId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getClassAttendanceForDate(classsId, date));
    }

    @PostMapping
    public ResponseEntity<AttendanceRecordDTO> markAttendance(@RequestBody AttendanceRecordDTO attendanceRecordDTO) {
        return ResponseEntity.ok(attendanceService.markAttendance(attendanceRecordDTO));
    }
}
