package com.fezaschools.fezasmart.result;

import com.fezaschools.fezasmart.events.BeforeDeleteExam;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteResult;
import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.exam.ExamRepository;
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
public class ResultService {

    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final ApplicationEventPublisher publisher;

    public ResultService(final ResultRepository resultRepository,
            final StudentRepository studentRepository,
            final ExamRepository examRepository,
            final ApplicationEventPublisher publisher) {
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.publisher = publisher;
    }

    public List<ResultDTO> findAll() {
        final List<Result> results = resultRepository.findAll(Sort.by("id"));
        return results.stream()
                .map(result -> mapToDTO(result, new ResultDTO()))
                .toList();
    }

    public PagedResponse<ResultDTO> findAll(Pageable pageable) {
        final Page<Result> page = resultRepository.findAll(pageable);
        List<ResultDTO> content = page.getContent().stream()
                .map(result -> mapToDTO(result, new ResultDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public ResultDTO get(final Integer id) {
        return resultRepository.findById(id)
                .map(result -> mapToDTO(result, new ResultDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ResultDTO resultDTO) {
        final Result result = new Result();
        mapToEntity(resultDTO, result);
        return resultRepository.save(result).getId();
    }

    public void update(final Integer id, final ResultDTO resultDTO) {
        final Result result = resultRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(resultDTO, result);
        resultRepository.save(result);
    }

    public void delete(final Integer id) {
        final Result result = resultRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteResult(id));
        resultRepository.delete(result);
    }

    private ResultDTO mapToDTO(final Result result, final ResultDTO resultDTO) {
        resultDTO.setId(result.getId());
        resultDTO.setTotalScore(result.getTotalScore());
        resultDTO.setAveragePercentage(result.getAveragePercentage());
        resultDTO.setTotalPoints(result.getTotalPoints());
        resultDTO.setDivision(result.getDivision());
        resultDTO.setRankInClass(result.getRankInClass());
        resultDTO.setRemark(result.getRemark());
        resultDTO.setComputedAt(result.getComputedAt());
        resultDTO.setStudent(result.getStudent() == null ? null : result.getStudent().getId());
        resultDTO.setExam(result.getExam() == null ? null : result.getExam().getId());
        return resultDTO;
    }

    private Result mapToEntity(final ResultDTO resultDTO, final Result result) {
        result.setTotalScore(resultDTO.getTotalScore());
        result.setAveragePercentage(resultDTO.getAveragePercentage());
        result.setTotalPoints(resultDTO.getTotalPoints());
        result.setDivision(resultDTO.getDivision());
        result.setRankInClass(resultDTO.getRankInClass());
        result.setRemark(resultDTO.getRemark());
        result.setComputedAt(resultDTO.getComputedAt());
        final Student student = resultDTO.getStudent() == null ? null : studentRepository.findById(resultDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        result.setStudent(student);
        final Exam exam = resultDTO.getExam() == null ? null : examRepository.findById(resultDTO.getExam())
                .orElseThrow(() -> new NotFoundException("exam not found"));
        result.setExam(exam);
        return result;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final Result studentResult = resultRepository.findFirstByStudentId(event.getId());
        if (studentResult != null) {
            referencedException.setKey("student.result.student.referenced");
            referencedException.addParam(studentResult.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteExam.class)
    public void on(final BeforeDeleteExam event) {
        final ReferencedException referencedException = new ReferencedException();
        final Result examResult = resultRepository.findFirstByExamId(event.getId());
        if (examResult != null) {
            referencedException.setKey("exam.result.exam.referenced");
            referencedException.addParam(examResult.getId());
            throw referencedException;
        }
    }

}
