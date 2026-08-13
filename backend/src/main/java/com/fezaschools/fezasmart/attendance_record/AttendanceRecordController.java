package com.fezaschools.fezasmart.attendance_record;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-records")
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService) {
        this.attendanceRecordService = attendanceRecordService;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceRecordDTO>> findAll() {
        return ResponseEntity.ok(attendanceRecordService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<AttendanceRecordDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(attendanceRecordService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceRecordDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(attendanceRecordService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid AttendanceRecordDTO attendanceRecordDTO) {
        return new ResponseEntity<>(attendanceRecordService.create(attendanceRecordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid AttendanceRecordDTO attendanceRecordDTO) {
        attendanceRecordService.update(id, attendanceRecordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        attendanceRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
