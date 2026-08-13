package com.fezaschools.fezasmart.student_fee_assignment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-fee-assignments")
public class StudentFeeAssignmentController {

    private final StudentFeeAssignmentService studentFeeAssignmentService;

    public StudentFeeAssignmentController(StudentFeeAssignmentService studentFeeAssignmentService) {
        this.studentFeeAssignmentService = studentFeeAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentFeeAssignmentDTO>> findAll() {
        return ResponseEntity.ok(studentFeeAssignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentFeeAssignmentDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(studentFeeAssignmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        return new ResponseEntity<>(studentFeeAssignmentService.create(studentFeeAssignmentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        studentFeeAssignmentService.update(id, studentFeeAssignmentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        studentFeeAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
