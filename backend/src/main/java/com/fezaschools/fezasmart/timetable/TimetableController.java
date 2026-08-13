package com.fezaschools.fezasmart.timetable;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public ResponseEntity<List<TimetableDTO>> findAll() {
        return ResponseEntity.ok(timetableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimetableDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(timetableService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid TimetableDTO timetableDTO) {
        return new ResponseEntity<>(timetableService.create(timetableDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid TimetableDTO timetableDTO) {
        timetableService.update(id, timetableDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        timetableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
