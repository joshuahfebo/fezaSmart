package com.fezaschools.fezasmart.academic_year;

import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final ApplicationEventPublisher publisher;

    public AcademicYearService(final AcademicYearRepository academicYearRepository,
            final ApplicationEventPublisher publisher) {
        this.academicYearRepository = academicYearRepository;
        this.publisher = publisher;
    }

    public List<AcademicYearDTO> findAll() {
        final List<AcademicYear> academicYears = academicYearRepository.findAll(Sort.by("id"));
        return academicYears.stream()
                .map(academicYear -> mapToDTO(academicYear, new AcademicYearDTO()))
                .toList();
    }

    public AcademicYearDTO get(final Integer id) {
        return academicYearRepository.findById(id)
                .map(academicYear -> mapToDTO(academicYear, new AcademicYearDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AcademicYearDTO academicYearDTO) {
        final AcademicYear academicYear = new AcademicYear();
        mapToEntity(academicYearDTO, academicYear);
        return academicYearRepository.save(academicYear).getId();
    }

    public void update(final Integer id, final AcademicYearDTO academicYearDTO) {
        final AcademicYear academicYear = academicYearRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(academicYearDTO, academicYear);
        academicYearRepository.save(academicYear);
    }

    public void delete(final Integer id) {
        final AcademicYear academicYear = academicYearRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteAcademicYear(id));
        academicYearRepository.delete(academicYear);
    }

    private AcademicYearDTO mapToDTO(final AcademicYear academicYear,
            final AcademicYearDTO academicYearDTO) {
        academicYearDTO.setId(academicYear.getId());
        academicYearDTO.setName(academicYear.getName());
        academicYearDTO.setStartDate(academicYear.getStartDate());
        academicYearDTO.setEndDate(academicYear.getEndDate());
        academicYearDTO.setIsCurrent(academicYear.getIsCurrent());
        return academicYearDTO;
    }

    private AcademicYear mapToEntity(final AcademicYearDTO academicYearDTO,
            final AcademicYear academicYear) {
        academicYear.setName(academicYearDTO.getName());
        academicYear.setStartDate(academicYearDTO.getStartDate());
        academicYear.setEndDate(academicYearDTO.getEndDate());
        academicYear.setIsCurrent(academicYearDTO.getIsCurrent());
        return academicYear;
    }

}
