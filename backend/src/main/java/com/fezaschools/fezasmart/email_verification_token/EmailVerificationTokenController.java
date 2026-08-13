package com.fezaschools.fezasmart.email_verification_token;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email-verification-tokens")
public class EmailVerificationTokenController {

    private final EmailVerificationTokenService emailVerificationTokenService;

    public EmailVerificationTokenController(EmailVerificationTokenService emailVerificationTokenService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @GetMapping
    public ResponseEntity<List<EmailVerificationTokenDTO>> findAll() {
        return ResponseEntity.ok(emailVerificationTokenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailVerificationTokenDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(emailVerificationTokenService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid EmailVerificationTokenDTO emailVerificationTokenDTO) {
        return new ResponseEntity<>(emailVerificationTokenService.create(emailVerificationTokenDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid EmailVerificationTokenDTO emailVerificationTokenDTO) {
        emailVerificationTokenService.update(id, emailVerificationTokenDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        emailVerificationTokenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
