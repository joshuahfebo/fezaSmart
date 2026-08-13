package com.fezaschools.fezasmart.class_assignment;

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
@RequestMapping(value = "/api/classAssignments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClassAssignmentResource {

    private final ClassAssignmentService classAssignmentService;

    public ClassAssignmentResource(final ClassAssignmentService classAssignmentService) {
        this.classAssignmentService = classAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<ClassAssignmentDTO>> getAllClassAssignments() {
        return ResponseEntity.ok(classAssignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassAssignmentDTO> getClassAssignment(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(classAssignmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createClassAssignment(
            @RequestBody @Valid final ClassAssignmentDTO classAssignmentDTO) {
        final Integer createdId = classAssignmentService.create(classAssignmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateClassAssignment(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ClassAssignmentDTO classAssignmentDTO) {
        classAssignmentService.update(id, classAssignmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassAssignment(@PathVariable(name = "id") final Integer id) {
        classAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
