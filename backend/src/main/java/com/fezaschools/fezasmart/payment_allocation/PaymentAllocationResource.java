package com.fezaschools.fezasmart.payment_allocation;

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
@RequestMapping(value = "/api/paymentAllocations", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentAllocationResource {

    private final PaymentAllocationService paymentAllocationService;

    public PaymentAllocationResource(final PaymentAllocationService paymentAllocationService) {
        this.paymentAllocationService = paymentAllocationService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentAllocationDTO>> getAllPaymentAllocations() {
        return ResponseEntity.ok(paymentAllocationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentAllocationDTO> getPaymentAllocation(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(paymentAllocationService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createPaymentAllocation(
            @RequestBody @Valid final PaymentAllocationDTO paymentAllocationDTO) {
        final Integer createdId = paymentAllocationService.create(paymentAllocationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePaymentAllocation(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final PaymentAllocationDTO paymentAllocationDTO) {
        paymentAllocationService.update(id, paymentAllocationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentAllocation(
            @PathVariable(name = "id") final Integer id) {
        paymentAllocationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
