package com.fezaschools.fezasmart.timetable;

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
@RequestMapping(value = "/api/timetables", produces = MediaType.APPLICATION_JSON_VALUE)
public class TimetableResource {

    private final TimetableService timetableService;

    public TimetableResource(final TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public ResponseEntity<List<TimetableDTO>> getAllTimetables() {
        return ResponseEntity.ok(timetableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimetableDTO> getTimetable(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(timetableService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createTimetable(
            @RequestBody @Valid final TimetableDTO timetableDTO) {
        final Integer createdId = timetableService.create(timetableDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateTimetable(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final TimetableDTO timetableDTO) {
        timetableService.update(id, timetableDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable(name = "id") final Integer id) {
        timetableService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
