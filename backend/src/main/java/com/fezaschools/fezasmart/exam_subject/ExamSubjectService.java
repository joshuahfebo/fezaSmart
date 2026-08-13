package com.fezaschools.fezasmart.exam_subject;

import com.fezaschools.fezasmart.events.BeforeDeleteExam;
import com.fezaschools.fezasmart.events.BeforeDeleteExamSubject;
import com.fezaschools.fezasmart.events.BeforeDeleteSubject;
import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.exam.ExamRepository;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ExamSubjectService {

    private final ExamSubjectRepository examSubjectRepository;
    private final ExamRepository examRepository;
    private final SubjectRepository subjectRepository;
    private final ApplicationEventPublisher publisher;

    public ExamSubjectService(final ExamSubjectRepository examSubjectRepository,
            final ExamRepository examRepository, final SubjectRepository subjectRepository,
            final ApplicationEventPublisher publisher) {
        this.examSubjectRepository = examSubjectRepository;
        this.examRepository = examRepository;
        this.subjectRepository = subjectRepository;
        this.publisher = publisher;
    }

    public List<ExamSubjectDTO> findAll() {
        final List<ExamSubject> examSubjects = examSubjectRepository.findAll(Sort.by("id"));
        return examSubjects.stream()
                .map(examSubject -> mapToDTO(examSubject, new ExamSubjectDTO()))
                .toList();
    }

    public ExamSubjectDTO get(final Integer id) {
        return examSubjectRepository.findById(id)
                .map(examSubject -> mapToDTO(examSubject, new ExamSubjectDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ExamSubjectDTO examSubjectDTO) {
        final ExamSubject examSubject = new ExamSubject();
        mapToEntity(examSubjectDTO, examSubject);
        return examSubjectRepository.save(examSubject).getId();
    }

    public void update(final Integer id, final ExamSubjectDTO examSubjectDTO) {
        final ExamSubject examSubject = examSubjectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(examSubjectDTO, examSubject);
        examSubjectRepository.save(examSubject);
    }

    public void delete(final Integer id) {
        final ExamSubject examSubject = examSubjectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteExamSubject(id));
        examSubjectRepository.delete(examSubject);
    }

    private ExamSubjectDTO mapToDTO(final ExamSubject examSubject,
            final ExamSubjectDTO examSubjectDTO) {
        examSubjectDTO.setId(examSubject.getId());
        examSubjectDTO.setMaxScore(examSubject.getMaxScore());
        examSubjectDTO.setExam(examSubject.getExam() == null ? null : examSubject.getExam().getId());
        examSubjectDTO.setSubject(examSubject.getSubject() == null ? null : examSubject.getSubject().getId());
        return examSubjectDTO;
    }

    private ExamSubject mapToEntity(final ExamSubjectDTO examSubjectDTO,
            final ExamSubject examSubject) {
        examSubject.setMaxScore(examSubjectDTO.getMaxScore());
        final Exam exam = examSubjectDTO.getExam() == null ? null : examRepository.findById(examSubjectDTO.getExam())
                .orElseThrow(() -> new NotFoundException("exam not found"));
        examSubject.setExam(exam);
        final Subject subject = examSubjectDTO.getSubject() == null ? null : subjectRepository.findById(examSubjectDTO.getSubject())
                .orElseThrow(() -> new NotFoundException("subject not found"));
        examSubject.setSubject(subject);
        return examSubject;
    }

    @EventListener(BeforeDeleteExam.class)
    public void on(final BeforeDeleteExam event) {
        final ReferencedException referencedException = new ReferencedException();
        final ExamSubject examExamSubject = examSubjectRepository.findFirstByExamId(event.getId());
        if (examExamSubject != null) {
            referencedException.setKey("exam.examSubject.exam.referenced");
            referencedException.addParam(examExamSubject.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSubject.class)
    public void on(final BeforeDeleteSubject event) {
        final ReferencedException referencedException = new ReferencedException();
        final ExamSubject subjectExamSubject = examSubjectRepository.findFirstBySubjectId(event.getId());
        if (subjectExamSubject != null) {
            referencedException.setKey("subject.examSubject.subject.referenced");
            referencedException.addParam(subjectExamSubject.getId());
            throw referencedException;
        }
    }

}
