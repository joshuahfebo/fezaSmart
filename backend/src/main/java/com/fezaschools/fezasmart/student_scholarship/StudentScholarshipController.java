package com.fezaschools.fezasmart.student_scholarship;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-scholarships")
public class StudentScholarshipController {

    private final StudentScholarshipService studentScholarshipService;

    public StudentScholarshipController(StudentScholarshipService studentScholarshipService) {
        this.studentScholarshipService = studentScholarshipService;
    }

    @GetMapping
    public ResponseEntity<List<StudentScholarshipDTO>> findAll() {
        return ResponseEntity.ok(studentScholarshipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentScholarshipDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(studentScholarshipService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid StudentScholarshipDTO studentScholarshipDTO) {
        return new ResponseEntity<>(studentScholarshipService.create(studentScholarshipDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid StudentScholarshipDTO studentScholarshipDTO) {
        studentScholarshipService.update(id, studentScholarshipDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        studentScholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
