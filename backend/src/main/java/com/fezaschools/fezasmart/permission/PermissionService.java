package com.fezaschools.fezasmart.permission;

import com.fezaschools.fezasmart.events.BeforeDeletePermission;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final ApplicationEventPublisher publisher;

    public PermissionService(final PermissionRepository permissionRepository,
            final StudentRepository studentRepository, final StaffRepository staffRepository,
            final ApplicationEventPublisher publisher) {
        this.permissionRepository = permissionRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.publisher = publisher;
    }

    public List<PermissionDTO> findAll() {
        final List<Permission> permissions = permissionRepository.findAll(Sort.by("id"));
        return permissions.stream()
                .map(permission -> mapToDTO(permission, new PermissionDTO()))
                .toList();
    }

    public PermissionDTO get(final Integer id) {
        return permissionRepository.findById(id)
                .map(permission -> mapToDTO(permission, new PermissionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PermissionDTO permissionDTO) {
        final Permission permission = new Permission();
        mapToEntity(permissionDTO, permission);
        return permissionRepository.save(permission).getId();
    }

    public void update(final Integer id, final PermissionDTO permissionDTO) {
        final Permission permission = permissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(permissionDTO, permission);
        permissionRepository.save(permission);
    }

    public void delete(final Integer id) {
        final Permission permission = permissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeletePermission(id));
        permissionRepository.delete(permission);
    }

    private PermissionDTO mapToDTO(final Permission permission, final PermissionDTO permissionDTO) {
        permissionDTO.setId(permission.getId());
        permissionDTO.setLeaveRequestId(permission.getLeaveRequestId());
        permissionDTO.setTimeOutLimit(permission.getTimeOutLimit());
        permissionDTO.setTimeInLimit(permission.getTimeInLimit());
        permissionDTO.setActualTimeOut(permission.getActualTimeOut());
        permissionDTO.setActualTimeIn(permission.getActualTimeIn());
        permissionDTO.setReturned(permission.getReturned());
        permissionDTO.setStudent(permission.getStudent() == null ? null : permission.getStudent().getId());
        permissionDTO.setIssuedByStaff(permission.getIssuedByStaff() == null ? null : permission.getIssuedByStaff().getId());
        permissionDTO.setGuardOutStaff(permission.getGuardOutStaff() == null ? null : permission.getGuardOutStaff().getId());
        permissionDTO.setGuardInStaff(permission.getGuardInStaff() == null ? null : permission.getGuardInStaff().getId());
        return permissionDTO;
    }

    private Permission mapToEntity(final PermissionDTO permissionDTO, final Permission permission) {
        permission.setLeaveRequestId(permissionDTO.getLeaveRequestId());
        permission.setTimeOutLimit(permissionDTO.getTimeOutLimit());
        permission.setTimeInLimit(permissionDTO.getTimeInLimit());
        permission.setActualTimeOut(permissionDTO.getActualTimeOut());
        permission.setActualTimeIn(permissionDTO.getActualTimeIn());
        permission.setReturned(permissionDTO.getReturned());
        final Student student = permissionDTO.getStudent() == null ? null : studentRepository.findById(permissionDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        permission.setStudent(student);
        final Staff issuedByStaff = permissionDTO.getIssuedByStaff() == null ? null : staffRepository.findById(permissionDTO.getIssuedByStaff())
                .orElseThrow(() -> new NotFoundException("issuedByStaff not found"));
        permission.setIssuedByStaff(issuedByStaff);
        final Staff guardOutStaff = permissionDTO.getGuardOutStaff() == null ? null : staffRepository.findById(permissionDTO.getGuardOutStaff())
                .orElseThrow(() -> new NotFoundException("guardOutStaff not found"));
        permission.setGuardOutStaff(guardOutStaff);
        final Staff guardInStaff = permissionDTO.getGuardInStaff() == null ? null : staffRepository.findById(permissionDTO.getGuardInStaff())
                .orElseThrow(() -> new NotFoundException("guardInStaff not found"));
        permission.setGuardInStaff(guardInStaff);
        return permission;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final Permission studentPermission = permissionRepository.findFirstByStudentId(event.getId());
        if (studentPermission != null) {
            referencedException.setKey("student.permission.student.referenced");
            referencedException.addParam(studentPermission.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Permission issuedByStaffPermission = permissionRepository.findFirstByIssuedByStaffId(event.getId());
        if (issuedByStaffPermission != null) {
            referencedException.setKey("staff.permission.issuedByStaff.referenced");
            referencedException.addParam(issuedByStaffPermission.getId());
            throw referencedException;
        }
        final Permission guardOutStaffPermission = permissionRepository.findFirstByGuardOutStaffId(event.getId());
        if (guardOutStaffPermission != null) {
            referencedException.setKey("staff.permission.guardOutStaff.referenced");
            referencedException.addParam(guardOutStaffPermission.getId());
            throw referencedException;
        }
        final Permission guardInStaffPermission = permissionRepository.findFirstByGuardInStaffId(event.getId());
        if (guardInStaffPermission != null) {
            referencedException.setKey("staff.permission.guardInStaff.referenced");
            referencedException.addParam(guardInStaffPermission.getId());
            throw referencedException;
        }
    }

}
