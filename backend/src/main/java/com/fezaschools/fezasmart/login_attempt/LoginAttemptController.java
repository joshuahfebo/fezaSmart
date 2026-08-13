package com.fezaschools.fezasmart.login_attempt;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login-attempts")
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;

    public LoginAttemptController(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @GetMapping
    public ResponseEntity<List<LoginAttemptDTO>> findAll() {
        return ResponseEntity.ok(loginAttemptService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginAttemptDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(loginAttemptService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid LoginAttemptDTO loginAttemptDTO) {
        return new ResponseEntity<>(loginAttemptService.create(loginAttemptDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid LoginAttemptDTO loginAttemptDTO) {
        loginAttemptService.update(id, loginAttemptDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        loginAttemptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
