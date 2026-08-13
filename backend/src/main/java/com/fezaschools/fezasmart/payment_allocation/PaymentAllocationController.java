package com.fezaschools.fezasmart.payment_allocation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-allocations")
public class PaymentAllocationController {

    private final PaymentAllocationService paymentAllocationService;

    public PaymentAllocationController(PaymentAllocationService paymentAllocationService) {
        this.paymentAllocationService = paymentAllocationService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentAllocationDTO>> findAll() {
        return ResponseEntity.ok(paymentAllocationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentAllocationDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentAllocationService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid PaymentAllocationDTO paymentAllocationDTO) {
        return new ResponseEntity<>(paymentAllocationService.create(paymentAllocationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid PaymentAllocationDTO paymentAllocationDTO) {
        paymentAllocationService.update(id, paymentAllocationDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paymentAllocationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
