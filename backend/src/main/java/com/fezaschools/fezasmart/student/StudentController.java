package com.fezaschools.fezasmart.student;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<StudentDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(studentService.findAll(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentDTO> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(studentService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid StudentDTO studentDTO) {
        return new ResponseEntity<>(studentService.create(studentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid StudentDTO studentDTO) {
        studentService.update(id, studentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
