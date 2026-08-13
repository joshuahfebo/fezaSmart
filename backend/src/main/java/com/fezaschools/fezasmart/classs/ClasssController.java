package com.fezaschools.fezasmart.classs;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasssController {

    private final ClasssService classsService;

    public ClasssController(ClasssService classsService) {
        this.classsService = classsService;
    }

    @GetMapping
    public ResponseEntity<List<ClasssDTO>> findAll() {
        return ResponseEntity.ok(classsService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<ClasssDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(classsService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasssDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(classsService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid ClasssDTO classsDTO) {
        return new ResponseEntity<>(classsService.create(classsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ClasssDTO classsDTO) {
        classsService.update(id, classsDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        classsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
