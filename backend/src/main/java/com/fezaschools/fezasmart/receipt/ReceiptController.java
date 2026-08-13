package com.fezaschools.fezasmart.receipt;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> findAll() {
        return ResponseEntity.ok(receiptService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(receiptService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ReceiptDTO receiptDTO) {
        return new ResponseEntity<>(receiptService.create(receiptDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ReceiptDTO receiptDTO) {
        receiptService.update(id, receiptDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        receiptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
