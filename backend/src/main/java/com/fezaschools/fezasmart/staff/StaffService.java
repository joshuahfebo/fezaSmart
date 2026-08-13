package com.fezaschools.fezasmart.staff;

import com.fezaschools.fezasmart.class_assignment.ClassAssignment;
import com.fezaschools.fezasmart.class_assignment.ClassAssignmentRepository;
import com.fezaschools.fezasmart.department.Department;
import com.fezaschools.fezasmart.department.DepartmentRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteDepartment;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.PagedResponse;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final ClassAssignmentRepository classAssignmentRepository;
    private final ApplicationEventPublisher publisher;

    public StaffService(final StaffRepository staffRepository,
            final SchoolRepository schoolRepository, final UserRepository userRepository,
            final DepartmentRepository departmentRepository,
            final SubjectRepository subjectRepository,
            final ClassAssignmentRepository classAssignmentRepository,
            final ApplicationEventPublisher publisher) {
        this.staffRepository = staffRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
        this.classAssignmentRepository = classAssignmentRepository;
        this.publisher = publisher;
    }

    public List<StaffDTO> findAll() {
        final List<Staff> staffs = staffRepository.findAll(Sort.by("id"));
        return staffs.stream()
                .map(staff -> mapToDTO(staff, new StaffDTO()))
                .toList();
    }

    public PagedResponse<StaffDTO> findAll(Pageable pageable) {
        final Page<Staff> page = staffRepository.findAll(pageable);
        List<StaffDTO> content = page.getContent().stream()
                .map(staff -> mapToDTO(staff, new StaffDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public StaffDTO get(final Integer id) {
        return staffRepository.findById(id)
                .map(staff -> mapToDTO(staff, new StaffDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StaffDTO staffDTO) {
        final Staff staff = new Staff();
        mapToEntity(staffDTO, staff);
        return staffRepository.save(staff).getId();
    }

    public void update(final Integer id, final StaffDTO staffDTO) {
        final Staff staff = staffRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(staffDTO, staff);
        staffRepository.save(staff);
    }

    public void delete(final Integer id) {
        final Staff staff = staffRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteStaff(id));
        staffRepository.delete(staff);
    }

    private StaffDTO mapToDTO(final Staff staff, final StaffDTO staffDTO) {
        staffDTO.setId(staff.getId());
        staffDTO.setFirstName(staff.getFirstName());
        staffDTO.setLastName(staff.getLastName());
        staffDTO.setDob(staff.getDob());
        staffDTO.setGender(staff.getGender());
        staffDTO.setStaffNumber(staff.getStaffNumber());
        staffDTO.setCreatedAt(staff.getCreatedAt());
        staffDTO.setUpdatedAt(staff.getUpdatedAt());
        staffDTO.setDeletedAt(staff.getDeletedAt());
        staffDTO.setDeletedBy(staff.getDeletedBy());
        staffDTO.setRestoreToken(staff.getRestoreToken());
        staffDTO.setSchool(staff.getSchool() == null ? null : staff.getSchool().getId());
        staffDTO.setUser(staff.getUser() == null ? null : staff.getUser().getId());
        staffDTO.setDepartment(staff.getDepartment() == null ? null : staff.getDepartment().getId());
        staffDTO.setTeacherSubjectSubjects(staff.getTeacherSubjectSubjects().stream()
                .map(subject -> subject.getId())
                .toList());
        return staffDTO;
    }

    private Staff mapToEntity(final StaffDTO staffDTO, final Staff staff) {
        staff.setFirstName(staffDTO.getFirstName());
        staff.setLastName(staffDTO.getLastName());
        staff.setDob(staffDTO.getDob());
        staff.setGender(staffDTO.getGender());
        staff.setStaffNumber(staffDTO.getStaffNumber());
        staff.setCreatedAt(staffDTO.getCreatedAt());
        staff.setUpdatedAt(staffDTO.getUpdatedAt());
        staff.setDeletedAt(staffDTO.getDeletedAt());
        staff.setDeletedBy(staffDTO.getDeletedBy());
        staff.setRestoreToken(staffDTO.getRestoreToken());
        final School school = staffDTO.getSchool() == null ? null : schoolRepository.findById(staffDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        staff.setSchool(school);
        final User user = staffDTO.getUser() == null ? null : userRepository.findById(staffDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        staff.setUser(user);
        final Department department = staffDTO.getDepartment() == null ? null : departmentRepository.findById(staffDTO.getDepartment())
                .orElseThrow(() -> new NotFoundException("department not found"));
        staff.setDepartment(department);
        final List<Subject> teacherSubjectSubjects = subjectRepository.findAllById(
                staffDTO.getTeacherSubjectSubjects() == null ? List.of() : staffDTO.getTeacherSubjectSubjects());
        if (teacherSubjectSubjects.size() != (staffDTO.getTeacherSubjectSubjects() == null ? 0 : staffDTO.getTeacherSubjectSubjects().size())) {
            throw new NotFoundException("one of teacherSubjectSubjects not found");
        }
        staff.setTeacherSubjectSubjects(new HashSet<>(teacherSubjectSubjects));
        return staff;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final Staff schoolStaff = staffRepository.findFirstBySchoolId(event.getId());
        if (schoolStaff != null) {
            referencedException.setKey("school.staff.school.referenced");
            referencedException.addParam(schoolStaff.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Staff userStaff = staffRepository.findFirstByUserId(event.getId());
        if (userStaff != null) {
            referencedException.setKey("user.staff.user.referenced");
            referencedException.addParam(userStaff.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteDepartment.class)
    public void on(final BeforeDeleteDepartment event) {
        final ReferencedException referencedException = new ReferencedException();
        final Staff departmentStaff = staffRepository.findFirstByDepartmentId(event.getId());
        if (departmentStaff != null) {
            referencedException.setKey("department.staff.department.referenced");
            referencedException.addParam(departmentStaff.getId());
            throw referencedException;
        }
    }

}
