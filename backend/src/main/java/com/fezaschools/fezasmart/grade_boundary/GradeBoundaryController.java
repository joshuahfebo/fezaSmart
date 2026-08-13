package com.fezaschools.fezasmart.grade_boundary;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grade-boundaries")
public class GradeBoundaryController {

    private final GradeBoundaryService gradeBoundaryService;

    public GradeBoundaryController(GradeBoundaryService gradeBoundaryService) {
        this.gradeBoundaryService = gradeBoundaryService;
    }

    @GetMapping
    public ResponseEntity<List<GradeBoundaryDTO>> findAll() {
        return ResponseEntity.ok(gradeBoundaryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeBoundaryDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(gradeBoundaryService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid GradeBoundaryDTO gradeBoundaryDTO) {
        return new ResponseEntity<>(gradeBoundaryService.create(gradeBoundaryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid GradeBoundaryDTO gradeBoundaryDTO) {
        gradeBoundaryService.update(id, gradeBoundaryDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        gradeBoundaryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
