package com.fezaschools.fezasmart.staff_role;

import com.fezaschools.fezasmart.events.BeforeDeleteRole;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.role.Role;
import com.fezaschools.fezasmart.role.RoleRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StaffRoleService {

    private final StaffRoleRepository staffRoleRepository;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;

    public StaffRoleService(final StaffRoleRepository staffRoleRepository,
            final StaffRepository staffRepository, final RoleRepository roleRepository) {
        this.staffRoleRepository = staffRoleRepository;
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
    }

    public List<StaffRoleDTO> findAll() {
        final List<StaffRole> staffRoles = staffRoleRepository.findAll(Sort.by("id"));
        return staffRoles.stream()
                .map(staffRole -> mapToDTO(staffRole, new StaffRoleDTO()))
                .toList();
    }

    public StaffRoleDTO get(final Long id) {
        return staffRoleRepository.findById(id)
                .map(staffRole -> mapToDTO(staffRole, new StaffRoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final StaffRoleDTO staffRoleDTO) {
        final StaffRole staffRole = new StaffRole();
        mapToEntity(staffRoleDTO, staffRole);
        return staffRoleRepository.save(staffRole).getId();
    }

    public void update(final Long id, final StaffRoleDTO staffRoleDTO) {
        final StaffRole staffRole = staffRoleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(staffRoleDTO, staffRole);
        staffRoleRepository.save(staffRole);
    }

    public void delete(final Long id) {
        final StaffRole staffRole = staffRoleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        staffRoleRepository.delete(staffRole);
    }

    private StaffRoleDTO mapToDTO(final StaffRole staffRole, final StaffRoleDTO staffRoleDTO) {
        staffRoleDTO.setId(staffRole.getId());
        staffRoleDTO.setAssignedAt(staffRole.getAssignedAt());
        staffRoleDTO.setStaff(staffRole.getStaff() == null ? null : staffRole.getStaff().getId());
        staffRoleDTO.setRole(staffRole.getRole() == null ? null : staffRole.getRole().getId());
        return staffRoleDTO;
    }

    private StaffRole mapToEntity(final StaffRoleDTO staffRoleDTO, final StaffRole staffRole) {
        staffRole.setAssignedAt(staffRoleDTO.getAssignedAt());
        final Staff staff = staffRoleDTO.getStaff() == null ? null : staffRepository.findById(staffRoleDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        staffRole.setStaff(staff);
        final Role role = staffRoleDTO.getRole() == null ? null : roleRepository.findById(staffRoleDTO.getRole())
                .orElseThrow(() -> new NotFoundException("role not found"));
        staffRole.setRole(role);
        return staffRole;
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final StaffRole staffStaffRole = staffRoleRepository.findFirstByStaffId(event.getId());
        if (staffStaffRole != null) {
            referencedException.setKey("staff.staffRole.staff.referenced");
            referencedException.addParam(staffStaffRole.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteRole.class)
    public void on(final BeforeDeleteRole event) {
        final ReferencedException referencedException = new ReferencedException();
        final StaffRole roleStaffRole = staffRoleRepository.findFirstByRoleId(event.getId());
        if (roleStaffRole != null) {
            referencedException.setKey("role.staffRole.role.referenced");
            referencedException.addParam(roleStaffRole.getId());
            throw referencedException;
        }
    }

}
