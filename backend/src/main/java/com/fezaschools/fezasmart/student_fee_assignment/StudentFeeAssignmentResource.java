package com.fezaschools.fezasmart.student_fee_assignment;

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
@RequestMapping(value = "/api/studentFeeAssignments", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentFeeAssignmentResource {

    private final StudentFeeAssignmentService studentFeeAssignmentService;

    public StudentFeeAssignmentResource(
            final StudentFeeAssignmentService studentFeeAssignmentService) {
        this.studentFeeAssignmentService = studentFeeAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentFeeAssignmentDTO>> getAllStudentFeeAssignments() {
        return ResponseEntity.ok(studentFeeAssignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentFeeAssignmentDTO> getStudentFeeAssignment(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(studentFeeAssignmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createStudentFeeAssignment(
            @RequestBody @Valid final StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        final Integer createdId = studentFeeAssignmentService.create(studentFeeAssignmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateStudentFeeAssignment(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        studentFeeAssignmentService.update(id, studentFeeAssignmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentFeeAssignment(
            @PathVariable(name = "id") final Integer id) {
        studentFeeAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
