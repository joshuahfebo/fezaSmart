package com.fezaschools.fezasmart.password_reset_token;

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
@RequestMapping(value = "/api/passwordResetTokens", produces = MediaType.APPLICATION_JSON_VALUE)
public class PasswordResetTokenResource {

    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetTokenResource(final PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @GetMapping
    public ResponseEntity<List<PasswordResetTokenDTO>> getAllPasswordResetTokens() {
        return ResponseEntity.ok(passwordResetTokenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasswordResetTokenDTO> getPasswordResetToken(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(passwordResetTokenService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createPasswordResetToken(
            @RequestBody @Valid final PasswordResetTokenDTO passwordResetTokenDTO) {
        final Integer createdId = passwordResetTokenService.create(passwordResetTokenDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePasswordResetToken(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final PasswordResetTokenDTO passwordResetTokenDTO) {
        passwordResetTokenService.update(id, passwordResetTokenDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasswordResetToken(
            @PathVariable(name = "id") final Integer id) {
        passwordResetTokenService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
