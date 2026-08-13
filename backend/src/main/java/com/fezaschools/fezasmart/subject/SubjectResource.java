package com.fezaschools.fezasmart.subject;

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
@RequestMapping(value = "/api/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubjectResource {

    private final SubjectService subjectService;

    public SubjectResource(final SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubject(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(subjectService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createSubject(@RequestBody @Valid final SubjectDTO subjectDTO) {
        final Integer createdId = subjectService.create(subjectDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateSubject(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final SubjectDTO subjectDTO) {
        subjectService.update(id, subjectDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable(name = "id") final Integer id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
