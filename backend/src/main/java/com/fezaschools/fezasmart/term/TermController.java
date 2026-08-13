package com.fezaschools.fezasmart.term;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> findAll() {
        return ResponseEntity.ok(termService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(termService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid TermDTO termDTO) {
        return new ResponseEntity<>(termService.create(termDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid TermDTO termDTO) {
        termService.update(id, termDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        termService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
