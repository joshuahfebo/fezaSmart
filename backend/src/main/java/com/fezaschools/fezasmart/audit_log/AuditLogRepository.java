package com.fezaschools.fezasmart.audit_log;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    AuditLog findFirstByUserId(Integer id);

}
