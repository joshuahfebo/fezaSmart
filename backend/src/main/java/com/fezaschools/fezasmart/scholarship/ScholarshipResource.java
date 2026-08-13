package com.fezaschools.fezasmart.scholarship;

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
@RequestMapping(value = "/api/scholarships", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScholarshipResource {

    private final ScholarshipService scholarshipService;

    public ScholarshipResource(final ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    @GetMapping
    public ResponseEntity<List<ScholarshipDTO>> getAllScholarships() {
        return ResponseEntity.ok(scholarshipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipDTO> getScholarship(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(scholarshipService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createScholarship(
            @RequestBody @Valid final ScholarshipDTO scholarshipDTO) {
        final Integer createdId = scholarshipService.create(scholarshipDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateScholarship(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ScholarshipDTO scholarshipDTO) {
        scholarshipService.update(id, scholarshipDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScholarship(@PathVariable(name = "id") final Integer id) {
        scholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
