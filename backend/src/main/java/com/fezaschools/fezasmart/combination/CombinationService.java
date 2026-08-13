package com.fezaschools.fezasmart.combination;

import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.classs.ClasssRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteClasss;
import com.fezaschools.fezasmart.events.BeforeDeleteSubject;
import com.fezaschools.fezasmart.events.BeforeDeleteTimetable;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.timetable.Timetable;
import com.fezaschools.fezasmart.timetable.TimetableRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class CombinationService {

    private final CombinationRepository combinationRepository;
    private final ClasssRepository classsRepository;
    private final TimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;

    public CombinationService(final CombinationRepository combinationRepository,
            final ClasssRepository classsRepository, final TimetableRepository timetableRepository,
            final SubjectRepository subjectRepository) {
        this.combinationRepository = combinationRepository;
        this.classsRepository = classsRepository;
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<CombinationDTO> findAll() {
        final List<Combination> combinations = combinationRepository.findAll(Sort.by("id"));
        return combinations.stream()
                .map(combination -> mapToDTO(combination, new CombinationDTO()))
                .toList();
    }

    public CombinationDTO get(final Integer id) {
        return combinationRepository.findById(id)
                .map(combination -> mapToDTO(combination, new CombinationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CombinationDTO combinationDTO) {
        final Combination combination = new Combination();
        mapToEntity(combinationDTO, combination);
        return combinationRepository.save(combination).getId();
    }

    public void update(final Integer id, final CombinationDTO combinationDTO) {
        final Combination combination = combinationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(combinationDTO, combination);
        combinationRepository.save(combination);
    }

    public void delete(final Integer id) {
        final Combination combination = combinationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        combinationRepository.delete(combination);
    }

    private CombinationDTO mapToDTO(final Combination combination,
            final CombinationDTO combinationDTO) {
        combinationDTO.setId(combination.getId());
        combinationDTO.setName(combination.getName());
        combinationDTO.setCreatedAt(combination.getCreatedAt());
        combinationDTO.setClasss(combination.getClasss() == null ? null : combination.getClasss().getId());
        combinationDTO.setTimetable(combination.getTimetable() == null ? null : combination.getTimetable().getId());
        combinationDTO.setCombinationSubjectSubjects(combination.getCombinationSubjectSubjects().stream()
                .map(subject -> subject.getId())
                .toList());
        return combinationDTO;
    }

    private Combination mapToEntity(final CombinationDTO combinationDTO,
            final Combination combination) {
        combination.setName(combinationDTO.getName());
        combination.setCreatedAt(combinationDTO.getCreatedAt());
        final Classs classs = combinationDTO.getClasss() == null ? null : classsRepository.findById(combinationDTO.getClasss())
                .orElseThrow(() -> new NotFoundException("classs not found"));
        combination.setClasss(classs);
        final Timetable timetable = combinationDTO.getTimetable() == null ? null : timetableRepository.findById(combinationDTO.getTimetable())
                .orElseThrow(() -> new NotFoundException("timetable not found"));
        combination.setTimetable(timetable);
        final List<Subject> combinationSubjectSubjects = subjectRepository.findAllById(
                combinationDTO.getCombinationSubjectSubjects() == null ? List.of() : combinationDTO.getCombinationSubjectSubjects());
        if (combinationSubjectSubjects.size() != (combinationDTO.getCombinationSubjectSubjects() == null ? 0 : combinationDTO.getCombinationSubjectSubjects().size())) {
            throw new NotFoundException("one of combinationSubjectSubjects not found");
        }
        combination.setCombinationSubjectSubjects(new HashSet<>(combinationSubjectSubjects));
        return combination;
    }

    @EventListener(BeforeDeleteClasss.class)
    public void on(final BeforeDeleteClasss event) {
        final ReferencedException referencedException = new ReferencedException();
        final Combination classsCombination = combinationRepository.findFirstByClasssId(event.getId());
        if (classsCombination != null) {
            referencedException.setKey("classs.combination.classs.referenced");
            referencedException.addParam(classsCombination.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTimetable.class)
    public void on(final BeforeDeleteTimetable event) {
        final ReferencedException referencedException = new ReferencedException();
        final Combination timetableCombination = combinationRepository.findFirstByTimetableId(event.getId());
        if (timetableCombination != null) {
            referencedException.setKey("timetable.combination.timetable.referenced");
            referencedException.addParam(timetableCombination.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSubject.class)
    public void on(final BeforeDeleteSubject event) {
        // remove many-to-many relations at owning side
        combinationRepository.findAllByCombinationSubjectSubjectsId(event.getId()).forEach(combination ->
                combination.getCombinationSubjectSubjects().removeIf(subject -> subject.getId().equals(event.getId())));
    }

}
