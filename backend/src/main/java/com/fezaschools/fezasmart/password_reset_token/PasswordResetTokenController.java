package com.fezaschools.fezasmart.password_reset_token;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password-reset-tokens")
public class PasswordResetTokenController {

    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetTokenController(PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @GetMapping
    public ResponseEntity<List<PasswordResetTokenDTO>> findAll() {
        return ResponseEntity.ok(passwordResetTokenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasswordResetTokenDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(passwordResetTokenService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid PasswordResetTokenDTO passwordResetTokenDTO) {
        return new ResponseEntity<>(passwordResetTokenService.create(passwordResetTokenDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid PasswordResetTokenDTO passwordResetTokenDTO) {
        passwordResetTokenService.update(id, passwordResetTokenDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        passwordResetTokenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
