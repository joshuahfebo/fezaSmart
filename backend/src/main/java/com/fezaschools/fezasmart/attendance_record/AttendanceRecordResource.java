package com.fezaschools.fezasmart.attendance_record;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/attendanceRecords", produces = MediaType.APPLICATION_JSON_VALUE)
public class AttendanceRecordResource {

    private final AttendanceRecordService attendanceRecordService;

    public AttendanceRecordResource(final AttendanceRecordService attendanceRecordService) {
        this.attendanceRecordService = attendanceRecordService;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceRecordDTO>> getAllAttendanceRecords() {
        return ResponseEntity.ok(attendanceRecordService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceRecordDTO> getAttendanceRecord(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(attendanceRecordService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createAttendanceRecord(
            @RequestBody @Valid final AttendanceRecordDTO attendanceRecordDTO) {
        final Integer createdId = attendanceRecordService.create(attendanceRecordDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateAttendanceRecord(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final AttendanceRecordDTO attendanceRecordDTO) {
        attendanceRecordService.update(id, attendanceRecordDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceRecord(
            @PathVariable(name = "id") final Integer id) {
        attendanceRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
