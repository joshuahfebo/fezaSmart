package com.fezaschools.fezasmart.result;

import com.fezaschools.fezasmart.util.PagedResponse;
import com.fezaschools.fezasmart.security.RequireRole;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "GENERAL_SECOND_MASTER", "SECOND_MASTER"})
    public ResponseEntity<List<ResultDTO>> findAll() {
        return ResponseEntity.ok(resultService.findAll());
    }

    @GetMapping("/paginated")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "GENERAL_SECOND_MASTER", "SECOND_MASTER"})
    public ResponseEntity<PagedResponse<ResultDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(resultService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "GENERAL_SECOND_MASTER", "SECOND_MASTER"})
    public ResponseEntity<ResultDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(resultService.get(id));
    }

    @PostMapping
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "SECOND_MASTER"})
    public ResponseEntity<Integer> create(@RequestBody @Valid ResultDTO resultDTO) {
        return new ResponseEntity<>(resultService.create(resultDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER", "SECOND_MASTER"})
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ResultDTO resultDTO) {
        resultService.update(id, resultDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @RequireRole({"SUPER_ADMIN", "HEAD_MASTER"})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        resultService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
