package com.fezaschools.fezasmart.student_point;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-points")
public class StudentPointController {

    private final StudentPointService studentPointService;

    public StudentPointController(StudentPointService studentPointService) {
        this.studentPointService = studentPointService;
    }

    @GetMapping
    public ResponseEntity<List<StudentPointDTO>> findAll() {
        return ResponseEntity.ok(studentPointService.findAll());
    }

    @GetMapping("/{pointType}")
    public ResponseEntity<StudentPointDTO> get(@PathVariable String pointType) {
        return ResponseEntity.ok(studentPointService.get(pointType));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid StudentPointDTO studentPointDTO) {
        return new ResponseEntity<>(studentPointService.create(studentPointDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{pointType}")
    public ResponseEntity<Void> update(@PathVariable String pointType, @RequestBody @Valid StudentPointDTO studentPointDTO) {
        studentPointService.update(pointType, studentPointDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pointType}")
    public ResponseEntity<Void> delete(@PathVariable String pointType) {
        studentPointService.delete(pointType);
        return ResponseEntity.noContent().build();
    }
}
