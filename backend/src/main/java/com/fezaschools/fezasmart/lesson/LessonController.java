package com.fezaschools.fezasmart.lesson;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> findAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(lessonService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid LessonDTO lessonDTO) {
        return new ResponseEntity<>(lessonService.create(lessonDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid LessonDTO lessonDTO) {
        lessonService.update(id, lessonDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
