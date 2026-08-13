package com.fezaschools.fezasmart.combination;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combinations")
public class CombinationController {

    private final CombinationService combinationService;

    public CombinationController(CombinationService combinationService) {
        this.combinationService = combinationService;
    }

    @GetMapping
    public ResponseEntity<List<CombinationDTO>> findAll() {
        return ResponseEntity.ok(combinationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CombinationDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(combinationService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid CombinationDTO combinationDTO) {
        return new ResponseEntity<>(combinationService.create(combinationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid CombinationDTO combinationDTO) {
        combinationService.update(id, combinationDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        combinationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
