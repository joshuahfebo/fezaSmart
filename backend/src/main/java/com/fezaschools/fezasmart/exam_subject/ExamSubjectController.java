package com.fezaschools.fezasmart.exam_subject;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-subjects")
public class ExamSubjectController {

    private final ExamSubjectService examSubjectService;

    public ExamSubjectController(ExamSubjectService examSubjectService) {
        this.examSubjectService = examSubjectService;
    }

    @GetMapping
    public ResponseEntity<List<ExamSubjectDTO>> findAll() {
        return ResponseEntity.ok(examSubjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamSubjectDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(examSubjectService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ExamSubjectDTO examSubjectDTO) {
        return new ResponseEntity<>(examSubjectService.create(examSubjectDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ExamSubjectDTO examSubjectDTO) {
        examSubjectService.update(id, examSubjectDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        examSubjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
