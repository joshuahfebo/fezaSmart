package com.fezaschools.fezasmart.guard_shift;

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
@RequestMapping(value = "/api/guardShifts", produces = MediaType.APPLICATION_JSON_VALUE)
public class GuardShiftResource {

    private final GuardShiftService guardShiftService;

    public GuardShiftResource(final GuardShiftService guardShiftService) {
        this.guardShiftService = guardShiftService;
    }

    @GetMapping
    public ResponseEntity<List<GuardShiftDTO>> getAllGuardShifts() {
        return ResponseEntity.ok(guardShiftService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuardShiftDTO> getGuardShift(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(guardShiftService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createGuardShift(
            @RequestBody @Valid final GuardShiftDTO guardShiftDTO) {
        final Integer createdId = guardShiftService.create(guardShiftDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateGuardShift(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final GuardShiftDTO guardShiftDTO) {
        guardShiftService.update(id, guardShiftDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuardShift(@PathVariable(name = "id") final Integer id) {
        guardShiftService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
