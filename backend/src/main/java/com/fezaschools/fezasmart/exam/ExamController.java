package com.fezaschools.fezasmart.exam;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public ResponseEntity<List<ExamDTO>> findAll() {
        return ResponseEntity.ok(examService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<ExamDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(examService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(examService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ExamDTO examDTO) {
        return new ResponseEntity<>(examService.create(examDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ExamDTO examDTO) {
        examService.update(id, examDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        examService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
