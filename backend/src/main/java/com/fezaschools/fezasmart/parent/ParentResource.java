package com.fezaschools.fezasmart.parent;

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
@RequestMapping(value = "/api/parents", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParentResource {

    private final ParentService parentService;

    public ParentResource(final ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping
    public ResponseEntity<List<ParentDTO>> getAllParents() {
        return ResponseEntity.ok(parentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDTO> getParent(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(parentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createParent(@RequestBody @Valid final ParentDTO parentDTO) {
        final Integer createdId = parentService.create(parentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateParent(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ParentDTO parentDTO) {
        parentService.update(id, parentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable(name = "id") final Integer id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
