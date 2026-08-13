package com.fezaschools.fezasmart.leave_request;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO>> findAll() {
        return ResponseEntity.ok(leaveRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid LeaveRequestDTO leaveRequestDTO) {
        return new ResponseEntity<>(leaveRequestService.create(leaveRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid LeaveRequestDTO leaveRequestDTO) {
        leaveRequestService.update(id, leaveRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        leaveRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
