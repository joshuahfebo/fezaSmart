package com.fezaschools.fezasmart.school;

import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher publisher;

    public SchoolService(final SchoolRepository schoolRepository,
            final ApplicationEventPublisher publisher) {
        this.schoolRepository = schoolRepository;
        this.publisher = publisher;
    }

    public List<SchoolDTO> findAll() {
        final List<School> schools = schoolRepository.findAll(Sort.by("id"));
        return schools.stream()
                .map(school -> mapToDTO(school, new SchoolDTO()))
                .toList();
    }

    public SchoolDTO get(final Integer id) {
        return schoolRepository.findById(id)
                .map(school -> mapToDTO(school, new SchoolDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SchoolDTO schoolDTO) {
        final School school = new School();
        mapToEntity(schoolDTO, school);
        return schoolRepository.save(school).getId();
    }

    public void update(final Integer id, final SchoolDTO schoolDTO) {
        final School school = schoolRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(schoolDTO, school);
        schoolRepository.save(school);
    }

    public void delete(final Integer id) {
        final School school = schoolRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteSchool(id));
        schoolRepository.delete(school);
    }

    private SchoolDTO mapToDTO(final School school, final SchoolDTO schoolDTO) {
        schoolDTO.setId(school.getId());
        schoolDTO.setName(school.getName());
        schoolDTO.setLocation(school.getLocation());
        schoolDTO.setIsActive(school.getIsActive());
        schoolDTO.setCreatedAt(school.getCreatedAt());
        schoolDTO.setUpdatedAt(school.getUpdatedAt());
        schoolDTO.setDeletedAt(school.getDeletedAt());
        schoolDTO.setDeletedBy(school.getDeletedBy());
        schoolDTO.setRestoreToken(school.getRestoreToken());
        return schoolDTO;
    }

    private School mapToEntity(final SchoolDTO schoolDTO, final School school) {
        school.setName(schoolDTO.getName());
        school.setLocation(schoolDTO.getLocation());
        school.setIsActive(schoolDTO.getIsActive());
        school.setCreatedAt(schoolDTO.getCreatedAt());
        school.setUpdatedAt(schoolDTO.getUpdatedAt());
        school.setDeletedAt(schoolDTO.getDeletedAt());
        school.setDeletedBy(schoolDTO.getDeletedBy());
        school.setRestoreToken(schoolDTO.getRestoreToken());
        return school;
    }

}
