package com.fezaschools.fezasmart.leave_request;

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
@RequestMapping(value = "/api/leaveRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class LeaveRequestResource {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestResource(final LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO> getLeaveRequest(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(leaveRequestService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createLeaveRequest(
            @RequestBody @Valid final LeaveRequestDTO leaveRequestDTO) {
        final Long createdId = leaveRequestService.create(leaveRequestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLeaveRequest(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LeaveRequestDTO leaveRequestDTO) {
        leaveRequestService.update(id, leaveRequestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable(name = "id") final Long id) {
        leaveRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
