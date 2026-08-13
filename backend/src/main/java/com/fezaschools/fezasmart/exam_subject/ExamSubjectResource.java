package com.fezaschools.fezasmart.exam_subject;

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
@RequestMapping(value = "/api/examSubjects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExamSubjectResource {

    private final ExamSubjectService examSubjectService;

    public ExamSubjectResource(final ExamSubjectService examSubjectService) {
        this.examSubjectService = examSubjectService;
    }

    @GetMapping
    public ResponseEntity<List<ExamSubjectDTO>> getAllExamSubjects() {
        return ResponseEntity.ok(examSubjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamSubjectDTO> getExamSubject(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(examSubjectService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createExamSubject(
            @RequestBody @Valid final ExamSubjectDTO examSubjectDTO) {
        final Integer createdId = examSubjectService.create(examSubjectDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateExamSubject(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ExamSubjectDTO examSubjectDTO) {
        examSubjectService.update(id, examSubjectDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamSubject(@PathVariable(name = "id") final Integer id) {
        examSubjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
