package com.fezaschools.fezasmart.student_score;

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
@RequestMapping(value = "/api/studentScores", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentScoreResource {

    private final StudentScoreService studentScoreService;

    public StudentScoreResource(final StudentScoreService studentScoreService) {
        this.studentScoreService = studentScoreService;
    }

    @GetMapping
    public ResponseEntity<List<StudentScoreDTO>> getAllStudentScores() {
        return ResponseEntity.ok(studentScoreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentScoreDTO> getStudentScore(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(studentScoreService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createStudentScore(
            @RequestBody @Valid final StudentScoreDTO studentScoreDTO) {
        final Integer createdId = studentScoreService.create(studentScoreDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateStudentScore(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final StudentScoreDTO studentScoreDTO) {
        studentScoreService.update(id, studentScoreDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentScore(@PathVariable(name = "id") final Integer id) {
        studentScoreService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
