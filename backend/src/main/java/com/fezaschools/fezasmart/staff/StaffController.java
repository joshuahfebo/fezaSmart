package com.fezaschools.fezasmart.staff;

import com.fezaschools.fezasmart.util.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffDTO>> findAll() {
        return ResponseEntity.ok(staffService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<StaffDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(staffService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(staffService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid StaffDTO staffDTO) {
        return new ResponseEntity<>(staffService.create(staffDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid StaffDTO staffDTO) {
        staffService.update(id, staffDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
