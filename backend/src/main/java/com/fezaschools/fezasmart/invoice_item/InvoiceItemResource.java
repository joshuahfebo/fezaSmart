package com.fezaschools.fezasmart.invoice_item;

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
@RequestMapping(value = "/api/invoiceItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class InvoiceItemResource {

    private final InvoiceItemService invoiceItemService;

    public InvoiceItemResource(final InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemDTO>> getAllInvoiceItems() {
        return ResponseEntity.ok(invoiceItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItemDTO> getInvoiceItem(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(invoiceItemService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createInvoiceItem(
            @RequestBody @Valid final InvoiceItemDTO invoiceItemDTO) {
        final Integer createdId = invoiceItemService.create(invoiceItemDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateInvoiceItem(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final InvoiceItemDTO invoiceItemDTO) {
        invoiceItemService.update(id, invoiceItemDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable(name = "id") final Integer id) {
        invoiceItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
