package com.fezaschools.fezasmart.violation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
public class ViolationController {

    private final ViolationService violationService;

    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }

    @GetMapping
    public ResponseEntity<List<ViolationDTO>> findAll() {
        return ResponseEntity.ok(violationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViolationDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(violationService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ViolationDTO violationDTO) {
        return new ResponseEntity<>(violationService.create(violationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ViolationDTO violationDTO) {
        violationService.update(id, violationDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        violationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
