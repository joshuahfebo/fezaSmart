package com.fezaschools.fezasmart.grade_boundary;

import com.fezaschools.fezasmart.events.BeforeDeleteExam;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.events.BeforeDeleteSubject;
import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.exam.ExamRepository;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class GradeBoundaryService {

    private final GradeBoundaryRepository gradeBoundaryRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;

    public GradeBoundaryService(final GradeBoundaryRepository gradeBoundaryRepository,
            final SchoolRepository schoolRepository, final SubjectRepository subjectRepository,
            final ExamRepository examRepository) {
        this.gradeBoundaryRepository = gradeBoundaryRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
        this.examRepository = examRepository;
    }

    public List<GradeBoundaryDTO> findAll() {
        final List<GradeBoundary> gradeBoundaries = gradeBoundaryRepository.findAll(Sort.by("id"));
        return gradeBoundaries.stream()
                .map(gradeBoundary -> mapToDTO(gradeBoundary, new GradeBoundaryDTO()))
                .toList();
    }

    public GradeBoundaryDTO get(final Integer id) {
        return gradeBoundaryRepository.findById(id)
                .map(gradeBoundary -> mapToDTO(gradeBoundary, new GradeBoundaryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final GradeBoundaryDTO gradeBoundaryDTO) {
        final GradeBoundary gradeBoundary = new GradeBoundary();
        mapToEntity(gradeBoundaryDTO, gradeBoundary);
        return gradeBoundaryRepository.save(gradeBoundary).getId();
    }

    public void update(final Integer id, final GradeBoundaryDTO gradeBoundaryDTO) {
        final GradeBoundary gradeBoundary = gradeBoundaryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(gradeBoundaryDTO, gradeBoundary);
        gradeBoundaryRepository.save(gradeBoundary);
    }

    public void delete(final Integer id) {
        final GradeBoundary gradeBoundary = gradeBoundaryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        gradeBoundaryRepository.delete(gradeBoundary);
    }

    private GradeBoundaryDTO mapToDTO(final GradeBoundary gradeBoundary,
            final GradeBoundaryDTO gradeBoundaryDTO) {
        gradeBoundaryDTO.setId(gradeBoundary.getId());
        gradeBoundaryDTO.setMinPercentage(gradeBoundary.getMinPercentage());
        gradeBoundaryDTO.setMaxPercentage(gradeBoundary.getMaxPercentage());
        gradeBoundaryDTO.setLetterGrade(gradeBoundary.getLetterGrade());
        gradeBoundaryDTO.setPointGrade(gradeBoundary.getPointGrade());
        gradeBoundaryDTO.setRemark(gradeBoundary.getRemark());
        gradeBoundaryDTO.setType(gradeBoundary.getType());
        gradeBoundaryDTO.setSchool(gradeBoundary.getSchool() == null ? null : gradeBoundary.getSchool().getId());
        gradeBoundaryDTO.setSubject(gradeBoundary.getSubject() == null ? null : gradeBoundary.getSubject().getId());
        gradeBoundaryDTO.setExam(gradeBoundary.getExam() == null ? null : gradeBoundary.getExam().getId());
        return gradeBoundaryDTO;
    }

    private GradeBoundary mapToEntity(final GradeBoundaryDTO gradeBoundaryDTO,
            final GradeBoundary gradeBoundary) {
        gradeBoundary.setMinPercentage(gradeBoundaryDTO.getMinPercentage());
        gradeBoundary.setMaxPercentage(gradeBoundaryDTO.getMaxPercentage());
        gradeBoundary.setLetterGrade(gradeBoundaryDTO.getLetterGrade());
        gradeBoundary.setPointGrade(gradeBoundaryDTO.getPointGrade());
        gradeBoundary.setRemark(gradeBoundaryDTO.getRemark());
        gradeBoundary.setType(gradeBoundaryDTO.getType());
        final School school = gradeBoundaryDTO.getSchool() == null ? null : schoolRepository.findById(gradeBoundaryDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        gradeBoundary.setSchool(school);
        final Subject subject = gradeBoundaryDTO.getSubject() == null ? null : subjectRepository.findById(gradeBoundaryDTO.getSubject())
                .orElseThrow(() -> new NotFoundException("subject not found"));
        gradeBoundary.setSubject(subject);
        final Exam exam = gradeBoundaryDTO.getExam() == null ? null : examRepository.findById(gradeBoundaryDTO.getExam())
                .orElseThrow(() -> new NotFoundException("exam not found"));
        gradeBoundary.setExam(exam);
        return gradeBoundary;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final GradeBoundary schoolGradeBoundary = gradeBoundaryRepository.findFirstBySchoolId(event.getId());
        if (schoolGradeBoundary != null) {
            referencedException.setKey("school.gradeBoundary.school.referenced");
            referencedException.addParam(schoolGradeBoundary.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSubject.class)
    public void on(final BeforeDeleteSubject event) {
        final ReferencedException referencedException = new ReferencedException();
        final GradeBoundary subjectGradeBoundary = gradeBoundaryRepository.findFirstBySubjectId(event.getId());
        if (subjectGradeBoundary != null) {
            referencedException.setKey("subject.gradeBoundary.subject.referenced");
            referencedException.addParam(subjectGradeBoundary.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteExam.class)
    public void on(final BeforeDeleteExam event) {
        final ReferencedException referencedException = new ReferencedException();
        final GradeBoundary examGradeBoundary = gradeBoundaryRepository.findFirstByExamId(event.getId());
        if (examGradeBoundary != null) {
            referencedException.setKey("exam.gradeBoundary.exam.referenced");
            referencedException.addParam(examGradeBoundary.getId());
            throw referencedException;
        }
    }

}
