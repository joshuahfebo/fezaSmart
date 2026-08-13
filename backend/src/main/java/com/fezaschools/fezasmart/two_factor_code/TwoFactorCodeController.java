package com.fezaschools.fezasmart.two_factor_code;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/two-factor-codes")
public class TwoFactorCodeController {

    private final TwoFactorCodeService twoFactorCodeService;

    public TwoFactorCodeController(TwoFactorCodeService twoFactorCodeService) {
        this.twoFactorCodeService = twoFactorCodeService;
    }

    @GetMapping
    public ResponseEntity<List<TwoFactorCodeDTO>> findAll() {
        return ResponseEntity.ok(twoFactorCodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TwoFactorCodeDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(twoFactorCodeService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid TwoFactorCodeDTO twoFactorCodeDTO) {
        return new ResponseEntity<>(twoFactorCodeService.create(twoFactorCodeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid TwoFactorCodeDTO twoFactorCodeDTO) {
        twoFactorCodeService.update(id, twoFactorCodeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        twoFactorCodeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
