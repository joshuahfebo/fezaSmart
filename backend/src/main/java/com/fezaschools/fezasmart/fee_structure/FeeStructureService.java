package com.fezaschools.fezasmart.fee_structure;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.events.BeforeDeleteFeeStructure;
import com.fezaschools.fezasmart.events.BeforeDeleteSchool;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.school.SchoolRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FeeStructureService {

    private final FeeStructureRepository feeStructureRepository;
    private final SchoolRepository schoolRepository;
    private final AcademicYearRepository academicYearRepository;
    private final ApplicationEventPublisher publisher;

    public FeeStructureService(final FeeStructureRepository feeStructureRepository,
            final SchoolRepository schoolRepository,
            final AcademicYearRepository academicYearRepository,
            final ApplicationEventPublisher publisher) {
        this.feeStructureRepository = feeStructureRepository;
        this.schoolRepository = schoolRepository;
        this.academicYearRepository = academicYearRepository;
        this.publisher = publisher;
    }

    public List<FeeStructureDTO> findAll() {
        final List<FeeStructure> feeStructures = feeStructureRepository.findAll(Sort.by("id"));
        return feeStructures.stream()
                .map(feeStructure -> mapToDTO(feeStructure, new FeeStructureDTO()))
                .toList();
    }

    public FeeStructureDTO get(final Integer id) {
        return feeStructureRepository.findById(id)
                .map(feeStructure -> mapToDTO(feeStructure, new FeeStructureDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final FeeStructureDTO feeStructureDTO) {
        final FeeStructure feeStructure = new FeeStructure();
        mapToEntity(feeStructureDTO, feeStructure);
        return feeStructureRepository.save(feeStructure).getId();
    }

    public void update(final Integer id, final FeeStructureDTO feeStructureDTO) {
        final FeeStructure feeStructure = feeStructureRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(feeStructureDTO, feeStructure);
        feeStructureRepository.save(feeStructure);
    }

    public void delete(final Integer id) {
        final FeeStructure feeStructure = feeStructureRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteFeeStructure(id));
        feeStructureRepository.delete(feeStructure);
    }

    private FeeStructureDTO mapToDTO(final FeeStructure feeStructure,
            final FeeStructureDTO feeStructureDTO) {
        feeStructureDTO.setId(feeStructure.getId());
        feeStructureDTO.setName(feeStructure.getName());
        feeStructureDTO.setDescription(feeStructure.getDescription());
        feeStructureDTO.setSchool(feeStructure.getSchool() == null ? null : feeStructure.getSchool().getId());
        feeStructureDTO.setAcademicYear(feeStructure.getAcademicYear() == null ? null : feeStructure.getAcademicYear().getId());
        return feeStructureDTO;
    }

    private FeeStructure mapToEntity(final FeeStructureDTO feeStructureDTO,
            final FeeStructure feeStructure) {
        feeStructure.setName(feeStructureDTO.getName());
        feeStructure.setDescription(feeStructureDTO.getDescription());
        final School school = feeStructureDTO.getSchool() == null ? null : schoolRepository.findById(feeStructureDTO.getSchool())
                .orElseThrow(() -> new NotFoundException("school not found"));
        feeStructure.setSchool(school);
        final AcademicYear academicYear = feeStructureDTO.getAcademicYear() == null ? null : academicYearRepository.findById(feeStructureDTO.getAcademicYear())
                .orElseThrow(() -> new NotFoundException("academicYear not found"));
        feeStructure.setAcademicYear(academicYear);
        return feeStructure;
    }

    @EventListener(BeforeDeleteSchool.class)
    public void on(final BeforeDeleteSchool event) {
        final ReferencedException referencedException = new ReferencedException();
        final FeeStructure schoolFeeStructure = feeStructureRepository.findFirstBySchoolId(event.getId());
        if (schoolFeeStructure != null) {
            referencedException.setKey("school.feeStructure.school.referenced");
            referencedException.addParam(schoolFeeStructure.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteAcademicYear.class)
    public void on(final BeforeDeleteAcademicYear event) {
        final ReferencedException referencedException = new ReferencedException();
        final FeeStructure academicYearFeeStructure = feeStructureRepository.findFirstByAcademicYearId(event.getId());
        if (academicYearFeeStructure != null) {
            referencedException.setKey("academicYear.feeStructure.academicYear.referenced");
            referencedException.addParam(academicYearFeeStructure.getId());
            throw referencedException;
        }
    }

}
