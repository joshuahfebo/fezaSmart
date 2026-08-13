package com.fezaschools.fezasmart.email_verification_token;

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
@RequestMapping(value = "/api/emailVerificationTokens", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailVerificationTokenResource {

    private final EmailVerificationTokenService emailVerificationTokenService;

    public EmailVerificationTokenResource(
            final EmailVerificationTokenService emailVerificationTokenService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @GetMapping
    public ResponseEntity<List<EmailVerificationTokenDTO>> getAllEmailVerificationTokens() {
        return ResponseEntity.ok(emailVerificationTokenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailVerificationTokenDTO> getEmailVerificationToken(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(emailVerificationTokenService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createEmailVerificationToken(
            @RequestBody @Valid final EmailVerificationTokenDTO emailVerificationTokenDTO) {
        final Integer createdId = emailVerificationTokenService.create(emailVerificationTokenDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateEmailVerificationToken(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final EmailVerificationTokenDTO emailVerificationTokenDTO) {
        emailVerificationTokenService.update(id, emailVerificationTokenDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailVerificationToken(
            @PathVariable(name = "id") final Integer id) {
        emailVerificationTokenService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
