package com.fezaschools.fezasmart.result;

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
@RequestMapping(value = "/api/results", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResultResource {

    private final ResultService resultService;

    public ResultResource(final ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public ResponseEntity<List<ResultDTO>> getAllResults() {
        return ResponseEntity.ok(resultService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO> getResult(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(resultService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createResult(@RequestBody @Valid final ResultDTO resultDTO) {
        final Integer createdId = resultService.create(resultDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateResult(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ResultDTO resultDTO) {
        resultService.update(id, resultDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable(name = "id") final Integer id) {
        resultService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
