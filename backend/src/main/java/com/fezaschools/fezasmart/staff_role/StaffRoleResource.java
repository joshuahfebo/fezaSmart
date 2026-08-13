package com.fezaschools.fezasmart.staff_role;

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
@RequestMapping(value = "/api/staffRoles", produces = MediaType.APPLICATION_JSON_VALUE)
public class StaffRoleResource {

    private final StaffRoleService staffRoleService;

    public StaffRoleResource(final StaffRoleService staffRoleService) {
        this.staffRoleService = staffRoleService;
    }

    @GetMapping
    public ResponseEntity<List<StaffRoleDTO>> getAllStaffRoles() {
        return ResponseEntity.ok(staffRoleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffRoleDTO> getStaffRole(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(staffRoleService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createStaffRole(
            @RequestBody @Valid final StaffRoleDTO staffRoleDTO) {
        final Long createdId = staffRoleService.create(staffRoleDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateStaffRole(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final StaffRoleDTO staffRoleDTO) {
        staffRoleService.update(id, staffRoleDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaffRole(@PathVariable(name = "id") final Long id) {
        staffRoleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
