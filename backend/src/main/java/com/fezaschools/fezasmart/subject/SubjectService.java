package com.fezaschools.fezasmart.subject;

import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.PagedResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(final SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectDTO> findAll() {
        final List<Subject> subjects = subjectRepository.findAll(Sort.by("id"));
        return subjects.stream()
                .map(subject -> mapToDTO(subject, new SubjectDTO()))
                .toList();
    }

    public PagedResponse<SubjectDTO> findAll(Pageable pageable) {
        final Page<Subject> page = subjectRepository.findAll(pageable);
        List<SubjectDTO> content = page.getContent().stream()
                .map(subject -> mapToDTO(subject, new SubjectDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public SubjectDTO get(final Integer id) {
        return subjectRepository.findById(id)
                .map(subject -> mapToDTO(subject, new SubjectDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SubjectDTO subjectDTO) {
        final Subject subject = new Subject();
        mapToEntity(subjectDTO, subject);
        return subjectRepository.save(subject).getId();
    }

    public void update(final Integer id, final SubjectDTO subjectDTO) {
        final Subject subject = subjectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subjectDTO, subject);
        subjectRepository.save(subject);
    }

    public void delete(final Integer id) {
        final Subject subject = subjectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        subjectRepository.delete(subject);
    }

    private SubjectDTO mapToDTO(final Subject subject, final SubjectDTO subjectDTO) {
        subjectDTO.setId(subject.getId());
        subjectDTO.setName(subject.getName());
        subjectDTO.setType(subject.getType());
        subjectDTO.setTeacherSubjectStaffs(subject.getTeacherSubjectStaffs().stream()
                .map(staff -> staff.getId())
                .toList());
        return subjectDTO;
    }

    private Subject mapToEntity(final SubjectDTO subjectDTO, final Subject subject) {
        subject.setName(subjectDTO.getName());
        subject.setType(subjectDTO.getType());
        return subject;
    }

}
