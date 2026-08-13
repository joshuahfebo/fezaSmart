package com.fezaschools.fezasmart.student_point;

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
public class StudentPointService {

    private final StudentPointRepository studentPointRepository;
    private final StudentRepository studentRepository;

    public StudentPointService(final StudentPointRepository studentPointRepository,
            final StudentRepository studentRepository) {
        this.studentPointRepository = studentPointRepository;
        this.studentRepository = studentRepository;
    }

    public List<StudentPointDTO> findAll() {
        final List<StudentPoint> studentPoints = studentPointRepository.findAll(Sort.by("pointType"));
        return studentPoints.stream()
                .map(studentPoint -> mapToDTO(studentPoint, new StudentPointDTO()))
                .toList();
    }

    public StudentPointDTO get(final String pointType) {
        return studentPointRepository.findById(pointType)
                .map(studentPoint -> mapToDTO(studentPoint, new StudentPointDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final StudentPointDTO studentPointDTO) {
        final StudentPoint studentPoint = new StudentPoint();
        mapToEntity(studentPointDTO, studentPoint);
        studentPoint.setPointType(studentPointDTO.getPointType());
        return studentPointRepository.save(studentPoint).getPointType();
    }

    public void update(final String pointType, final StudentPointDTO studentPointDTO) {
        final StudentPoint studentPoint = studentPointRepository.findById(pointType)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentPointDTO, studentPoint);
        studentPointRepository.save(studentPoint);
    }

    public void delete(final String pointType) {
        final StudentPoint studentPoint = studentPointRepository.findById(pointType)
                .orElseThrow(NotFoundException::new);
        studentPointRepository.delete(studentPoint);
    }

    private StudentPointDTO mapToDTO(final StudentPoint studentPoint,
            final StudentPointDTO studentPointDTO) {
        studentPointDTO.setPointType(studentPoint.getPointType());
        studentPointDTO.setCurrentPoints(studentPoint.getCurrentPoints());
        studentPointDTO.setStudent(studentPoint.getStudent() == null ? null : studentPoint.getStudent().getId());
        return studentPointDTO;
    }

    private StudentPoint mapToEntity(final StudentPointDTO studentPointDTO,
            final StudentPoint studentPoint) {
        studentPoint.setCurrentPoints(studentPointDTO.getCurrentPoints());
        final Student student = studentPointDTO.getStudent() == null ? null : studentRepository.findById(studentPointDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        studentPoint.setStudent(student);
        return studentPoint;
    }

    public boolean pointTypeExists(final String pointType) {
        return studentPointRepository.existsByPointTypeIgnoreCase(pointType);
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentPoint studentStudentPoint = studentPointRepository.findFirstByStudentId(event.getId());
        if (studentStudentPoint != null) {
            referencedException.setKey("student.studentPoint.student.referenced");
            referencedException.addParam(studentStudentPoint.getPointType());
            throw referencedException;
        }
    }

}
