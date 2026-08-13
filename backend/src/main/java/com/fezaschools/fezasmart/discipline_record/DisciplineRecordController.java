package com.fezaschools.fezasmart.discipline_record;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discipline-records")
public class DisciplineRecordController {

    private final DisciplineRecordService disciplineRecordService;

    public DisciplineRecordController(DisciplineRecordService disciplineRecordService) {
        this.disciplineRecordService = disciplineRecordService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplineRecordDTO>> findAll() {
        return ResponseEntity.ok(disciplineRecordService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineRecordDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(disciplineRecordService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid DisciplineRecordDTO disciplineRecordDTO) {
        return new ResponseEntity<>(disciplineRecordService.create(disciplineRecordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid DisciplineRecordDTO disciplineRecordDTO) {
        disciplineRecordService.update(id, disciplineRecordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        disciplineRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
