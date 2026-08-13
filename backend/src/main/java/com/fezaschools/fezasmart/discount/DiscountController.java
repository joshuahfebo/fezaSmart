package com.fezaschools.fezasmart.discount;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> findAll() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(discountService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid DiscountDTO discountDTO) {
        return new ResponseEntity<>(discountService.create(discountDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid DiscountDTO discountDTO) {
        discountService.update(id, discountDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
