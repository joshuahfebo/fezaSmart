package com.fezaschools.fezasmart.classs;

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
@RequestMapping(value = "/api/classses", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClasssResource {

    private final ClasssService classsService;

    public ClasssResource(final ClasssService classsService) {
        this.classsService = classsService;
    }

    @GetMapping
    public ResponseEntity<List<ClasssDTO>> getAllClassses() {
        return ResponseEntity.ok(classsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasssDTO> getClasss(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(classsService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createClasss(@RequestBody @Valid final ClasssDTO classsDTO) {
        final Integer createdId = classsService.create(classsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateClasss(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ClasssDTO classsDTO) {
        classsService.update(id, classsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasss(@PathVariable(name = "id") final Integer id) {
        classsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
