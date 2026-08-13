package com.fezaschools.fezasmart.timetable;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.events.BeforeDeleteTimetable;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final AcademicYearRepository academicYearRepository;
    private final ApplicationEventPublisher publisher;

    public TimetableService(final TimetableRepository timetableRepository,
            final AcademicYearRepository academicYearRepository,
            final ApplicationEventPublisher publisher) {
        this.timetableRepository = timetableRepository;
        this.academicYearRepository = academicYearRepository;
        this.publisher = publisher;
    }

    public List<TimetableDTO> findAll() {
        final List<Timetable> timetables = timetableRepository.findAll(Sort.by("id"));
        return timetables.stream()
                .map(timetable -> mapToDTO(timetable, new TimetableDTO()))
                .toList();
    }

    public TimetableDTO get(final Integer id) {
        return timetableRepository.findById(id)
                .map(timetable -> mapToDTO(timetable, new TimetableDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TimetableDTO timetableDTO) {
        final Timetable timetable = new Timetable();
        mapToEntity(timetableDTO, timetable);
        return timetableRepository.save(timetable).getId();
    }

    public void update(final Integer id, final TimetableDTO timetableDTO) {
        final Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(timetableDTO, timetable);
        timetableRepository.save(timetable);
    }

    public void delete(final Integer id) {
        final Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTimetable(id));
        timetableRepository.delete(timetable);
    }

    private TimetableDTO mapToDTO(final Timetable timetable, final TimetableDTO timetableDTO) {
        timetableDTO.setId(timetable.getId());
        timetableDTO.setTitle(timetable.getTitle());
        timetableDTO.setSchoolId(timetable.getSchoolId());
        timetableDTO.setCreatedAt(timetable.getCreatedAt());
        timetableDTO.setAcademicYear(timetable.getAcademicYear() == null ? null : timetable.getAcademicYear().getId());
        return timetableDTO;
    }

    private Timetable mapToEntity(final TimetableDTO timetableDTO, final Timetable timetable) {
        timetable.setTitle(timetableDTO.getTitle());
        timetable.setSchoolId(timetableDTO.getSchoolId());
        timetable.setCreatedAt(timetableDTO.getCreatedAt());
        final AcademicYear academicYear = timetableDTO.getAcademicYear() == null ? null : academicYearRepository.findById(timetableDTO.getAcademicYear())
                .orElseThrow(() -> new NotFoundException("academicYear not found"));
        timetable.setAcademicYear(academicYear);
        return timetable;
    }

    @EventListener(BeforeDeleteAcademicYear.class)
    public void on(final BeforeDeleteAcademicYear event) {
        final ReferencedException referencedException = new ReferencedException();
        final Timetable academicYearTimetable = timetableRepository.findFirstByAcademicYearId(event.getId());
        if (academicYearTimetable != null) {
            referencedException.setKey("academicYear.timetable.academicYear.referenced");
            referencedException.addParam(academicYearTimetable.getId());
            throw referencedException;
        }
    }

}
