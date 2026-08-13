package com.fezaschools.fezasmart.lesson;

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
@RequestMapping(value = "/api/lessons", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonResource {

    private final LessonService lessonService;

    public LessonResource(final LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getAllLessons() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(lessonService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createLesson(@RequestBody @Valid final LessonDTO lessonDTO) {
        final Integer createdId = lessonService.create(lessonDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateLesson(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final LessonDTO lessonDTO) {
        lessonService.update(id, lessonDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable(name = "id") final Integer id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
