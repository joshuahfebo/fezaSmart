package com.fezaschools.fezasmart.fee_structure;

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
@RequestMapping(value = "/api/feeStructures", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeeStructureResource {

    private final FeeStructureService feeStructureService;

    public FeeStructureResource(final FeeStructureService feeStructureService) {
        this.feeStructureService = feeStructureService;
    }

    @GetMapping
    public ResponseEntity<List<FeeStructureDTO>> getAllFeeStructures() {
        return ResponseEntity.ok(feeStructureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeStructureDTO> getFeeStructure(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(feeStructureService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createFeeStructure(
            @RequestBody @Valid final FeeStructureDTO feeStructureDTO) {
        final Integer createdId = feeStructureService.create(feeStructureDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateFeeStructure(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final FeeStructureDTO feeStructureDTO) {
        feeStructureService.update(id, feeStructureDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeStructure(@PathVariable(name = "id") final Integer id) {
        feeStructureService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
