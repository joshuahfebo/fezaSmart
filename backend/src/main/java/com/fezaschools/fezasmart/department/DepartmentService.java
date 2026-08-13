package com.fezaschools.fezasmart.department;

import com.fezaschools.fezasmart.events.BeforeDeleteDepartment;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final SchoolRepository schoolRepository;
    private final StaffRepository staffRepository;
    private final ApplicationEventPublisher publisher;

    public DepartmentService(final DepartmentRepository departmentRepository,
            final SchoolRepository schoolRepository, final StaffRepository staffRepository,
            final ApplicationEventPublisher publisher) {
        this.departmentRepository = departmentRepository;
        this.schoolRepository = schoolRepository;
        this.staffRepository = staffRepository;
        this.publisher = publisher;
    }

    public List<DepartmentDTO> findAll() {
        final List<Department> departments = departmentRepository.findAll(Sort.by("id"));
        return departments.stream()
                .map(department -> mapToDTO(department, new DepartmentDTO()))
                .toList();
    }

    public DepartmentDTO get(final Integer id) {
        return departmentRepository.findById(id)
                .map(department -> mapToDTO(department, new DepartmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DepartmentDTO departmentDTO) {
        final Department department = new Department();
        mapToEntity(departmentDTO, department);
        return departmentRepository.save(department).getId();
    }

    public void update(final Integer id, final DepartmentDTO departmentDTO) {
        final Department department = departmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(departmentDTO, department);
        departmentRepository.save(department);
    }

    public void delete(final Integer id) {
        final Department department = departmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteDepartment(id));
        departmentRepository.delete(department);
    }

    private DepartmentDTO mapToDTO(final Department department, final DepartmentDTO departmentDTO) {
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setDescription(department.getDescription());
        departmentDTO.setCreatedAt(department.getCreatedAt());
        departmentDTO.setSchool(department.getSchool() == null ? null : department.getSchool().getId());
        departmentDTO.setHeadStaff(department.getHeadStaff() == null ? null : department.getHeadStaff().getId());
        return departmentDTO;
    }

    private Department mapToEntity(final DepartmentDTO departmentDTO, final Department department) {
        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());
        department.setCreatedAt(departmentDTO.getCreatedAt());
        final School school = departmentDTO.getSchool() == null ? null : schoolRepository.findById(departmentDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        department.setSchool(school);
        final Staff headStaff = departmentDTO.getHeadStaff() == null ? null : staffRepository.findById(departmentDTO.getHeadStaff())
                .orElseThrow(() -> new NotFoundException("headStaff not found"));
        department.setHeadStaff(headStaff);
        return department;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final Department schoolDepartment = departmentRepository.findFirstBySchoolId(event.getId());
        if (schoolDepartment != null) {
            referencedException.setKey("school.department.school.referenced");
            referencedException.addParam(schoolDepartment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Department headStaffDepartment = departmentRepository.findFirstByHeadStaffId(event.getId());
        if (headStaffDepartment != null) {
            referencedException.setKey("staff.department.headStaff.referenced");
            referencedException.addParam(headStaffDepartment.getId());
            throw referencedException;
        }
    }

}
