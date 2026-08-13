package com.fezaschools.fezasmart.staff;

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
@RequestMapping(value = "/api/staffs", produces = MediaType.APPLICATION_JSON_VALUE)
public class StaffResource {

    private final StaffService staffService;

    public StaffResource(final StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaffs() {
        return ResponseEntity.ok(staffService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaff(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(staffService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> createStaff(@RequestBody @Valid final StaffDTO staffDTO) {
        final Integer createdId = staffService.create(staffDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateStaff(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final StaffDTO staffDTO) {
        staffService.update(id, staffDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable(name = "id") final Integer id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
