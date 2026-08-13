package com.fezaschools.fezasmart.student_scholarship;

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
@RequestMapping(value = "/api/studentScholarships", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentScholarshipResource {

    private final StudentScholarshipService studentScholarshipService;

    public StudentScholarshipResource(final StudentScholarshipService studentScholarshipService) {
        this.studentScholarshipService = studentScholarshipService;
    }

    @GetMapping
    public ResponseEntity<List<StudentScholarshipDTO>> getAllStudentScholarships() {
        return ResponseEntity.ok(studentScholarshipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentScholarshipDTO> getStudentScholarship(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(studentScholarshipService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createStudentScholarship(
            @RequestBody @Valid final StudentScholarshipDTO studentScholarshipDTO) {
        final Integer createdId = studentScholarshipService.create(studentScholarshipDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateStudentScholarship(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final StudentScholarshipDTO studentScholarshipDTO) {
        studentScholarshipService.update(id, studentScholarshipDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentScholarship(
            @PathVariable(name = "id") final Integer id) {
        studentScholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
