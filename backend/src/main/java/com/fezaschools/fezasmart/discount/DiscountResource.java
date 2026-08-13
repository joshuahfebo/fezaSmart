package com.fezaschools.fezasmart.discount;

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
@RequestMapping(value = "/api/discounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscountResource {

    private final DiscountService discountService;

    public DiscountResource(final DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscount(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(discountService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createDiscount(
            @RequestBody @Valid final DiscountDTO discountDTO) {
        final Integer createdId = discountService.create(discountDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateDiscount(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final DiscountDTO discountDTO) {
        discountService.update(id, discountDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable(name = "id") final Integer id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
