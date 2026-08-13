package com.fezaschools.fezasmart.grade_boundary;

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
@RequestMapping(value = "/api/gradeBoundaries", produces = MediaType.APPLICATION_JSON_VALUE)
public class GradeBoundaryResource {

    private final GradeBoundaryService gradeBoundaryService;

    public GradeBoundaryResource(final GradeBoundaryService gradeBoundaryService) {
        this.gradeBoundaryService = gradeBoundaryService;
    }

    @GetMapping
    public ResponseEntity<List<GradeBoundaryDTO>> getAllGradeBoundaries() {
        return ResponseEntity.ok(gradeBoundaryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeBoundaryDTO> getGradeBoundary(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(gradeBoundaryService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createGradeBoundary(
            @RequestBody @Valid final GradeBoundaryDTO gradeBoundaryDTO) {
        final Integer createdId = gradeBoundaryService.create(gradeBoundaryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateGradeBoundary(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final GradeBoundaryDTO gradeBoundaryDTO) {
        gradeBoundaryService.update(id, gradeBoundaryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGradeBoundary(@PathVariable(name = "id") final Integer id) {
        gradeBoundaryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
