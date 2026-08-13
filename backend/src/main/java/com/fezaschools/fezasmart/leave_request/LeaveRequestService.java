package com.fezaschools.fezasmart.leave_request;

import com.fezaschools.fezasmart.events.BeforeDeletePermission;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.permission.Permission;
import com.fezaschools.fezasmart.permission.PermissionRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PermissionRepository permissionRepository;

    public LeaveRequestService(final LeaveRequestRepository leaveRequestRepository,
            final StudentRepository studentRepository, final UserRepository userRepository,
            final StaffRepository staffRepository,
            final PermissionRepository permissionRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<LeaveRequestDTO> findAll() {
        final List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll(Sort.by("id"));
        return leaveRequests.stream()
                .map(leaveRequest -> mapToDTO(leaveRequest, new LeaveRequestDTO()))
                .toList();
    }

    public LeaveRequestDTO get(final Long id) {
        return leaveRequestRepository.findById(id)
                .map(leaveRequest -> mapToDTO(leaveRequest, new LeaveRequestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LeaveRequestDTO leaveRequestDTO) {
        final LeaveRequest leaveRequest = new LeaveRequest();
        mapToEntity(leaveRequestDTO, leaveRequest);
        return leaveRequestRepository.save(leaveRequest).getId();
    }

    public void update(final Long id, final LeaveRequestDTO leaveRequestDTO) {
        final LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(leaveRequestDTO, leaveRequest);
        leaveRequestRepository.save(leaveRequest);
    }

    public void delete(final Long id) {
        final LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        leaveRequestRepository.delete(leaveRequest);
    }

    private LeaveRequestDTO mapToDTO(final LeaveRequest leaveRequest,
            final LeaveRequestDTO leaveRequestDTO) {
        leaveRequestDTO.setId(leaveRequest.getId());
        leaveRequestDTO.setReason(leaveRequest.getReason());
        leaveRequestDTO.setStatus(leaveRequest.getStatus());
        leaveRequestDTO.setRequestedAt(leaveRequest.getRequestedAt());
        leaveRequestDTO.setProcessedAt(leaveRequest.getProcessedAt());
        leaveRequestDTO.setStudent(leaveRequest.getStudent() == null ? null : leaveRequest.getStudent().getId());
        leaveRequestDTO.setRequesterUser(leaveRequest.getRequesterUser() == null ? null : leaveRequest.getRequesterUser().getId());
        leaveRequestDTO.setProcessedByStaff(leaveRequest.getProcessedByStaff() == null ? null : leaveRequest.getProcessedByStaff().getId());
        leaveRequestDTO.setPermission(leaveRequest.getPermission() == null ? null : leaveRequest.getPermission().getId());
        return leaveRequestDTO;
    }

    private LeaveRequest mapToEntity(final LeaveRequestDTO leaveRequestDTO,
            final LeaveRequest leaveRequest) {
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setStatus(leaveRequestDTO.getStatus());
        leaveRequest.setRequestedAt(leaveRequestDTO.getRequestedAt());
        leaveRequest.setProcessedAt(leaveRequestDTO.getProcessedAt());
        final Student student = leaveRequestDTO.getStudent() == null ? null : studentRepository.findById(leaveRequestDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        leaveRequest.setStudent(student);
        final User requesterUser = leaveRequestDTO.getRequesterUser() == null ? null : userRepository.findById(leaveRequestDTO.getRequesterUser())
                .orElseThrow(() -> new NotFoundException("requesterUser not found"));
        leaveRequest.setRequesterUser(requesterUser);
        final Staff processedByStaff = leaveRequestDTO.getProcessedByStaff() == null ? null : staffRepository.findById(leaveRequestDTO.getProcessedByStaff())
                .orElseThrow(() -> new NotFoundException("processedByStaff not found"));
        leaveRequest.setProcessedByStaff(processedByStaff);
        final Permission permission = leaveRequestDTO.getPermission() == null ? null : permissionRepository.findById(leaveRequestDTO.getPermission())
                .orElseThrow(() -> new NotFoundException("permission not found"));
        leaveRequest.setPermission(permission);
        return leaveRequest;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final LeaveRequest studentLeaveRequest = leaveRequestRepository.findFirstByStudentId(event.getId());
        if (studentLeaveRequest != null) {
            referencedException.setKey("student.leaveRequest.student.referenced");
            referencedException.addParam(studentLeaveRequest.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final LeaveRequest requesterUserLeaveRequest = leaveRequestRepository.findFirstByRequesterUserId(event.getId());
        if (requesterUserLeaveRequest != null) {
            referencedException.setKey("user.leaveRequest.requesterUser.referenced");
            referencedException.addParam(requesterUserLeaveRequest.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final LeaveRequest processedByStaffLeaveRequest = leaveRequestRepository.findFirstByProcessedByStaffId(event.getId());
        if (processedByStaffLeaveRequest != null) {
            referencedException.setKey("staff.leaveRequest.processedByStaff.referenced");
            referencedException.addParam(processedByStaffLeaveRequest.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeletePermission.class)
    public void on(final BeforeDeletePermission event) {
        final ReferencedException referencedException = new ReferencedException();
        final LeaveRequest permissionLeaveRequest = leaveRequestRepository.findFirstByPermissionId(event.getId());
        if (permissionLeaveRequest != null) {
            referencedException.setKey("permission.leaveRequest.permission.referenced");
            referencedException.addParam(permissionLeaveRequest.getId());
            throw referencedException;
        }
    }

}
