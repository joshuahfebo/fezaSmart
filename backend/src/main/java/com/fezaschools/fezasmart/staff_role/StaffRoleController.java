package com.fezaschools.fezasmart.staff_role;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-roles")
public class StaffRoleController {

    private final StaffRoleService staffRoleService;

    public StaffRoleController(StaffRoleService staffRoleService) {
        this.staffRoleService = staffRoleService;
    }

    @GetMapping
    public ResponseEntity<List<StaffRoleDTO>> findAll() {
        return ResponseEntity.ok(staffRoleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffRoleDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(staffRoleService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid StaffRoleDTO staffRoleDTO) {
        return new ResponseEntity<>(staffRoleService.create(staffRoleDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid StaffRoleDTO staffRoleDTO) {
        staffRoleService.update(id, staffRoleDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffRoleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
