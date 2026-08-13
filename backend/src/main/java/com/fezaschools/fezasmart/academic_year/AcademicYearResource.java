package com.fezaschools.fezasmart.academic_year;

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
@RequestMapping(value = "/api/academicYears", produces = MediaType.APPLICATION_JSON_VALUE)
public class AcademicYearResource {

    private final AcademicYearService academicYearService;

    public AcademicYearResource(final AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearDTO>> getAllAcademicYears() {
        return ResponseEntity.ok(academicYearService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearDTO> getAcademicYear(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(academicYearService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createAcademicYear(
            @RequestBody @Valid final AcademicYearDTO academicYearDTO) {
        final Integer createdId = academicYearService.create(academicYearDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateAcademicYear(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final AcademicYearDTO academicYearDTO) {
        academicYearService.update(id, academicYearDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicYear(@PathVariable(name = "id") final Integer id) {
        academicYearService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
