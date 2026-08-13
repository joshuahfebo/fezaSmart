package com.fezaschools.fezasmart.school;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<List<SchoolDTO>> findAll() {
        return ResponseEntity.ok(schoolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(schoolService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid SchoolDTO schoolDTO) {
        return new ResponseEntity<>(schoolService.create(schoolDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid SchoolDTO schoolDTO) {
        schoolService.update(id, schoolDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        schoolService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
