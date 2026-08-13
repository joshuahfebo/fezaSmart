package com.fezaschools.fezasmart.attendance_record;

import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.classs.ClasssRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteClasss;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.PagedResponse;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final StudentRepository studentRepository;
    private final ClasssRepository classsRepository;
    private final StaffRepository staffRepository;
    private final ApplicationEventPublisher publisher;

    public AttendanceRecordService(final AttendanceRecordRepository attendanceRecordRepository,
            final StudentRepository studentRepository,
            final ClasssRepository classsRepository,
            final StaffRepository staffRepository,
            final ApplicationEventPublisher publisher) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.studentRepository = studentRepository;
        this.classsRepository = classsRepository;
        this.staffRepository = staffRepository;
        this.publisher = publisher;
    }

    public List<AttendanceRecordDTO> findAll() {
        final List<AttendanceRecord> attendanceRecords = attendanceRecordRepository.findAll(Sort.by("id"));
        return attendanceRecords.stream()
                .map(attendanceRecord -> mapToDTO(attendanceRecord, new AttendanceRecordDTO()))
                .toList();
    }

    public PagedResponse<AttendanceRecordDTO> findAll(Pageable pageable) {
        final Page<AttendanceRecord> page = attendanceRecordRepository.findAll(pageable);
        List<AttendanceRecordDTO> content = page.getContent().stream()
                .map(attendanceRecord -> mapToDTO(attendanceRecord, new AttendanceRecordDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public AttendanceRecordDTO get(final Integer id) {
        return attendanceRecordRepository.findById(id)
                .map(attendanceRecord -> mapToDTO(attendanceRecord, new AttendanceRecordDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AttendanceRecordDTO attendanceRecordDTO) {
        final AttendanceRecord attendanceRecord = new AttendanceRecord();
        mapToEntity(attendanceRecordDTO, attendanceRecord);
        return attendanceRecordRepository.save(attendanceRecord).getId();
    }

    public void update(final Integer id, final AttendanceRecordDTO attendanceRecordDTO) {
        final AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(attendanceRecordDTO, attendanceRecord);
        attendanceRecordRepository.save(attendanceRecord);
    }

    public void delete(final Integer id) {
        final AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        attendanceRecordRepository.delete(attendanceRecord);
    }

    private AttendanceRecordDTO mapToDTO(final AttendanceRecord attendanceRecord,
            final AttendanceRecordDTO attendanceRecordDTO) {
        attendanceRecordDTO.setId(attendanceRecord.getId());
        attendanceRecordDTO.setDate(attendanceRecord.getDate());
        attendanceRecordDTO.setStatus(attendanceRecord.getStatus());
        attendanceRecordDTO.setCreatedAt(attendanceRecord.getCreatedAt());
        attendanceRecordDTO.setStudent(attendanceRecord.getStudent() == null ? null : attendanceRecord.getStudent().getId());
        attendanceRecordDTO.setClasss(attendanceRecord.getClasss() == null ? null : attendanceRecord.getClasss().getId());
        attendanceRecordDTO.setMarkedByStaff(attendanceRecord.getMarkedByStaff() == null ? null : attendanceRecord.getMarkedByStaff().getId());
        return attendanceRecordDTO;
    }

    private AttendanceRecord mapToEntity(final AttendanceRecordDTO attendanceRecordDTO,
            final AttendanceRecord attendanceRecord) {
        attendanceRecord.setDate(attendanceRecordDTO.getDate());
        attendanceRecord.setStatus(attendanceRecordDTO.getStatus());
        attendanceRecord.setCreatedAt(attendanceRecordDTO.getCreatedAt());
        final Student student = attendanceRecordDTO.getStudent() == null ? null : studentRepository.findById(attendanceRecordDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        attendanceRecord.setStudent(student);
        final Classs classs = attendanceRecordDTO.getClasss() == null ? null : classsRepository.findById(attendanceRecordDTO.getClasss())
                .orElseThrow(() -> new NotFoundException("classs not found"));
        attendanceRecord.setClasss(classs);
        final Staff markedByStaff = attendanceRecordDTO.getMarkedByStaff() == null ? null : staffRepository.findById(attendanceRecordDTO.getMarkedByStaff())
                .orElseThrow(() -> new NotFoundException("markedByStaff not found"));
        attendanceRecord.setMarkedByStaff(markedByStaff);
        return attendanceRecord;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final AttendanceRecord studentAttendanceRecord = attendanceRecordRepository.findFirstByStudentId(event.getId());
        if (studentAttendanceRecord != null) {
            referencedException.setKey("student.attendanceRecord.student.referenced");
            referencedException.addParam(studentAttendanceRecord.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteClasss.class)
    public void on(final BeforeDeleteClasss event) {
        final ReferencedException referencedException = new ReferencedException();
        final AttendanceRecord classsAttendanceRecord = attendanceRecordRepository.findFirstByClasssId(event.getId());
        if (classsAttendanceRecord != null) {
            referencedException.setKey("classs.attendanceRecord.classs.referenced");
            referencedException.addParam(classsAttendanceRecord.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final AttendanceRecord markedByStaffAttendanceRecord = attendanceRecordRepository.findFirstByMarkedByStaffId(event.getId());
        if (markedByStaffAttendanceRecord != null) {
            referencedException.setKey("staff.attendanceRecord.markedByStaff.referenced");
            referencedException.addParam(markedByStaffAttendanceRecord.getId());
            throw referencedException;
        }
    }

}
