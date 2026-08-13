package com.fezaschools.fezasmart.student_score;

import com.fezaschools.fezasmart.events.BeforeDeleteExamSubject;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteStudentScore;
import com.fezaschools.fezasmart.exam_subject.ExamSubject;
import com.fezaschools.fezasmart.exam_subject.ExamSubjectRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.PagedResponse;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudentScoreService {

    private final StudentScoreRepository studentScoreRepository;
    private final StudentRepository studentRepository;
    private final ExamSubjectRepository examSubjectRepository;
    private final ApplicationEventPublisher publisher;

    public StudentScoreService(final StudentScoreRepository studentScoreRepository,
            final StudentRepository studentRepository,
            final ExamSubjectRepository examSubjectRepository,
            final ApplicationEventPublisher publisher) {
        this.studentScoreRepository = studentScoreRepository;
        this.studentRepository = studentRepository;
        this.examSubjectRepository = examSubjectRepository;
        this.publisher = publisher;
    }

    public List<StudentScoreDTO> findAll() {
        final List<StudentScore> studentScores = studentScoreRepository.findAll(Sort.by("id"));
        return studentScores.stream()
                .map(studentScore -> mapToDTO(studentScore, new StudentScoreDTO()))
                .toList();
    }

    public PagedResponse<StudentScoreDTO> findAll(Pageable pageable) {
        final Page<StudentScore> page = studentScoreRepository.findAll(pageable);
        List<StudentScoreDTO> content = page.getContent().stream()
                .map(studentScore -> mapToDTO(studentScore, new StudentScoreDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public StudentScoreDTO get(final Integer id) {
        return studentScoreRepository.findById(id)
                .map(studentScore -> mapToDTO(studentScore, new StudentScoreDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudentScoreDTO studentScoreDTO) {
        final StudentScore studentScore = new StudentScore();
        mapToEntity(studentScoreDTO, studentScore);
        return studentScoreRepository.save(studentScore).getId();
    }

    public void update(final Integer id, final StudentScoreDTO studentScoreDTO) {
        final StudentScore studentScore = studentScoreRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentScoreDTO, studentScore);
        studentScoreRepository.save(studentScore);
    }

    public void delete(final Integer id) {
        final StudentScore studentScore = studentScoreRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteStudentScore(id));
        studentScoreRepository.delete(studentScore);
    }

    private StudentScoreDTO mapToDTO(final StudentScore studentScore,
            final StudentScoreDTO studentScoreDTO) {
        studentScoreDTO.setId(studentScore.getId());
        studentScoreDTO.setScore(studentScore.getScore());
        studentScoreDTO.setStudent(studentScore.getStudent() == null ? null : studentScore.getStudent().getId());
        studentScoreDTO.setExamSubject(studentScore.getExamSubject() == null ? null : studentScore.getExamSubject().getId());
        return studentScoreDTO;
    }

    private StudentScore mapToEntity(final StudentScoreDTO studentScoreDTO,
            final StudentScore studentScore) {
        studentScore.setScore(studentScoreDTO.getScore());
        final Student student = studentScoreDTO.getStudent() == null ? null : studentRepository.findById(studentScoreDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        studentScore.setStudent(student);
        final ExamSubject examSubject = studentScoreDTO.getExamSubject() == null ? null : examSubjectRepository.findById(studentScoreDTO.getExamSubject())
                .orElseThrow(() -> new NotFoundException("examSubject not found"));
        studentScore.setExamSubject(examSubject);
        return studentScore;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentScore studentStudentScore = studentScoreRepository.findFirstByStudentId(event.getId());
        if (studentStudentScore != null) {
            referencedException.setKey("student.studentScore.student.referenced");
            referencedException.addParam(studentStudentScore.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteExamSubject.class)
    public void on(final BeforeDeleteExamSubject event) {
        final ReferencedException referencedException = new ReferencedException();
        final StudentScore examSubjectStudentScore = studentScoreRepository.findFirstByExamSubjectId(event.getId());
        if (examSubjectStudentScore != null) {
            referencedException.setKey("examSubject.studentScore.examSubject.referenced");
            referencedException.addParam(examSubjectStudentScore.getId());
            throw referencedException;
        }
    }

}
