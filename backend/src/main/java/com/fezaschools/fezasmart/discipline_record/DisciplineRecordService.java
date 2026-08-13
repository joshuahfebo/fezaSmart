package com.fezaschools.fezasmart.discipline_record;

import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteViolation;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import com.fezaschools.fezasmart.violation.Violation;
import com.fezaschools.fezasmart.violation.ViolationRepository;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DisciplineRecordService {

    private final DisciplineRecordRepository disciplineRecordRepository;
    private final StudentRepository studentRepository;
    private final ViolationRepository violationRepository;
    private final StaffRepository staffRepository;

    public DisciplineRecordService(final DisciplineRecordRepository disciplineRecordRepository,
            final StudentRepository studentRepository,
            final ViolationRepository violationRepository, final StaffRepository staffRepository) {
        this.disciplineRecordRepository = disciplineRecordRepository;
        this.studentRepository = studentRepository;
        this.violationRepository = violationRepository;
        this.staffRepository = staffRepository;
    }

    public List<DisciplineRecordDTO> findAll() {
        final List<DisciplineRecord> disciplineRecords = disciplineRecordRepository.findAll(Sort.by("id"));
        return disciplineRecords.stream()
                .map(disciplineRecord -> mapToDTO(disciplineRecord, new DisciplineRecordDTO()))
                .toList();
    }

    public DisciplineRecordDTO get(final Integer id) {
        return disciplineRecordRepository.findById(id)
                .map(disciplineRecord -> mapToDTO(disciplineRecord, new DisciplineRecordDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DisciplineRecordDTO disciplineRecordDTO) {
        final DisciplineRecord disciplineRecord = new DisciplineRecord();
        mapToEntity(disciplineRecordDTO, disciplineRecord);
        return disciplineRecordRepository.save(disciplineRecord).getId();
    }

    public void update(final Integer id, final DisciplineRecordDTO disciplineRecordDTO) {
        final DisciplineRecord disciplineRecord = disciplineRecordRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(disciplineRecordDTO, disciplineRecord);
        disciplineRecordRepository.save(disciplineRecord);
    }

    public void delete(final Integer id) {
        final DisciplineRecord disciplineRecord = disciplineRecordRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        disciplineRecordRepository.delete(disciplineRecord);
    }

    private DisciplineRecordDTO mapToDTO(final DisciplineRecord disciplineRecord,
            final DisciplineRecordDTO disciplineRecordDTO) {
        disciplineRecordDTO.setId(disciplineRecord.getId());
        disciplineRecordDTO.setPointsDeducted(disciplineRecord.getPointsDeducted());
        disciplineRecordDTO.setComment(disciplineRecord.getComment());
        disciplineRecordDTO.setCreatedAt(disciplineRecord.getCreatedAt());
        disciplineRecordDTO.setStudent(disciplineRecord.getStudent() == null ? null : disciplineRecord.getStudent().getId());
        disciplineRecordDTO.setViolation(disciplineRecord.getViolation() == null ? null : disciplineRecord.getViolation().getId());
        disciplineRecordDTO.setStaff(disciplineRecord.getStaff() == null ? null : disciplineRecord.getStaff().getId());
        return disciplineRecordDTO;
    }

    private DisciplineRecord mapToEntity(final DisciplineRecordDTO disciplineRecordDTO,
            final DisciplineRecord disciplineRecord) {
        disciplineRecord.setPointsDeducted(disciplineRecordDTO.getPointsDeducted());
        disciplineRecord.setComment(disciplineRecordDTO.getComment());
        disciplineRecord.setCreatedAt(disciplineRecordDTO.getCreatedAt());
        final Student student = disciplineRecordDTO.getStudent() == null ? null : studentRepository.findById(disciplineRecordDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        disciplineRecord.setStudent(student);
        final Violation violation = disciplineRecordDTO.getViolation() == null ? null : violationRepository.findById(disciplineRecordDTO.getViolation())
                .orElseThrow(() -> new NotFoundException("violation not found"));
        disciplineRecord.setViolation(violation);
        final Staff staff = disciplineRecordDTO.getStaff() == null ? null : staffRepository.findById(disciplineRecordDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        disciplineRecord.setStaff(staff);
        return disciplineRecord;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final DisciplineRecord studentDisciplineRecord = disciplineRecordRepository.findFirstByStudentId(event.getId());
        if (studentDisciplineRecord != null) {
            referencedException.setKey("student.disciplineRecord.student.referenced");
            referencedException.addParam(studentDisciplineRecord.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteViolation.class)
    public void on(final BeforeDeleteViolation event) {
        final ReferencedException referencedException = new ReferencedException();
        final DisciplineRecord violationDisciplineRecord = disciplineRecordRepository.findFirstByViolationId(event.getId());
        if (violationDisciplineRecord != null) {
            referencedException.setKey("violation.disciplineRecord.violation.referenced");
            referencedException.addParam(violationDisciplineRecord.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final DisciplineRecord staffDisciplineRecord = disciplineRecordRepository.findFirstByStaffId(event.getId());
        if (staffDisciplineRecord != null) {
            referencedException.setKey("staff.disciplineRecord.staff.referenced");
            referencedException.addParam(staffDisciplineRecord.getId());
            throw referencedException;
        }
    }

}
