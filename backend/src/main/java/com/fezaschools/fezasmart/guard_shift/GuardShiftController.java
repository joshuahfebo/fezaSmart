package com.fezaschools.fezasmart.guard_shift;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guard-shifts")
public class GuardShiftController {

    private final GuardShiftService guardShiftService;

    public GuardShiftController(GuardShiftService guardShiftService) {
        this.guardShiftService = guardShiftService;
    }

    @GetMapping
    public ResponseEntity<List<GuardShiftDTO>> findAll() {
        return ResponseEntity.ok(guardShiftService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuardShiftDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(guardShiftService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid GuardShiftDTO guardShiftDTO) {
        return new ResponseEntity<>(guardShiftService.create(guardShiftDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid GuardShiftDTO guardShiftDTO) {
        guardShiftService.update(id, guardShiftDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        guardShiftService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
