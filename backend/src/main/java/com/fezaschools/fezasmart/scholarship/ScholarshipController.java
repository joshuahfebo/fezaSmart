package com.fezaschools.fezasmart.scholarship;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    public ScholarshipController(ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    @GetMapping
    public ResponseEntity<List<ScholarshipDTO>> findAll() {
        return ResponseEntity.ok(scholarshipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(scholarshipService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ScholarshipDTO scholarshipDTO) {
        return new ResponseEntity<>(scholarshipService.create(scholarshipDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ScholarshipDTO scholarshipDTO) {
        scholarshipService.update(id, scholarshipDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        scholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
