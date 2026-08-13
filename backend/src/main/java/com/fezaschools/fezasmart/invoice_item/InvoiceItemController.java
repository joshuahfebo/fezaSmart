package com.fezaschools.fezasmart.invoice_item;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-items")
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;

    public InvoiceItemController(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemDTO>> findAll() {
        return ResponseEntity.ok(invoiceItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItemDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(invoiceItemService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid InvoiceItemDTO invoiceItemDTO) {
        return new ResponseEntity<>(invoiceItemService.create(invoiceItemDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid InvoiceItemDTO invoiceItemDTO) {
        invoiceItemService.update(id, invoiceItemDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        invoiceItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
