package com.fezaschools.fezasmart.exam;

import com.fezaschools.fezasmart.events.BeforeDeleteTerm;
import com.fezaschools.fezasmart.events.BeforeDeleteExam;
import com.fezaschools.fezasmart.term.Term;
import com.fezaschools.fezasmart.term.TermRepository;
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
public class ExamService {

    private final ExamRepository examRepository;
    private final TermRepository termRepository;
    private final ApplicationEventPublisher publisher;

    public ExamService(final ExamRepository examRepository,
            final TermRepository termRepository,
            final ApplicationEventPublisher publisher) {
        this.examRepository = examRepository;
        this.termRepository = termRepository;
        this.publisher = publisher;
    }

    public List<ExamDTO> findAll() {
        final List<Exam> exams = examRepository.findAll(Sort.by("id"));
        return exams.stream()
                .map(exam -> mapToDTO(exam, new ExamDTO()))
                .toList();
    }

    public PagedResponse<ExamDTO> findAll(Pageable pageable) {
        final Page<Exam> page = examRepository.findAll(pageable);
        List<ExamDTO> content = page.getContent().stream()
                .map(exam -> mapToDTO(exam, new ExamDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public ExamDTO get(final Integer id) {
        return examRepository.findById(id)
                .map(exam -> mapToDTO(exam, new ExamDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ExamDTO examDTO) {
        final Exam exam = new Exam();
        mapToEntity(examDTO, exam);
        return examRepository.save(exam).getId();
    }

    public void update(final Integer id, final ExamDTO examDTO) {
        final Exam exam = examRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(examDTO, exam);
        examRepository.save(exam);
    }

    public void delete(final Integer id) {
        final Exam exam = examRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteExam(id));
        examRepository.delete(exam);
    }

    private ExamDTO mapToDTO(final Exam exam, final ExamDTO examDTO) {
        examDTO.setId(exam.getId());
        examDTO.setName(exam.getName());
        examDTO.setExamDate(exam.getExamDate());
        examDTO.setTerm(exam.getTerm() == null ? null : exam.getTerm().getId());
        return examDTO;
    }

    private Exam mapToEntity(final ExamDTO examDTO, final Exam exam) {
        exam.setName(examDTO.getName());
        exam.setExamDate(examDTO.getExamDate());
        final Term term = examDTO.getTerm() == null ? null : termRepository.findById(examDTO.getTerm())
                .orElseThrow(() -> new NotFoundException("term not found"));
        exam.setTerm(term);
        return exam;
    }

    @EventListener(BeforeDeleteTerm.class)
    public void on(final BeforeDeleteTerm event) {
        final ReferencedException referencedException = new ReferencedException();
        final Exam termExam = examRepository.findFirstByTermId(event.getId());
        if (termExam != null) {
            referencedException.setKey("term.exam.term.referenced");
            referencedException.addParam(termExam.getId());
            throw referencedException;
        }
    }

}
