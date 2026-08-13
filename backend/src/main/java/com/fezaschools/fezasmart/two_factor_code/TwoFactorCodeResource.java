package com.fezaschools.fezasmart.two_factor_code;

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
@RequestMapping(value = "/api/twoFactorCodes", produces = MediaType.APPLICATION_JSON_VALUE)
public class TwoFactorCodeResource {

    private final TwoFactorCodeService twoFactorCodeService;

    public TwoFactorCodeResource(final TwoFactorCodeService twoFactorCodeService) {
        this.twoFactorCodeService = twoFactorCodeService;
    }

    @GetMapping
    public ResponseEntity<List<TwoFactorCodeDTO>> getAllTwoFactorCodes() {
        return ResponseEntity.ok(twoFactorCodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TwoFactorCodeDTO> getTwoFactorCode(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(twoFactorCodeService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createTwoFactorCode(
            @RequestBody @Valid final TwoFactorCodeDTO twoFactorCodeDTO) {
        final Integer createdId = twoFactorCodeService.create(twoFactorCodeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateTwoFactorCode(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final TwoFactorCodeDTO twoFactorCodeDTO) {
        twoFactorCodeService.update(id, twoFactorCodeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTwoFactorCode(@PathVariable(name = "id") final Integer id) {
        twoFactorCodeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
