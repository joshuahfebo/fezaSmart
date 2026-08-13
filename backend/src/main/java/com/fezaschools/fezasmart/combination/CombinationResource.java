package com.fezaschools.fezasmart.combination;

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
@RequestMapping(value = "/api/combinations", produces = MediaType.APPLICATION_JSON_VALUE)
public class CombinationResource {

    private final CombinationService combinationService;

    public CombinationResource(final CombinationService combinationService) {
        this.combinationService = combinationService;
    }

    @GetMapping
    public ResponseEntity<List<CombinationDTO>> getAllCombinations() {
        return ResponseEntity.ok(combinationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CombinationDTO> getCombination(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(combinationService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createCombination(
            @RequestBody @Valid final CombinationDTO combinationDTO) {
        final Integer createdId = combinationService.create(combinationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateCombination(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final CombinationDTO combinationDTO) {
        combinationService.update(id, combinationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCombination(@PathVariable(name = "id") final Integer id) {
        combinationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
