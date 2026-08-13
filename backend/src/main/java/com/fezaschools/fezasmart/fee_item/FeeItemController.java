package com.fezaschools.fezasmart.fee_item;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-items")
public class FeeItemController {

    private final FeeItemService feeItemService;

    public FeeItemController(FeeItemService feeItemService) {
        this.feeItemService = feeItemService;
    }

    @GetMapping
    public ResponseEntity<List<FeeItemDTO>> findAll() {
        return ResponseEntity.ok(feeItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeItemDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(feeItemService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid FeeItemDTO feeItemDTO) {
        return new ResponseEntity<>(feeItemService.create(feeItemDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid FeeItemDTO feeItemDTO) {
        feeItemService.update(id, feeItemDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        feeItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
