package com.fezaschools.fezasmart.student_score;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-scores")
public class StudentScoreController {

    private final StudentScoreService studentScoreService;

    public StudentScoreController(StudentScoreService studentScoreService) {
        this.studentScoreService = studentScoreService;
    }

    @GetMapping
    public ResponseEntity<List<StudentScoreDTO>> findAll() {
        return ResponseEntity.ok(studentScoreService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<StudentScoreDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(studentScoreService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentScoreDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(studentScoreService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid StudentScoreDTO studentScoreDTO) {
        return new ResponseEntity<>(studentScoreService.create(studentScoreDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid StudentScoreDTO studentScoreDTO) {
        studentScoreService.update(id, studentScoreDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        studentScoreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
