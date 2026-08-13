package com.fezaschools.fezasmart.violation;

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
@RequestMapping(value = "/api/violations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ViolationResource {

    private final ViolationService violationService;

    public ViolationResource(final ViolationService violationService) {
        this.violationService = violationService;
    }

    @GetMapping
    public ResponseEntity<List<ViolationDTO>> getAllViolations() {
        return ResponseEntity.ok(violationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViolationDTO> getViolation(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(violationService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createViolation(
            @RequestBody @Valid final ViolationDTO violationDTO) {
        final Integer createdId = violationService.create(violationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateViolation(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ViolationDTO violationDTO) {
        violationService.update(id, violationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViolation(@PathVariable(name = "id") final Integer id) {
        violationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
