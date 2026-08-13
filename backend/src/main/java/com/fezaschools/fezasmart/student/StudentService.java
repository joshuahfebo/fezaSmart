package com.fezaschools.fezasmart.student;

import com.fezaschools.fezasmart.events.BeforeDeleteParent;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.parent.Parent;
import com.fezaschools.fezasmart.parent.ParentRepository;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final ApplicationEventPublisher publisher;

    public StudentService(final StudentRepository studentRepository,
            final SchoolRepository schoolRepository, final UserRepository userRepository,
            final ParentRepository parentRepository, final ApplicationEventPublisher publisher) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
        this.parentRepository = parentRepository;
        this.publisher = publisher;
    }

    public List<StudentDTO> findAll() {
        final List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> mapToDTO(student, new StudentDTO()))
                .toList();
    }

    public PagedResponse<StudentDTO> findAll(Pageable pageable) {
        final Page<Student> page = studentRepository.findAll(pageable);
        List<StudentDTO> content = page.getContent().stream()
                .map(student -> mapToDTO(student, new StudentDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public StudentDTO get(final Integer id) {
        return studentRepository.findById(id)
                .map(student -> mapToDTO(student, new StudentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public StudentDTO getByUserId(final Integer userId) {
        return studentRepository.findOptionalByUserId(userId)
                .map(student -> mapToDTO(student, new StudentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudentDTO studentDTO) {
        final Student student = new Student();
        mapToEntity(studentDTO, student);
        return studentRepository.save(student).getId();
    }

    public void update(final Integer id, final StudentDTO studentDTO) {
        final Student student = studentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentDTO, student);
        studentRepository.save(student);
    }

    public void delete(final Integer id) {
        final Student student = studentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteStudent(id));
        studentRepository.delete(student);
    }

    private StudentDTO mapToDTO(final Student student, final StudentDTO studentDTO) {
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setMiddleName(student.getMiddleName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setControlNumber(student.getControlNumber());
        studentDTO.setDob(student.getDob());
        studentDTO.setGender(student.getGender());
        studentDTO.setCreatedAt(student.getCreatedAt());
        studentDTO.setUpdatedAt(student.getUpdatedAt());
        studentDTO.setDeletedAt(student.getDeletedAt());
        studentDTO.setDeletedBy(student.getDeletedBy());
        studentDTO.setRestoreToken(student.getRestoreToken());
        studentDTO.setSchool(student.getSchool() == null ? null : student.getSchool().getId());
        studentDTO.setUser(student.getUser() == null ? null : student.getUser().getId());
        studentDTO.setStudentParentParents(student.getStudentParentParents().stream()
                .map(parent -> parent.getId())
                .toList());
        return studentDTO;
    }

    private Student mapToEntity(final StudentDTO studentDTO, final Student student) {
        student.setFirstName(studentDTO.getFirstName());
        student.setMiddleName(studentDTO.getMiddleName());
        student.setLastName(studentDTO.getLastName());
        student.setControlNumber(studentDTO.getControlNumber());
        student.setDob(studentDTO.getDob());
        student.setGender(studentDTO.getGender());
        student.setCreatedAt(studentDTO.getCreatedAt());
        student.setUpdatedAt(studentDTO.getUpdatedAt());
        student.setDeletedAt(studentDTO.getDeletedAt());
        student.setDeletedBy(studentDTO.getDeletedBy());
        student.setRestoreToken(studentDTO.getRestoreToken());
        final School school = studentDTO.getSchool() == null ? null : schoolRepository.findById(studentDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        student.setSchool(school);
        final User user = studentDTO.getUser() == null ? null : userRepository.findById(studentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        student.setUser(user);
        final List<Parent> studentParentParents = parentRepository.findAllById(
                studentDTO.getStudentParentParents() == null ? List.of() : studentDTO.getStudentParentParents());
        if (studentParentParents.size() != (studentDTO.getStudentParentParents() == null ? 0 : studentDTO.getStudentParentParents().size())) {
            throw new NotFoundException("one of studentParentParents not found");
        }
        student.setStudentParentParents(new HashSet<>(studentParentParents));
        return student;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final Student schoolStudent = studentRepository.findFirstBySchoolId(event.getId());
        if (schoolStudent != null) {
            referencedException.setKey("school.student.school.referenced");
            referencedException.addParam(schoolStudent.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Student userStudent = studentRepository.findFirstByUserId(event.getId());
        if (userStudent != null) {
            referencedException.setKey("user.student.user.referenced");
            referencedException.addParam(userStudent.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteParent.class)
    public void on(final BeforeDeleteParent event) {
        // remove many-to-many relations at owning side
        studentRepository.findAllByStudentParentParentsId(event.getId()).forEach(student ->
                student.getStudentParentParents().removeIf(parent -> parent.getId().equals(event.getId())));
    }

}
