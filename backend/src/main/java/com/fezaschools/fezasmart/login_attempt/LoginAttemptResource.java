package com.fezaschools.fezasmart.login_attempt;

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
@RequestMapping(value = "/api/loginAttempts", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginAttemptResource {

    private final LoginAttemptService loginAttemptService;

    public LoginAttemptResource(final LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @GetMapping
    public ResponseEntity<List<LoginAttemptDTO>> getAllLoginAttempts() {
        return ResponseEntity.ok(loginAttemptService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginAttemptDTO> getLoginAttempt(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(loginAttemptService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createLoginAttempt(
            @RequestBody @Valid final LoginAttemptDTO loginAttemptDTO) {
        final Integer createdId = loginAttemptService.create(loginAttemptDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateLoginAttempt(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final LoginAttemptDTO loginAttemptDTO) {
        loginAttemptService.update(id, loginAttemptDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoginAttempt(@PathVariable(name = "id") final Integer id) {
        loginAttemptService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
