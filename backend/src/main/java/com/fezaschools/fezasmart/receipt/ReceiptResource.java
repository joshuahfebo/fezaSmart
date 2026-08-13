package com.fezaschools.fezasmart.receipt;

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
@RequestMapping(value = "/api/receipts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReceiptResource {

    private final ReceiptService receiptService;

    public ReceiptResource(final ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        return ResponseEntity.ok(receiptService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDTO> getReceipt(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(receiptService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createReceipt(@RequestBody @Valid final ReceiptDTO receiptDTO) {
        final Integer createdId = receiptService.create(receiptDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateReceipt(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ReceiptDTO receiptDTO) {
        receiptService.update(id, receiptDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable(name = "id") final Integer id) {
        receiptService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
