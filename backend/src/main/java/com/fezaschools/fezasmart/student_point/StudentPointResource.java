package com.fezaschools.fezasmart.student_point;

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
@RequestMapping(value = "/api/studentPoints", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentPointResource {

    private final StudentPointService studentPointService;

    public StudentPointResource(final StudentPointService studentPointService) {
        this.studentPointService = studentPointService;
    }

    @GetMapping
    public ResponseEntity<List<StudentPointDTO>> getAllStudentPoints() {
        return ResponseEntity.ok(studentPointService.findAll());
    }

    @GetMapping("/{pointType}")
    public ResponseEntity<StudentPointDTO> getStudentPoint(
            @PathVariable(name = "pointType") final String pointType) {
        return ResponseEntity.ok(studentPointService.get(pointType));
    }

    @PostMapping
    public ResponseEntity<String> createStudentPoint(
            @RequestBody @Valid final StudentPointDTO studentPointDTO) {
        final String createdPointType = studentPointService.create(studentPointDTO);
        return new ResponseEntity<>('"' + createdPointType + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{pointType}")
    public ResponseEntity<String> updateStudentPoint(
            @PathVariable(name = "pointType") final String pointType,
            @RequestBody @Valid final StudentPointDTO studentPointDTO) {
        studentPointService.update(pointType, studentPointDTO);
        return ResponseEntity.ok('"' + pointType + '"');
    }

    @DeleteMapping("/{pointType}")
    public ResponseEntity<Void> deleteStudentPoint(
            @PathVariable(name = "pointType") final String pointType) {
        studentPointService.delete(pointType);
        return ResponseEntity.noContent().build();
    }

}
