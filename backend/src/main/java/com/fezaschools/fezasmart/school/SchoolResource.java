package com.fezaschools.fezasmart.school;

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
@RequestMapping(value = "/api/schools", produces = MediaType.APPLICATION_JSON_VALUE)
public class SchoolResource {

    private final SchoolService schoolService;

    public SchoolResource(final SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        return ResponseEntity.ok(schoolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolDTO> getSchool(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(schoolService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createSchool(@RequestBody @Valid final SchoolDTO schoolDTO) {
        final Integer createdId = schoolService.create(schoolDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateSchool(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final SchoolDTO schoolDTO) {
        schoolService.update(id, schoolDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable(name = "id") final Integer id) {
        schoolService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
