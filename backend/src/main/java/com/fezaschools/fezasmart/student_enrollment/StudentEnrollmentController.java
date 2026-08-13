package com.fezaschools.fezasmart.student_enrollment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class StudentEnrollmentController {

    private final StudentEnrollmentService studentEnrollmentService;

    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollmentDTO>> findAll() {
        return ResponseEntity.ok(studentEnrollmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollmentDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(studentEnrollmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid StudentEnrollmentDTO studentEnrollmentDTO) {
        return new ResponseEntity<>(studentEnrollmentService.create(studentEnrollmentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid StudentEnrollmentDTO studentEnrollmentDTO) {
        studentEnrollmentService.update(id, studentEnrollmentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        studentEnrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
