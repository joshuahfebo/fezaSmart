package com.fezaschools.fezasmart.subject;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> findAll() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<SubjectDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subjectService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(subjectService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid SubjectDTO subjectDTO) {
        return new ResponseEntity<>(subjectService.create(subjectDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid SubjectDTO subjectDTO) {
        subjectService.update(id, subjectDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
