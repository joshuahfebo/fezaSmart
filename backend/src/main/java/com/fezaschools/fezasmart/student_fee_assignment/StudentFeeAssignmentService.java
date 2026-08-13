package com.fezaschools.fezasmart.student_fee_assignment;

import com.fezaschools.fezasmart.events.BeforeDeleteFeeStructure;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.fee_structure.FeeStructure;
import com.fezaschools.fezasmart.fee_structure.FeeStructureRepository;
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
public class StudentFeeAssignmentService {

    private final StudentFeeAssignmentRepository studentFeeAssignmentRepository;
    private final StudentRepository studentRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final StaffRepository staffRepository;

    public StudentFeeAssignmentService(
            final StudentFeeAssignmentRepository studentFeeAssignmentRepository,
            final StudentRepository studentRepository,
            final FeeStructureRepository feeStructureRepository,
            final StaffRepository staffRepository) {
        this.studentFeeAssignmentRepository = studentFeeAssignmentRepository;
        this.studentRepository = studentRepository;
        this.feeStructureRepository = feeStructureRepository;
        this.staffRepository = staffRepository;
    }

    public List<StudentFeeAssignmentDTO> findAll() {
        final List<StudentFeeAssignment> studentFeeAssignments = studentFeeAssignmentRepository.findAll(Sort.by("id"));
        return studentFeeAssignments.stream()
                .map(studentFeeAssignment -> mapToDTO(studentFeeAssignment, new StudentFeeAssignmentDTO()))
                .toList();
    }

    public StudentFeeAssignmentDTO get(final Integer id) {
        return studentFeeAssignmentRepository.findById(id)
                .map(studentFeeAssignment -> mapToDTO(studentFeeAssignment, new StudentFeeAssignmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        final StudentFeeAssignment studentFeeAssignment = new StudentFeeAssignment();
        mapToEntity(studentFeeAssignmentDTO, studentFeeAssignment);
        return studentFeeAssignmentRepository.save(studentFeeAssignment).getId();
    }

    public void update(final Integer id, final StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        final StudentFeeAssignment studentFeeAssignment = studentFeeAssignmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentFeeAssignmentDTO, studentFeeAssignment);
        studentFeeAssignmentRepository.save(studentFeeAssignment);
    }

    public void delete(final Integer id) {
        final StudentFeeAssignment studentFeeAssignment = studentFeeAssignmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        studentFeeAssignmentRepository.delete(studentFeeAssignment);
    }

    private StudentFeeAssignmentDTO mapToDTO(final StudentFeeAssignment studentFeeAssignment,
            final StudentFeeAssignmentDTO studentFeeAssignmentDTO) {
        studentFeeAssignmentDTO.setId(studentFeeAssignment.getId());
        studentFeeAssignmentDTO.setAssignedAt(studentFeeAssignment.getAssignedAt());
        studentFeeAssignmentDTO.setStudent(studentFeeAssignment.getStudent() == null ? null : studentFeeAssignment.getStudent().getId());
        studentFeeAssignmentDTO.setFeeStructure(studentFeeAssignment.getFeeStructure() == null ? null : studentFeeAssignment.getFeeStructure().getId());
        studentFeeAssignmentDTO.setAssignedBy(studentFeeAssignment.getAssignedBy() == null ? null : studentFeeAssignment.getAssignedBy().getId());
        return studentFeeAssignmentDTO;
    }

    private StudentFeeAssignment mapToEntity(final StudentFeeAssignmentDTO studentFeeAssignmentDTO,
            final StudentFeeAssignment studentFeeAssignment) {
        studentFeeAssignment.setAssignedAt(studentFeeAssignmentDTO.getAssignedAt());
        final Student student = studentFeeAssignmentDTO.getStudent() == null ? null : studentRepository.findById(studentFeeAssignmentDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        studentFeeAssignment.setStudent(student);
        final FeeStructure feeStructure = studentFeeAssignmentDTO.getFeeStructure() == null ? null : feeStructureRepository.findById(studentFeeAssignmentDTO.getFeeStructure())
                .orElseThrow(() -> new NotFoundException("feeStructure not found"));
        studentFeeAssignment.setFeeStructure(feeStructure);
        final Staff assignedBy = studentFeeAssignmentDTO.getAssignedBy() == null ? null : staffRepository.findById(studentFeeAssignmentDTO.getAssignedBy())
                .orElseThrow(() -> new NotFoundException("assignedBy not found"));
        studentFeeAssignment.setAssignedBy(assignedBy);
        return studentFeeAssignment;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentFeeAssignment studentStudentFeeAssignment = studentFeeAssignmentRepository.findFirstByStudentId(event.getId());
        if (studentStudentFeeAssignment != null) {
            referencedException.setKey("student.studentFeeAssignment.student.referenced");
            referencedException.addParam(studentStudentFeeAssignment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteFeeStructure.class)
    public void on(final BeforeDeleteFeeStructure event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentFeeAssignment feeStructureStudentFeeAssignment = studentFeeAssignmentRepository.findFirstByFeeStructureId(event.getId());
        if (feeStructureStudentFeeAssignment != null) {
            referencedException.setKey("feeStructure.studentFeeAssignment.feeStructure.referenced");
            referencedException.addParam(feeStructureStudentFeeAssignment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentFeeAssignment assignedByStudentFeeAssignment = studentFeeAssignmentRepository.findFirstByAssignedById(event.getId());
        if (assignedByStudentFeeAssignment != null) {
            referencedException.setKey("staff.studentFeeAssignment.assignedBy.referenced");
            referencedException.addParam(assignedByStudentFeeAssignment.getId());
            throw referencedException;
        }
    }

}
