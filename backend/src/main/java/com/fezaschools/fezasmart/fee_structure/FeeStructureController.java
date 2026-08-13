package com.fezaschools.fezasmart.fee_structure;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-structures")
public class FeeStructureController {

    private final FeeStructureService feeStructureService;

    public FeeStructureController(FeeStructureService feeStructureService) {
        this.feeStructureService = feeStructureService;
    }

    @GetMapping
    public ResponseEntity<List<FeeStructureDTO>> findAll() {
        return ResponseEntity.ok(feeStructureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeStructureDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(feeStructureService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid FeeStructureDTO feeStructureDTO) {
        return new ResponseEntity<>(feeStructureService.create(feeStructureDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid FeeStructureDTO feeStructureDTO) {
        feeStructureService.update(id, feeStructureDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        feeStructureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
