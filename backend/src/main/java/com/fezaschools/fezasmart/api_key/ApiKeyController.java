package com.fezaschools.fezasmart.api_key;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyDTO>> findAll() {
        return ResponseEntity.ok(apiKeyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(apiKeyService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ApiKeyDTO apiKeyDTO) {
        return new ResponseEntity<>(apiKeyService.create(apiKeyDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ApiKeyDTO apiKeyDTO) {
        apiKeyService.update(id, apiKeyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        apiKeyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
