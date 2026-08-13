package com.fezaschools.fezasmart.permission;

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
@RequestMapping(value = "/api/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionResource {

    private final PermissionService permissionService;

    public PermissionResource(final PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermission(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(permissionService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createPermission(
            @RequestBody @Valid final PermissionDTO permissionDTO) {
        final Integer createdId = permissionService.create(permissionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePermission(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final PermissionDTO permissionDTO) {
        permissionService.update(id, permissionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable(name = "id") final Integer id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
