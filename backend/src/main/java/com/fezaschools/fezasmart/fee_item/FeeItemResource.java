package com.fezaschools.fezasmart.fee_item;

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
@RequestMapping(value = "/api/feeItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeeItemResource {

    private final FeeItemService feeItemService;

    public FeeItemResource(final FeeItemService feeItemService) {
        this.feeItemService = feeItemService;
    }

    @GetMapping
    public ResponseEntity<List<FeeItemDTO>> getAllFeeItems() {
        return ResponseEntity.ok(feeItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeItemDTO> getFeeItem(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(feeItemService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createFeeItem(@RequestBody @Valid final FeeItemDTO feeItemDTO) {
        final Integer createdId = feeItemService.create(feeItemDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateFeeItem(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final FeeItemDTO feeItemDTO) {
        feeItemService.update(id, feeItemDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeItem(@PathVariable(name = "id") final Integer id) {
        feeItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
