package com.fezaschools.fezasmart.class_assignment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-assignments")
public class ClassAssignmentController {

    private final ClassAssignmentService classAssignmentService;

    public ClassAssignmentController(ClassAssignmentService classAssignmentService) {
        this.classAssignmentService = classAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<ClassAssignmentDTO>> findAll() {
        return ResponseEntity.ok(classAssignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassAssignmentDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(classAssignmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ClassAssignmentDTO classAssignmentDTO) {
        return new ResponseEntity<>(classAssignmentService.create(classAssignmentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ClassAssignmentDTO classAssignmentDTO) {
        classAssignmentService.update(id, classAssignmentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        classAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
