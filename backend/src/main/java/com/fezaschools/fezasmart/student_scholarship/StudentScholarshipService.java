package com.fezaschools.fezasmart.student_scholarship;

import com.fezaschools.fezasmart.events.BeforeDeleteScholarship;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.scholarship.Scholarship;
import com.fezaschools.fezasmart.scholarship.ScholarshipRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudentScholarshipService {

    private final StudentScholarshipRepository studentScholarshipRepository;
    private final StudentRepository studentRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final StaffRepository staffRepository;

    public StudentScholarshipService(
            final StudentScholarshipRepository studentScholarshipRepository,
            final StudentRepository studentRepository,
            final ScholarshipRepository scholarshipRepository,
            final StaffRepository staffRepository) {
        this.studentScholarshipRepository = studentScholarshipRepository;
        this.studentRepository = studentRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.staffRepository = staffRepository;
    }

    public List<StudentScholarshipDTO> findAll() {
        final List<StudentScholarship> studentScholarships = studentScholarshipRepository.findAll(Sort.by("id"));
        return studentScholarships.stream()
                .map(studentScholarship -> mapToDTO(studentScholarship, new StudentScholarshipDTO()))
                .toList();
    }

    public StudentScholarshipDTO get(final Integer id) {
        return studentScholarshipRepository.findById(id)
                .map(studentScholarship -> mapToDTO(studentScholarship, new StudentScholarshipDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudentScholarshipDTO studentScholarshipDTO) {
        final StudentScholarship studentScholarship = new StudentScholarship();
        mapToEntity(studentScholarshipDTO, studentScholarship);
        return studentScholarshipRepository.save(studentScholarship).getId();
    }

    public void update(final Integer id, final StudentScholarshipDTO studentScholarshipDTO) {
        final StudentScholarship studentScholarship = studentScholarshipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentScholarshipDTO, studentScholarship);
        studentScholarshipRepository.save(studentScholarship);
    }

    public void delete(final Integer id) {
        final StudentScholarship studentScholarship = studentScholarshipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        studentScholarshipRepository.delete(studentScholarship);
    }

    private StudentScholarshipDTO mapToDTO(final StudentScholarship studentScholarship,
            final StudentScholarshipDTO studentScholarshipDTO) {
        studentScholarshipDTO.setId(studentScholarship.getId());
        studentScholarshipDTO.setAwardedDate(studentScholarship.getAwardedDate());
        studentScholarshipDTO.setValidUntil(studentScholarship.getValidUntil());
        studentScholarshipDTO.setStudent(studentScholarship.getStudent() == null ? null : studentScholarship.getStudent().getId());
        studentScholarshipDTO.setScholarship(studentScholarship.getScholarship() == null ? null : studentScholarship.getScholarship().getId());
        studentScholarshipDTO.setAwardedBy(studentScholarship.getAwardedBy() == null ? null : studentScholarship.getAwardedBy().getId());
        return studentScholarshipDTO;
    }

    private StudentScholarship mapToEntity(final StudentScholarshipDTO studentScholarshipDTO,
            final StudentScholarship studentScholarship) {
        studentScholarship.setAwardedDate(studentScholarshipDTO.getAwardedDate());
        studentScholarship.setValidUntil(studentScholarshipDTO.getValidUntil());
        final Student student = studentScholarshipDTO.getStudent() == null ? null : studentRepository.findById(studentScholarshipDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        studentScholarship.setStudent(student);
        final Scholarship scholarship = studentScholarshipDTO.getScholarship() == null ? null : scholarshipRepository.findById(studentScholarshipDTO.getScholarship())
                .orElseThrow(() -> new NotFoundException("scholarship not found"));
        studentScholarship.setScholarship(scholarship);
        final Staff awardedBy = studentScholarshipDTO.getAwardedBy() == null ? null : staffRepository.findById(studentScholarshipDTO.getAwardedBy())
                .orElseThrow(() -> new NotFoundException("awardedBy not found"));
        studentScholarship.setAwardedBy(awardedBy);
        return studentScholarship;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentScholarship studentStudentScholarship = studentScholarshipRepository.findFirstByStudentId(event.getId());
        if (studentStudentScholarship != null) {
            referencedException.setKey("student.studentScholarship.student.referenced");
            referencedException.addParam(studentStudentScholarship.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteScholarship.class)
    public void on(final BeforeDeleteScholarship event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentScholarship scholarshipStudentScholarship = studentScholarshipRepository.findFirstByScholarshipId(event.getId());
        if (scholarshipStudentScholarship != null) {
            referencedException.setKey("scholarship.studentScholarship.scholarship.referenced");
            referencedException.addParam(scholarshipStudentScholarship.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentScholarship awardedByStudentScholarship = studentScholarshipRepository.findFirstByAwardedById(event.getId());
        if (awardedByStudentScholarship != null) {
            referencedException.setKey("staff.studentScholarship.awardedBy.referenced");
            referencedException.addParam(awardedByStudentScholarship.getId());
            throw referencedException;
        }
    }

}
