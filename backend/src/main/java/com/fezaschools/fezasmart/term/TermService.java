package com.fezaschools.fezasmart.term;

import com.fezaschools.fezasmart.events.BeforeDeleteTerm;
import com.fezaschools.fezasmart.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TermService {

    private final TermRepository termRepository;
    private final ApplicationEventPublisher publisher;

    public TermService(final TermRepository termRepository,
            final ApplicationEventPublisher publisher) {
        this.termRepository = termRepository;
        this.publisher = publisher;
    }

    public List<TermDTO> findAll() {
        final List<Term> terms = termRepository.findAll(Sort.by("id"));
        return terms.stream()
                .map(term -> mapToDTO(term, new TermDTO()))
                .toList();
    }

    public TermDTO get(final Integer id) {
        return termRepository.findById(id)
                .map(term -> mapToDTO(term, new TermDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TermDTO termDTO) {
        final Term term = new Term();
        mapToEntity(termDTO, term);
        return termRepository.save(term).getId();
    }

    public void update(final Integer id, final TermDTO termDTO) {
        final Term term = termRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(termDTO, term);
        termRepository.save(term);
    }

    public void delete(final Integer id) {
        final Term term = termRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTerm(id));
        termRepository.delete(term);
    }

    private TermDTO mapToDTO(final Term term, final TermDTO termDTO) {
        termDTO.setId(term.getId());
        termDTO.setAcademicYearId(term.getAcademicYearId());
        termDTO.setTermNumber(term.getTermNumber());
        termDTO.setStartDate(term.getStartDate());
        termDTO.setEndDate(term.getEndDate());
        return termDTO;
    }

    private Term mapToEntity(final TermDTO termDTO, final Term term) {
        term.setAcademicYearId(termDTO.getAcademicYearId());
        term.setTermNumber(termDTO.getTermNumber());
        term.setStartDate(termDTO.getStartDate());
        term.setEndDate(termDTO.getEndDate());
        return term;
    }

}
