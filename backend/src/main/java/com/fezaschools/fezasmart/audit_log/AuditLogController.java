package com.fezaschools.fezasmart.audit_log;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogDTO>> findAll() {
        return ResponseEntity.ok(auditLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(auditLogService.get(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid AuditLogDTO auditLogDTO) {
        return new ResponseEntity<>(auditLogService.create(auditLogDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid AuditLogDTO auditLogDTO) {
        auditLogService.update(id, auditLogDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        auditLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
