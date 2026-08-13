package com.fezaschools.fezasmart.academic_year;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearDTO>> findAll() {
        return ResponseEntity.ok(academicYearService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(academicYearService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid AcademicYearDTO academicYearDTO) {
        return new ResponseEntity<>(academicYearService.create(academicYearDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid AcademicYearDTO academicYearDTO) {
        academicYearService.update(id, academicYearDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        academicYearService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
