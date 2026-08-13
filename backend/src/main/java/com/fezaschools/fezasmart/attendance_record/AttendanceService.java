package com.fezaschools.fezasmart.attendance_record;

import com.fezaschools.fezasmart.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceService(final AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public AttendanceSummaryDTO getStudentAttendanceSummary(Integer studentId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> records = attendanceRecordRepository
                .findByStudentIdAndDateBetween(studentId, startDate, endDate);

        long totalDays = records.size();
        long presentDays = records.stream().filter(r -> "PRESENT".equals(r.getStatus())).count();
        long absentDays = records.stream().filter(r -> "ABSENT".equals(r.getStatus())).count();
        long lateDays = records.stream().filter(r -> "LATE".equals(r.getStatus())).count();
        long excusedDays = records.stream().filter(r -> "EXCUSED".equals(r.getStatus())).count();

        double attendancePercentage = totalDays > 0
                ? (double) presentDays / totalDays * 100.0
                : 0.0;

        AttendanceSummaryDTO summary = new AttendanceSummaryDTO();
        summary.setStudentId(studentId);
        summary.setStartDate(startDate);
        summary.setEndDate(endDate);
        summary.setTotalDays(totalDays);
        summary.setPresentDays(presentDays);
        summary.setAbsentDays(absentDays);
        summary.setLateDays(lateDays);
        summary.setExcusedDays(excusedDays);
        summary.setAttendancePercentage(Math.round(attendancePercentage * 100.0) / 100.0);

        return summary;
    }

    public List<AttendanceRecordDTO> getStudentAttendanceRecords(Integer studentId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> records = attendanceRecordRepository
                .findByStudentIdAndDateBetween(studentId, startDate, endDate);

        return records.stream()
                .map(r -> mapToDTO(r, new AttendanceRecordDTO()))
                .toList();
    }

    public List<AttendanceRecordDTO> getClassAttendanceForDate(Integer classsId, LocalDate date) {
        List<AttendanceRecord> records = attendanceRecordRepository
                .findByClasssIdAndDate(classsId, date);

        return records.stream()
                .map(r -> mapToDTO(r, new AttendanceRecordDTO()))
                .toList();
    }

    public AttendanceRecordDTO markAttendance(AttendanceRecordDTO dto) {
        AttendanceRecord record = new AttendanceRecord();
        record.setDate(dto.getDate());
        record.setStatus(dto.getStatus());
        record.setCreatedAt(java.time.OffsetDateTime.now());

        AttendanceRecord saved = attendanceRecordRepository.save(record);
        return mapToDTO(saved, new AttendanceRecordDTO());
    }

    private AttendanceRecordDTO mapToDTO(final AttendanceRecord record, final AttendanceRecordDTO dto) {
        dto.setId(record.getId());
        dto.setDate(record.getDate());
        dto.setStatus(record.getStatus());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setStudent(record.getStudent() == null ? null : record.getStudent().getId());
        dto.setClasss(record.getClasss() == null ? null : record.getClasss().getId());
        dto.setMarkedByStaff(record.getMarkedByStaff() == null ? null : record.getMarkedByStaff().getId());
        return dto;
    }
}
