package com.fezaschools.fezasmart.discipline_record;

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
@RequestMapping(value = "/api/disciplineRecords", produces = MediaType.APPLICATION_JSON_VALUE)
public class DisciplineRecordResource {

    private final DisciplineRecordService disciplineRecordService;

    public DisciplineRecordResource(final DisciplineRecordService disciplineRecordService) {
        this.disciplineRecordService = disciplineRecordService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplineRecordDTO>> getAllDisciplineRecords() {
        return ResponseEntity.ok(disciplineRecordService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineRecordDTO> getDisciplineRecord(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(disciplineRecordService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createDisciplineRecord(
            @RequestBody @Valid final DisciplineRecordDTO disciplineRecordDTO) {
        final Integer createdId = disciplineRecordService.create(disciplineRecordDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateDisciplineRecord(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final DisciplineRecordDTO disciplineRecordDTO) {
        disciplineRecordService.update(id, disciplineRecordDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisciplineRecord(
            @PathVariable(name = "id") final Integer id) {
        disciplineRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
