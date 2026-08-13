package com.fezaschools.fezasmart.audit_log;

import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(final AuditLogRepository auditLogRepository,
            final UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public List<AuditLogDTO> findAll() {
        final List<AuditLog> auditLogs = auditLogRepository.findAll(Sort.by("id"));
        return auditLogs.stream()
                .map(auditLog -> mapToDTO(auditLog, new AuditLogDTO()))
                .toList();
    }

    public AuditLogDTO get(final Integer id) {
        return auditLogRepository.findById(id)
                .map(auditLog -> mapToDTO(auditLog, new AuditLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AuditLogDTO auditLogDTO) {
        final AuditLog auditLog = new AuditLog();
        mapToEntity(auditLogDTO, auditLog);
        return auditLogRepository.save(auditLog).getId();
    }

    public void update(final Integer id, final AuditLogDTO auditLogDTO) {
        final AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(auditLogDTO, auditLog);
        auditLogRepository.save(auditLog);
    }

    public void delete(final Integer id) {
        final AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        auditLogRepository.delete(auditLog);
    }

    private AuditLogDTO mapToDTO(final AuditLog auditLog, final AuditLogDTO auditLogDTO) {
        auditLogDTO.setId(auditLog.getId());
        auditLogDTO.setEntity(auditLog.getEntity());
        auditLogDTO.setEntityId(auditLog.getEntityId());
        auditLogDTO.setAction(auditLog.getAction());
        auditLogDTO.setOldValue(auditLog.getOldValue());
        auditLogDTO.setNewValue(auditLog.getNewValue());
        auditLogDTO.setIpAddress(auditLog.getIpAddress());
        auditLogDTO.setCreatedAt(auditLog.getCreatedAt());
        auditLogDTO.setUser(auditLog.getUser() == null ? null : auditLog.getUser().getId());
        return auditLogDTO;
    }

    private AuditLog mapToEntity(final AuditLogDTO auditLogDTO, final AuditLog auditLog) {
        auditLog.setEntity(auditLogDTO.getEntity());
        auditLog.setEntityId(auditLogDTO.getEntityId());
        auditLog.setAction(auditLogDTO.getAction());
        auditLog.setOldValue(auditLogDTO.getOldValue());
        auditLog.setNewValue(auditLogDTO.getNewValue());
        auditLog.setIpAddress(auditLogDTO.getIpAddress());
        auditLog.setCreatedAt(auditLogDTO.getCreatedAt());
        final User user = auditLogDTO.getUser() == null ? null : userRepository.findById(auditLogDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        auditLog.setUser(user);
        return auditLog;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final AuditLog userAuditLog = auditLogRepository.findFirstByUserId(event.getId());
        if (userAuditLog != null) {
            referencedException.setKey("user.auditLog.user.referenced");
            referencedException.addParam(userAuditLog.getId());
            throw referencedException;
        }
    }

}
