package com.fezaschools.fezasmart.api_key;

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
@RequestMapping(value = "/api/apiKeys", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiKeyResource {

    private final ApiKeyService apiKeyService;

    public ApiKeyResource(final ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyDTO>> getAllApiKeys() {
        return ResponseEntity.ok(apiKeyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyDTO> getApiKey(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(apiKeyService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createApiKey(@RequestBody @Valid final ApiKeyDTO apiKeyDTO) {
        final Integer createdId = apiKeyService.create(apiKeyDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateApiKey(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ApiKeyDTO apiKeyDTO) {
        apiKeyService.update(id, apiKeyDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiKey(@PathVariable(name = "id") final Integer id) {
        apiKeyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
