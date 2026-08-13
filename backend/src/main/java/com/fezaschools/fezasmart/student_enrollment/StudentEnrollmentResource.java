package com.fezaschools.fezasmart.student_enrollment;

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
@RequestMapping(value = "/api/studentEnrollments", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentEnrollmentResource {

    private final StudentEnrollmentService studentEnrollmentService;

    public StudentEnrollmentResource(final StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollmentDTO>> getAllStudentEnrollments() {
        return ResponseEntity.ok(studentEnrollmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollmentDTO> getStudentEnrollment(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(studentEnrollmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createStudentEnrollment(
            @RequestBody @Valid final StudentEnrollmentDTO studentEnrollmentDTO) {
        final Integer createdId = studentEnrollmentService.create(studentEnrollmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateStudentEnrollment(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final StudentEnrollmentDTO studentEnrollmentDTO) {
        studentEnrollmentService.update(id, studentEnrollmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentEnrollment(
            @PathVariable(name = "id") final Integer id) {
        studentEnrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
