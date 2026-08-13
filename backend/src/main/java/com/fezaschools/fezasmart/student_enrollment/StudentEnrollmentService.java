package com.fezaschools.fezasmart.student_enrollment;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.classs.ClasssRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.events.BeforeDeleteClasss;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudentEnrollmentService {

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClasssRepository classsRepository;
    private final AcademicYearRepository academicYearRepository;

    public StudentEnrollmentService(final StudentEnrollmentRepository studentEnrollmentRepository,
            final StudentRepository studentRepository, final ClasssRepository classsRepository,
            final AcademicYearRepository academicYearRepository) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.studentRepository = studentRepository;
        this.classsRepository = classsRepository;
        this.academicYearRepository = academicYearRepository;
    }

    public List<StudentEnrollmentDTO> findAll() {
        final List<StudentEnrollment> studentEnrollments = studentEnrollmentRepository.findAll(Sort.by("id"));
        return studentEnrollments.stream()
                .map(studentEnrollment -> mapToDTO(studentEnrollment, new StudentEnrollmentDTO()))
                .toList();
    }

    public StudentEnrollmentDTO get(final Integer id) {
        return studentEnrollmentRepository.findById(id)
                .map(studentEnrollment -> mapToDTO(studentEnrollment, new StudentEnrollmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudentEnrollmentDTO studentEnrollmentDTO) {
        final StudentEnrollment studentEnrollment = new StudentEnrollment();
        mapToEntity(studentEnrollmentDTO, studentEnrollment);
        return studentEnrollmentRepository.save(studentEnrollment).getId();
    }

    public void update(final Integer id, final StudentEnrollmentDTO studentEnrollmentDTO) {
        final StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentEnrollmentDTO, studentEnrollment);
        studentEnrollmentRepository.save(studentEnrollment);
    }

    public void delete(final Integer id) {
        final StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        studentEnrollmentRepository.delete(studentEnrollment);
    }

    private StudentEnrollmentDTO mapToDTO(final StudentEnrollment studentEnrollment,
            final StudentEnrollmentDTO studentEnrollmentDTO) {
        studentEnrollmentDTO.setId(studentEnrollment.getId());
        studentEnrollmentDTO.setEnrollmentDate(studentEnrollment.getEnrollmentDate());
        studentEnrollmentDTO.setIsCurrent(studentEnrollment.getIsCurrent());
        studentEnrollmentDTO.setStudent(studentEnrollment.getStudent() == null ? null : studentEnrollment.getStudent().getId());
        studentEnrollmentDTO.setClasss(studentEnrollment.getClasss() == null ? null : studentEnrollment.getClasss().getId());
        studentEnrollmentDTO.setAcademicYear(studentEnrollment.getAcademicYear() == null ? null : studentEnrollment.getAcademicYear().getId());
        return studentEnrollmentDTO;
    }

    private StudentEnrollment mapToEntity(final StudentEnrollmentDTO studentEnrollmentDTO,
            final StudentEnrollment studentEnrollment) {
        studentEnrollment.setEnrollmentDate(studentEnrollmentDTO.getEnrollmentDate());
        studentEnrollment.setIsCurrent(studentEnrollmentDTO.getIsCurrent());
        final Student student = studentEnrollmentDTO.getStudent() == null ? null : studentRepository.findById(studentEnrollmentDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        studentEnrollment.setStudent(student);
        final Classs classs = studentEnrollmentDTO.getClasss() == null ? null : classsRepository.findById(studentEnrollmentDTO.getClasss())
                .orElseThrow(() -> new NotFoundException("classs not found"));
        studentEnrollment.setClasss(classs);
        final AcademicYear academicYear = studentEnrollmentDTO.getAcademicYear() == null ? null : academicYearRepository.findById(studentEnrollmentDTO.getAcademicYear())
                .orElseThrow(() -> new NotFoundException("academicYear not found"));
        studentEnrollment.setAcademicYear(academicYear);
        return studentEnrollment;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentEnrollment studentStudentEnrollment = studentEnrollmentRepository.findFirstByStudentId(event.getId());
        if (studentStudentEnrollment != null) {
            referencedException.setKey("student.studentEnrollment.student.referenced");
            referencedException.addParam(studentStudentEnrollment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteClasss.class)
    public void on(final BeforeDeleteClasss event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentEnrollment classsStudentEnrollment = studentEnrollmentRepository.findFirstByClasssId(event.getId());
        if (classsStudentEnrollment != null) {
            referencedException.setKey("classs.studentEnrollment.classs.referenced");
            referencedException.addParam(classsStudentEnrollment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteAcademicYear.class)
    public void on(final BeforeDeleteAcademicYear event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentEnrollment academicYearStudentEnrollment = studentEnrollmentRepository.findFirstByAcademicYearId(event.getId());
        if (academicYearStudentEnrollment != null) {
            referencedException.setKey("academicYear.studentEnrollment.academicYear.referenced");
            referencedException.addParam(academicYearStudentEnrollment.getId());
            throw referencedException;
        }
    }

}
