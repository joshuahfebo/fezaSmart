package com.fezaschools.fezasmart.classs;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.events.BeforeDeleteClasss;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
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
public class ClasssService {

    private final ClasssRepository classsRepository;
    private final SchoolRepository schoolRepository;
    private final AcademicYearRepository academicYearRepository;
    private final ApplicationEventPublisher publisher;

    public ClasssService(final ClasssRepository classsRepository,
            final SchoolRepository schoolRepository,
            final AcademicYearRepository academicYearRepository,
            final ApplicationEventPublisher publisher) {
        this.classsRepository = classsRepository;
        this.schoolRepository = schoolRepository;
        this.academicYearRepository = academicYearRepository;
        this.publisher = publisher;
    }

    public List<ClasssDTO> findAll() {
        final List<Classs> classsList = classsRepository.findAll(Sort.by("id"));
        return classsList.stream()
                .map(classs -> mapToDTO(classs, new ClasssDTO()))
                .toList();
    }

    public PagedResponse<ClasssDTO> findAll(Pageable pageable) {
        final Page<Classs> page = classsRepository.findAll(pageable);
        List<ClasssDTO> content = page.getContent().stream()
                .map(classs -> mapToDTO(classs, new ClasssDTO()))
                .toList();
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public ClasssDTO get(final Integer id) {
        return classsRepository.findById(id)
                .map(classs -> mapToDTO(classs, new ClasssDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClasssDTO classsDTO) {
        final Classs classs = new Classs();
        mapToEntity(classsDTO, classs);
        return classsRepository.save(classs).getId();
    }

    public void update(final Integer id, final ClasssDTO classsDTO) {
        final Classs classs = classsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(classsDTO, classs);
        classsRepository.save(classs);
    }

    public void delete(final Integer id) {
        final Classs classs = classsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteClasss(id));
        classsRepository.delete(classs);
    }

    private ClasssDTO mapToDTO(final Classs classs, final ClasssDTO classsDTO) {
        classsDTO.setId(classs.getId());
        classsDTO.setName(classs.getName());
        classsDTO.setCreatedAt(classs.getCreatedAt());
        classsDTO.setSchool(classs.getSchool() == null ? null : classs.getSchool().getId());
        classsDTO.setAcademicYear(classs.getAcademicYear() == null ? null : classs.getAcademicYear().getId());
        return classsDTO;
    }

    private Classs mapToEntity(final ClasssDTO classsDTO, final Classs classs) {
        classs.setName(classsDTO.getName());
        classs.setCreatedAt(classsDTO.getCreatedAt());
        final School school = classsDTO.getSchool() == null ? null : schoolRepository.findById(classsDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        classs.setSchool(school);
        final AcademicYear academicYear = classsDTO.getAcademicYear() == null ? null : academicYearRepository.findById(classsDTO.getAcademicYear())
                .orElseThrow(() -> new NotFoundException("academicYear not found"));
        classs.setAcademicYear(academicYear);
        return classs;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final Classs schoolClasss = classsRepository.findFirstBySchoolId(event.getId());
        if (schoolClasss != null) {
            referencedException.setKey("school.classs.school.referenced");
            referencedException.addParam(schoolClasss.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteAcademicYear.class)
    public void on(final BeforeDeleteAcademicYear event) {
        final ReferencedException referencedException = new ReferencedException();
        final Classs academicYearClasss = classsRepository.findFirstByAcademicYearId(event.getId());
        if (academicYearClasss != null) {
            referencedException.setKey("academicYear.classs.academicYear.referenced");
            referencedException.addParam(academicYearClasss.getId());
            throw referencedException;
        }
    }

}
