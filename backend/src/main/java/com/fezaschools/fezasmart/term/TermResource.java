package com.fezaschools.fezasmart.term;

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
@RequestMapping(value = "/api/terms", produces = MediaType.APPLICATION_JSON_VALUE)
public class TermResource {

    private final TermService termService;

    public TermResource(final TermService termService) {
        this.termService = termService;
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> getAllTerms() {
        return ResponseEntity.ok(termService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> getTerm(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(termService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createTerm(@RequestBody @Valid final TermDTO termDTO) {
        final Integer createdId = termService.create(termDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateTerm(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final TermDTO termDTO) {
        termService.update(id, termDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable(name = "id") final Integer id) {
        termService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
